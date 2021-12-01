package metricsengine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link metricsengine.MetricDescription}
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricDescriptionTest {

	/**
	 * Test method for {@link metricsengine.MetricDescription#MetricDescription(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, metricsengine.MetricDescription.EnumTypeOfScale, java.lang.String)}.
	 */
	@Test
	public void testMetricDescription() {
		MetricDescription metricTotalNumberOfIssuesDescription = new MetricDescription(
				"I1", 
				"Total number of Issues", 
				"", 
				"", 
				"Orientation process", 
				"How many issues have been defined in the repository?", 
				"NI = Number of issues", 
				"Repository", 
				"NI >= 0. Best low values", 
				MetricDescription.EnumTypeOfScale.ABSOLUTE, 
				"NI : Counter");
		assertNotNull(metricTotalNumberOfIssuesDescription);
		assertEquals("I1", metricTotalNumberOfIssuesDescription.getName());
		assertEquals("Total number of Issues", metricTotalNumberOfIssuesDescription.getDescription());
		assertEquals("", metricTotalNumberOfIssuesDescription.getAuthor());
		assertEquals("", metricTotalNumberOfIssuesDescription.getYear());
		assertEquals("Orientation process", metricTotalNumberOfIssuesDescription.getCategory());
		assertEquals("How many issues have been defined in the repository?", metricTotalNumberOfIssuesDescription.getIntention());
		assertEquals("NI = Number of issues", metricTotalNumberOfIssuesDescription.getFormula());
		assertEquals("Repository", metricTotalNumberOfIssuesDescription.getSourceOfMeasurement());
		assertEquals("NI >= 0. Best low values", metricTotalNumberOfIssuesDescription.getInterpretation());
		assertEquals(MetricDescription.EnumTypeOfScale.ABSOLUTE.toString(), metricTotalNumberOfIssuesDescription.getTypeOfScale());
		assertEquals("NI : Counter", metricTotalNumberOfIssuesDescription.getTypeOfMeasure());
	}
}
