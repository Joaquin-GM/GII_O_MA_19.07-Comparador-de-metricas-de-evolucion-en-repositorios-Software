package metricsengine.values;

/**
 * Decimal value.
 * 
 * @author MALB
 *
 */
public class ValueDecimal extends NumericValue {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 301704522215335980L;
	/**
	 * Double value.
	 */
	private double value;
	
	/**
	 * Initialize the value.
	 * 
	 * @param value The value to set.
	 */
	public ValueDecimal(double value) {
		this.value = value;
	}
	
	public ValueDecimal(int value) {
		this.value = value;
	}

	/**
	 * Gets the value.
	 * 
	 * @return The value.
	 */
	public double getValue() {
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
		return (int) value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public IValue valueFactory(int value) {
		return new ValueDecimal((double) value);
	}

	@Override
	public IValue valueFactory(double value) {
		return new ValueDecimal(value);
	}

}
