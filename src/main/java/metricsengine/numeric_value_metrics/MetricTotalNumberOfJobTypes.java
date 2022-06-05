package metricsengine.numeric_value_metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import datamodel.CustomGithubApiJob;
import datamodel.CustomGitlabApiJob;
import datamodel.Repository;
import datamodel.RepositorySourceType;
import metricsengine.MetricDescription;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueDecimal;
import metricsengine.values.ValueInteger;

/**
 * Computes the total number of jobs successfully executed.
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class MetricTotalNumberOfJobTypes extends NumericValueMetricTemplate {
	/**
	 * Description.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private static final long serialVersionUID = -1039405933018960457L;

	/**
	 * Default metric description.
	 */
	public static final MetricDescription DEFAULT_METRIC_DESCRIPTION = new MetricDescription("IC3",
			"Total number of executed jobs types", "Need GitLab connection with authorization", "Joaquin Garcia Molina",
			"CI/CD", "How many types of jobs have been successfully executed?",
			"NTJE = Number of types of the jobs executed", "Repository", "NTJE >= 0, better greater values.",
			MetricDescription.EnumTypeOfScale.ABSOLUTE, "NTJE: Count");

	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueDecimal(1.0);

	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueDecimal(8.0);

	private static MetricTotalNumberOfJobTypes instance = null;

	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private MetricTotalNumberOfJobTypes() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE,
				NumericValueMetricTemplate.EVAL_FUNC_BETWEEN_Q1_Q3);
	}

	public static MetricTotalNumberOfJobTypes getInstance() {
		if (instance == null)
			instance = new MetricTotalNumberOfJobTypes();
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
		// If not authenticated the metric is not calculated, GitLabApi requires
		// authentication for returning jobs, so it can be calculated
		if (repository.getRepositoryDataSourceType().equals(RepositorySourceType.GitLab)) {
			Collection<CustomGitlabApiJob> repositoryJobs = repository.getRepositoryInternalMetrics().getJobs();
			if (repositoryJobs == null) {
				return false;
			}
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

		List<String> jobTypesList = new ArrayList<String>();

		if (repository.getRepositoryDataSourceType().equals(RepositorySourceType.GitLab)) {
			List<CustomGitlabApiJob> repositoryJobs = repository.getRepositoryInternalMetrics().getJobs().stream()
					.collect(Collectors.toList());

			for (int i = 0; i < repositoryJobs.size(); i++) {
				CustomGitlabApiJob job = repositoryJobs.get(i);
				if (job.getName() != null && !jobTypesList.contains(job.getName())) {
					jobTypesList.add(job.getName());
				}
			}
		} else {
			// GitHub
			List<CustomGithubApiJob> repositoryJobs = repository.getRepositoryInternalMetrics().getGHJobs().stream()
					.collect(Collectors.toList());

			for (int i = 0; i < repositoryJobs.size(); i++) {
				CustomGithubApiJob job = repositoryJobs.get(i);
				if (job.getName() != null && !jobTypesList.contains(job.getName())) {
					jobTypesList.add(job.getName());
				}
			}
		}

		return new ValueInteger(jobTypesList.size());
	}
}
