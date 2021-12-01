package app;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.ws.rs.NotSupportedException;

import org.apache.commons.io.output.ByteArrayOutputStream;

import app.listeners.Listener;
import app.listeners.RepositoriesCollectionUpdatedEvent;
import datamodel.Repository;
import exceptions.RepositoriesCollectionServiceException;
import metricsengine.Measure;
import metricsengine.Metric;
import metricsengine.MetricsResults;
import metricsengine.numeric_value_metrics.MetricAverageDaysBetweenCommits;
import metricsengine.numeric_value_metrics.MetricAverageDaysToCloseAnIssue;
import metricsengine.numeric_value_metrics.MetricChangeActivityRange;
import metricsengine.numeric_value_metrics.MetricCommitsPerIssue;
import metricsengine.numeric_value_metrics.MetricDaysBetweenFirstAndLastCommit;
import metricsengine.numeric_value_metrics.MetricPeakChange;
import metricsengine.numeric_value_metrics.MetricPercentageClosedIssues;
import metricsengine.numeric_value_metrics.MetricTotalNumberOfIssues;
import metricsengine.numeric_value_metrics.ProjectEvaluation;
import metricsengine.values.IValue;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueUncalculated;

/**
 * It contains a set of repositories.
 * <p>
 * Duplicates are not allowed.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 */
public class RepositoriesCollectionService implements Serializable {
	
	private static final long serialVersionUID = 6585069143415079761L;
	
	/**
	 * Single instance.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static RepositoriesCollectionService instance;
	
	/**
	 * Set of repositories.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private RepositoriesCollection repositoriesCollection = new RepositoriesCollection();
	
	private HashSet<Listener<RepositoriesCollectionUpdatedEvent>> repositoriesCollectionUpdatedListeners = new HashSet<>();
	
	private RepositoriesCollectionService() {}

	/**
	 * Gets the single instance of the class.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the single instance of the class.
	 */
	public static RepositoriesCollectionService getInstance() {
		if (instance == null) instance = new RepositoriesCollectionService();
		return instance;
	}

	/**
	 * Gets the collection of repositories.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the collection of repositories.
	 */
	public Collection<Repository> getRepositories() {
		return repositoriesCollection;
	}
	
	/**
	 * Adds a repository and notify all RepositoriesCollectionUpdatedEvent Listeners.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repository Repository to add.
	 * @throws RepositoriesCollectionServiceException when repository already exists.
	 */
	public void addRepository(Repository repository) throws RepositoriesCollectionServiceException {
		if (!repositoriesCollection.collection.add(repository)) throw new RepositoriesCollectionServiceException(RepositoriesCollectionServiceException.REPOSITORY_ALREADY_EXISTS);
		notifyRepositoriesCollectionUpdatedListeners();
	}
	
	/**
	 * Removes a repository and notify all RepositoriesCollectionUpdatedEvent Listeners.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repository Repository to remove.
	 * @throws RepositoriesCollectionServiceException if repository dosen't exists.
	 */
	public void removeRepository(Repository repository) throws RepositoriesCollectionServiceException {
		if (!repositoriesCollection.collection.remove(repository)) throw new RepositoriesCollectionServiceException(RepositoriesCollectionServiceException.NOT_EXIST_REPOSITORY);
		notifyRepositoriesCollectionUpdatedListeners();
	}
	
	/**
	 * Exports the repositories serialized to the returned input stream.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return an input stream with the repositories collection.
	 * @throws RepositoriesCollectionServiceException when problems occur when exporting.
	 */
	public InputStream exportRepositories () throws RepositoriesCollectionServiceException {
		try (
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(bos);
		){
			objectOut.writeObject(repositoriesCollection.collection);
			objectOut.flush();
			return bos.toInputStream();
		} catch (Exception e) {
			throw new RepositoriesCollectionServiceException(RepositoriesCollectionServiceException.EXPORT_ERROR, e);
		}
	}
	
	/**
	 * Exports the repositories to a csv file and return the input stream..
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return An input stream containing the csv file.
	 * @throws RepositoriesCollectionServiceException when problems occur when exporting.
	 */
	public InputStream exportRepositoriesToCSV () throws RepositoriesCollectionServiceException {
		try (
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
		){
			StringBuffer sb = new StringBuffer();
			String header = "";
			header += "Name" + ";";
			header += "URL" + ";";
			header += "Date" + ";";
			header +=  MetricTotalNumberOfIssues.DEFAULT_METRIC_DESCRIPTION.getName() + ";";
			header +=  MetricCommitsPerIssue.DEFAULT_METRIC_DESCRIPTION.getName() + ";";
			header +=  MetricPercentageClosedIssues.DEFAULT_METRIC_DESCRIPTION.getName() + ";";
			header +=  MetricAverageDaysToCloseAnIssue.DEFAULT_METRIC_DESCRIPTION.getName() + ";";
			header +=  MetricAverageDaysBetweenCommits.DEFAULT_METRIC_DESCRIPTION.getName() + ";";
			header +=  MetricDaysBetweenFirstAndLastCommit.DEFAULT_METRIC_DESCRIPTION.getName() + ";";
			header +=  MetricChangeActivityRange.DEFAULT_METRIC_DESCRIPTION.getName().split("-")[0].trim() + ";";
			header +=  MetricPeakChange.DEFAULT_METRIC_DESCRIPTION.getName().split("-")[0].trim() + ";";
			sb.append(header + "\n");
			String line;
			for (Repository repository : repositoriesCollection) {
				line = "";
				line += (repository.getName() != null?repository.getName():"") + ";";
				line += (repository.getUrl() != null?repository.getUrl():"") + ";";
				line += (repository.getMetricsResults() != null?repository.getMetricsResults().getLastModificationDate():"") + ";";
				line += getValueMeasuredForMetric(repository, MetricTotalNumberOfIssues.class) + ";";
				line += getValueMeasuredForMetric(repository, MetricCommitsPerIssue.class) + ";";
				line += getValueMeasuredForMetric(repository, MetricPercentageClosedIssues.class) + ";";
				line += getValueMeasuredForMetric(repository, MetricAverageDaysToCloseAnIssue.class) + ";";
				line += getValueMeasuredForMetric(repository, MetricAverageDaysBetweenCommits.class) + ";";
				line += getValueMeasuredForMetric(repository, MetricDaysBetweenFirstAndLastCommit.class) + ";";
				line += getValueMeasuredForMetric(repository, MetricChangeActivityRange.class) + ";";
				line += getValueMeasuredForMetric(repository, MetricPeakChange.class);
				sb.append(line + "\n");
			}
			bos.write(sb.toString().getBytes());
			return bos.toInputStream();
		} catch (Exception e) {
			throw new RepositoriesCollectionServiceException(RepositoriesCollectionServiceException.EXPORT_ERROR, e);
		}
	}
	
