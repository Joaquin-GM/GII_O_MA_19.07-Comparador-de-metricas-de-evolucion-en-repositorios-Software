package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricPeakChangeFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -9113173780385568143L;

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public MetricPeakChangeFactory() {
	}

	/* (non-Javadoc)
	 * @see metricsengine.MetricFactory#getMetric()
	 */
	@Override
	public Metric getMetric() {
		return MetricPeakChange.getInstance();
	}

}
