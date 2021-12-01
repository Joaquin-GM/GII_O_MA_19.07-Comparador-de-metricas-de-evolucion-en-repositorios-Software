package app.listeners;

import java.io.Serializable;

/**
 * Event that triggers when the list of projects is modified.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class RepositoriesCollectionUpdatedEvent implements Serializable, Event {

	private static final long serialVersionUID = 1061622858938632134L;

	/**
	 * Empty constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public RepositoriesCollectionUpdatedEvent() {}
}
