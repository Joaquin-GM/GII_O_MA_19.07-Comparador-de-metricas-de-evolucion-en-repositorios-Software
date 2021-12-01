package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricAverageDaysToCloseAnIssueFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 7698838893409360877L;

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public MetricAverageDaysToCloseAnIssueFactory() {}

	/* (non-Javadoc)
	 * @see metricsengine.MetricFactory#getMetric()
	 */
	@Override
	public Metric getMetric() {
		return MetricAverageDaysToCloseAnIssue.getInstance();
	}

}
