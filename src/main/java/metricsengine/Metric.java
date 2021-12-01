package metricsengine;

import java.io.Serializable;

import datamodel.Repository;
import metricsengine.values.IValue;

/**
 * Defines the interface of a metric.
 * 
 * @author MALB
 *
 */
public interface Metric extends Serializable {
	
	@FunctionalInterface
	public interface EvaluationFunction {
		EvaluationResult evaluate(IValue value, IValue minValue, IValue maxValue);
	}
	
	String getName();

	MetricDescription getDescription();

	IValue getValueMinDefault();

	IValue getValueMaxDefault();

	/**
	 * Calculate the metric for a repository passed by parameter and add it to the set passed also by parameter.
	 * 
	 * @param repository Entity to be measured
	 * @param metricConfig Configuration to apply in the metric. 
	 * @param metricsResults Collector where to store the result.
	 * @return The calculated value or ValueUncalculated if imposible to calculate.
	 */
	IValue calculate(Repository repository,MetricConfiguration metricConfig, MetricsResults metricsResults);

	EvaluationResult evaluate(IValue measuredValue);

	EvaluationFunction getEvaluationFunction();
}
