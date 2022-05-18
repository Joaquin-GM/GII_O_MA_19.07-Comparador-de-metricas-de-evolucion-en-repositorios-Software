package app;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.listeners.CurrentMetricProfileChangedEvent;
import app.listeners.Listener;
import datamodel.Repository;
import datamodel.RepositoryInternalMetrics;
import exceptions.MetricsServiceException;
import exceptions.RepositoryDataSourceException;
import metricsengine.Measure;
import metricsengine.Metric;
import metricsengine.MetricConfiguration;
import metricsengine.MetricProfile;
import metricsengine.MetricsResults;
import metricsengine.numeric_value_metrics.MetricAverageDaysBetweenCommitsFactory;
import metricsengine.numeric_value_metrics.MetricAverageDaysToCloseAnIssueFactory;
import metricsengine.numeric_value_metrics.MetricChangeActivityRangeFactory;
import metricsengine.numeric_value_metrics.MetricCommitsPerIssueFactory;
import metricsengine.numeric_value_metrics.MetricDaysBetweenFirstAndLastCommitFactory;
import metricsengine.numeric_value_metrics.MetricJobsLastMonthFactory;
import metricsengine.numeric_value_metrics.MetricJobsLastYearFactory;
import metricsengine.numeric_value_metrics.MetricPeakChangeFactory;
import metricsengine.numeric_value_metrics.MetricPercentageClosedIssuesFactory;
import metricsengine.numeric_value_metrics.MetricReleasesLastMonthFactory;
import metricsengine.numeric_value_metrics.MetricTotalNumberOfIssuesFactory;
import metricsengine.values.IValue;
import metricsengine.values.NumericValue;
import repositorydatasource.RepositoryDataSource;

