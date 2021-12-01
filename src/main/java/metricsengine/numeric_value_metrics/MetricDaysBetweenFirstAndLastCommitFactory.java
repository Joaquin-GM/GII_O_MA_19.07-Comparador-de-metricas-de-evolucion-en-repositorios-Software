package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

public class MetricDaysBetweenFirstAndLastCommitFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 7135615653028365473L;

	public MetricDaysBetweenFirstAndLastCommitFactory() {}

	@Override
	public Metric getMetric() {
		return MetricDaysBetweenFirstAndLastCommit.getInstance();
	}

}
