package app;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.listeners.ConnectionChangedEvent;
import app.listeners.Listener;
import datamodel.Repository;
import datamodel.RepositoryInternalMetrics;
import datamodel.RepositorySourceType;
import datamodel.User;
import exceptions.RepositoryDataSourceException;
import gui.views.connectionforms.ConnectionInfoComponent;
import repositorydatasource.RepositoryDataSource;
import repositorydatasource.RepositoryDataSourceFactory;
import repositorydatasource.RepositoryDataSourceFactoryGithub;
import repositorydatasource.RepositoyDataSourceFactoryGitlab;

/**
 * Wrapper of RepositoryDataSource with listeners and updateRepository function.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class RepositoryDataSourceService implements Serializable, RepositoryDataSource {
	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryDataSourceService.class);

	private static final long serialVersionUID = -6197642368639361682L;

	private static RepositoryDataSourceService instance;

	private RepositoryDataSource repositoryDataSourceGitLab;
	private RepositoryDataSource repositoryDataSourceGitHub;

	public RepositoryDataSource getRepositoryDataSourceGitLab() {
		return repositoryDataSourceGitLab;
	}

	public RepositoryDataSource getRepositoryDataSourceGitHub() {
		return repositoryDataSourceGitHub;
	}

	private Set<Listener<ConnectionChangedEvent>> connectionChangedEventListeners = new HashSet<>();

	/**
	 * Constructor that instantiates the repository data source.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private RepositoryDataSourceService() {
		this.repositoryDataSourceGitLab = new RepositoyDataSourceFactoryGitlab().getRepositoryDataSource();
		// TODO tengo que instanciar también con GitHub y usar una u otra instancia
		// dependiendo del repositorio
		this.repositoryDataSourceGitHub = new RepositoryDataSourceFactoryGithub().getRepositoryDataSource();

	}

	/**
	 * Gets the single instance.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the unique instance of the repository data source service
	 */
	public static RepositoryDataSourceService getInstance() {
		if (instance == null)
			instance = new RepositoryDataSourceService();
		return instance;
	}

	/**
	 * Sets the repository data source through a repository data source factory.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repositoryDataSourceFactory the repositoryDataSource to set
	 */
	public void setRepositoryDataSource(RepositoryDataSourceFactory repositoryDataSourceFactory,
			RepositorySourceType repositorySourceType) {
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			this.repositoryDataSourceGitLab = repositoryDataSourceFactory.getRepositoryDataSource();
		} else {
			this.repositoryDataSourceGitHub = repositoryDataSourceFactory.getRepositoryDataSource();
		}
	}

	/**
	 * Adds a connection changed listener.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param listener
	 */
	public void addConnectionChangedEventListener(Listener<ConnectionChangedEvent> listener) {
		connectionChangedEventListeners.add(listener);
	}

	/**
	 * Removes a connection changed listener.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param listener
	 */
	public void removeConnectionChangedEventListener(Listener<ConnectionChangedEvent> listener) {
		connectionChangedEventListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.RepositoryDataSource#connect()
	 */
	@Override
	public void connect(RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		EnumConnectionType before = getConnectionType(repositorySourceType);
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			this.repositoryDataSourceGitLab.connect(repositorySourceType);
		} else {
			this.repositoryDataSourceGitHub.connect(repositorySourceType);
		}
		EnumConnectionType after = getConnectionType(repositorySourceType);
		connectionChangedEventListeners.forEach(l -> l.on(new ConnectionChangedEvent(before, after, repositorySourceType)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.RepositoryDataSource#connect(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void connect(String username, String password, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		EnumConnectionType before = getConnectionType(repositorySourceType);
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			this.repositoryDataSourceGitLab.connect(username, password, repositorySourceType);
		} else {
			this.repositoryDataSourceGitHub.connect(username, password, repositorySourceType);
		}

		EnumConnectionType after = getConnectionType(repositorySourceType);
		connectionChangedEventListeners.forEach(l -> l.on(new ConnectionChangedEvent(before, after, repositorySourceType)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.RepositoryDataSource#connect(java.lang.String)
	 */
	@Override
	public void connect(String token, RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		EnumConnectionType before = getConnectionType(repositorySourceType);
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			this.repositoryDataSourceGitLab.connect(token, repositorySourceType);
		} else {
			this.repositoryDataSourceGitHub.connect(token, repositorySourceType);
		}
		EnumConnectionType after = getConnectionType(repositorySourceType);
		connectionChangedEventListeners.forEach(l -> l.on(new ConnectionChangedEvent(before, after, repositorySourceType)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.RepositoryDataSource#disconnect()
	 */
	@Override
	public void disconnect(RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		EnumConnectionType before = getConnectionType(repositorySourceType);
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			this.repositoryDataSourceGitLab.disconnect(repositorySourceType);
		} else {
			this.repositoryDataSourceGitHub.disconnect(repositorySourceType);
		}
		EnumConnectionType after = getConnectionType(repositorySourceType);
		connectionChangedEventListeners.forEach(l -> l.on(new ConnectionChangedEvent(before, after, repositorySourceType)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.RepositoryDataSource#getConnectionType()
	 */
	@Override
	public EnumConnectionType getConnectionType(RepositorySourceType repositorySourceType) {
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			return this.repositoryDataSourceGitLab.getConnectionType(repositorySourceType);
		} else {
			return this.repositoryDataSourceGitHub.getConnectionType(repositorySourceType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.RepositoryDataSource#getCurrentUser()
	 */
	@Override
	public User getCurrentUser(RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			return this.repositoryDataSourceGitLab.getCurrentUser(repositorySourceType);
		} else {
			return this.repositoryDataSourceGitHub.getCurrentUser(repositorySourceType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.RepositoryDataSource#getCurrentUserRepositories()
	 */
	@Override
	public Collection<Repository> getCurrentUserRepositories(RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			return this.repositoryDataSourceGitLab.getCurrentUserRepositories(repositorySourceType);
		} else {
			return this.repositoryDataSourceGitHub.getCurrentUserRepositories(repositorySourceType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repositorydatasource.RepositoryDataSource#getAllUserRepositories(java.lang.
	 * String)
	 */
	@Override
	public Collection<Repository> getAllUserRepositories(String username, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			return this.repositoryDataSourceGitLab.getAllUserRepositories(username, repositorySourceType);
		} else {
			return this.repositoryDataSourceGitHub.getAllUserRepositories(username, repositorySourceType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repositorydatasource.RepositoryDataSource#getAllGroupRepositories(java.lang.
	 * String)
	 */
	@Override
	public Collection<Repository> getAllGroupRepositories(String groupName, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			return this.repositoryDataSourceGitLab.getAllGroupRepositories(groupName, repositorySourceType);
		} else {
			return this.repositoryDataSourceGitHub.getAllGroupRepositories(groupName, repositorySourceType);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repositorydatasource.RepositoryDataSource#getRepository(java.lang.String)
	 */
	@Override
	public Repository getRepository(String repositoryHTTPSURL, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			return this.repositoryDataSourceGitLab.getRepository(repositoryHTTPSURL, repositorySourceType);
		} else {
			return this.repositoryDataSourceGitHub.getRepository(repositoryHTTPSURL, repositorySourceType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.RepositoryDataSource#getRepository(int)
	 */
	@Override
	public Repository getRepository(Long repositoryId, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			return this.repositoryDataSourceGitLab.getRepository(repositoryId, repositorySourceType);
		} else {
			return this.repositoryDataSourceGitHub.getRepository(repositoryId, repositorySourceType);
		}
	}

	/**
	 * Updates the id, name and url of a repository.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repository Repository to update.
	 * @throws RepositoryDataSourceException If the repository could not be
	 *                                       recovered.
	 */
	public void updateRepository(Repository repository, RepositorySourceType repositorySourceType)
			throws RepositoryDataSourceException {
		Repository repo;

		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			repo = this.repositoryDataSourceGitLab.getRepository(repository.getId(), repositorySourceType);
		} else {
			repo = this.repositoryDataSourceGitHub.getRepository(repository.getId(), repositorySourceType);
		}

		repository.setId(repo.getId());
		repository.setName(repo.getName());
		repository.setUrl(repo.getUrl());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repositorydatasource.RepositoryDataSource#getRepositoryInternalMetrics(
	 * datamodel.Repository)
	 */
	@Override
	public RepositoryInternalMetrics getRepositoryInternalMetrics(Repository repository,
			RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			return this.repositoryDataSourceGitLab.getRepositoryInternalMetrics(repository, repositorySourceType);
		} else {
			return this.repositoryDataSourceGitHub.getRepositoryInternalMetrics(repository, repositorySourceType);
		}
	}
}