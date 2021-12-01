package metricsengine.values;

/**
 * Value that obtains a measure of a metric that could not be calculated.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ValueUncalculated implements IValue {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -3313123488849246520L;
	public static final String VALUE = "NC";
	
	/* (non-Javadoc)
	 * @see metricsengine.values.IValue#valueToString()
	 */
	@Override
	public String getValueString() {
		return VALUE;
	}

	@Override
	public IValue valueFactory(double value) {
		return new ValueUncalculated();
	}

	@Override
	public IValue valueFactory(int value) {
		return new ValueUncalculated();
	}

}
