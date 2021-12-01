package metricsengine;

import java.io.Serializable;

import datamodel.Repository;
import exceptions.MetricsEngineException;
import metricsengine.values.IValue;

/**
 * Wrapper for a metric that allows you to use other minimum and maximum values, 
 * instead of the default values.
 *  
 * @author MALB
 *
 */
public class MetricConfiguration implements Metric, Serializable {
	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -875910175265274713L;

	/**
	 * Metric.
	 */
	private MetricFactory metricFactory;
	
	/**
	 * Minimum value.
	 */
	private IValue valueMin;
	
	/**
	 * Maximum value.
	 */
	private IValue valueMax;
	
	/**
	 * Constructor.
	 * 
	 * @param metric Metric to configure.
	 * @param valueMin Minimum value.
	 * @param valueMax Maximum value.
	 */
	public MetricConfiguration(MetricFactory metricFactory, IValue valueMin, IValue valueMax) {
		if (metricFactory == null)
			throw new IllegalArgumentException("There can be no metric configuration without specifying a metric");
		if (valueMin == null || valueMax == null)
			throw new IllegalArgumentException("There can be no metric configuration without configuration values");
		this.metricFactory = metricFactory;
		this.valueMin = valueMin;
		this.valueMax = valueMax;
	}
	
	/**
	 * Constructor that sets the metric with its default values.
	 * 
	 * @param metric Metric to configure.
	 */
	public MetricConfiguration(MetricFactory metricFactory) {
		if (metricFactory == null)
			throw new IllegalArgumentException("There can be no metric configuration without specifying a metric");
		this.metricFactory = metricFactory;
		this.valueMin = metricFactory.getMetric().getValueMinDefault();
		this.valueMax = metricFactory.getMetric().getValueMaxDefault();
	}
	
	/**
	 * Gets the metric.
	 * 
	 * @return The metric.
	 */
	public MetricFactory getMetricFactory() {
		return metricFactory;
	}
	
	/**
	 * Gets the metric.
	 * 
	 * @return The metric.
	 */
	public Metric getMetric() {
		return metricFactory.getMetric();
	}

	/**
	 * Gets the minimum value.
	 * 
	 * @return The minimum value.
	 */
	public IValue getValueMin() {
		return valueMin;
	}

	/**
	 * Gets the maximum value.
	 * 
	 * @return The maximum value.
	 */
	public IValue getValueMax() {
		return valueMax;
	}

	/* (non-Javadoc)
	 * @see metricsengine.IMetric#calculate(repositorydatasource.model.Repository, metricsengine.MetricConfiguration, metricsengine.MetricsResults)
	 */
	@Override
	public IValue calculate(Repository repository, MetricConfiguration metricConfig, MetricsResults metricsResults) {
		return getMetric().calculate(repository, metricConfig, metricsResults);
	}
	
	/**
	 * 
	 * Execute the calculate method of IMetric taking the same instance 
	 * as an argument to the MetricConfiguration parameter.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repository  Entity to be measured
	 * @param metricsResults Collector where to store the result
	 * @return The calculated value.
	 * @throws MetricsEngineException 
	 * @see {@link Metric#calculate(Repository, MetricConfiguration, MetricsResults)}
	 */
	public IValue calculate(Repository repository, MetricsResults metricsResults) {
		return getMetric().calculate(repository, this, metricsResults);
	}

	@Override
	public EvaluationResult evaluate(IValue value) {
		return getMetric().getEvaluationFunction().evaluate(value, valueMin, valueMax);
	}

	@Override
	public IValue getValueMaxDefault() {
		return getMetric().getValueMaxDefault();
	}

	@Override
	public IValue getValueMinDefault() {
		return getMetric().getValueMinDefault();
	}

	@Override
	public MetricDescription getDescription() {
		return getMetric().getDescription();
	}

	@Override
	public String getName() {
		return getMetric().getName();
	}

	@Override
	public EvaluationFunction getEvaluationFunction() {
		return getMetric().getEvaluationFunction();
	}
}
