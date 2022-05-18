package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

/**
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class MetricReleasesLastYearFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private static final long serialVersionUID = 4464575072523772923L;

	/**
	 * Constructor.
	 *
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	public MetricReleasesLastYearFactory() {}

	/* (non-Javadoc)
	 * @see metricsengine.MetricFactory#getMetric()
	 */
	@Override
	public Metric getMetric() {
		return MetricReleasesLastYear.getInstance();
	}

}
