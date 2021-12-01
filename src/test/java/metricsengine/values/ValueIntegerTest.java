package metricsengine.values;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test for {@link metricsengine.values.ValueInteger}
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ValueIntegerTest {

	/**
	 * Test method for {@link metricsengine.values.ValueInteger#ValueInteger(int)}.
	 */
	@ParameterizedTest
	@ValueSource(ints = { Integer.MAX_VALUE, Integer.MIN_VALUE, -10, 0, 10 })
	public void testValueInteger(int i) {
		ValueInteger value = new ValueInteger(i);
		assertNotNull(value);
		assertEquals(i, value.getValue());
		assertEquals(String.valueOf(i), value.getValueString());
	}
}
