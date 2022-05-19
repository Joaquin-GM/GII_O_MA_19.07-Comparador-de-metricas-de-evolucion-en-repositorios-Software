package metricsengine.numeric_value_metrics;

import java.util.ArrayList;
import java.util.Date;
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
 * Computes the releases executed the last year.
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class MetricReleasesLastYear extends NumericValueMetricTemplate {
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
			"DC2",
			"Releases released last year",
			"Need GitLab connection with authorization", 
			"Joaquin Garcia Molina",
			"CI/CD",
			"How many releases have been successfully released last year?",
			"NRRLY = Releases released last year",
			"Repository", 
			"NRRLY >= 0, better greater values.",
			MetricDescription.EnumTypeOfScale.ABSOLUTE,
			"NRRLY: Count"
	);

	/**
	 * Minimum acceptable value.
	 */
	public static final NumericValue DEFAULT_MIN_VALUE = new ValueDecimal(1.0);

	/**
	 * Maximum acceptable value.
	 */
	public static final NumericValue DEFAULT_MAX_VALUE = new ValueDecimal(5.0);

	private static MetricReleasesLastYear instance = null;

	/**
	 * Constructor that initializes the metric with default values.
	 *
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private MetricReleasesLastYear() {
		super(DEFAULT_METRIC_DESCRIPTION, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE,
				NumericValueMetricTemplate.EVAL_FUNC_BETWEEN_Q1_Q3);
	}

	public static MetricReleasesLastYear getInstance() {
		if (instance == null)
			instance = new MetricReleasesLastYear();
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
		Date now = new Date();
		long day365 = 365l * 24 * 60 * 60 * 1000;
		Date currentYearLimitDate = new Date((now.getTime() - day365));

		List<CustomGitlabApiRelease> releasesLastYear = new ArrayList<CustomGitlabApiRelease>();
		List<CustomGitlabApiRelease> repositoryReleases = repository.getRepositoryInternalMetrics().getReleases().stream()
				.collect(Collectors.toList());

		for (int i = 0; i < repositoryReleases.size(); i++) {
			CustomGitlabApiRelease release = repositoryReleases.get(i);

			if (release.getRelease() != null && release.getRelease().getReleasedAt() != null && release.getRelease().getReleasedAt().after(currentYearLimitDate)) {
				releasesLastYear.add(release);
			}
		}
		return new ValueInteger(releasesLastYear.size());
	}
}
