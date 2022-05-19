package metricsengine.numeric_value_metrics;

import datamodel.Repository;
import metricsengine.EvaluationResult;
import metricsengine.Measure;
import metricsengine.Metric;
import metricsengine.MetricConfiguration;
import metricsengine.MetricDescription;
import metricsengine.MetricsResults;
import metricsengine.values.IValue;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueUncalculated;
import repositorydatasource.RepositoryDataSourceUsingGitlabAPI;

/**
 * Partially implements the IMetric interface.
 * 
 * @author MALB
 *
 */
public abstract class NumericValueMetricTemplate implements Metric {	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 8459616601304750512L;

	protected transient static final EvaluationFunction EVAL_FUNC_GREATER_THAN_Q1 = (measuredValue, minValue, maxValue) -> {
		try {
			Double value, min;
			value = NumericValueMetricTemplate.formatTwoDecimals(((NumericValue) measuredValue).doubleValue());
			min = NumericValueMetricTemplate.formatTwoDecimals(((NumericValue) minValue).doubleValue());
			if (value > min) return EvaluationResult.GOOD;
			else if (value.equals(min)) return EvaluationResult.WARNING;
			else return EvaluationResult.BAD;
		} catch (Exception e){
			return EvaluationResult.BAD;
		}
	};

	protected transient static final EvaluationFunction EVAL_FUNC_BETWEEN_Q1_Q3 = (measuredValue, minValue, maxValue) -> {
		try {
			Double value, min, max;
			value = NumericValueMetricTemplate.formatTwoDecimals(((NumericValue) measuredValue).doubleValue());
			min = NumericValueMetricTemplate.formatTwoDecimals(((NumericValue) minValue).doubleValue());
			max = NumericValueMetricTemplate.formatTwoDecimals(((NumericValue) maxValue).doubleValue());
			if (value > min && value < max) return EvaluationResult.GOOD;
			else if (value.equals(min)  || value.equals(max)) return EvaluationResult.WARNING;
			else return EvaluationResult.BAD;
		} catch (Exception e){
			return EvaluationResult.BAD;
		}
	};

	/**
	 * The description of the metric.
	 */
	private MetricDescription description;
	
	/**
	 * Minimum value by default.
	 */
	private IValue valueMinDefault;
	
	/**
	 * Maximum value by default.
	 */
	private IValue valueMaxDefault;
	
	private transient EvaluationFunction evaluationFunction;
	
	/**
	 * Constructor of a metric that establishes the description and the default values.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param description Description of the metric.
	 * @param valueMinDefault Minimum value by default.
	 * @param valueMaxDefault Maximum value by default.
	 */
	protected NumericValueMetricTemplate(MetricDescription description, NumericValue valueMinDefault, NumericValue valueMaxDefault, EvaluationFunction evaluationFunction) {
		setDescription(description);
		setValueMinDefault(valueMinDefault);
		setValueMaxDefault(valueMaxDefault);
		setEvaluationFunction(evaluationFunction);
	}

	/**
	 * Gets the name of the metric.
	 * 
	 * @return The name of the metric.
	 */
	@Override
	public String getName() {
		return description.getName();
	}

	/**
	 * Gets the description of the metric.
	 * 
	 * @return The description of the metric. 
	 */
	@Override
	public MetricDescription getDescription() {
		return description;
	}

	/**
	 * Gets the minimum value by default.
	 * 
	 * @return The minimum value by default.
	 */
	@Override
	public IValue getValueMinDefault() {
		return valueMinDefault;
	}

	/**
	 * Gets the maximum value by default.
	 * 
	 * @return The maximum value by default.
	 */
	@Override
	public IValue getValueMaxDefault() {
		return valueMaxDefault;
	}
	
	@Override
	public EvaluationFunction getEvaluationFunction() {
		return evaluationFunction;
	}

	/**
	 * Sets the description of the metric.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param description the description to set
	 */
	private void setDescription(MetricDescription description) {
		if(description == null)
			throw new IllegalArgumentException("'description' can't be null");
		this.description = description;
	}

	/**
	 * Sets the minimum value by default.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param valueMinDefault the valueMinDefault to set
	 */
	private void setValueMinDefault(IValue valueMinDefault) {
		if(valueMinDefault == null)
			throw new IllegalArgumentException("'valueMinDefault' can't be null");
		this.valueMinDefault = valueMinDefault;
	}

	/**
	 * Sets the maximum value by default.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param valueMaxDefault the valueMaxDefault to set
	 */
	private void setValueMaxDefault(IValue valueMaxDefault) {
		if(valueMaxDefault == null)
			throw new IllegalArgumentException("'valueMaxDefault' can't be null");
		this.valueMaxDefault = valueMaxDefault;
	}

	/**
	 * Sets the evaluationFunction.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param evaluationFunction the evaluationFunction to set
	 */
	private void setEvaluationFunction(EvaluationFunction evaluationFunction) {
		this.evaluationFunction = evaluationFunction;
	}

	/* (non-Javadoc)
	 * @see metricsengine.IMetric#calculate(repositorydatasource.model.Repository, metricsengine.MetricsResults)
	 */
	@Override
	public IValue calculate(Repository repository, MetricConfiguration metricConfig, MetricsResults metricsResults) {
		IValue value;
		if (repository == null || metricConfig == null ||  metricsResults == null) throw new IllegalArgumentException("All parameters must be not null");
		value = (check(repository))?run(repository):new ValueUncalculated();
		metricsResults.addMeasure(new Measure(metricConfig, value));
		return value;
	}
	
	/**
	 * Check if it is possible to calculate the metric.
	 * 
	 * @param repository Repository from which to obtain the metric
	 * @return True if it is possible to calculate the metric, false otherwise.
	 */
	protected abstract Boolean check(Repository repository);
	
	/**
	 * Calculate the metric.
	 * 
	 * @param repository Repository from which to obtain the metric
	 * @return The calculated value
	 */
	protected abstract NumericValue run(Repository repository);
	
	/* (non-Javadoc)
	 * @see metricsengine.Metric#evaluate(metricsengine.values.IValue)
	 */
	@Override
	public EvaluationResult evaluate(IValue measuredValue) {
		if (measuredValue instanceof NumericValue)
			return getEvaluationFunction().evaluate(measuredValue, valueMinDefault, valueMaxDefault);
		else
			return EvaluationResult.BAD;
	}

	protected static Double formatTwoDecimals(Double number) {
		return Math.round(number * 100.0) / 100.0;
	}
}
