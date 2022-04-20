package metricsengine.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import datamodel.Repository;
import datamodel.RepositoryInternalMetrics;
import metricsengine.numeric_value_metrics.MetricAverageDaysToCloseAnIssue;
import metricsengine.numeric_value_metrics.MetricAverageDaysToCloseAnIssueFactory;
import metricsengine.values.IValue;
import metricsengine.values.ValueDecimal;

/**
 * Unit test for {@link metricsengine.numeric_value_metrics.MetricAverageDaysToCloseAnIssue}.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricAverageDaysToCloseAnIssueTest {

	/**
	 * Metric under test.
	 */
	private static MetricAverageDaysToCloseAnIssue metricAverageDaysToCloseAnIssue;
	
	/**
	 * Instance the metric under test before all the tests are executed.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		metricAverageDaysToCloseAnIssue = (MetricAverageDaysToCloseAnIssue) new MetricAverageDaysToCloseAnIssueFactory().getMetric();
	}

	/**
	 * Test method for metricsengine.numeric_value_metrics.MetricAverageDaysToCloseAnIssue#MetricAverageDaysToCloseAnIssue().
	 */
	@Test
	public void testMetricAverageDaysToCloseAnIssue() {
		assertEquals(MetricAverageDaysToCloseAnIssue.DEFAULT_METRIC_DESCRIPTION, metricAverageDaysToCloseAnIssue.getDescription(), "Expected default static description");
		assertEquals(MetricAverageDaysToCloseAnIssue.DEFAULT_MIN_VALUE , metricAverageDaysToCloseAnIssue.getValueMinDefault(), "Expected default static min value");
		assertEquals(MetricAverageDaysToCloseAnIssue.DEFAULT_MAX_VALUE, metricAverageDaysToCloseAnIssue.getValueMaxDefault(), "Expected default static max value");
		assertEquals(MetricAverageDaysToCloseAnIssue.DEFAULT_METRIC_DESCRIPTION.getName(), metricAverageDaysToCloseAnIssue.getName(), "Expected default static name");
	}

	/**
	 * Test method for {@link metricsengine.numeric_value_metrics.MetricAverageDaysToCloseAnIssue#check(datamodel.Repository)}.
	 * <p>
	 * Check "check" method for values in this formula: <br/>
	 * "ADCI = SUM(DCI) / NCI. ADCI = Average of days to close an issue. NCI = Number of closed issues. DCI = Vector with the days it took to close each issue."
	 */
	@ParameterizedTest(name= "[{index}]: DCI: {0}, NCI: {1}, Test Case: {3}")
	@MethodSource
	public void testCheck(List<Integer> daysToCloseEachIssue, Integer numberOfClosedIssues, Boolean expectedValue, String testCase) {
		Repository repository = new Repository("URL", "Test", 1L);
		repository.setRepositoryInternalMetrics(new RepositoryInternalMetrics(null, null, numberOfClosedIssues, daysToCloseEachIssue, null, null));
		assertEquals(expectedValue, metricAverageDaysToCloseAnIssue.check(repository), 
				"Should return " + expectedValue +
				" when daysToCloseEachIssue=" + String.valueOf(daysToCloseEachIssue) +
				", numberOfClosedIssues=" + numberOfClosedIssues +
				". Test Case: (" + testCase + ")");
	}

	/**
	 * Test method for {@link metricsengine.numeric_value_metrics.MetricAverageDaysToCloseAnIssue#run(datamodel.Repository)}.
	 * <p>
	 *  Check "run" method for values in this formula: <br/>
	 * "ADCI = SUM(DCI) / NCI. ADCI = Average of days to close an issue. NCI = Number of closed issues. DCI = Vector with the days it took to close each issue."
	 */
	@ParameterizedTest(name= "[{index}]: DCI: {0}, NCI: {1}, Test Case: {3}")
	@MethodSource
	public void testRun(List<Integer> daysToCloseEachIssue, Integer numberOfClosedIssues, IValue expected, String testCase) {
		Repository repository = new Repository("URL", "Test", 1L);
		repository.setRepositoryInternalMetrics(new RepositoryInternalMetrics(null, null, numberOfClosedIssues, daysToCloseEachIssue, null, null));
		IValue actual = metricAverageDaysToCloseAnIssue.run(repository);
		assertEquals(expected.getValueString(), actual.getValueString(), "Incorrect calculation in test case: " + testCase);
	}

	/**
	 * Arguments for {@link #testCheck(List, Integer, Boolean, String)}.
	 * <p>
	 * Test cases for the formula: <br/>
	 * "ADCI = SUM(DCI) / NCI. ADCI = Average of days to close an issue. NCI = Number of closed issues. DCI = Vector with the days it took to close each issue."
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return Stream of: List<Integer> daysToCloseEachIssue, Integer numberOfClosedIssues, Boolean expectedValue, String testCase
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> testCheck() {
		Integer[] lEmpty = {};
		Integer[] lOK = {3,65,21,75,0,12,436,1552,0};
		Integer[] lOK0 = {0,0,0,0,0};
		Integer[] lWithNegativeNumbers = {1,2,-5,2,5,8,123,-23};
		Integer[] lWithNulls = {1,5,432, 1372, null, 2, 90, null, 1};
		return Stream.of(
				Arguments.of(null, 1, false, "DCI = NULL"),
				Arguments.of(Arrays.asList(lOK), null, false, "NCI = NULL"),
				Arguments.of(null, null, false, "DCI, NCI = NULL"),
				Arguments.of(Arrays.asList(lOK), 1, false, "DCI.size != NCI"),
				Arguments.of(Arrays.asList(lEmpty), 0, false, "DCI.size == NCI == 0"),
				Arguments.of(Arrays.asList(lWithNegativeNumbers), lWithNegativeNumbers.length, false, "DCI.element = x < 0"),
				Arguments.of(Arrays.asList(lWithNulls), lWithNulls.length, false, "DCI.element = null"),
				Arguments.of(Arrays.asList(lOK), lOK.length, true, "DCI , NCI, All DCI.elements != null; All DCI.elements >= 0, NCI = DCI.size > 0"),
				Arguments.of(Arrays.asList(lOK0), lOK0.length, true, "DCI , NCI, All DCI.elements != null; All DCI.elements = 0, NCI = DCI.size > 0")
		);
	}
	
	/**
	 * Arguments for {@link #testRun(List, Integer, IValue, String)
	 * <p>
	 * Test cases for the formula: <br/>
	 * "ADCI = SUM(DCI) / NCI. ADCI = Average of days to close an issue. NCI = Number of closed issues. DCI = Vector with the days it took to close each issue."
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return Stream of: List<Integer> daysToCloseEachIssue, Integer numberOfClosedIssues, IValue expected, String testCase
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> testRun() {
		Integer[] lOK = {3,65,21,75,0,12,436,1552,0};
		Integer[] lOK0 = {0,0,0,0,0};
		return Stream.of(
				Arguments.of(Arrays.asList(lOK), lOK.length, new ValueDecimal((double) (3+65+21+75+12+436+1552) / 9), "DCI , NCI, All DCI.elements != null; All DCI.elements >= 0, NCI = DCI.size > 0"),
				Arguments.of(Arrays.asList(lOK0), lOK0.length, new ValueDecimal(0.0), "DCI , NCI, All DCI.elements != null; All DCI.elements = 0, NCI = DCI.size > 0")
		);
	}
}