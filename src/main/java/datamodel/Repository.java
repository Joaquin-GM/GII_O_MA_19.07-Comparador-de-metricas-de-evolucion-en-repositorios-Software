package datamodel;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import metricsengine.EvaluationResult;
import metricsengine.Measure;
import metricsengine.Metric;
import metricsengine.MetricConfiguration;
import metricsengine.MetricsResults;
import metricsengine.numeric_value_metrics.ProjectEvaluation;
import metricsengine.numeric_value_metrics.ProjectEvaluationFactory;
import metricsengine.values.IValue;
import metricsengine.values.ValueDecimal;
import metricsengine.values.ValueUncalculated;

/**
 * A repository data class.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class Repository implements Serializable {
		
	/**
	 * Serial.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 5094587490166499680L;

	/**
	 * HTTPS URL from the repository.
	 */
	private String url = null;
	
	/**
	 * Name of the repository.
	 */
	private String name = null;
	
	/**
	 * ID of the repository.
	 */
	private Long id = null;

	private RepositoryInternalMetrics repositoryInternalMetrics = new RepositoryInternalMetrics();
	
	private MetricsResults metricsResults = new MetricsResults();
	
	private Measure projectEvaluation = new Measure(new MetricConfiguration(new ProjectEvaluationFactory()), new ValueUncalculated());
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, name, url);
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
		if (!(obj instanceof Repository))
			return false;
		Repository other = (Repository) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name) && Objects.equals(url, other.url);
	}

	/**
	 * Constructor that defines the repository, without specifying the metrics that are obtained from it.
	 * 
	 * @param url HTTPS URL from the repository
	 * @param name Name of the repository
	 * @param id  ID of the repository
	 */
	public Repository(String url, String name, Long id) {
		setUrl(url);
		setName(name);
		setId(id);
	}

	/**
	 * Gets the HTTPS URL from the repository.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the HTTPS URL from the repository.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the name of the repository.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the repository.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the ID of the repository.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the ID of the repository.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the repositoryInternalMetrics.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the repositoryInternalMetrics
	 */
	public RepositoryInternalMetrics getRepositoryInternalMetrics() {
		return repositoryInternalMetrics;
	}

	/**
	 * Sets the repositoryInternalMetrics.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param repositoryInternalMetrics the repositoryInternalMetrics to set
	 */
	public void setRepositoryInternalMetrics(RepositoryInternalMetrics repositoryInternalMetrics) {
		this.repositoryInternalMetrics = repositoryInternalMetrics;
	}
	

	/**
	 * Gets the metricsResults.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the metricsResults
	 */
	public MetricsResults getMetricsResults() {
		return metricsResults;
	}

	/**
	 * Sets the metricsResults.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param metricsResults the metricsResults to set
	 */
	public void setMetricsResults(MetricsResults metricsResults) {
		this.metricsResults = metricsResults;
		evaluateProject(metricsResults);
	}

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param metricsResults
	 */
	public void evaluateProject(MetricsResults metricsResults) {
		int contGoodMeasures = 0;
		double projectEval = 0;
		IValue projectEvalValue;
		
		for (Measure measure : metricsResults.getMeasures()) {
			if (measure.evaluate().equals(EvaluationResult.GOOD))
				contGoodMeasures++;
		}
		if(metricsResults.getMeasures().size() > 0) {
			projectEval = ((double)contGoodMeasures*100/metricsResults.getMeasures().size());
			projectEvalValue = new ValueDecimal(projectEval);
		} else {
			projectEvalValue = new ValueUncalculated();
		}
		this.projectEvaluation = new Measure(new MetricConfiguration(new ProjectEvaluationFactory()), projectEvalValue);
	}	

	/**
	 * Gets the projectEvaluation.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the projectEvaluation
	 */
	public Measure getProjectEvaluation() {
		return projectEvaluation;
	}

	public static Comparator<Repository> getComparatorByMetric(Class<? extends Metric> metricType) {
		return new Comparator<Repository>() {
			
			@Override
			public int compare(Repository r1, Repository r2) {
				Measure m1, m2;
				IValue v1, v2;
				if (r1.getMetricsResults() != null && r2.getMetricsResults() != null) {
					if(metricType == ProjectEvaluation.class) {
						m1 = r1.getProjectEvaluation();
						m2 = r2.getProjectEvaluation();
					} else {
						m1 = r1.getMetricsResults().getMeasureForTheMetric(metricType);
						m2 = r2.getMetricsResults().getMeasureForTheMetric(metricType);						
					}
					v1 = (m1 == null)? new ValueUncalculated():m1.getMeasuredValue();
					v2 = (m2 == null)? new ValueUncalculated():m2.getMeasuredValue();
					return IValue.VALUE_COMPARATOR.compare(v1, v2);
				} else if (r1.getMetricsResults() == null && r2.getMetricsResults() != null) {
					return -1;
				} else if (r1.getMetricsResults() != null && r2.getMetricsResults() == null) {
					return 1;
				} else {
					return 0;
				}
			}
		};
	}
	
	public static Comparator<Repository> getComparatorByName() {
		return new Comparator<Repository>() {
			
			@Override
			public int compare(Repository o1, Repository o2) {
				return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
			}
		};
	}
}
