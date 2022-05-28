package repositorydatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.IGitHubConstants;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.;

import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import datamodel.Repository;
//import datamodel.RepositoryInternalMetrics;
//import datamodel.User;
import datamodel.*;
import exceptions.RepositoryDataSourceException;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;

/**
 * @author Carlos López Nozal - clopezno
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class RepositoryDataSourceUsingGithubAPI implements RepositoryDataSource {

	private static final long serialVersionUID = -5228642659883160983L;
	/**
	 * Single instance of the class.
	 */
	private static RepositoryDataSourceUsingGithubAPI instance;

	/**
	 * Connection type.
	 */
	private EnumConnectionType connectionType;

	/**
	 * API that helps to connect to Github.
	 * 
	 * @see GithubApi
	 */
	private GitHubClient githubclientApi;
	private UserService userService;
	private CommitService commitService;
	private IssueService issueService;
	private RepositoryService repositoryService;

	/**
	 * Current user.
	 * 
	 */
	private datamodel.User currentUser;

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryDataSourceUsingGithubAPI.class);

	private static final String HOST_URL = "https://github.com";

	private Map<Long, org.eclipse.egit.github.core.Repository> mapUrlIdRepo;

	/**
	 * Constructor that returns a not connected githubrepositorydatasource.
	 */
	private RepositoryDataSourceUsingGithubAPI() {
		connectionType = EnumConnectionType.NOT_CONNECTED;
		githubclientApi = null;
		currentUser = null;
		mapUrlIdRepo = new HashMap<Long, org.eclipse.egit.github.core.Repository>();
	}

	/**
	 * Gets the single instance of the class.
	 * 
	 * @return A github repository data source.
	 */
	public static RepositoryDataSourceUsingGithubAPI getGithubRepositoryDataSource() {
		if (RepositoryDataSourceUsingGithubAPI.instance == null)
			RepositoryDataSourceUsingGithubAPI.instance = new RepositoryDataSourceUsingGithubAPI();
		return instance;
	}

	@Override
	public void connect(RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		if (connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
			githubclientApi = new GitHubClient();
			currentUser = null;
			connectionType = EnumConnectionType.CONNECTED;
			LOGGER.info("Established connection with GitHub");
		} else {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED);
		}

	}

	@Override
	public void connect(String username, String password, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		try {
			if (username == null || password == null || username.isBlank() || password.isBlank())
				throw new RepositoryDataSourceException(RepositoryDataSourceException.LOGIN_ERROR);
			if (connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				githubclientApi = new GitHubClient();
				githubclientApi.setCredentials(username, password);
				userService = new UserService(githubclientApi);
				currentUser = getCurrentUser(userService.getUser());
				connectionType = EnumConnectionType.LOGGED;
				LOGGER.info("Login to Github");
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED);
			}
		} catch (IOException e) {
			reset();
			throw new RepositoryDataSourceException(RepositoryDataSourceException.LOGIN_ERROR);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void connect(String token, RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		try {
			if (connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				githubclientApi = new GitHubClient();
				githubclientApi.setOAuth2Token(token);
				userService = new UserService(githubclientApi);
				currentUser = getCurrentUser(userService.getUser());
				connectionType = EnumConnectionType.LOGGED;
				LOGGER.info("Login to Github");
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED);
			}
		} catch (IOException e) {
			reset();
			throw new RepositoryDataSourceException(RepositoryDataSourceException.LOGIN_ERROR);
		} catch (RepositoryDataSourceException e) {
			throw e;
		}

	}

	@Override
	public void disconnect(RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		if (connectionType != EnumConnectionType.NOT_CONNECTED) {
			reset();
		} else {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_DISCONNECTED);
		}

	}

	/**
	 * Reset the connection.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private void reset() {
		connectionType = EnumConnectionType.NOT_CONNECTED;
		githubclientApi = null;
		currentUser = null;
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

	/**
	 * Obtains a user from the github user.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @author Carlos López Nozal - clopezno
	 * @return
	 */
	@Override
	public datamodel.User getCurrentUser(RepositorySourceType repositorySourceType) {
		return currentUser;
	}

	/**
	 * Obtains a user from the github user.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @author Carlos López Nozal - clopezno
	 * @param githubUser
	 * @return
	 */
	private datamodel.User getCurrentUser(org.eclipse.egit.github.core.User githubUser) {
		return new datamodel.User((long) githubUser.getId(), githubUser.getAvatarUrl(), githubUser.getEmail(),
				githubUser.getName(), githubUser.getLogin()

		);
	}

	@Override
	public Collection<datamodel.Repository> getCurrentUserRepositories(RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {

		try {
			Collection<datamodel.Repository> resultrepositories = new ArrayList<datamodel.Repository>();
			if (connectionType != EnumConnectionType.NOT_CONNECTED) {
				repositoryService = new RepositoryService(githubclientApi);
				List<org.eclipse.egit.github.core.Repository> lRepositories = repositoryService
						.getRepositories(currentUser.getUsername());

				for (org.eclipse.egit.github.core.Repository repo : lRepositories) {
					mapUrlIdRepo.put(repo.getId(), repo);
					resultrepositories.add(new datamodel.Repository(repo.getHtmlUrl(), repo.getName(), repo.getId()));
				}
				return resultrepositories;
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
			}
		} catch (IOException e) {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.REPOSITORY_NOT_FOUND);
		}
	}

	@Override
	public Collection<datamodel.Repository> getAllUserRepositories(String userIdOrUsername,
			RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		Collection<datamodel.Repository> repositories;
		try {
			if (currentUser != null && currentUser.getUsername().equals(userIdOrUsername)) {
				repositories = getCurrentUserRepositories(repositorySourceType);
			} else if (!connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				repositoryService = new RepositoryService(githubclientApi);
				List<org.eclipse.egit.github.core.Repository> lRepositories = repositoryService
						.getRepositories(userIdOrUsername);
				Collection<datamodel.Repository> resultrepositories = new ArrayList<datamodel.Repository>();
				for (org.eclipse.egit.github.core.Repository repo : lRepositories) {
					mapUrlIdRepo.put(repo.getId(), repo);
					resultrepositories.add(new datamodel.Repository(repo.getHtmlUrl(), repo.getName(), repo.getId()));
				}
				return resultrepositories;
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
			}
			return repositories;
		} catch (RepositoryDataSourceException e) {
			throw e;
		} catch (IOException e) {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.USER_NOT_FOUND);
		}
	}

	@Override
	public Collection<datamodel.Repository> getAllGroupRepositories(String groupName,
			RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		// API github for group and user work same way
		return getAllUserRepositories(groupName, repositorySourceType);
	}

	@Override
	public datamodel.Repository getRepository(String repositoryHTTPSURL, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		Long projectId;
		if (!connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
			projectId = getProjectId(repositoryHTTPSURL);
			String projectName = getRepositoryName(repositoryHTTPSURL);
			if (projectId != null) {
				return new datamodel.Repository(repositoryHTTPSURL, projectName, projectId);
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.REPOSITORY_NOT_FOUND);
			}
		} else {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
		}
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
			repositoryService = new RepositoryService(githubclientApi);
			String sProyecto = repositoryURL.replaceAll(RepositoryDataSourceUsingGithubAPI.HOST_URL + "/", "");
			String nombreProyecto = sProyecto.split("/")[sProyecto.split("/").length - 1];
			String propietarioYGrupo = sProyecto.replaceAll("/" + nombreProyecto, "");
			Repository repo = repositoryService.getRepository(propietarioYGrupo, nombreProyecto);
			mapUrlIdRepo.put(repo.getId(), repo);
			return repo.getId();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Gets the repository name specifying the project ID.
	 * 
	 * @param projectId ID of the project.
	 * @return Repository name or null if fail.
	 */
	private String getRepositoryName(String repositoryURL) {
		try {
			if (repositoryURL == null)
				return null;
			repositoryService = new RepositoryService(githubclientApi);
			String sProyecto = repositoryURL.replaceAll(RepositoryDataSourceUsingGithubAPI.HOST_URL + "/", "");
			String nombreProyecto = sProyecto.split("/")[sProyecto.split("/").length - 1];
			String propietarioYGrupo = sProyecto.replaceAll("/" + nombreProyecto, "");
			Repository pProyecto = repositoryService.getRepository(propietarioYGrupo, nombreProyecto);
			return pProyecto.getName();

		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Gets Repository from repositoryId.
	 * 
	 * @param repositoryID Repository identifier .
	 * @return Repository.
	 */
	@Override
	public datamodel.Repository getRepository(Long repositoryId, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		if (!connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
			if (mapUrlIdRepo.containsKey(repositoryId)) {
				Repository repository = mapUrlIdRepo.get(repositoryId);
				return new datamodel.Repository(repository.getUrl(), repository.getName(), repositoryId);
			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.REPOSITORY_NOT_FOUND);
			}
		} else {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.NOT_CONNECTED);
		}
	}

	@Override
	public RepositoryInternalMetrics getRepositoryInternalMetrics(datamodel.Repository repository,
			RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		
		LOGGER.info("getRepositoryInternalMetrics en RepositoryDataSourceUsingGithubAPI");

		
		repositoryService = new RepositoryService(githubclientApi);
		LOGGER.info("despues de instanciar el servico");

		getRepository(repository.getId(), repositorySourceType);
		LOGGER.info("despues de  getRepository");
		Long projectId = repository.getId();

		LOGGER.info("projectId obtenida: " + projectId);
		
		
		Integer totalNumberOfIssues = getTotalNumberOfIssues(projectId);
		LOGGER.info("totalNumberOfIssues obtenidas: " + String.valueOf(totalNumberOfIssues));
		
		Integer totalNumberOfCommits = getTotalNumberOfCommits(projectId);
		Integer numberOfClosedIssues = getNumberOfClosedIssues(projectId);
		List<Integer> daysToCloseEachIssue = getDaysToCloseEachIssue(projectId);
		Set<Date> commitDates = getCommitsDates(projectId);
		Integer lifeSpanMonths = getRepositoryLifeInMonths(projectId);
		
		
		List<Job> jobs = getRepositoryJobs(projectId);
		List<Release> releases = getRepositoryReleases(projectId);
		
		
		RepositoryInternalMetrics repositoryInternalMetrics = new RepositoryInternalMetrics(
				totalNumberOfIssues,
				totalNumberOfCommits,
				numberOfClosedIssues,
				daysToCloseEachIssue,
				commitDates,
				lifeSpanMonths
		);
		


		LOGGER.info("repositoryInternalMetrics obtenidas: ");
		LOGGER.info(repositoryInternalMetrics.toString());
		
		return repositoryInternalMetrics;
	}

	/**
	 * Gets total number of issues of a project specifying the project ID.
	 * 
	 * @param projectId ID of the project.
	 * @return Total number of issues.
	 *
	 * @throws IOException
	 */
	private Integer getTotalNumberOfIssues(Long repoId) {
		try {
			issueService = new IssueService(githubclientApi);

			Map<String, String> filtro = new HashMap<String, String>();
			filtro.put("state", "all");
			Repository repository = mapUrlIdRepo.get(repoId);
			RepositoryId githubRepoId = RepositoryId.createFromUrl(repository.getHtmlUrl());

			return this.issueService.getIssues(githubRepoId, filtro).size();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Gets total number of closed issues of a project specifying the project ID.
	 * 
	 * @param projectId ID of the project.
	 * @return Total number of issues.
	 *
	 * @throws IOException
	 */
	private Integer getNumberOfClosedIssues(Long repoId) {
		try {
			issueService = new IssueService(githubclientApi);

			Map<String, String> filtro = new HashMap<String, String>();
			filtro.put("state", "closed");
			Repository repository = mapUrlIdRepo.get(repoId);
			RepositoryId githubRepoId = RepositoryId.createFromUrl(repository.getHtmlUrl());

			return this.issueService.getIssues(githubRepoId, filtro).size();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Gets total number of commits of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return Total number of commits of a project.
	 */
	private Integer getTotalNumberOfCommits(Long repoId) {
		try {
			commitService = new CommitService(githubclientApi);
			Repository repository = mapUrlIdRepo.get(repoId);
			RepositoryId githubRepoId = RepositoryId.createFromUrl(repository.getHtmlUrl());

			return commitService.getCommits(githubRepoId).size();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Gets days to close each issue of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return Days to close each issue of a project or null if fail.
	 * @throws IOException
	 */
	private List<Integer> getDaysToCloseEachIssue(Long repoId) {
		try {
			issueService = new IssueService(githubclientApi);
			Map<String, String> filtro = new HashMap<String, String>();
			filtro.put("state", "closed");
			Repository repository = mapUrlIdRepo.get(repoId);
			RepositoryId githubRepoId = RepositoryId.createFromUrl(repository.getHtmlUrl());
			List<Issue> lissues = issueService.getIssues(githubRepoId, filtro);
			List<Integer> ldaystoclose = new ArrayList<Integer>();

			for (Issue issue : lissues) {
				long ldays = (issue.getClosedAt().getTime() - issue.getCreatedAt().getTime()) / (1000 * 60 * 60 * 24);
				ldaystoclose.add((int) ldays);
			}
			return ldaystoclose;
		} catch (IOException e) {
			return null;
		}

	}

	/**
	 * Gets dates of commits of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return Set of dates of commits of a project or null if fail.
	 * @throws IOException
	 */
	private Set<Date> getCommitsDates(Long repoId) {

		try {
			commitService = new CommitService(githubclientApi);
			List<RepositoryCommit> lcommits;
			Repository repository = mapUrlIdRepo.get(repoId);
			RepositoryId githubRepoId = RepositoryId.createFromUrl(repository.getHtmlUrl());

			lcommits = commitService.getCommits(githubRepoId);

			Set<Date> sdates = new HashSet<Date>();

			for (int i = 0; i < lcommits.size(); i++) {
				sdates.add(new Date(lcommits.get(i).getCommit().getAuthor().getDate().getTime()));
			}
			return sdates;
		} catch (IOException e) {
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
	private Integer getRepositoryLifeInMonths(Long repoId) {
		try {
			repositoryService = new RepositoryService(githubclientApi);
			Repository repository = mapUrlIdRepo.get(repoId);
			RepositoryId githubRepoId = RepositoryId.createFromUrl(repository.getHtmlUrl());

			Date createdAtDate = repositoryService.getRepository(githubRepoId).getCreatedAt();
			Date lastActivityDate = repositoryService.getRepository(githubRepoId).getUpdatedAt();

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
		} catch (IOException e) {
			return null;
		}
	}
}
