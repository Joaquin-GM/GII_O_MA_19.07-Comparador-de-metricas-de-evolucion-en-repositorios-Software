package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

public class MetricPercentageClosedIssuesFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -256167150085853852L;

	public MetricPercentageClosedIssuesFactory() {
	}

	@Override
	public Metric getMetric() {
		return MetricPercentageClosedIssues.getInstance();
	}

}
