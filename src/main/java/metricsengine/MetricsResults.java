package metricsengine;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

/**
 * Collect the measures of the metrics.
 * 
 * @author MALB
 * @since 03/12/2018
 *
 */
public class MetricsResults implements Comparable<MetricsResults>, Serializable{
	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -8858272628476925489L;

	/**
	 * Collection of measures.
	 */
	private Collection<Measure> measures = new HashSet<Measure>();

	private Date creationDate = new Date();
	
	private Date lastModificationDate = creationDate;
	
	/**
	 * Constructor.
	 */
	public MetricsResults() {}
	
	/**
	 * Gets a copy of the collection of measures.
	 * 
	 * @return The collection of measures.
	 */
	public Collection<Measure> getMeasures() {
		return new HashSet<Measure>(measures);
	}
	
	/**
	 * Gets the creationDate.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return (Date) creationDate.clone();
	}

	/**
	 * Gets the lastModificationDate.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the lastModificationDate
	 */
	public Date getLastModificationDate() {
		return (Date) lastModificationDate.clone();
	}

	/**
	 * Adds a measure to the collection.
	 * 
	 * @param measure Measure of a metric.
	 */
	public void addMeasure(Measure measure) {
		measures.add(measure);
		lastModificationDate = new Date();
	}

	public Measure getMeasureForTheMetric(Class<? extends Metric> metricType) {
		for (Measure measure : getMeasures()) {
			if (measure.getMetricConfiguration().getMetric().getClass() == metricType)
				return measure;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(creationDate, lastModificationDate);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MetricsResults))
			return false;
		MetricsResults other = (MetricsResults) obj;
		return Objects.equals(creationDate, other.creationDate)
				&& Objects.equals(lastModificationDate, other.lastModificationDate);
	}

	@Override
	public int compareTo(MetricsResults o) {
		return lastModificationDate.compareTo(o.lastModificationDate);
	}
	
}
