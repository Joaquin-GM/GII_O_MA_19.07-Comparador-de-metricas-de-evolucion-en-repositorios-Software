package metricsengine.numeric_value_metrics;

import datamodel.Repository;
import metricsengine.MetricDescription;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueDecimal;

/**
 * Computes the percentage of closed issues.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricPercentageClosedIssues extends NumericValueMetricTemplate {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -8897628050510118688L;

	/**
	 * Default metric description.
	 */
	public static final MetricDescription DEFAULT_METRIC_DESCRIPTION = new MetricDescription(
			"I3",
			"Percentage of issues closed",
			"",
			"",
			"Process Orientation",
			"What percentage of issues have been closed?",
			"PIC = (NCI/TNI) * 100. PIC = Percentage of issues closed.TNI = Total number of issues. NCI = Number of closed issues",
			"TNI, NCI:Repository",
			"PIC >= 0 and PIC <= 100. Better large values",
			MetricDescription.EnumTypeOfScale.RATIO,
			"TNI,NCI:Count");
	
	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueDecimal(87.0);
	
	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueDecimal(100.0);
	
	private static MetricPercentageClosedIssues instance = null;
	
	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private MetricPercentageClosedIssues() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, NumericValueMetricTemplate.EVAL_FUNC_GREATER_THAN_Q1);
	}
	
	public static MetricPercentageClosedIssues getInstance() {
		if (instance == null) instance = new MetricPercentageClosedIssues();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see metricsengine.metrics.AMetric#check(repositorydatasource.model.Repository)
	 */
	@Override
	public Boolean check(Repository repository) {
		Integer tni = repository.getRepositoryInternalMetrics().getTotalNumberOfIssues();
		Integer nci = repository.getRepositoryInternalMetrics().getNumberOfClosedIssues();
		return tni != null && 
				tni > 0 &&
				nci != null &&
				nci >= 0;
	}

	/* (non-Javadoc)
	 * @see metricsengine.metrics.AMetric#run(repositorydatasource.model.Repository)
	 */
	@Override
	public NumericValue run(Repository repository) {
		double result = (double) repository.getRepositoryInternalMetrics().getNumberOfClosedIssues() * 100 / repository.getRepositoryInternalMetrics().getTotalNumberOfIssues() ;
		return new ValueDecimal(result);
	}

}
