package repositorydatasource;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.gitlab4j.api.Constants.IssueState;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.IssueFilter;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.ProjectFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datamodel.Repository;
import datamodel.RepositoryInternalMetrics;
import datamodel.User;
import exceptions.RepositoryDataSourceException;

/**
 * Implements IRepositoryDataSource that obtains the data from GitLab
 * 
 * @author migue
 *
 */
public class RepositoryDataSourceUsingGitlabAPI implements RepositoryDataSource {

	/**
	 * Serial.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 2565951561088053095L;
	
	/**
	 * Default Host URL.
	 */
	public static final String HOST_URL = "https://gitlab.com";

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryDataSourceUsingGitlabAPI.class);

	/**
	 * Single instance of the class.
	 */
	private static RepositoryDataSourceUsingGitlabAPI instance;

	/**
	 * Connection type.
	 */
	private EnumConnectionType connectionType;

	/**
	 * API that helps to connect to GitLab.
	 * 
	 * @see GitLabApi
	 */
	private GitLabApi gitLabApi;
	
	/**
	 * Current user.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private User currentUser;

	/**
	 * Constructor that returns a not connected gitlabrepositorydatasource.
	 */
	private RepositoryDataSourceUsingGitlabAPI() {
		connectionType = EnumConnectionType.NOT_CONNECTED;
		gitLabApi = null;
		currentUser = null;
	}
	
	/**
	 * Gets the single instance of the class.
	 * 
	 * @return A gitlab repository data source.
	 */
	public static RepositoryDataSourceUsingGitlabAPI getGitLabRepositoryDataSource() {
		if (RepositoryDataSourceUsingGitlabAPI.instance == null) RepositoryDataSourceUsingGitlabAPI.instance = new RepositoryDataSourceUsingGitlabAPI();
		return instance;
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSource#connect()
	 */
	@Override
	public void connect() throws RepositoryDataSourceException {
		if( connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
			gitLabApi = new GitLabApi(RepositoryDataSourceUsingGitlabAPI.HOST_URL, "");
			currentUser = null;
			connectionType = EnumConnectionType.CONNECTED;
			LOGGER.info("Established connection with GitLab");
		} else {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED);
		}
	}
	
	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSource#connect(java.lang.String, java.lang.String)
	 */
	@Override
	public void connect(String username, String password) throws RepositoryDataSourceException {
		try {
			if(username == null || password == null || username.isBlank() || password.isBlank()) throw new RepositoryDataSourceException(RepositoryDataSourceException.LOGIN_ERROR);
			if(connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				gitLabApi = GitLabApi.oauth2Login(RepositoryDataSourceUsingGitlabAPI.HOST_URL, username, password.toCharArray());
				currentUser = getCurrentUser(gitLabApi.getUserApi().getCurrentUser());
				connectionType = EnumConnectionType.LOGGED;
				LOGGER.info("Login to GitLab");
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED);
			}
		} catch (GitLabApiException e) {
			reset();
			throw new RepositoryDataSourceException(RepositoryDataSourceException.LOGIN_ERROR);
		} catch (Exception  e) {
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSource#connect(java.lang.String)
	 */
	@Override
	public void connect(String token) throws RepositoryDataSourceException {
		try {
			if(connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				gitLabApi = new GitLabApi(RepositoryDataSourceUsingGitlabAPI.HOST_URL, token);
				currentUser = getCurrentUser(gitLabApi.getUserApi().getCurrentUser());
				connectionType = EnumConnectionType.LOGGED;
				LOGGER.info("Login to GitLab");
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED);
			}
		} catch (GitLabApiException e) {
			reset();
			throw new RepositoryDataSourceException(RepositoryDataSourceException.LOGIN_ERROR);
		} catch (RepositoryDataSourceException e) {
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSource#disconnect()
	 */
	@Override
	public void disconnect() throws RepositoryDataSourceException {
		if (connectionType != EnumConnectionType.NOT_CONNECTED) {
			reset();
		} else {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_DISCONNECTED);
		}
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSource#getConnectionType()
	 */
	@Override
	public EnumConnectionType getConnectionType() {
		return connectionType;
	}
	
	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSource#getCurrentUser()
	 */
	@Override
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * Obtains a user from the gitlab user.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param gitlabUser
	 * @return
	 */
	private User getCurrentUser(org.gitlab4j.api.models.User gitlabUser) {
		return new User(
				gitlabUser.getId(),
				gitlabUser.getAvatarUrl(),
				gitlabUser.getEmail(),
				gitlabUser.getName(),
				gitlabUser.getUsername()
		);
	}
	
	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSource#getAllUserRepositories()
	 */
	@Override
	public Collection<Repository> getCurrentUserRepositories() throws RepositoryDataSourceException {
		try {
			if (connectionType != EnumConnectionType.NOT_CONNECTED) {
				return gitLabApi.getProjectApi().getUserProjectsStream(
							currentUser.getId(),
							new ProjectFilter())
						.map(p -> 
							new Repository(p.getWebUrl(), p.getName(), p.getId()))
						.collect(Collectors.toList());
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
			}
		} catch (GitLabApiException e) {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.REPOSITORY_NOT_FOUND);
		}
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSource#getAllUserRepositories(java.lang.String)
	 */
	@Override
	public Collection<Repository> getAllUserRepositories(String userIdOrUsername) throws RepositoryDataSourceException {
		Collection<Repository> repositories;
		try {
			if (currentUser != null && currentUser.getUsername().equals(userIdOrUsername)) {
				repositories = getCurrentUserRepositories();
			} else if(! connectionType.equals(EnumConnectionType.NOT_CONNECTED)){
				repositories = gitLabApi.getProjectApi()
						.getUserProjectsStream(userIdOrUsername, new ProjectFilter())
							.map(p -> new Repository(p.getWebUrl(), p.getName(), p.getId()))
							.collect(Collectors.toList());
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
			}
			return repositories;
		} catch (RepositoryDataSourceException e) {
			throw e;
		} catch (GitLabApiException e) {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.USER_NOT_FOUND);
		}
	}

	@Override
	public Collection<Repository> getAllGroupRepositories(String groupIdOrPath) throws RepositoryDataSourceException {
		Collection<Repository> repositories;
		try {
			if(! connectionType.equals(EnumConnectionType.NOT_CONNECTED)){
				repositories = gitLabApi.getGroupApi()
						.getOptionalGroup(groupIdOrPath)
						.orElseThrow(() -> new RepositoryDataSourceException(RepositoryDataSourceException.GROUP_NOT_FOUND))
						.getProjects()
						.stream()
						.map(p -> new Repository(p.getWebUrl(), p.getName(), p.getId()))
						.collect(Collectors.toList());
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
			}
			return repositories;
		} catch (RepositoryDataSourceException e) {
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSource#getRepository(java.lang.String)
	 */
	@Override
	public Repository getRepository(String repositoryHTTPSURL) throws RepositoryDataSourceException{
		LOGGER.info("--getRepository---");
		LOGGER.info(repositoryHTTPSURL);
		Long projectId;
		if (! connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
			projectId = getProjectId(repositoryHTTPSURL);
			LOGGER.info(String.valueOf(projectId));
			if (projectId != null) {
				return new Repository(repositoryHTTPSURL, getRepositoryName(projectId), projectId);
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.REPOSITORY_NOT_FOUND);
			}
		} else {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
		}
	}

	@Override
	public Repository getRepository(Long repositoryId) throws RepositoryDataSourceException {
		try {
			LOGGER.info("--getRepository 2---");
			LOGGER.info(String.valueOf(repositoryId));
			if (! connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				Project pProyecto = gitLabApi.getProjectApi().getProject(repositoryId);
				if (pProyecto != null) {
					return new Repository(pProyecto.getWebUrl(), pProyecto.getName(), repositoryId);
				} else {
					throw new RepositoryDataSourceException(RepositoryDataSourceException.REPOSITORY_NOT_FOUND);
				}
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
			}
		} catch (GitLabApiException e) {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.REPOSITORY_NOT_FOUND);
		}
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.IRepositoryDataSource#updateRepositoryInternalMetrics(model.Repository)
	 */
	@Override
	public RepositoryInternalMetrics getRepositoryInternalMetrics(Repository repository) throws RepositoryDataSourceException {
		LOGGER.info("getRepositoryInternalMetrics");
		LOGGER.info(String.valueOf(repository));
		getRepository(repository.getId()); // Si no se obtiene, lanza una excepción
		Long projectId = repository.getId();
		Integer totalNumberOfIssues = getTotalNumberOfIssues(projectId);
		Integer totalNumberOfCommits = getTotalNumberOfCommits(projectId);
		Integer numberOfClosedIssues = getNumberOfClosedIssues(projectId);
		List<Integer> daysToCloseEachIssue = getDaysToCloseEachIssue(projectId);
		Set<Date> commitDates = getCommitsDates(projectId);
		Integer lifeSpanMonths = getRepositoryLifeInMonths(projectId);
		RepositoryInternalMetrics repositoryInternalMetrics = new RepositoryInternalMetrics(
				totalNumberOfIssues,
				totalNumberOfCommits,
				numberOfClosedIssues,
				daysToCloseEachIssue,
				commitDates,
				lifeSpanMonths
		);
		LOGGER.info("projectId " + projectId);
		LOGGER.info("totalNumberOfIssues " + totalNumberOfIssues);
		LOGGER.info("totalNumberOfCommits " + totalNumberOfCommits);
		LOGGER.info("numberOfClosedIssues " + numberOfClosedIssues);
		LOGGER.info("daysToCloseEachIssue " + daysToCloseEachIssue);
		LOGGER.info("commitDates " + commitDates);
		LOGGER.info("lifeSpanMonths " + lifeSpanMonths);
		return repositoryInternalMetrics;
	}

	/**
	 * Reset the connection.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private void reset() {
		connectionType = EnumConnectionType.NOT_CONNECTED;
		gitLabApi = null;
		currentUser = null;
	}

	/**
	 * Gets the ID of a project using the Project URL.
	 * 
	 * @param repositoryURL Project URL.
	 * @return ID of a project.
	 */
	private Long getProjectId(String repositoryURL) {
		try {
			LOGGER.info("getProjectId");
			LOGGER.info(String.valueOf(repositoryURL));
			if(repositoryURL == null) return null;
			String sProyecto = repositoryURL.replaceAll(RepositoryDataSourceUsingGitlabAPI.HOST_URL + "/", "");
			String nombreProyecto = sProyecto.split("/")[sProyecto.split("/").length - 1];
			String propietarioYGrupo = sProyecto.replaceAll("/" + nombreProyecto, "");
			Project pProyecto = gitLabApi.getProjectApi().getProject(propietarioYGrupo, nombreProyecto);
			return pProyecto.getId();
		} catch (GitLabApiException e) {
			return null;
		}
	}
	
	/**
	 * Gets the repository name specifying the project ID.
	 * 
	 * @param projectId ID of the project.
	 * @return Repository name or null if fail.
	 */
	private String getRepositoryName(Long projectId) {
		try {
			LOGGER.info("getRepositoryName");
			LOGGER.info(String.valueOf(projectId));
			return gitLabApi.getProjectApi().getProject(projectId).getName();
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Gets total number of issues of a project specifying the project ID.
	 * 
	 * @param projectId ID of the project.
	 * @return Total number of issues or -1 if fail.
	 */
	private Integer getTotalNumberOfIssues(Long projectId) {	
		try {
			LOGGER.info("getTotalNumberOfIssues");

			var returnedValueNoNum = gitLabApi.getIssuesApi().getIssuesStream(projectId, new IssueFilter());
			LOGGER.info("returnedValueNoNum");
			LOGGER.info(String.valueOf(returnedValueNoNum));

			var returnedValue = (int) returnedValueNoNum.count();
			LOGGER.info("returnedValue");
			LOGGER.info(String.valueOf(returnedValue));

			return returnedValue;
//			return (int) gitLabApi.getIssuesApi().getIssuesStream(projectId, new IssueFilter()).count();
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Gets total number of commits of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return Total number of commits of a project or -1 if fail.
	 */
	private Integer getTotalNumberOfCommits(Long projectId) {
		try {
			LOGGER.info("getTotalNumberOfCommits");

			var returnedValueNoNum = gitLabApi.getCommitsApi().getCommitStream(projectId);
			LOGGER.info("returnedValueNoNum");
			LOGGER.info(String.valueOf(returnedValueNoNum));

			var returnedValue = (int) returnedValueNoNum.count();
			LOGGER.info("returnedValue");
			LOGGER.info(String.valueOf(returnedValue));

			return returnedValue;
//			return (int) gitLabApi.getCommitsApi().getCommitStream(projectId).count();
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Gets number of closed issues of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return Number of closed issues of a project or -1 if fail.
	 */
	private Integer getNumberOfClosedIssues(Long projectId) {
		try {
			return (int) gitLabApi.getIssuesApi().getIssuesStream(projectId, new IssueFilter().withState(IssueState.CLOSED)).count();
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Gets days to close each issue of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return Days to close each issue of a project or null if fail.
	 */
	private List<Integer> getDaysToCloseEachIssue(Long projectId) {
		try {
			return gitLabApi.getIssuesApi().getIssuesStream(projectId, new IssueFilter().withState(IssueState.CLOSED))
					.filter(issue -> issue.getCreatedAt() != null && issue.getClosedAt() != null)
					.map(issue -> (int) ((issue.getClosedAt().getTime() - issue.getCreatedAt().getTime()) / (1000 * 60 * 60 * 24 )))
					.collect(Collectors.toList());
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Gets dates of commits of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return Set of dates of commits of a project or null if fail.
	 */
	private Set<Date> getCommitsDates(Long projectId) {
		try {
			return gitLabApi.getCommitsApi().getCommitStream(projectId)
					.map(commit -> commit.getCommittedDate())
					.collect(Collectors.toSet());
		} catch (GitLabApiException e) {
			return null;
		}
	}
	
	/**
	 * Computes the number of months that have passed since the creation of the repository
	 * until the date of last activity.
	 * 
	 * @param projectId ID of the project.
	 * @return  Number of months that have passed since the creation of the repository
	 * until the date of last activity.
	 */
	private Integer getRepositoryLifeInMonths(Long projectId) {
		try {
			Date createdAtDate = gitLabApi.getProjectApi().getProject(projectId).getCreatedAt();
			Date lastActivityDate = gitLabApi.getProjectApi().getProject(projectId).getLastActivityAt();
			
			if (createdAtDate == null || lastActivityDate == null) return null;
			
			Calendar createdAtCalendar = new GregorianCalendar();
			createdAtCalendar.setTime(createdAtDate);
			Calendar lastActivityCalendar = new GregorianCalendar();
			lastActivityCalendar.setTime(lastActivityDate);
			
			int diffYear = lastActivityCalendar.get(Calendar.YEAR) - createdAtCalendar.get(Calendar.YEAR);
			int diffMonth = diffYear * 12 + lastActivityCalendar.get(Calendar.MONTH) - createdAtCalendar.get(Calendar.MONTH);
			 
			return (diffMonth == 0)?1:diffMonth;
		} catch (GitLabApiException e) {
			return null;
		}
	}
}