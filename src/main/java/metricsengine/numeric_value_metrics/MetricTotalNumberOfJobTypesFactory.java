package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

/**
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class MetricTotalNumberOfJobTypesFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private static final long serialVersionUID = 5564575072523772927L;

	/**
	 * Constructor.
	 *
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	public MetricTotalNumberOfJobTypesFactory() {}

	/* (non-Javadoc)
	 * @see metricsengine.MetricFactory#getMetric()
	 */
	@Override
	public Metric getMetric() {
		return MetricTotalNumberOfJobTypes.getInstance();
	}

}
