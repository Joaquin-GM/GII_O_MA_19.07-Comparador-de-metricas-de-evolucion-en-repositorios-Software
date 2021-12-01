package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricAverageDaysBetweenCommitsFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 6564575072523761823L;

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public MetricAverageDaysBetweenCommitsFactory() {}

	/* (non-Javadoc)
	 * @see metricsengine.MetricFactory#getMetric()
	 */
	@Override
	public Metric getMetric() {
		return MetricAverageDaysBetweenCommits.getInstance();
	}

}
