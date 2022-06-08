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
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GHWorkflowJob;
import org.kohsuke.github.GHWorkflowRun;
import org.kohsuke.github.GHWorkflowRun.Status;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

/*
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.IGitHubConstants;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.IssueService;

import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
*/

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
	private GitHub githubclientApi;
	/*
	 * private UserService userService; private CommitService commitService; private
	 * IssueService issueService; private RepositoryService repositoryService;
	 */

	/**
	 * Current user.
	 * 
	 */
	private User currentUser;
	private GHUser ghUser;

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryDataSourceUsingGithubAPI.class);

	private static final String HOST_URL = "https://github.com";

	private Map<Long, GHRepository> mapUrlIdRepo;

	/**
	 * Constructor that returns a not connected githubrepositorydatasource.
	 */
	private RepositoryDataSourceUsingGithubAPI() {
		connectionType = EnumConnectionType.NOT_CONNECTED;
		githubclientApi = null;
		currentUser = null;
		mapUrlIdRepo = new HashMap<Long, GHRepository>();
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
			// githubclientApi = new GitHubClient();
			try {
				githubclientApi = GitHub.connect();
				currentUser = null;
				connectionType = EnumConnectionType.CONNECTED;
				LOGGER.info("Established connection with GitHub public way");

			} catch (IOException e) {
				LOGGER.error("Error connecting to GitHub: " + e.toString());
			}

		} else {
			throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED);
		}

	}

	@Override
	public void connect(String username, String password, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		// This type of connection is deprecated for GitHub
		// See: https://docs.github.com/es/rest/overview/other-authentication-methods

		/*
		 * try { if (username == null || password == null || username.isBlank() ||
		 * password.isBlank()) throw new
		 * RepositoryDataSourceException(RepositoryDataSourceException.LOGIN_ERROR); if
		 * (connectionType.equals(EnumConnectionType.NOT_CONNECTED)) { githubclientApi =
		 * new GitHubClient(); githubclientApi.setCredentials(username, password);
		 * userService = new UserService(githubclientApi); currentUser =
		 * getCurrentUser(userService.getUser()); connectionType =
		 * EnumConnectionType.LOGGED; LOGGER.info("Login to Github"); } else { throw new
		 * RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED
		 * ); } } catch (IOException e) { reset(); throw new
		 * RepositoryDataSourceException(RepositoryDataSourceException.LOGIN_ERROR); }
		 * catch (Exception e) { throw e; }
		 */

	}

	@Override
	public void connect(String token, RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		try {
			if (connectionType.equals(EnumConnectionType.NOT_CONNECTED)) {
				// If you don't specify the GitHub user id then the sdk will retrieve it via user endpoint
				githubclientApi = new GitHubBuilder().withOAuthToken(token).build();
				ghUser = githubclientApi.getMyself();
				currentUser = getCurrentUser(ghUser); // esto puede que esté mal en teoría si mete el token ya con la anterior instruccion debería funcionar
				connectionType = EnumConnectionType.LOGGED;
				LOGGER.info("Logged to Github");

			} else {
				throw new RepositoryDataSourceException(RepositoryDataSourceException.ALREADY_CONNECTED);
			}
		} catch (IOException e) {
			reset();
			LOGGER.error("Error connecting to GitHub with token: " + e.toString());
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
	 * @throws IOException 
	 */
	private datamodel.User getCurrentUser(GHUser githubUser) throws IOException {
		return new datamodel.User((long) githubUser.getId(), githubUser.getAvatarUrl(), githubUser.getEmail(),
				githubUser.getLogin(), githubUser.getLogin()

		);
	}

	@Override
	public Collection<datamodel.Repository> getCurrentUserRepositories(RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {

		try {
			Collection<datamodel.Repository> resultrepositories = new ArrayList<datamodel.Repository>();
			if (connectionType != EnumConnectionType.NOT_CONNECTED) {
				List<GHRepository> lRepositories = githubclientApi.searchRepositories().user(currentUser.getName()).list().toList();
	
				for (GHRepository repo : lRepositories) {
					mapUrlIdRepo.put(repo.getId(), repo);
					resultrepositories.add(new datamodel.Repository(repo.getHtmlUrl().toString(), repo.getName(), repo.getId()));
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
				List<GHRepository> lRepositories = githubclientApi.searchRepositories().user(userIdOrUsername).list().toList();
				
				Collection<datamodel.Repository> resultrepositories = new ArrayList<datamodel.Repository>();
				for (GHRepository repo : lRepositories) {
					mapUrlIdRepo.put(repo.getId(), repo);
					resultrepositories.add(new datamodel.Repository(repo.getHtmlUrl().toString(), repo.getName(), repo.getId()));
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
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @param repositoryURL Project URL.
	 * @return ID of a project.
	 */
	private Long getProjectId(String repositoryURL) {
		try {
			if (repositoryURL == null)
				return null;
			
			String sProyecto = repositoryURL.replaceAll(RepositoryDataSourceUsingGithubAPI.HOST_URL + "/", "");
			String nombreProyecto = sProyecto.split("/")[sProyecto.split("/").length - 1];
			String propietarioYGrupo = sProyecto.replaceAll("/" + nombreProyecto, "");
		
			GHRepository ghRepo = githubclientApi.getRepository(sProyecto);
			
			Repository repo = new datamodel.Repository(ghRepo.getHtmlUrl().toString(), ghRepo.getName(), ghRepo.getId());
			
			mapUrlIdRepo.put(ghRepo.getId(), ghRepo);
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
			
			String sProyecto = repositoryURL.replaceAll(RepositoryDataSourceUsingGithubAPI.HOST_URL + "/", "");
			String nombreProyecto = sProyecto.split("/")[sProyecto.split("/").length - 1];
			String propietarioYGrupo = sProyecto.replaceAll("/" + nombreProyecto, "");
			
			GHRepository ghRepo = githubclientApi.getRepository(sProyecto);
			
			return ghRepo.getName();

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
				GHRepository repo = mapUrlIdRepo.get(repositoryId);
				return new datamodel.Repository(repo.getHtmlUrl().toString(), repo.getName(), repo.getId());
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
		getRepository(repository.getId(), repositorySourceType);
		Long projectId = repository.getId();

		LOGGER.info("projectId obtenida: " + projectId);

		Integer totalNumberOfIssues = getTotalNumberOfIssues(projectId);
		LOGGER.info("totalNumberOfIssues obtenidas: " + String.valueOf(totalNumberOfIssues));

		Integer totalNumberOfCommits = getTotalNumberOfCommits(projectId);
		LOGGER.info("totalNumberOfCommits obtenido: " + String.valueOf(totalNumberOfCommits));
		
		Integer numberOfClosedIssues = getNumberOfClosedIssues(projectId);
		LOGGER.info("numberOfClosedIssues obtenido: " + String.valueOf(numberOfClosedIssues));
		
		List<Integer> daysToCloseEachIssue = getDaysToCloseEachIssue(projectId);
		LOGGER.info("daysToCloseEachIssue obtenido: " + String.valueOf(daysToCloseEachIssue));
		
		Set<Date> commitDates = getCommitsDates(projectId);
		LOGGER.info("commitDates obtenidas (size): " + String.valueOf(commitDates.size()));
		
		Integer lifeSpanMonths = getRepositoryLifeInMonths(projectId);
		LOGGER.info("lifeSpanMonths obtenido: " + String.valueOf(lifeSpanMonths));
		

		List<CustomGithubApiJob> jobs = getRepositoryJobs(projectId);
		LOGGER.info("jobs obtenidos: " + String.valueOf(jobs));
		
		List<CustomGithubApiRelease> releases = getRepositoryReleases(projectId);
		LOGGER.info("releases obtenidas: " + String.valueOf(releases.size()));

		RepositoryInternalMetrics repositoryInternalMetrics = new RepositoryInternalMetrics(totalNumberOfIssues,
				totalNumberOfCommits, numberOfClosedIssues, daysToCloseEachIssue, commitDates, lifeSpanMonths);
		repositoryInternalMetrics.setGHJobs(jobs);
		repositoryInternalMetrics.setGHReleases(releases);

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
			return githubclientApi.getRepositoryById(repoId).queryIssues().state(GHIssueState.ALL).list().toList().size();
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
			return githubclientApi.getRepositoryById(repoId).queryIssues().state(GHIssueState.CLOSED).list().toList().size();
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
			return githubclientApi.getRepositoryById(repoId).queryCommits().list().toList().size();
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
			List<GHIssue> issues = githubclientApi.getRepositoryById(repoId).queryIssues().state(GHIssueState.CLOSED).list().toList();
			List<Integer> ldaystoclose = new ArrayList<Integer>();
			
			for (GHIssue issue : issues) {
				long ldays = Math.abs((issue.getClosedAt().getTime() - issue.getCreatedAt().getTime()) / (1000 * 60 * 60 * 24));
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
			List<GHCommit> commits = githubclientApi.getRepositoryById(repoId).queryCommits().list().toList();
			Set<Date> sdates = new HashSet<Date>();
			for (int i = 0; i < commits.size(); i++) {
				sdates.add(new Date(commits.get(i).getCommitDate().getTime()));
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
			Date createdAtDate = githubclientApi.getRepositoryById(repoId).getCreatedAt();
			Date lastActivityDate = githubclientApi.getRepositoryById(repoId).getUpdatedAt();

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
	
	/**
	 * Get a list of jobs of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return List of jobs of a project or null if fail.
	 */
	private List<CustomGithubApiJob> getRepositoryJobs(Long projectId) {
		try {
			List<GHWorkflowRun> ghWorkFlowRuns = githubclientApi.getRepositoryById(projectId).queryWorkflowRuns().status(Status.COMPLETED).list().toList();
			List<CustomGithubApiJob> ghWorkflowJobs = new ArrayList<CustomGithubApiJob>();
					
			for (GHWorkflowRun ghWorkFlowRun : ghWorkFlowRuns) {
				List<GHWorkflowJob> ghCurrentWorkflowJobs = ghWorkFlowRun.listAllJobs().toList();
				
				for (GHWorkflowJob job: ghCurrentWorkflowJobs) {
					ghWorkflowJobs.add(new CustomGithubApiJob(job));
				}
			}
			
			return ghWorkflowJobs;
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Get a list of releases of a project.
	 * 
	 * @param projectId ID of the project.
	 * @return List of releases of a project or null if fail.
	 */
	private List<CustomGithubApiRelease> getRepositoryReleases(Long projectId) {
		try {
			List<GHRelease> releases = githubclientApi.getRepositoryById(projectId).listReleases().toList();
			
			List<CustomGithubApiRelease> customGitlabApiReleases = new ArrayList<CustomGithubApiRelease>();
						
			for (GHRelease release : releases) {
				CustomGithubApiRelease newCustomGitlabApiRelease= new CustomGithubApiRelease(release);
				customGitlabApiReleases.add(newCustomGitlabApiRelease);
			}
			
			return customGitlabApiReleases;
		} catch (IOException e) {
			return null;
		}
	}
}
