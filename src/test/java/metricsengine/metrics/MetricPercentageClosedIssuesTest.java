package metricsengine.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import datamodel.Repository;
import datamodel.RepositoryInternalMetrics;
import metricsengine.numeric_value_metrics.MetricPercentageClosedIssues;
import metricsengine.numeric_value_metrics.MetricPercentageClosedIssuesFactory;
import metricsengine.values.IValue;
import metricsengine.values.ValueDecimal;

/**
 * Unit test for {@link metricsengine.numeric_value_metrics.MetricPercentageClosedIssues}.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricPercentageClosedIssuesTest {

	/**
	 * Metric under test.
	 */
	private static MetricPercentageClosedIssues metricPercentageClosedIssues;
	

	/**
	 * Instance the metric under test before all the tests are executed.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		metricPercentageClosedIssues = (MetricPercentageClosedIssues) new MetricPercentageClosedIssuesFactory().getMetric();
	}

	/**
	 * Test method for {@link metricsengine.numeric_value_metrics.MetricPercentageClosedIssues#MetricPercentageClosedIssues()}.
	 */
	@Test
	public void testMetricPercentageClosedIssues() {
		assertEquals(MetricPercentageClosedIssues.DEFAULT_METRIC_DESCRIPTION, metricPercentageClosedIssues.getDescription(), "Expected default static description");
		assertEquals(MetricPercentageClosedIssues.DEFAULT_MIN_VALUE , metricPercentageClosedIssues.getValueMinDefault(), "Expected default static min value");
		assertEquals(MetricPercentageClosedIssues.DEFAULT_MAX_VALUE, metricPercentageClosedIssues.getValueMaxDefault(), "Expected default static max value");
		assertEquals(MetricPercentageClosedIssues.DEFAULT_METRIC_DESCRIPTION.getName(), metricPercentageClosedIssues.getName(), "Expected default static name");
	}

	/**
	 * Test method for {@link metricsengine.numeric_value_metrics.MetricPercentageClosedIssues#check(datamodel.Repository)}.
	 * <p>
	 * Check "check" method for values in this formula: <br/>
	 * PIC = (NCI/TNI) * 100. PIC = Percentage of issues closed.TNI = Total number of issues. NCI = Number of closed issues
	 * 
	 */
	@ParameterizedTest(name= "[{index}]: TNI: {0}, NCI: {1}, Calculable: {2}, Test Case: {3}")
	@MethodSource
	public void testCheck(Integer totalNumberOfIssues, Integer numberOfClosedIssues, Boolean expectedValue, String testCase) {
		Repository repository = new Repository("URL", "Test", 1);
		repository.setRepositoryInternalMetrics(new RepositoryInternalMetrics(totalNumberOfIssues, null, numberOfClosedIssues, null, null, null));
		assertEquals(expectedValue, metricPercentageClosedIssues.check(repository), 
				"Should return " + expectedValue +
				" when totalNumberOfIssues=" + totalNumberOfIssues +
				", numberOfClosedIssues=" + numberOfClosedIssues +
				". Test Case: (" + testCase + ")");
	}

	/**
	 * Test method for {@link metricsengine.numeric_value_metrics.MetricPercentageClosedIssues#run(datamodel.Repository)}.
	 * <p>
	 * Check "run" method for values in this formula: <br/>
	 * "PIC = (NCI/TNI) * 100. PIC = Percentage of issues closed.TNI = Total number of issues. NCI = Number of closed issues"
	 */
	@ParameterizedTest(name = "[{index}] TNI = {0}, NCI = {1}, Test Case: {2}")
	@MethodSource
	public void testRun(Integer totalNumberOfIssues, Integer numberOfClosedIssues, IValue expected, String testCase) {
		Repository repository = new Repository("URL", "Test", 1);
		repository.setRepositoryInternalMetrics(new RepositoryInternalMetrics(totalNumberOfIssues, null, numberOfClosedIssues, null, null, null));
		IValue actual = metricPercentageClosedIssues.run(repository);
		assertEquals(expected.getValueString(), actual.getValueString(), "Incorrect calculation in test case: " + testCase);
	}

	/**
	 * Arguments for {@link #testCheck(Integer, Integer, Boolean, String)}
	 * <p>
	 * Test cases for the formula: <br/>
	 * PIC = (NCI/TNI) * 100. PIC = Percentage of issues closed.TNI = Total number of issues. NCI = Number of closed issues
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return Stream of: Integer totalNumberOfIssues, Integer numberOfClosedIssues, Boolean expectedValue, String testCase
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> testCheck() {
		return Stream.of(
				Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE, true, "TNI, NCI = Integer.MAX_VALUE"),
				Arguments.of(Integer.MAX_VALUE, 1, true, "TNI = Integer.MAX_VALUE"),
				Arguments.of(1, Integer.MAX_VALUE, true, "NCI"),
				Arguments.of(Integer.MIN_VALUE, Integer.MIN_VALUE, false, "TNI, NCI = Integer.MIN_VALUE"),
				Arguments.of(Integer.MIN_VALUE, 1, false, "TNI = Integer.MIN_VALUE"),
				Arguments.of(1, Integer.MIN_VALUE, false, "NCI = Integer.MIN_VALUE"),
				Arguments.of(0, 0, false, "TNI, NCI = 0"),
				Arguments.of(0, 1, false, "TNI = 0"),
				Arguments.of(1, 0, true, "NCI = 0"),
				Arguments.of(1, 1, true, "TNI = NCI.TNI, NCI > 0"),
				Arguments.of(10, 5, true, "TNI > NCI.TNI, NCI > 0"),
				Arguments.of(12, 432, true, "TNI < NCI.TNI, NCI > 0"),
				Arguments.of(-1, -1, false, "TNI = NCI.TNI, NCI < 0"),
				Arguments.of(-10, -5, false, "TNI < NCI.TNI, NCI < 0"),
				Arguments.of(-12, -432, false, "TNI > NCI.TNI, NCI < 0"),
				Arguments.of(-10, 1, false, "TNI < 0"),
				Arguments.of(10, -23, false, "NCI < 0"),
				Arguments.of(null, null, false, "TNI, NCI = null"),
				Arguments.of(null, 5, false, "TNI = null"),
				Arguments.of(1, null, false, "NCI = null")
		);
	}
	
	/**
	 * Arguments for {@link #testRun(Integer, Integer, IValue, String)}.
	 * <p>
	 * Test cases for the formula: <br/>
	 * PIC = (NCI/TNI) * 100. PIC = Percentage of issues closed.TNI = Total number of issues. NCI = Number of closed issues
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return Stream of: Integer totalNumberOfIssues, Integer numberOfClosedIssues, IValue expected, String testCase
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> testRun() {
		return Stream.of(
				Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE, new ValueDecimal(100.0), "TNI, NCI = Integer.MAX_VALUE"),
				Arguments.of(Integer.MAX_VALUE, 1, new ValueDecimal((double) 100 / Integer.MAX_VALUE), "TNI = Integer.MAX_VALUE"),
				Arguments.of(1, Integer.MAX_VALUE, new ValueDecimal((double) Integer.MAX_VALUE * 100), "NCI = Integer.MAX_VALUE"),
				Arguments.of(1, 0, new ValueDecimal(0.0), "NCI = 0"),
				Arguments.of(5, 10, new ValueDecimal(200.0), "NCI > TNI. TNI, NCI > 0"),
				Arguments.of(1, 1, new ValueDecimal(100.0), "TNI = NCI"),
				Arguments.of(15, 10, new ValueDecimal((double) 1000 / 15), "NCI < TNI. TNI, NCI > 0")
		);
	}
}