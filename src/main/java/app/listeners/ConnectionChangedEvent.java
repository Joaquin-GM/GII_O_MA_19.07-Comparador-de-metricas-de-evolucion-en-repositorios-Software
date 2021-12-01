package app.listeners;

import java.io.Serializable;

import repositorydatasource.RepositoryDataSource.EnumConnectionType;

/**
 * Connection change event.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ConnectionChangedEvent implements Serializable, Event{

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
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param connectionTypeBefore Type of connection before making the change.
	 * @param connectionTypeAfter Type of connection after making the change.
	 */
	public ConnectionChangedEvent(EnumConnectionType connectionTypeBefore, EnumConnectionType connectionTypeAfter) {
		this.connectionTypeBefore = connectionTypeBefore;
		this.connectionTypeAfter = connectionTypeAfter;
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
