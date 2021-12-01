package metricsengine.metrics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import metricsengine.MetricDescription;
import metricsengine.MetricDescription.EnumTypeOfScale;
import metricsengine.values.ValueDecimal;
import metricsengine.values.ValueInteger;

/**
 * The class provides methods that create arguments for the parameterized tests.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ArgumentsProviders {

	/**
	 * Arguments for {@link metricsengine.numeric_value_metrics.NumericValueMetricTemplate#AMetric(MetricDescription, IValue, IValue)}
	 * <p>
	 * Returns: description, Ivalue min, Ivalue max.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return description, Ivalue min, Ivalue max.
	 */
	public static Stream<Arguments> argumentsForAMetricConstructorWithArguments(){
		MetricDescription metricDescription = new MetricDescription("Prueba", "", "","","","","","","",EnumTypeOfScale.ABSOLUTE, "");
		return Stream.of(
				Arguments.of(metricDescription, new ValueInteger(1), new ValueInteger(10)),
				Arguments.of(metricDescription, new ValueDecimal(1.0), new ValueDecimal(10.9523))
		);
	}
	
	/**
	 * Arguments for {@link metricsengine.numeric_value_metrics.NumericValueMetricTemplate#AMetric(MetricDescription, IValue, IValue)}
	 * <p>
	 * Returns: description, Ivalue min, Ivalue max.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return description, Ivalue min, Ivalue max.
	 */
	public static Stream<Arguments> argumentsForAMetricConstructorWithNullArguments(){
		MetricDescription metricDescription = new MetricDescription("Prueba", "", "","","","","","","",EnumTypeOfScale.ABSOLUTE, "");
		return Stream.of(
				Arguments.of(null, new ValueInteger(1), new ValueInteger(10)),
				Arguments.of(metricDescription, null, new ValueInteger(10)),
				Arguments.of(metricDescription, new ValueDecimal(1.0), null)
		);
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
	 * <li>CD.size == TNC < 2
	 * <li>CD.element = null
	 * <li>CD with same day
	 * <li>Any CD
	 * </ul>
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return totalNumberOfCommits, commitDates, expectedValue, testCase
	 * @throws ParseException When parsing Dates
	 */
	public static Stream<Arguments> argsForCheckMethodInCommitDates() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Set<Date> setEmpty = new HashSet<Date>();
		
		Set<Date> setWithNulls = new HashSet<Date>();
		setWithNulls.add(dateFormat.parse("02/01/2019 10:00"));
		setWithNulls.add(null);
		setWithNulls.add(dateFormat.parse("20/02/2019 15:41"));
	
		Set<Date> setOneElement = new HashSet<Date>();
		setOneElement.add(dateFormat.parse("02/01/2019 10:00"));
		
		Set<Date> setSameDay = new HashSet<Date>();
		setSameDay.add(dateFormat.parse("02/01/2019 10:00"));
		setSameDay.add(dateFormat.parse("02/01/2019 21:00"));
		setSameDay.add(dateFormat.parse("02/01/2019 13:42"));
		
		Set<Date> setAnyDates = new HashSet<Date>();
		setAnyDates.add(dateFormat.parse("01/01/2018 00:00"));
		setAnyDates.add(dateFormat.parse("04/11/2016 14:09"));
		setAnyDates.add(dateFormat.parse("12/10/1492 23:55"));
		setAnyDates.add(dateFormat.parse("13/12/2010 11:55"));
		setAnyDates.add(dateFormat.parse("18/08/2018 18:22"));
		setAnyDates.add(dateFormat.parse("11/11/2014 23:59"));
		
		return Stream.of(
				Arguments.of(null, setAnyDates, false, "TNC = NULL"),
				Arguments.of(1, null, false, "CD = NULL"),
				Arguments.of(null, null, false, "TNC, CD = NULL"),
				Arguments.of(5, setAnyDates, false, "CD.size != TNC"),
				Arguments.of(setEmpty.size(), setEmpty, false, "CD.size == TNC == 0"),
				Arguments.of(setOneElement.size(), setOneElement, false, "CD.size == TNC < 2"),
				Arguments.of(setWithNulls.size(), setWithNulls, false, "CD.element = null"),
				Arguments.of(setSameDay.size(), setSameDay, true, "CD with same day"),
				Arguments.of(setAnyDates.size(), setAnyDates, true, "Any CD")
		);
	}
}