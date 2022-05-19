package metricsengine.numeric_value_metrics;

import java.util.List;
import java.util.stream.Collectors;

import app.RepositoryDataSourceService;
import datamodel.CustomGitlabApiRelease;
import datamodel.Repository;
import metricsengine.MetricDescription;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueDecimal;
import metricsengine.values.ValueInteger;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;

/**
 * Computes the total number of releases successfully released.
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class MetricTotalNumberOfReleases extends NumericValueMetricTemplate {
	/**
	 * Description.
	 * 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private static final long serialVersionUID = -1039405944018960444L;

	/**
	 * Default metric description.
	 */
	public static final MetricDescription DEFAULT_METRIC_DESCRIPTION = new MetricDescription(
			"DC1",
			"Total number of releases released",
			"Need GitLab connection with authorization", 
			"Joaquin Garcia Molina",
			"CI/CD",
			"How many releases have been successfully released?",
			"NRR = Number of releases released",
			"Repository", 
			"NRR >= 0, better greater values.",
			MetricDescription.EnumTypeOfScale.ABSOLUTE,
			"NRR: Count"
	);

	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueDecimal(1.0);

	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueDecimal(10.0);

	private static MetricTotalNumberOfReleases instance = null;

	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private MetricTotalNumberOfReleases() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE,
				NumericValueMetricTemplate.EVAL_FUNC_BETWEEN_Q1_Q3);
	}

	public static MetricTotalNumberOfReleases getInstance() {
		if (instance == null)
			instance = new MetricTotalNumberOfReleases();
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
		List<CustomGitlabApiRelease> repositoryReleases = repository.getRepositoryInternalMetrics().getReleases().stream()
				.collect(Collectors.toList());

		return new ValueInteger(repositoryReleases.size());
	}
}
