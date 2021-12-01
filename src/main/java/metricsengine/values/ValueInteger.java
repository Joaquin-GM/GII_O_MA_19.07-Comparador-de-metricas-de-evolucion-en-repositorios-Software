package metricsengine.values;

/**
 * Integer value.
 * 
 * @author MALB
 *
 */
public class ValueInteger extends NumericValue {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -7832402828225892645L;
	/**
	 * Integer value.
	 */
	private int value;
	
	/**
	 * Initialize the value.
	 * 
	 * @param value The value to set.
	 */
	public ValueInteger(int value) {
		this.value = value;
	}

	/**
	 * Gets the value.
	 * 
	 * @return The value.
	 */
	public int getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see metricsengine.IValue#getString()
	 */
	@Override
	public String getValueString() {
		return String.valueOf(value);
	}

	@Override
	public int intValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public IValue valueFactory(int value) {
		return new ValueInteger(value);
	}

	@Override
	public IValue valueFactory(double value) {
		return new ValueInteger((int) Math.round(value));
	}
}
