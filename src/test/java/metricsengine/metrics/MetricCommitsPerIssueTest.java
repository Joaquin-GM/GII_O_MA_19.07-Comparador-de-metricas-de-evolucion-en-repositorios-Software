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
import metricsengine.numeric_value_metrics.MetricCommitsPerIssue;
import metricsengine.numeric_value_metrics.MetricCommitsPerIssueFactory;
import metricsengine.values.IValue;
import metricsengine.values.ValueDecimal;

/**
 * Unit test for {@link metricsengine.numeric_value_metrics.MetricCommitsPerIssue}.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricCommitsPerIssueTest {

	/**
	 * Metric under test.
	 */
	private static MetricCommitsPerIssue metricCommitsPerIssue;
	
	/**
	 * Instance the metric under test before all the tests are executed.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		metricCommitsPerIssue = (MetricCommitsPerIssue) new MetricCommitsPerIssueFactory().getMetric();
	}

	/**
	 * Test method for {@link metricsengine.numeric_value_metrics.MetricCommitsPerIssue#MetricCommitsPerIssue()}.
	 */
	@Test
	public void testMetricCommitsPerIssue() {
		assertEquals(MetricCommitsPerIssue.DEFAULT_METRIC_DESCRIPTION, metricCommitsPerIssue.getDescription(), "Expected default static description");
		assertEquals(MetricCommitsPerIssue.DEFAULT_MIN_VALUE , metricCommitsPerIssue.getValueMinDefault(), "Expected default static min value");
		assertEquals(MetricCommitsPerIssue.DEFAULT_MAX_VALUE, metricCommitsPerIssue.getValueMaxDefault(), "Expected default static max value");
		assertEquals(MetricCommitsPerIssue.DEFAULT_METRIC_DESCRIPTION.getName(), metricCommitsPerIssue.getName(), "Expected default static name");
	}

	/**
	 * Test method for {@link metricsengine.numeric_value_metrics.MetricCommitsPerIssue#check(datamodel.Repository)}.
	 * <p>
	 * Check "check" method for values in this formula: <br/>
	 * "CI = TNI/TNC. CI = Commits per issue.TNI = Total number of issues. TNC = Total number of commits"
	 */
	@ParameterizedTest(name= "[{index}]: TNI: {0}, TNC: {1}, Test Case: {3}")
	@MethodSource
	public void testCheck(Integer totalNumberOfIssues, Integer totalNumberOfCommits, Boolean expected, String testCase) {
		Repository repository = new Repository("URL", "Test", 1);
		repository.setRepositoryInternalMetrics(new RepositoryInternalMetrics(totalNumberOfIssues, totalNumberOfCommits, null, null, null, null));
		assertEquals(expected, metricCommitsPerIssue.check(repository), 
				"Should return " + expected + 
				" when totalNumberOfIssues=" + String.valueOf(totalNumberOfIssues) +
				", totalNumberOfCommits=" + String.valueOf(totalNumberOfCommits) +
				". Test Case: (" + testCase + ")");
	}
	
	/**
	 * Test method for {@link metricsengine.numeric_value_metrics.MetricCommitsPerIssue#run(datamodel.Repository)}.
	 * <p>
	 * Check "run" method for values in this formula: <br/>
	 * "CI = TNI/TNC. CI = Commits per issue.TNI = Total number of issues. TNC = Total number of commits"
	 */
	@ParameterizedTest(name = "[{index}]: TNI: {0}, TNC: {1}, Test Case: {3}")
	@MethodSource
	public void testRun(Integer totalNumberOfIssues, Integer totalNumberOfCommits, IValue expected, String testCase) {
		Repository repository = new Repository("URL", "Test", 1);
		repository.setRepositoryInternalMetrics(new RepositoryInternalMetrics(totalNumberOfIssues, totalNumberOfCommits, null, null, null, null));
		IValue actual = metricCommitsPerIssue.run(repository);
		assertEquals(expected.getValueString(), actual.getValueString(), "Incorrect calculation in test case: " + testCase);
	}
	
	/**
	 * Arguments for {@link #testCheck(Integer, Integer, Boolean, String)}.
	 * <p>
	 * Test cases for the formula: <br/>
	 * "CI = TNI/TNC. CI = Commits per issue.TNI = Total number of issues. TNC = Total number of commits"
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return Stream of: Integer totalNumberOfIssues, Integer totalNumberOfCommits, Boolean expected, String testCase
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> testCheck() {
		return Stream.of(
				Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE, true, "TNI, TNC = Integer.MAX_VALUE"),
				Arguments.of(Integer.MAX_VALUE, 1, true, "TNI = Integer.MAX_VALUE"),
				Arguments.of(1, Integer.MAX_VALUE, true, "TNC = Integer.MAX_VALUE"),
				Arguments.of(Integer.MIN_VALUE, Integer.MIN_VALUE, false, "TNI, TNC = Integer.MIN_VALUE"),
				Arguments.of(Integer.MIN_VALUE, 1, false, "TNI = Integer.MIN_VALUE"),
				Arguments.of(1, Integer.MIN_VALUE, false, "TNC = Integer.MIN_VALUE"),
				Arguments.of(0, 0, false, "TNI, TNC = 0"),
				Arguments.of(0, 1, true, "TNI = 0"),
				Arguments.of(10, 0, false, "TNC = 0"),
				Arguments.of(1, 1, true, "TNI = TNC. TNI, TNC > 0"),
				Arguments.of(10, 5, true, "TNI > TNC. TNI, TNC > 0"),
				Arguments.of(12, 432, true, "TNI < TNC. TNI, TNC > 0"),
				Arguments.of(-1, -1, false, "TNI = TNC. TNI, TNC < 0"),
				Arguments.of(-10, -5, false, "TNI < TNC. TNI, TNC < 0"),
				Arguments.of(-12, -432, false, "TNI > TNC. TNI, TNC < 0"),
				Arguments.of(-10, 1, false, "TNI < 0"),
				Arguments.of(10, -23, false, "TNC < 0"),
				Arguments.of(null, null, false, "TNI, TNC = null"),
				Arguments.of(null, 5, false, "TNI = null"),
				Arguments.of(1, null, false, "TNC = null")
		);
	}
	
	/**
	 * Arguments for {@link #testRun(Integer, Integer, IValue, String)}.
	 * <p>
	 * Test cases for the formula: <br/>
	 * "CI = TNI/TNC. CI = Commits per issue.TNI = Total number of issues. TNC = Total number of commits"
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return Stream of: Integer totalNumberOfIssues, Integer totalNumberOfCommits, IValue expected, String testCase
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> testRun() {
		return Stream.of(				        
				Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE, new ValueDecimal(1.0), "TNI, TNC = Integer.MAX_VALUE"),
				Arguments.of(Integer.MAX_VALUE, 1, new ValueDecimal((double) 1 / Integer.MAX_VALUE), "TNI = Integer.MAX_VALUE"),
				Arguments.of(1, Integer.MAX_VALUE, new ValueDecimal((double) Integer.MAX_VALUE), "TNC = Integer.MAX_VALUE"),
				Arguments.of(1, 0, new ValueDecimal(0.0), "TNC = 0"),
				Arguments.of(5, 5, new ValueDecimal(1.0), "TNI = TNC.TNI, TNC > 0"),
				Arguments.of(5, 10, new ValueDecimal(2.0), "TNC > TNI.TNI, TNC > 0"),
				Arguments.of(432, 12, new ValueDecimal((double) 12 / 432), "TNC < TNI.TNI, TNC > 0")
		);
	}
}