/**
 * Facade for the use of the metric motor.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricsService implements Serializable {

	private static final long serialVersionUID = 6475817245020418420L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);
	
	public static final String DEFAULT_PROFILE_NAME = "DEFAULT";
	
	public static final String IMPORTED_PROFILE_NAME = "IMPORTED";
	
	public static final String NEW_PROFILE_NAME = "CALCULATED";

	private static final MetricProfile DEFAULT_METRIC_PROFILE = createDefaultMetricProfile() ;
	
	private MetricProfile currentMetricProfile = DEFAULT_METRIC_PROFILE;
	
	private Set<Listener<CurrentMetricProfileChangedEvent>> currentMetricProfileChangedEventListeners = new HashSet<Listener<CurrentMetricProfileChangedEvent>>();
	
	private static MetricsService metricsService = null;
	
	private MetricsService() {}

	/**
	 * Initializes the default metric profile.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static MetricProfile createDefaultMetricProfile() {
		MetricProfile defaultMetricProfile = new MetricProfile(DEFAULT_PROFILE_NAME);
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricTotalNumberOfIssuesFactory()));
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricCommitsPerIssueFactory()));
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricPercentageClosedIssuesFactory()));
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricAverageDaysToCloseAnIssueFactory()));
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricAverageDaysBetweenCommitsFactory()));
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricDaysBetweenFirstAndLastCommitFactory()));
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricChangeActivityRangeFactory()));
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricPeakChangeFactory()));
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricJobsLastMonthFactory()));
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricJobsLastYearFactory()));
		defaultMetricProfile.addMetricConfiguration(new MetricConfiguration(new MetricReleasesLastMonthFactory()));
		return defaultMetricProfile;
	}

	/**
	 * Gets the single instance of metricsService.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the single instance of metricsService.
	 */
	public static MetricsService getMetricsService() {
		if (metricsService == null) metricsService = new MetricsService();
		return metricsService;
	}
    
    /**
     * Gets the current profile.
     * 
     * @author Miguel Ángel León Bardavío - mlb0029
     * @return the current profile.
     */
    public MetricProfile getCurrentMetricProfile() {
    	MetricProfile toReturn = new MetricProfile(currentMetricProfile.getName());
    	for (MetricConfiguration mc : currentMetricProfile.getMetricConfigurationCollection()) {
			toReturn.addMetricConfiguration(mc);
		}
    	return toReturn;
    }
    
    /**
     * Sets the current profile to the default one.
     * 
     * @author Miguel Ángel León Bardavío - mlb0029
     */
    public void setCurrentMetricProfileToDefault() {
    	MetricProfile old = currentMetricProfile;
    	currentMetricProfile = DEFAULT_METRIC_PROFILE;
    	notifyRepositoriesCollectionUpdatedListeners(old, DEFAULT_METRIC_PROFILE);
    }
    
    /**
     * Creates a new metric profile and sets as current metric profile.
     * 
     * @author Miguel Ángel León Bardavío - mlb0029
     * @throws MetricsServiceException When error calculating the metric profile.
     */
    public void setCurrentMetricProfileToCalculated() throws MetricsServiceException {
    	try {
    		MetricProfile oldMetricProfile = currentMetricProfile;
			MetricProfile metricProfile = new MetricProfile(NEW_PROFILE_NAME);
			Collection<Repository> repositories = RepositoriesCollectionService.getInstance().getRepositories();
			
			ArrayList<Double> datasetForMetric;
			Double q1ForMetric, q3ForMetric;
			IValue q1ForMetricConfig, q3ForMetricConfig;
			DescriptiveStatistics descriptiveStatisticsForMetric;
			
			IValue valueMeasuredForMetricInRepository;
			Double value;
			
			for (MetricConfiguration metricC : DEFAULT_METRIC_PROFILE.getMetricConfigurationCollection()) {
				datasetForMetric = new ArrayList<Double>();
				for (Repository repository : repositories) {
					valueMeasuredForMetricInRepository = getValueMeasuredForMetric(repository, metricC.getMetric().getClass());
					if (valueMeasuredForMetricInRepository != null && valueMeasuredForMetricInRepository instanceof NumericValue) {
						value = ((NumericValue) valueMeasuredForMetricInRepository).doubleValue();
						datasetForMetric.add(value);
					}
				}
				descriptiveStatisticsForMetric = new DescriptiveStatistics(datasetForMetric.stream().mapToDouble(x -> x).toArray());
				q1ForMetric = descriptiveStatisticsForMetric.getPercentile(25);
				q3ForMetric = descriptiveStatisticsForMetric.getPercentile(75);
				q1ForMetricConfig = metricC.getValueMin().valueFactory(q1ForMetric);
				q3ForMetricConfig = metricC.getValueMax().valueFactory(q3ForMetric);
				metricProfile.addMetricConfiguration(new MetricConfiguration(metricC.getMetricFactory(), q1ForMetricConfig, q3ForMetricConfig));
			}
			currentMetricProfile = metricProfile;
			notifyRepositoriesCollectionUpdatedListeners(oldMetricProfile, metricProfile);
		} catch (Exception e) {
			LOGGER.error("Error deleting a repository. Exception occurred: " + e.getMessage());
			throw new MetricsServiceException("Error while calculating the metric profile.", e);
		}
    }
    
    
    /**
     * Modify the current profile to the imported one.
     * 
     * @author Miguel Ángel León Bardavío - mlb0029
     * @param inputStream input stream
     * @throws MetricsServiceException When file corrupted or another import error.
     */
    public void importCurrentMetricProfile(InputStream inputStream) throws MetricsServiceException {
		try (
			ObjectInputStream objectIn = new ObjectInputStream(inputStream);
		) {
			MetricProfile oldMetricProfile = currentMetricProfile;
			MetricProfile mP = (MetricProfile) objectIn.readObject();
			mP.setName(IMPORTED_PROFILE_NAME);
			currentMetricProfile = mP;
			notifyRepositoriesCollectionUpdatedListeners(oldMetricProfile, currentMetricProfile);
		} catch (StreamCorruptedException e) {
			throw new MetricsServiceException(MetricsServiceException.CORRUPTED, e);
		} catch (Exception e) {
			throw new MetricsServiceException(MetricsServiceException.IMPORT_ERROR, e);
		}
	}

	/**
	 * Exports the current metric profile and returns as an input stream.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return An input stream that contains the current metric profile.
	 * @throws MetricsServiceException When error ocurrs.
	 */
	public InputStream exportCurrentMetricProfile() throws MetricsServiceException {
		try (
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(bos);
		){
			objectOut.writeObject(currentMetricProfile);
			objectOut.flush();
			return bos.toInputStream();
		} catch (Exception e) {
			throw new MetricsServiceException(MetricsServiceException.EXPORT_ERROR, e);
		}
	}

	/**
	 * Gets the value measured in a repository for the metric passed by parameter.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repository Repository from which the measure is obtained.
	 * @param metricType Metric.
	 * @return Value measured in that repository for that metric.
	 */
	private IValue getValueMeasuredForMetric(Repository repository, Class<? extends Metric> metricType) {
		MetricsResults mr = repository.getMetricsResults();
		if (mr == null ) return null;
		Measure measure = mr.getMeasureForTheMetric(metricType);
		if (measure == null) return null;
		IValue measuredValue = measure.getMeasuredValue();
		return measuredValue;
	}

	/**
	 * Obtains the measures of the repository and evaluate them following the current profile.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repository Repository from which the measures are obtained.
	 * @throws RepositoryDataSourceException When have not been able to obtain the measurements.
	 */
	public void obtainAndEvaluateRepositoryMetrics(Repository repository) throws RepositoryDataSourceException {
		RepositoryDataSource repositoryDataSource = RepositoryDataSourceService.getInstance();
		RepositoryInternalMetrics repositoryInternalMetrics = null;
		
		repositoryInternalMetrics = repositoryDataSource.getRepositoryInternalMetrics(repository);
		repository.setRepositoryInternalMetrics(repositoryInternalMetrics);
		
		evaluateRepositoryMetrics(repository);
	}

	/**
	 * Sets the metrics configutations of the repository.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repository Project.
	 * @throws RepositoryDataSourceException When problems evaluating.
	 */
	public void evaluateRepositoryMetrics(Repository repository) throws RepositoryDataSourceException {
		MetricsResults metricsResults = new MetricsResults();
		    	
		for (MetricConfiguration metricConfiguration : currentMetricProfile.getMetricConfigurationCollection()) {
			metricConfiguration.calculate(repository, metricsResults);
		}
		
		repository.setMetricsResults(metricsResults);
	}
	
	/**
	 * Sets the metrics configutations of the repository collection.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @see RepositoriesCollectionService
	 * @throws RepositoryDataSourceException When problems evaluating.
	 */
	public void evaluateRepositoryCollection() throws RepositoryDataSourceException {
		RepositoriesCollectionService rc = RepositoriesCollectionService.getInstance();
		for (Repository repository : rc.getRepositories()) {
			evaluateRepositoryMetrics(repository);
		}
	}
	
	/**
	 * Adds a current metric profile c hanged event listener.
	 * 
	 * @param listener Listener to add.
	 * @return true if not already contain the specified element
	 */
	public boolean addCurrentMetricProfileChangedEventListener(Listener<CurrentMetricProfileChangedEvent> listener) {
		return currentMetricProfileChangedEventListeners.add(listener);
	}

	/**
	 * Removes a current metric profile c hanged event listener.
	 * 
	 * @param listener Listener to remove.
	 * @return true if the listener existed.
	 */
	public boolean removeCurrentMetricProfileChangedEventListener(Listener<CurrentMetricProfileChangedEvent> listener) {
		return currentMetricProfileChangedEventListeners.remove(listener);
	}
	
	/**
	 * Notify listeners
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param previousMetricProfile Previous metric profile.
	 * @param newMetricProfile New Metric profile.
	 */
	private void notifyRepositoriesCollectionUpdatedListeners(MetricProfile previousMetricProfile, MetricProfile newMetricProfile) {
		currentMetricProfileChangedEventListeners.forEach(l -> l.on(new CurrentMetricProfileChangedEvent(previousMetricProfile, newMetricProfile)));
	}
}
