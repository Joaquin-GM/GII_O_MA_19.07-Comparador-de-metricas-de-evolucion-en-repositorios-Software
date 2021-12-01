package metricsengine.numeric_value_metrics;

import datamodel.Repository;
import metricsengine.EvaluationResult;
import metricsengine.Measure;
import metricsengine.MetricDescription;
import metricsengine.MetricsResults;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueDecimal;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ProjectEvaluation extends NumericValueMetricTemplate {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -8287665587103409958L;

	/**
	 * Default metric description.
	 */
	public static final MetricDescription DEFAULT_METRIC_DESCRIPTION = new MetricDescription(
			"Metric Calification",
			"Metric Calification",
			"",
			"",
			"Evolution Orientation",
			"What percentage of metrics are within a favorable range?",
			"MC = (GM/TNM) * 100. MC = Metric calification in percentage. TNM = Total number of measured values. GM = Number of good measures",
			"TNI, NCI:Repository",
			"MC >= 0 and MC <= 100. Better large values",
			MetricDescription.EnumTypeOfScale.RATIO,
			"GM,TNM: Count");
	
	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueDecimal(50.0);
	
	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueDecimal(100.0);
	
	private static ProjectEvaluation instance = null;
	
	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private ProjectEvaluation() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, NumericValueMetricTemplate.EVAL_FUNC_GREATER_THAN_Q1);
	}
	
	public static ProjectEvaluation getInstance() {
		if (instance == null) instance = new ProjectEvaluation();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see metricsengine.numeric_value_metrics.NumericValueMetricTemplate#check(datamodel.Repository)
	 */
	@Override
	protected Boolean check(Repository repository) {
		if(repository.getMetricsResults() != null && 
				repository.getMetricsResults().getMeasures() != null &&
				repository.getMetricsResults().getMeasures().size() > 0)
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see metricsengine.numeric_value_metrics.NumericValueMetricTemplate#run(datamodel.Repository)
	 */
	@Override
	protected NumericValue run(Repository repository) {
		int conGoodMetrics = 0;
		
		MetricsResults metricsResults = repository.getMetricsResults();
		if (metricsResults != null && metricsResults.getMeasures() != null) {
			for (Measure measure : metricsResults.getMeasures()) {
				if(measure.evaluate() == EvaluationResult.GOOD && 
						measure.getMetricConfiguration().getMetric().getClass() != this.getClass())
					conGoodMetrics++;
			} 
		}
		
		Double result = (double) (conGoodMetrics*100/metricsResults.getMeasures().size());
		return new ValueDecimal(result);
	}

}
