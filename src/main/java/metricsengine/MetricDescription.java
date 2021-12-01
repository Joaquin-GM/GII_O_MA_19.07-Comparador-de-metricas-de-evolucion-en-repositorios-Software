package metricsengine;

import java.io.Serializable;

/**
 * Description of a metric.
 * 
 * @author MALB
 *
 */
public class MetricDescription implements Serializable{
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 1005568648142879120L;
	/**
	 * Name.
	 */
	private String name;
	/**
	 * Short description.
	 */
	private String description;
	/**
	 * Author of the metric.
	 */
	private String author;
	/**
	 * Year in which the metric was defined. 
	 */
	private String year;
	/**
	 * Category in which it is classified.
	 */
	private String category;
	/**
	 * Intention.
	 */
	private String intention;
	/**
	 * How to calculate the metric.
	 */
	private String formula;
	/**
	 * Source where the metric comes from.
	 */
	private String sourceOfMeasurement;
	/**
	 * Results analysis.
	 */
	private String interpretation;
	/**
	 * Type of scale: Nominal, Ordinal, Interval, Ratio, Absolute.
	 */
	private String typeOfScale;
	/**
	 * Type of measure: Count, Time.
	 */
	private String typeOfMeasure;
	
	/**
	 * @param name Name.
	 * @param description Short description.
	 * @param author Author of the metric.
	 * @param year Year of metric definition.
	 * @param category Category in which it is classified.
	 * @param intention Intention.
	 * @param formula How to calculate the metric.
	 * @param sourceOfMeasurement Source where the metric comes from.
	 * @param interpretation Results analysis.
	 * @param typeOfScale Type of scale: Nominal, Ordinal, Interval, Ratio, Absolute.
	 * @param typeOfMeasure Type of measure: Count, Time.
	 */
	public MetricDescription(String name, String description, String author, String year, String category, String intention, String formula,
			String sourceOfMeasurement, String interpretation, EnumTypeOfScale typeOfScale, String typeOfMeasure) {
		this.name = name;
		this.description = description;
		this.author = author;
		this.year = year;
		this.category = category;
		this.intention = intention;
		this.formula = formula;
		this.sourceOfMeasurement = sourceOfMeasurement;
		this.interpretation = interpretation;
		this.typeOfScale = typeOfScale.toString();
		this.typeOfMeasure = typeOfMeasure;
	}

	/**
	 * Gets the name of the metric.
	 * 
	 * @return The name of the metric.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets a short description of the metric.
	 * 
	 * @return A short description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the author of the metric.
	 * 
	 * @return The author of the metric.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gets the year in which the metric was defined.
	 * 
	 * @return The year in which the metric was defined. 
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Gets the category in which it is classified.
	 * 
	 * @return Category in which it is classified.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Gets the intention.
	 *
	 * @return The intention.
	 */
	public String getIntention() {
		return intention;
	}

	/**
	 * Gets the formula to calculate the metric.
	 * 
	 * @return The formula.
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * Gets the source where the metric comes from.
	 * 
	 * @return The source of measurement.
	 */
	public String getSourceOfMeasurement() {
		return sourceOfMeasurement;
	}

	/**
	 * Gets how to interpretate the results.
	 * 
	 * @return The interpretation.
	 */
	public String getInterpretation() {
		return interpretation;
	}

	/**
	 * Gets the type of scale: Nominal, Ordinal, Interval, Ratio, Absolute.
	 * 
	 * @return The type of scale.
	 */
	public String getTypeOfScale() {
		return typeOfScale;
	}

	/**
	 * Gets the type of measure: Count, Time.
	 * 
	 * @return The type of measure.
	 */
	public String getTypeOfMeasure() {
		return typeOfMeasure;
	}
	
	/**
	 * Type of scale: Nominal, Ordinal, Interval, Ratio, Absolute.
	 * 
	 * @author MALB
	 *
	 */
	public enum EnumTypeOfScale {
		NOMINAL, ORDINAL, INTERVAL, RATIO, ABSOLUTE
	}
}
