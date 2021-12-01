package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

public class MetricChangeActivityRangeFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -7569247147847455904L;

	public MetricChangeActivityRangeFactory() {}

	@Override
	public Metric getMetric() {
		return MetricChangeActivityRange.getInstance();
	}

}
