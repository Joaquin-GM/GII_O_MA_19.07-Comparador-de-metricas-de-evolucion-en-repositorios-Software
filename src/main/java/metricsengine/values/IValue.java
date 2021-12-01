package metricsengine.values;

import java.io.Serializable;

/**
 * Defines the interface of a metric value.
 *  
 * @author MALB
 *
 */
public interface IValue extends Serializable{
	
	public static final ValueComparator VALUE_COMPARATOR = new ValueComparator();
	
	/**
	 * Return a string representing the value.
	 * 
	 * @return A value parsed to string.
	 */
	String getValueString();

	IValue valueFactory(double value);

	IValue valueFactory(int value);	
}
