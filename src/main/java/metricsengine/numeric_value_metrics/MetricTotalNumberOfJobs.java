package metricsengine.numeric_value_metrics;

import java.util.List;
import java.util.stream.Collectors;

import org.kohsuke.github.GHWorkflowJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.RepositoryDataSourceService;
import datamodel.CustomGitlabApiJob;
import datamodel.Repository;
import datamodel.RepositorySourceType;
import metricsengine.MetricDescription;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueDecimal;
import metricsengine.values.ValueInteger;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;

/**
 * Computes the total number of jobs successfully executed.
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class MetricTotalNumberOfJobs extends NumericValueMetricTemplate {
	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricTotalNumberOfJobs.class);

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
			"IC1",
			"Total number of jobs executed",
			"Need GitLab connection with authorization",
			"Joaquin Garcia Molina", 
			"CI/CD",
			"How many jobs have been successfully executed?",
			"NJE = Number of jobs executed",
			"Repository", 
			"NJE >= 0, better greater values.",
			MetricDescription.EnumTypeOfScale.ABSOLUTE,
			"NJE: Count"
	);

	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueDecimal(1.0);

	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueDecimal(1000.0);

	private static MetricTotalNumberOfJobs instance = null;

	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private MetricTotalNumberOfJobs() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE,
				NumericValueMetricTemplate.EVAL_FUNC_BETWEEN_Q1_Q3);
	}

	public static MetricTotalNumberOfJobs getInstance() {
		if (instance == null)
			instance = new MetricTotalNumberOfJobs();
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
		if (rds.getConnectionType(repository.getRepositoryDataSourceType()) != EnumConnectionType.LOGGED) {
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
		LOGGER.info("MetricTotalNumberOfJobs numericValue");
		
		if (repository.getRepositoryDataSourceType().equals(RepositorySourceType.GitLab)) {
			List<CustomGitlabApiJob> repositoryJobs = repository.getRepositoryInternalMetrics().getJobs().stream()
					.collect(Collectors.toList());

			return new ValueInteger(repositoryJobs.size());
		} else {
			// GitHub
			List<GHWorkflowJob> repositoryJobs = repository.getRepositoryInternalMetrics().getGHJobs().stream()
					.collect(Collectors.toList());

			return new ValueInteger(repositoryJobs.size());
		}
		
	}
}
