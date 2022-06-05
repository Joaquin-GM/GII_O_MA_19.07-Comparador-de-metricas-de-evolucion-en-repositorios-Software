package repositorydatasource;

import java.util.ArrayList;
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
import org.gitlab4j.api.models.Job;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.ProjectFilter;
import org.gitlab4j.api.models.Release;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.RepositoryDataSourceService;
import datamodel.CustomGitlabApiJob;
import datamodel.CustomGitlabApiRelease;
import datamodel.Repository;
import datamodel.RepositoryInternalMetrics;
import datamodel.RepositorySourceType;
import datamodel.User;
import exceptions.RepositoryDataSourceException;

/**
 * Implements IRepositoryDataSource that obtains the data from GitLab
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 * @author Joaquin Garcia Molina - Joaquin-GM
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
		if (RepositoryDataSourceUsingGitlabAPI.instance == null)
			RepositoryDataSourceUsingGitlabAPI.instance = new RepositoryDataSourceUsingGitlabAPI();
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.IRepositoryDataSource#connect()
	 */
	@Override
	public void connect(RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		if (connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
			gitLabApi = new GitLabApi(RepositoryDataSourceUsingGitlabAPI.HOST_URL, "");
			currentUser = null;
			connectionType = EnumConnectionType.CONNECTED;
			LOGGER.info("Established connection with GitLab");
		} else {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.IRepositoryDataSource#connect(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void connect(String username, String password, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		try {
			if (username == null || password == null || username.isBlank() || password.isBlank())
				throw new RepositoryDataSourceException(RepositoryDataSourceException.LOGIN_ERROR);
			if (connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				gitLabApi = GitLabApi.oauth2Login(RepositoryDataSourceUsingGitlabAPI.HOST_URL, username,
						password.toCharArray());
				currentUser = getCurrentUser(gitLabApi.getUserApi().getCurrentUser());
				connectionType = EnumConnectionType.LOGGED;
				LOGGER.info("Logged to GitLab");
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED);
			}
		} catch (GitLabApiException e) {
			reset();
			throw new RepositoryDataSourceException(RepositoryDataSourceException.LOGIN_ERROR);
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.IRepositoryDataSource#connect(java.lang.String)
	 */
	@Override
	public void connect(String token, RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		try {
			if (connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				gitLabApi = new GitLabApi(RepositoryDataSourceUsingGitlabAPI.HOST_URL, token);
				currentUser = getCurrentUser(gitLabApi.getUserApi().getCurrentUser());
				connectionType = EnumConnectionType.LOGGED;
				LOGGER.info("Logged to GitLab");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.IRepositoryDataSource#disconnect()
	 */
	@Override
	public void disconnect(RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		if (connectionType != EnumConnectionType.NOT_CONNECTED) {
			reset();
		} else {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_DISCONNECTED);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.IRepositoryDataSource#getConnectionType()
	 */
	@Override
	public EnumConnectionType getConnectionType(RepositorySourceType repositorySourceType) {
		return connectionType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.IRepositoryDataSource#getCurrentUser()
	 */
	@Override
	public User getCurrentUser(RepositorySourceType repositorySourceType) {
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
		return new User(gitlabUser.getId(), gitlabUser.getAvatarUrl(), gitlabUser.getEmail(), gitlabUser.getName(),
				gitlabUser.getUsername());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.IRepositoryDataSource#getAllUserRepositories()
	 */
	@Override
	public Collection<Repository> getCurrentUserRepositories(RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		try {
			if (connectionType != EnumConnectionType.NOT_CONNECTED) {
				return gitLabApi.getProjectApi().getUserProjectsStream(currentUser.getId(), new ProjectFilter())
						.map(p -> new Repository(p.getWebUrl(), p.getName(), p.getId())).collect(Collectors.toList());
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
			}
		} catch (GitLabApiException e) {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.REPOSITORY_NOT_FOUND);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repositorydatasource.IRepositoryDataSource#getAllUserRepositories(java.lang.
	 * String)
	 */
	@Override
	public Collection<Repository> getAllUserRepositories(String userIdOrUsername,
			RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		Collection<Repository> repositories;
		try {
			if (currentUser != null && currentUser.getUsername().equals(userIdOrUsername)) {
				repositories = getCurrentUserRepositories(repositorySourceType);
			} else if (!connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				repositories = gitLabApi.getProjectApi().getUserProjectsStream(userIdOrUsername, new ProjectFilter())
						.map(p -> new Repository(p.getWebUrl(), p.getName(), p.getId())).collect(Collectors.toList());
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
	public Collection<Repository> getAllGroupRepositories(String groupIdOrPath,
			RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		Collection<Repository> repositories;
		try {
			if (!connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				repositories = gitLabApi.getGroupApi().getOptionalGroup(groupIdOrPath)
						.orElseThrow(
								() -> new RepositoryDataSourceException(RepositoryDataSourceException.GROUP_NOT_FOUND))
						.getProjects().stream().map(p -> new Repository(p.getWebUrl(), p.getName(), p.getId()))
						.collect(Collectors.toList());
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
			}
			return repositories;
		} catch (RepositoryDataSourceException e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repositorydatasource.IRepositoryDataSource#getRepository(java.lang.String)
	 */
	@Override
	public Repository getRepository(String repositoryHTTPSURL, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		Long projectId;
		if (!connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
			projectId = getProjectId(repositoryHTTPSURL);
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
	public Repository getRepository(Long repositoryId, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		try {
			if (!connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repositorydatasource.IRepositoryDataSource#updateRepositoryInternalMetrics(
	 * model.Repository)
	 */
	@Override
	public RepositoryInternalMetrics getRepositoryInternalMetrics(Repository repository,
			RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		LOGGER.info("getRepositoryInternalMetrics using GitLabAPI");
		getRepository(repository.getId(), repositorySourceType);
		Long projectId = repository.getId();
		Integer totalNumberOfIssues = getTotalNumberOfIssues(projectId);
		Integer totalNumberOfCommits = getTotalNumberOfCommits(projectId);
		Integer numberOfClosedIssues = getNumberOfClosedIssues(projectId);
		List<Integer> daysToCloseEachIssue = getDaysToCloseEachIssue(projectId);
		Set<Date> commitDates = getCommitsDates(projectId);
		Integer lifeSpanMonths = getRepositoryLifeInMonths(projectId);

		RepositoryInternalMetrics repositoryInternalMetrics = null;
		RepositoryDataSourceService rds = RepositoryDataSourceService.getInstance();

		if (rds.getConnectionType(repositorySourceType) == EnumConnectionType.CONNECTED) {
			repositoryInternalMetrics = new RepositoryInternalMetrics(totalNumberOfIssues, totalNumberOfCommits,
					numberOfClosedIssues, daysToCloseEachIssue, commitDates, lifeSpanMonths);
		} else if (rds.getConnectionType(repositorySourceType) == EnumConnectionType.LOGGED) {
			// With logged connection we can get jobs (and releases) of the repository
			List<CustomGitlabApiJob> jobs = getRepositoryJobs(projectId);
			List<CustomGitlabApiRelease> releases = getRepositoryReleases(projectId);
			repositoryInternalMetrics = new RepositoryInternalMetrics(totalNumberOfIssues, totalNumberOfCommits,
					numberOfClosedIssues, daysToCloseEachIssue, commitDates, lifeSpanMonths, jobs, releases);
			LOGGER.info("jobs of the repository " + Integer.toString(jobs.size()));
		}

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
			if (repositoryURL == null)
				return null;
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
			var returnedValueNoNum = gitLabApi.getIssuesApi().getIssuesStream(projectId, new IssueFilter());
			var returnedValue = (int) returnedValueNoNum.count();
			return returnedValue;
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
			var returnedValueNoNum = gitLabApi.getCommitsApi().getCommitStream(projectId);
			var returnedValue = (int) returnedValueNoNum.count();
			return returnedValue;
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
			return (int) gitLabApi.getIssuesApi()
					.getIssuesStream(projectId, new IssueFilter().withState(IssueState.CLOSED)).count();
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Gets days to close each issue of a project.
	 * 
	 * Added second condition for Issued closed by milestones, they have closed
	 * status but all the closed parameter set to null, the only parameter revealing
	 * the date in with the status changed to closed is updated at.
	 * 
	 * @param projectId ID of the project.
	 * @return Days to close each issue of a project or null if fail.
	 */
	private List<Integer> getDaysToCloseEachIssue(Long projectId) {
		try {
			return gitLabApi.getIssuesApi().getIssuesStream(projectId, new IssueFilter().withState(IssueState.CLOSED))
					.filter(issue -> (issue.getCreatedAt() != null && issue.getClosedAt() != null)
							|| (issue.getState().equals(IssueState.CLOSED) && issue.getUpdatedAt() != null))
					.map(issue -> {
						if ((issue.getCreatedAt() != null && issue.getClosedAt() != null)) {
							return (int) Math.abs((issue.getClosedAt().getTime() - issue.getCreatedAt().getTime())
									/ (1000 * 60 * 60 * 24));

						} else if (issue.getState().equals(IssueState.CLOSED) && issue.getUpdatedAt() != null) {
							return (int) Math.abs((issue.getUpdatedAt().getTime() - issue.getCreatedAt().getTime())
									/ (1000 * 60 * 60 * 24));
						}
						return null;
					}).collect(Collectors.toList());
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
			return gitLabApi.getCommitsApi().getCommitStream(projectId).map(commit -> commit.getCommittedDate())
					.collect(Collectors.toSet());
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Computes the number of months that have passed since the creation of the
	 * repository until the date of last activity.
	 * 
	 * @param projectId ID of the project.
	 * @return Number of months that have passed since the creation of the
	 *         repository until the date of last activity.
	 */
	private Integer getRepositoryLifeInMonths(Long projectId) {
		try {
			Date createdAtDate = gitLabApi.getProjectApi().getProject(projectId).getCreatedAt();
			Date lastActivityDate = gitLabApi.getProjectApi().getProject(projectId).getLastActivityAt();

			if (createdAtDate == null || lastActivityDate == null)
				return null;

			Calendar createdAtCalendar = new GregorianCalendar();
			createdAtCalendar.setTime(createdAtDate);
			Calendar lastActivityCalendar = new GregorianCalendar();
			lastActivityCalendar.setTime(lastActivityDate);

			int diffYear = lastActivityCalendar.get(Calendar.YEAR) - createdAtCalendar.get(Calendar.YEAR);
			int diffMonth = diffYear * 12 + lastActivityCalendar.get(Calendar.MONTH)
					- createdAtCalendar.get(Calendar.MONTH);

			return (diffMonth == 0) ? 1 : diffMonth;
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Get a list of jobs of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return List of jobs of a project or null if fail.
	 */
	private List<CustomGitlabApiJob> getRepositoryJobs(Long projectId) {
		try {
			List<Job> jobs = gitLabApi.getJobApi().getJobsStream(projectId).collect(Collectors.toList());
			List<CustomGitlabApiJob> customGitlabApiJobs = new ArrayList<CustomGitlabApiJob>();

			for (Job job : jobs) {
				CustomGitlabApiJob newCustomGitlabApiJob = new CustomGitlabApiJob(job);
				customGitlabApiJobs.add(newCustomGitlabApiJob);
			}

			return customGitlabApiJobs;
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Get a list of releases of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return List of releases of a project or null if fail.
	 */
	private List<CustomGitlabApiRelease> getRepositoryReleases(Long projectId) {
		try {
			List<Release> releases = gitLabApi.getReleasesApi().getReleasesStream(projectId)
					.collect(Collectors.toList());

			List<CustomGitlabApiRelease> customGitlabApiReleases = new ArrayList<CustomGitlabApiRelease>();

			for (Release release : releases) {
				CustomGitlabApiRelease newCustomGitlabApiRelease = new CustomGitlabApiRelease(release);
				customGitlabApiReleases.add(newCustomGitlabApiRelease);
			}

			return customGitlabApiReleases;
		} catch (GitLabApiException e) {
			return null;
		}
	}
}