	private String getValueMeasuredForMetric(Repository repository, Class<? extends Metric> metricType) {
		String notCalculated = "NC";
		Measure measure;
		if (metricType == ProjectEvaluation.class) {
			measure = repository.getProjectEvaluation();
		} else {
			measure = getMeasureForMetric(repository, metricType);			
		}
		if (measure == null) return notCalculated;
		IValue value = measure.getMeasuredValue();
		if(value == null) return notCalculated;
		if (value instanceof NumericValue) {
			Double d = Math.round(((NumericValue) value).doubleValue() * 100.0) / 100.0;
			return d.toString();
		} else if (value instanceof ValueUncalculated)
			return notCalculated;
		else
			return value.getValueString();
	}
	
	private Measure getMeasureForMetric(Repository repository, Class<? extends Metric> metricType) {
		MetricsResults mr = repository.getMetricsResults();
		if (mr == null ) return null;
		Measure measure = mr.getMeasureForTheMetric(metricType);
		if (measure == null) return null;
		return measure;
	}
	
	/**
	 * Append or overwrite the current repositories.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 *
	 */
	public enum ImportMode {OVERWRITE, APPEND}
	
	/**
	 * Import repositories from file and evaluate them.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param inputStream input stream
	 * @param importMode import mode
	 * @see ImportMode
	 * @throws RepositoriesCollectionServiceException when problems occur when importing.
	 */
	@SuppressWarnings("unchecked")
	public void importRepositories(InputStream inputStream, ImportMode importMode) throws RepositoriesCollectionServiceException {
		try (
			ObjectInputStream objectIn = new ObjectInputStream(inputStream);
		) {
			HashSet<Repository> repositories = (HashSet<Repository>) objectIn.readObject();
			if(importMode == ImportMode.OVERWRITE) repositoriesCollection.collection.clear();
			repositoriesCollection.collection.addAll(repositories);
			MetricsService.getMetricsService().evaluateRepositoryCollection();
			notifyRepositoriesCollectionUpdatedListeners();
		} catch (StreamCorruptedException e) {
			throw new RepositoriesCollectionServiceException(RepositoriesCollectionServiceException.CORRUPTED, e);
		} catch (Exception e) {
			throw new RepositoriesCollectionServiceException(RepositoriesCollectionServiceException.IMPORT_ERROR, e);
		}
	}

	/**
	 * Adds a RepositoriesCollectionUpdated event listener.
	 * 
	 * @param listener Listener to add.
	 * @return true if this set did not already contain the specified element
	 */
	public boolean addRepositoriesCollectionUpdatedListener(Listener<RepositoriesCollectionUpdatedEvent> listener) {
		return repositoriesCollectionUpdatedListeners.add(listener);
	}

	/**
	 * Removes a RepositoriesCollectionUpdated event listener.
	 * 
	 * @param listener Listener to remove.
	 * @return true if the set contained the specified element
	 */
	public boolean removeRepositoriesCollectionUpdatedListener(Listener<RepositoriesCollectionUpdatedEvent> listener) {
		return repositoriesCollectionUpdatedListeners.remove(listener);
	}
	
	/**
	 * Notify all the RepositoriesCollectionUpdated event listeners.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private void notifyRepositoriesCollectionUpdatedListeners() {
		repositoriesCollectionUpdatedListeners.forEach(l -> l.on(new RepositoriesCollectionUpdatedEvent()));
	}

	/**
	 * Public collection that prevents data from being modified.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 *
	 */
	private class RepositoriesCollection implements Collection<Repository> {

		private HashSet<Repository> collection = new HashSet<Repository>();
		
		@Override
		public int size() {
			return collection.size();
		}

		@Override
		public boolean isEmpty() {
			return collection.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return collection.contains(o);
		}

		@Override
		public Iterator<Repository> iterator() {
			return collection.iterator();
		}

		@Override
		public Object[] toArray() {
			return collection.toArray();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return collection.toArray(a);
		}

		@Override
		public boolean add(Repository e) {
			throw new NotSupportedException("Not allowed. Use RepositoriesCollectionService instead.");
		}

		@Override
		public boolean remove(Object o) {
			throw new NotSupportedException("Not allowed. Use RepositoriesCollectionService instead.");
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return collection.containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends Repository> c) {
			throw new NotSupportedException("Not allowed. Use RepositoriesCollectionService instead.");
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			throw new NotSupportedException("Not allowed. Use RepositoriesCollectionService instead.");
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new NotSupportedException("Not allowed. Use RepositoriesCollectionService instead.");
		}

		@Override
		public void clear() {
			throw new NotSupportedException("Not allowed. Use RepositoriesCollectionService instead.");
		}
	}
}
