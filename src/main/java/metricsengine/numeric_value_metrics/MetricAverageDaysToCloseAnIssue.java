package metricsengine.numeric_value_metrics;

import java.util.Collection;

import datamodel.Repository;
import metricsengine.MetricDescription;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueDecimal;

/**
 * Computes the average of days to close an issue.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricAverageDaysToCloseAnIssue extends NumericValueMetricTemplate {
	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 664935243481317998L;

	/**
	 * Default metric description.
	 */
	public static final MetricDescription DEFAULT_METRIC_DESCRIPTION = new MetricDescription(
			"TI1",
			"Average of days to close an issue",
			"",
			"",
			"Time constraints",
			"How long does it take to close an issue?",
			"ADCI = SUM(DCI) / NCI. ADCI = Average of days to close an issue. NCI = Number of closed issues. DCI = Vector with the days it took to close each issue.",
			"DCI, NCI:Repository",
			"ADCI >= 0. Better small values",
			MetricDescription.EnumTypeOfScale.RATIO,
			"NCI:Count, DCI: Set of Counts");
		
	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueDecimal(2.2);
	
	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueDecimal(19.41);
	
	private static MetricAverageDaysToCloseAnIssue instance = null;
			
	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private MetricAverageDaysToCloseAnIssue() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, NumericValueMetricTemplate.EVAL_FUNC_BETWEEN_Q1_Q3);
	}
	
	public static MetricAverageDaysToCloseAnIssue getInstance() {
		if (instance == null) instance = new MetricAverageDaysToCloseAnIssue();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see metricsengine.metrics.AMetric#check(repositorydatasource.model.Repository)
	 */
	@Override
	public Boolean check(Repository repository) {
		Collection<Integer> daysToCloseEachIssue = repository.getRepositoryInternalMetrics().getDaysToCloseEachIssue();
		Integer numberOfClosedIssues = repository.getRepositoryInternalMetrics().getNumberOfClosedIssues();
		
		if (daysToCloseEachIssue == null ||
				numberOfClosedIssues == null ||
				numberOfClosedIssues != daysToCloseEachIssue.size() ||
				numberOfClosedIssues <= 0
				) {
			return false;
		} else {
			for (Integer numDays : daysToCloseEachIssue) {
				if (numDays == null || numDays < 0) return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see metricsengine.metrics.AMetric#run(repositorydatasource.model.Repository)
	 */
	@Override
	public NumericValue run(Repository repository) {
		double result = repository.getRepositoryInternalMetrics().getDaysToCloseEachIssue().stream().mapToInt(i -> i).average().orElseThrow();

		return new ValueDecimal(result);
	}
}