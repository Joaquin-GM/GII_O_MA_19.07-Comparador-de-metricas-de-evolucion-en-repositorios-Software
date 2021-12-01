package metricsengine.numeric_value_metrics;

import datamodel.Repository;
import metricsengine.MetricDescription;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueInteger;

/**
 * Compute the total number of issues of a repository.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricTotalNumberOfIssues extends NumericValueMetricTemplate {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 5961481794180264034L;

	/**
	 * Default metric description.
	 */
	public static final MetricDescription DEFAULT_METRIC_DESCRIPTION = new MetricDescription(
			"I1",
			"Total number of issues",
			"Jacek Ratzinger",
			"2007",
			"Process Orientation",
			"How many issues have been created in the repository?",
			"TNI = Total number of issues",
			"Repository",
			"TNI >= 0. Better small values",
			MetricDescription.EnumTypeOfScale.ABSOLUTE,
			"TNI : Count");
	
	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueInteger(6);
	
	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueInteger(44);
	
	private static MetricTotalNumberOfIssues instance = null;
	
	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private MetricTotalNumberOfIssues() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, NumericValueMetricTemplate.EVAL_FUNC_GREATER_THAN_Q1);
	}
	
	public static MetricTotalNumberOfIssues getInstance() {
		if (instance == null) instance = new MetricTotalNumberOfIssues();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see metricsengine.AMetric#check(repositorydatasource.model.Repository)
	 */
	@Override
	public Boolean check(Repository repository) {
		Integer tni = repository.getRepositoryInternalMetrics().getTotalNumberOfIssues();
		return tni != null &&
				tni >= 0;
	}

	/* (non-Javadoc)
	 * @see metricsengine.AMetric#run(repositorydatasource.model.Repository)
	 */
	@Override
	public NumericValue run(Repository repository) {
		return new ValueInteger(repository.getRepositoryInternalMetrics().getTotalNumberOfIssues());
	}
}
