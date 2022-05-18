package metricsengine.numeric_value_metrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.gitlab4j.api.models.Job;

import app.RepositoryDataSourceService;
import datamodel.Repository;
import metricsengine.MetricDescription;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueDecimal;
import metricsengine.values.ValueInteger;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;

/**
 * Computes the jobs executed the last year.
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class MetricJobsLastYear extends NumericValueMetricTemplate {
	/**
	 * Description.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private static final long serialVersionUID = -1039405944018960452L;

	/**
	 * Default metric description.
	 */
	public static final MetricDescription DEFAULT_METRIC_DESCRIPTION = new MetricDescription(
			"IC2",
			"Jobs executed last year",
			"Need GitLab connection with authorization",
			"Joaquin Garcia Molina", 
			"CI/CD",
			"How many jobs have been successfully executed last year?",
			"JELY = Jobs executed last year",
			"Repository", 
			"JELM >= 0, better greater values.",
			MetricDescription.EnumTypeOfScale.ABSOLUTE,
			"JELM: Count"
	);

	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueDecimal(1.0);

	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueDecimal(50.0);

	private static MetricJobsLastYear instance = null;

	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private MetricJobsLastYear() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE,
				NumericValueMetricTemplate.EVAL_FUNC_BETWEEN_Q1_Q3);
	}

	public static MetricJobsLastYear getInstance() {
		if (instance == null)
			instance = new MetricJobsLastYear();
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * metricsengine.metrics.AMetric#check(repositorydatasource.model.Repository)
	 */
	@Override
	public Boolean check(Repository repository) {
		// If not authenticated the metric is not calculated, GitLabApi requires authentication for 
		RepositoryDataSourceService rds = RepositoryDataSourceService.getInstance();
		if (rds.getConnectionType() != EnumConnectionType.LOGGED) {
			return false;
		}
		
		// Checks the repository is not empty
		Integer totalNumberOfCommits = repository.getRepositoryInternalMetrics().getTotalNumberOfCommits();

		if (totalNumberOfCommits != null && totalNumberOfCommits > 1) {

		} else {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see metricsengine.metrics.AMetric#run(repositorydatasource.model.Repository)
	 */
	@Override
	public NumericValue run(Repository repository) {
		Date now = new Date();
		long day30 = 30l * 24 * 60 * 60 * 1000;
		Date currentMonthLimitDate = new Date((now.getTime() - day30));

		List<Job> jobsLastMonth = new ArrayList<Job>();
		List<Job> repositoryJobs = repository.getRepositoryInternalMetrics().getJobs().stream()
				.collect(Collectors.toList());

		for (int i = 0; i < repositoryJobs.size(); i++) {
			Job job = repositoryJobs.get(i);

			if (job.getFinishedAt() != null && job.getFinishedAt().after(currentMonthLimitDate)) {
				jobsLastMonth.add(job);
			}
		}
		return new ValueInteger(jobsLastMonth.size());
	}
}
