package metricsengine.numeric_value_metrics;

import datamodel.Repository;
import metricsengine.MetricDescription;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueDecimal;

/**
 * Computes the commits per issue.
 * <p>
 * Total number of issues / Total number of commits.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricCommitsPerIssue extends NumericValueMetricTemplate {
	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -3512786510470075695L;

	/**
	 * Default metric description.
	 */
	public static final MetricDescription DEFAULT_METRIC_DESCRIPTION = new MetricDescription(
			"I2",
			"Commits per issue",
			"",
			"",
			"Process Orientation",
			"How many commits per issues?",
			"CI = TNC/TNI. CI = Commits per issue.TNI = Total number of issues. TNC = Total number of commits",
			"TNI, TNC:Repository",
			"CI >= 0. Better small values",
			MetricDescription.EnumTypeOfScale.RATIO,
			"TNI,TNC:Count");
	
	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueDecimal(0.5);
	
	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueDecimal(1.0);
	
	private static MetricCommitsPerIssue instance = null;
	
	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private MetricCommitsPerIssue() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, NumericValueMetricTemplate.EVAL_FUNC_BETWEEN_Q1_Q3);
	}
	
	public static MetricCommitsPerIssue getInstance() {
		if (instance == null) instance = new MetricCommitsPerIssue();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see metricsengine.metrics.AMetric#check(repositorydatasource.model.Repository)
	 */
	@Override
	public Boolean check(Repository repository) {
		Integer tni = repository.getRepositoryInternalMetrics().getTotalNumberOfIssues();
		Integer tnc = repository.getRepositoryInternalMetrics().getTotalNumberOfCommits();
		return tni != null && 
				tni >= 0 &&
				tnc != null &&
				tnc > 0;
	}

	/* (non-Javadoc)
	 * @see metricsengine.metrics.AMetric#run(repositorydatasource.model.Repository)
	 */
	@Override
	public NumericValue run(Repository repository) {
		double result = (double) repository.getRepositoryInternalMetrics().getTotalNumberOfCommits() / repository.getRepositoryInternalMetrics().getTotalNumberOfIssues();
		return new ValueDecimal(result);
	}
}
