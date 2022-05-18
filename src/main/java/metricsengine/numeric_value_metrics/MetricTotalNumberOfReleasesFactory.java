package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

/**
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class MetricTotalNumberOfReleasesFactory implements MetricFactory {

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
	public MetricTotalNumberOfReleasesFactory() {}

	/* (non-Javadoc)
	 * @see metricsengine.MetricFactory#getMetric()
	 */
	@Override
	public Metric getMetric() {
		return MetricTotalNumberOfReleases.getInstance();
	}

}
