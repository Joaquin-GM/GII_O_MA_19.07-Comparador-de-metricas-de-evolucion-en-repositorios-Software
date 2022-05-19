package app.listeners;

import java.io.Serializable;

import datamodel.RepositorySourceType;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;

/**
 * Connection change event.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class ConnectionChangedEvent implements Serializable, Event {

	private static final long serialVersionUID = 2055762274587204233L;

	/**
	 * Type of connection before making the change.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private EnumConnectionType connectionTypeBefore;

	/**
	 * Type of connection after making the change.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private EnumConnectionType connectionTypeAfter;

	/**
	 * 
	 */
	private RepositorySourceType repositorySourceType;

	public RepositorySourceType getRepositorySourceType() {
		return repositorySourceType;
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @param connectionTypeBefore Type of connection before making the change.
	 * @param connectionTypeAfter  Type of connection after making the change.
	 * @param repositorySourceType Type of data source (GitLab, GitHub)
	 */
	public ConnectionChangedEvent(EnumConnectionType connectionTypeBefore, EnumConnectionType connectionTypeAfter,
			RepositorySourceType repositorySourceType) {
		this.connectionTypeBefore = connectionTypeBefore;
		this.connectionTypeAfter = connectionTypeAfter;
		this.repositorySourceType = repositorySourceType;
	}

	/**
	 * Gets the type of connection before making the change.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the type of connection before making the change.
	 */
	public EnumConnectionType getConnectionTypeBefore() {
		return connectionTypeBefore;
	}

	/**
	 * Gets the type of connection after making the change.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the type of connection after making the change.
	 */
	public EnumConnectionType getConnectionTypeAfter() {
		return connectionTypeAfter;
	}

}
