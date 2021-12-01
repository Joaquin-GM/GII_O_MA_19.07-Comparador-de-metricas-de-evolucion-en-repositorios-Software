package metricsengine.values;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test for {@link metricsengine.values.ValueDecimal}
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ValueDecimalTest {

	/**
	 * Test method for {@link metricsengine.values.ValueDecimal#ValueDecimal(java.lang.Double)}.
	 */
	@ParameterizedTest
	@ValueSource(doubles = { Double.MAX_VALUE, Double.MIN_VALUE, -10.00, 0, 10.00, 1})
	public void testValueDecimal(double d) {
		ValueDecimal value = new ValueDecimal(d);
		assertNotNull(value);
		assertEquals(d, value.getValue());
		assertEquals(String.valueOf(d), value.getValueString());
	}

}
