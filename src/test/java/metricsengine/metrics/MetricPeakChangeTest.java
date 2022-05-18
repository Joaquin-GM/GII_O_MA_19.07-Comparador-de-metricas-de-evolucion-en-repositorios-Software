package metricsengine.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import datamodel.Repository;
import datamodel.RepositoryInternalMetrics;
import metricsengine.numeric_value_metrics.MetricPeakChange;
import metricsengine.numeric_value_metrics.MetricPeakChangeFactory;
import metricsengine.values.IValue;
import metricsengine.values.ValueDecimal;

/**
 * Unit test for {@link metricsengine.numeric_value_metrics.MetricPeakChange}.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricPeakChangeTest {

	/**
	 * Metric under test.
	 */
	private static MetricPeakChange metricPeakChange;
	
	/**
	 * Instance the metric under test before all the tests are executed.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		metricPeakChange = (MetricPeakChange) new MetricPeakChangeFactory().getMetric();
	}

	/**
	 * Test method for metricsengine.numeric_value_metrics.MetricPeakChange#MetricPeakChange().
	 */
	@Test
	public void testMetricPeakChange() {
		assertEquals(MetricPeakChange.DEFAULT_METRIC_DESCRIPTION, metricPeakChange.getDescription(), "Expected default static description");
		assertEquals(MetricPeakChange.DEFAULT_MIN_VALUE , metricPeakChange.getValueMinDefault(), "Expected default static min value");
		assertEquals(MetricPeakChange.DEFAULT_MAX_VALUE, metricPeakChange.getValueMaxDefault(), "Expected default static max value");
		assertEquals(MetricPeakChange.DEFAULT_METRIC_DESCRIPTION.getName(), metricPeakChange.getName(), "Expected default static name");
	}

	/**
	 * Test method for {@link metricsengine.numeric_value_metrics.MetricPeakChange#check(datamodel.Repository)}.
	 * <p>
	 * Check "check" method for values in this formula: <br/>
	 * "PCC = NCPM / TNC. PCC = Peak Change Count, NCPM = Number of commits in peak month, TNC = Total number of commits"
	 */
	@ParameterizedTest(name = "[{index}] TNC = {0}, CD = {1}, Test Case: {3}")
	@MethodSource
	public void testCheck(Integer totalNumberOfCommits, Set<Date> commitDates, Boolean expected, String testCase) {
		Repository repository = new Repository("URL", "Test", 1L);
		repository.setRepositoryInternalMetrics(new RepositoryInternalMetrics(null, totalNumberOfCommits, null, null, commitDates, null));
		assertEquals(expected, metricPeakChange.check(repository), 
				"Should return " + expected +
				" when totalNumberOfCommits=" + String.valueOf(totalNumberOfCommits) +
				", commitDates=" + commitDates +
				". Test Case: (" + testCase + ")");
	}

	/**
	 * Test method for {@link metricsengine.numeric_value_metrics.MetricPeakChange#run(datamodel.Repository)}.
	 * <p>
	 * Check "run" method for values in this formula: <br/>
	 * "PCC = NCPM / TNC. PCC = Peak Change Count, NCPM = Number of commits in peak month, TNC = Total number of commits"
	 */
	@ParameterizedTest(name = "[{index}] TNC = {0}, CD = {1}, Test Case: {3}")
	@MethodSource
	public void testRun(Integer totalNumberOfCommits, Set<Date> commitDates, IValue expected, String testCase) {
		Repository repository = new Repository("URL", "Test", 1L);
		repository.setRepositoryInternalMetrics(new RepositoryInternalMetrics(null, totalNumberOfCommits, null, null, commitDates, null));
		IValue actual = metricPeakChange.run(repository);
		assertEquals(expected.getValueString(), actual.getValueString(), "Incorrect calculation in test case: " + testCase);
	}

	/**
	 * Arguments for "check" method in metrics that uses "commit dates" and "total number of commits".
	 * <p>
	 * Test cases for "check" method that check "commit dates" and "total number of commits":
	 * <ul>
	 * <li>TNC = NULL
	 * <li>CD = NULL
	 * <li>TNC, CD = NULL
	 * <li>CD.size != TNC
	 * <li>CD.size == TNC == 0
	 * <li>CD.element = null
	 * <li>CD.size == TNC == 1
	 * <li>CD with same day
	 * <li>Two months with same number of commits
	 * <li>Months in different years
	 * </ul>
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return totalNumberOfCommits, commitDates, expectedValue, testCase
	 * @throws ParseException When parsing Dates
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> testCheck() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Set<Date> setEmpty = new HashSet<Date>();
		
		Set<Date> setWithNulls = new HashSet<Date>();
		setWithNulls.add(dateFormat.parse("02/01/2019 10:00"));
		setWithNulls.add(null);
		setWithNulls.add(dateFormat.parse("20/02/2019 15:41"));
	
		Set<Date> setOneElement = new HashSet<Date>();//1
		setOneElement.add(dateFormat.parse("02/01/2019 10:00"));
		
		Set<Date> setSameDay = new HashSet<Date>();//1
		setSameDay.add(dateFormat.parse("02/01/2019 10:00"));
		setSameDay.add(dateFormat.parse("02/01/2019 21:00"));
		setSameDay.add(dateFormat.parse("02/01/2019 13:42"));
		
		Set<Date> setDraw = new HashSet<Date>();//2
		setDraw.add(dateFormat.parse("01/01/2018 14:52"));
		setDraw.add(dateFormat.parse("10/01/2018 20:53"));
		setDraw.add(dateFormat.parse("01/02/2018 14:19"));
		setDraw.add(dateFormat.parse("20/02/2018 23:05"));
		setDraw.add(dateFormat.parse("16/03/2018 22:00"));
		
		Set<Date> setDifferentYears = new HashSet<Date>();//3
		setDifferentYears.add(dateFormat.parse("01/11/2018 01:40"));
		setDifferentYears.add(dateFormat.parse("10/11/2018 06:00"));
		setDifferentYears.add(dateFormat.parse("18/11/2018 08:30"));
		setDifferentYears.add(dateFormat.parse("01/01/2016 14:19"));
		setDifferentYears.add(dateFormat.parse("20/01/2017 20:52"));
		setDifferentYears.add(dateFormat.parse("12/01/2018 16:15"));
		setDifferentYears.add(dateFormat.parse("07/01/2018 15:25"));
		
		return Stream.of(
				Arguments.of(null, setSameDay, false, "TNC = NULL"),
				Arguments.of(1, null, false, "CD = NULL"),
				Arguments.of(null, null, false, "TNC, CD = NULL"),
				Arguments.of(setSameDay.size() + 1, setSameDay, true, "CD.size != TNC"),
				Arguments.of(setEmpty.size(), setEmpty, false, "CD.size == TNC == 0"),
				Arguments.of(setWithNulls.size(), setWithNulls, false, "CD.element = null"),
				Arguments.of(setOneElement.size(), setOneElement, true, "CD.size == TNC == 1"),
				Arguments.of(setSameDay.size(), setSameDay, true, "CD with same day"),
				Arguments.of(setDraw.size(), setDraw, true, "Two months with same number of commits"),
				Arguments.of(setDifferentYears.size(), setDifferentYears, true, "Months in different years")
		);
	}
	
	/**
	 * Arguments for {@link #testRun(Integer, Set, IValue, String)}.
	 * <p>
	 * Test cases for the formula: <br/>
	 * Test cases for "check" method that check "commit dates" and "total number of commits":
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return Stream of: Integer totalNumberOfCommits, Set<Date> commitDates, IValue expected, String testCase
	 * @throws ParseException When parsing Dates fail
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> testRun() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Set<Date> setOneElement = new HashSet<Date>();//1
		setOneElement.add(dateFormat.parse("02/01/2019 10:00"));
		
		Set<Date> setSameDay = new HashSet<Date>();//1
		setSameDay.add(dateFormat.parse("02/01/2019 10:00"));
		setSameDay.add(dateFormat.parse("02/01/2019 21:00"));
		setSameDay.add(dateFormat.parse("02/01/2019 13:42"));
		
		Set<Date> setDraw = new HashSet<Date>();//2
		setDraw.add(dateFormat.parse("01/01/2018 14:52"));
		setDraw.add(dateFormat.parse("10/01/2018 20:53"));
		setDraw.add(dateFormat.parse("01/02/2018 14:19"));
		setDraw.add(dateFormat.parse("20/02/2018 23:05"));
		setDraw.add(dateFormat.parse("16/03/2018 22:00"));
		
		Set<Date> setDifferentYears = new HashSet<Date>();//3
		setDifferentYears.add(dateFormat.parse("01/11/2018 01:40"));
		setDifferentYears.add(dateFormat.parse("10/11/2018 06:00"));
		setDifferentYears.add(dateFormat.parse("18/11/2018 08:30"));
		setDifferentYears.add(dateFormat.parse("01/01/2016 14:19"));
		setDifferentYears.add(dateFormat.parse("20/01/2017 20:52"));
		setDifferentYears.add(dateFormat.parse("12/01/2018 16:15"));
		setDifferentYears.add(dateFormat.parse("07/01/2018 15:25"));
		
		return Stream.of(
				Arguments.of(setOneElement.size(), setOneElement, new ValueDecimal(1.0), "CD.size == TNC == 1"),
				Arguments.of(setSameDay.size(), setSameDay, new ValueDecimal(1.0), "CD with same day"),
				Arguments.of(setDraw.size(), setDraw, new ValueDecimal((double) 2/5), "Two months with same number of commits"),
				Arguments.of(setDifferentYears.size(), setDifferentYears, new ValueDecimal((double) 3/7), "Months in different years")
		);
	}
}