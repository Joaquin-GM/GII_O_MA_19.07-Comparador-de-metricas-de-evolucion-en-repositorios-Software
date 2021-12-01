package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

public class MetricTotalNumberOfIssuesFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -2581601589904761326L;

	public MetricTotalNumberOfIssuesFactory() {
	}

	@Override
	public Metric getMetric() {
		return MetricTotalNumberOfIssues.getInstance();
	}

}
