package metricsengine.numeric_value_metrics;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import datamodel.Repository;
import metricsengine.MetricDescription;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueInteger;

/**
 * Computes the days between the first and the last commit.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricDaysBetweenFirstAndLastCommit extends NumericValueMetricTemplate {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -1644028814570031865L;

	/**
	 * Default metric description.
	 */
	public static final MetricDescription DEFAULT_METRIC_DESCRIPTION = new MetricDescription(
			"TC2",
			"Days between the first and the last commit",
			"",
			"",
			"Time constraints",
			"How many days have passed between the first and last commit?",
			"DBFLC = Max(CD) - Min(CD) (in days). DBFLC = Days between the first and the last commit, CD = Vector with de commits dates",
			"CD: Repository",
			"DBFLC >= 0, better large values.",
			MetricDescription.EnumTypeOfScale.ABSOLUTE,
			"CD: Set of times");
	
	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueInteger(81);
	
	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueInteger(198);
	
	private static MetricDaysBetweenFirstAndLastCommit instance = null;
			
	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private MetricDaysBetweenFirstAndLastCommit() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, NumericValueMetricTemplate.EVAL_FUNC_BETWEEN_Q1_Q3);
	}
	
	public static MetricDaysBetweenFirstAndLastCommit getInstance() {
		if (instance == null) instance = new MetricDaysBetweenFirstAndLastCommit();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see metricsengine.metrics.AMetric#check(repositorydatasource.model.Repository)
	 */
	@Override
	public Boolean check(Repository repository) {
		Collection<Date> commitDates = repository.getRepositoryInternalMetrics().getCommitDates();
		Integer totalNumberOfCommits = repository.getRepositoryInternalMetrics().getTotalNumberOfCommits();
		
		if(totalNumberOfCommits != null &&
				commitDates != null &&
//				commitDates.size() == totalNumberOfCommits && 
				totalNumberOfCommits > 1) {
			for (Date date : commitDates) {
				if (date == null) return false;
			}
		}else {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see metricsengine.metrics.AMetric#run(repositorydatasource.model.Repository)
	 */
	@Override
	public NumericValue run(Repository repository) {
		List<Date> commitDates = repository.getRepositoryInternalMetrics().getCommitDates().stream().sorted(Date::compareTo).collect(Collectors.toList());
		long firstDate = commitDates.get(0).getTime();
		long lastDate = commitDates.get(commitDates.size() - 1).getTime();
		int result = (int) ((lastDate - firstDate) / (1000 * 60 * 60 * 24));
		return new ValueInteger(result);
	}
}
