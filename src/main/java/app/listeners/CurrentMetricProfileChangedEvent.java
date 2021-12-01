package app.listeners;

import java.io.Serializable;

import metricsengine.MetricProfile;

/**
 * Event that triggers when the current metric profile has been replaced by another profile.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class CurrentMetricProfileChangedEvent implements Serializable, Event {

	private static final long serialVersionUID = 2616585155826316277L;

	/**
	 * Metric Profile before making the change.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private MetricProfile previousMetricProfile;
	
	/**
	 * Metric Profile after making the change.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private MetricProfile newMetricProfile;

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param previousMetricProfile Metric Profile before making the change.
	 * @param newMetricProfile Metric Profile after making the change.
	 */
	public CurrentMetricProfileChangedEvent(MetricProfile previousMetricProfile, MetricProfile newMetricProfile) {
		this.previousMetricProfile = previousMetricProfile;
		this.newMetricProfile = newMetricProfile;
	}

	/**
	 * Gets the previous metric profile.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the previous metric profile.
	 */
	public MetricProfile getPreviousMetricProfile() {
		return previousMetricProfile;
	}

	/**
	 * Gets the new metric profile.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the new metric profile.
	 */
	public MetricProfile getNewMetricProfile() {
		return newMetricProfile;
	}

}
