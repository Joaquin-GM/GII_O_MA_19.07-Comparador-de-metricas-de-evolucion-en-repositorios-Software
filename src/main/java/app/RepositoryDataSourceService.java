package app;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import app.listeners.ConnectionChangedEvent;
import app.listeners.Listener;
import datamodel.Repository;
import datamodel.RepositoryInternalMetrics;
import datamodel.User;
import exceptions.RepositoryDataSourceException;
import repositorydatasource.RepositoryDataSource;
import repositorydatasource.RepositoryDataSourceFactory;
import repositorydatasource.RepositoyDataSourceFactoryGitlab;

/**
 * Wrapper of RepositoryDataSource with listeners and updateRepository function.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class RepositoryDataSourceService implements Serializable, RepositoryDataSource {
	
	private static final long serialVersionUID = -6197642368639361682L;

	private static RepositoryDataSourceService instance;
	
	private RepositoryDataSource repositoryDataSource;
	
	private Set<Listener<ConnectionChangedEvent>> connectionChangedEventListeners = new HashSet<>();
	
	/**
	 * Constructor that instantiates the repository data sorce.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private RepositoryDataSourceService() {
		this.repositoryDataSource = new RepositoyDataSourceFactoryGitlab().getRepositoryDataSource();
	}

	/**
	 * Gets the single instance.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the unique instance of the repository data source service
	 */
	public static RepositoryDataSourceService getInstance() {
		if (instance == null) instance = new RepositoryDataSourceService();
		return instance;
	}
	
	/**
	 * Sets the repository data source through a repository data source factory.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repositoryDataSource the repositoryDataSource to set
	 */
	public void setRepositoryDataSource(RepositoryDataSourceFactory repositoryDataSourceFactory) {
		this.repositoryDataSource = repositoryDataSourceFactory.getRepositoryDataSource();
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

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#connect()
	 */
	@Override
	public void connect() throws RepositoryDataSourceException {
		EnumConnectionType before = getConnectionType();
		this.repositoryDataSource.connect();
		EnumConnectionType after = getConnectionType();
		connectionChangedEventListeners.forEach(l -> l.on(new ConnectionChangedEvent(before, after)));		
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#connect(java.lang.String, java.lang.String)
	 */
	@Override
	public void connect(String username, String password) throws RepositoryDataSourceException {
		EnumConnectionType before = getConnectionType();
		this.repositoryDataSource.connect(username, password);;
		EnumConnectionType after = getConnectionType();
		connectionChangedEventListeners.forEach(l -> l.on(new ConnectionChangedEvent(before, after)));
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#connect(java.lang.String)
	 */
	@Override
	public void connect(String token) throws RepositoryDataSourceException {
		EnumConnectionType before = getConnectionType();
		this.repositoryDataSource.connect(token);;
		EnumConnectionType after = getConnectionType();
		connectionChangedEventListeners.forEach(l -> l.on(new ConnectionChangedEvent(before, after)));
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#disconnect()
	 */
	@Override
	public void disconnect() throws RepositoryDataSourceException {
		EnumConnectionType before = getConnectionType();
		this.repositoryDataSource.disconnect();
		EnumConnectionType after = getConnectionType();
		connectionChangedEventListeners.forEach(l -> l.on(new ConnectionChangedEvent(before, after)));
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#getConnectionType()
	 */
	@Override
	public EnumConnectionType getConnectionType() {
		return this.repositoryDataSource.getConnectionType();
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#getCurrentUser()
	 */
	@Override
	public User getCurrentUser() throws RepositoryDataSourceException {
		return this.repositoryDataSource.getCurrentUser();
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#getCurrentUserRepositories()
	 */
	@Override
	public Collection<Repository> getCurrentUserRepositories() throws RepositoryDataSourceException {
		return this.repositoryDataSource.getCurrentUserRepositories();
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#getAllUserRepositories(java.lang.String)
	 */
	@Override
	public Collection<Repository> getAllUserRepositories(String username) throws RepositoryDataSourceException {
		return this.repositoryDataSource.getAllUserRepositories(username);
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#getAllGroupRepositories(java.lang.String)
	 */
	@Override
	public Collection<Repository> getAllGroupRepositories(String groupName) throws RepositoryDataSourceException {
		return this.repositoryDataSource.getAllGroupRepositories(groupName);
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#getRepository(java.lang.String)
	 */
	@Override
	public Repository getRepository(String repositoryHTTPSURL) throws RepositoryDataSourceException {
		return this.repositoryDataSource.getRepository(repositoryHTTPSURL);
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#getRepository(int)
	 */
	@Override
	public Repository getRepository(int repositoryId) throws RepositoryDataSourceException {
		return this.repositoryDataSource.getRepository(repositoryId);
	}

	/**
	 * Updates the id, name and url of a repository.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repository Repository to update.
	 * @throws RepositoryDataSourceException If the repository could not be recovered.
	 */
	public void updateRepository(Repository repository) throws RepositoryDataSourceException {
		Repository repo = this.repositoryDataSource.getRepository(repository.getId());
		repository.setId(repo.getId());
		repository.setName(repo.getName());
		repository.setUrl(repo.getUrl());
	}

	/* (non-Javadoc)
	 * @see repositorydatasource.RepositoryDataSource#getRepositoryInternalMetrics(datamodel.Repository)
	 */
	@Override
	public RepositoryInternalMetrics getRepositoryInternalMetrics(Repository repository)
			throws RepositoryDataSourceException {
		return this.repositoryDataSource.getRepositoryInternalMetrics(repository);
	}
}
