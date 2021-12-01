package app.listeners;

import java.io.Serializable;

/**
 * Listener rol on Listener Pattern Design.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public interface Listener<T extends Event> extends Serializable {
	/**
	 * Action that develops when the event is triggered.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param event Event that must trigger for the option to be performed. 
	 */
	void on(T event);
}
