package metricsengine;

import java.io.Serializable;
import java.util.Date;

import metricsengine.values.IValue;

/**
 * Measure computed of a metric.
 * 
 * @author MALB
 *
 */
public class Measure implements Serializable{
	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -2456572983795577849L;

	/**
	 * Metric that has been measured.
	 */
	private MetricConfiguration metricConfiguration;
	
	/**
	 * Value of the measurement.
	 */
	private IValue measuredValue;
	
	/**
	 * Date of measurement.
	 */
	private Date dateOfMeasurement;
	
	/**
	 * Constructor.
	 * 
	 * @param metricConfiguration Metric that has been measured.
	 * @param value Value of the measurement.
	 */
	public Measure(MetricConfiguration metricConfiguration, IValue value) {
		this.metricConfiguration = metricConfiguration;
		this.measuredValue = value;
		this.dateOfMeasurement = new Date();
	}
	
	/**
	 * The metric that has been measured.
	 * 
	 * @return The metric that has been measured.
	 */
	public MetricConfiguration getMetricConfiguration() {
		return metricConfiguration;
	}

	/**
	 * Sets the metricConfiguration.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param metricConfiguration the metricConfiguration to set
	 */
	public void setMetricConfiguration(MetricConfiguration metricConfiguration) {
		this.metricConfiguration = metricConfiguration;
	}

	/**
	 * Gets the value of the measurement.
	 * 
	 * @return The value of the measurement.
	 */
	public IValue getMeasuredValue() {
		return measuredValue;
	}
	
	/**
	 * Gets the date of measurement
	 * 
	 * @return The date of measurement.
	 */
	public Date getDateOfMeasurement() {
		return dateOfMeasurement;
	}
	
	public EvaluationResult evaluate() {
		return metricConfiguration.evaluate(measuredValue);
	}
}
