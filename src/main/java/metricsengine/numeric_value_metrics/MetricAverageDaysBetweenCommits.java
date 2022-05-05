package metricsengine.numeric_value_metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import datamodel.Repository;
import metricsengine.MetricDescription;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueDecimal;

/**
 * Computes the average of days between commits.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricAverageDaysBetweenCommits extends NumericValueMetricTemplate {
	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -1039405944018960452L;

	/**
	 * Default metric description.
	 */
	public static final MetricDescription DEFAULT_METRIC_DESCRIPTION = new MetricDescription(
			"TC1",
			"Average of days between commits",
			"",
			"",
			"Time constraints",
			"How much time is there between one commit and the next?",
			"ADBC = SUM([i]-[i-1]; [i] = 1 -> [i] < TNC; CD)/(TNC-1) (in days). ADBC = Average of days between commits, CD = Vector with de commits dates, TNC = Total number of commits.",
			"CD, TNC: Repository",
			"ADBC >= 0, better small values.",
			MetricDescription.EnumTypeOfScale.RATIO,
			"TNC:Count, CD: Set of times");
		
	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueDecimal(1.0);
	
	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueDecimal(4.3);
	
	private static MetricAverageDaysBetweenCommits instance = null;
	
	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private MetricAverageDaysBetweenCommits() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, NumericValueMetricTemplate.EVAL_FUNC_BETWEEN_Q1_Q3);
	}
	
	public static MetricAverageDaysBetweenCommits getInstance() {
		if (instance == null) instance = new MetricAverageDaysBetweenCommits();
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
		long date1;
		long date2;
		int daysBetweenCommits;
		List<Date> commitDates = repository.getRepositoryInternalMetrics().getCommitDates().stream().sorted().collect(Collectors.toList());
		List<Integer> lstDaysBetweenCommits = new ArrayList<Integer>();
		for (int i = 1; i < commitDates.size(); i++) {
			date1 = commitDates.get(i-1).getTime();
			date2 = commitDates.get(i).getTime();
			daysBetweenCommits = (int) ((date2 - date1) / (1000 * 60 * 60 * 24));
			lstDaysBetweenCommits.add(daysBetweenCommits);
		}
		return new ValueDecimal(lstDaysBetweenCommits.stream().mapToInt(i -> i.intValue()).average().orElseThrow());
	}
}
