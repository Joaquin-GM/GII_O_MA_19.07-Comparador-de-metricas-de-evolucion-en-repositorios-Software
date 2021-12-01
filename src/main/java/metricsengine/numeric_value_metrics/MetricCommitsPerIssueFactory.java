package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

public class MetricCommitsPerIssueFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -5455636812931452479L;

	public MetricCommitsPerIssueFactory() {}

	@Override
	public Metric getMetric() {
		return MetricCommitsPerIssue.getInstance();
	}

}
