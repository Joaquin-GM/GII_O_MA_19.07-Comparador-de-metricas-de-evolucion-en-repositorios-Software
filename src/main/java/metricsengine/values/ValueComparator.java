package metricsengine.values;

import java.util.Comparator;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ValueComparator implements Comparator<IValue> {

	@Override
	public int compare(IValue o1, IValue o2) {
		if (o1 instanceof NumericValue && o2 instanceof NumericValue) {
			NumericValue n1 = (NumericValue) o1;
			NumericValue n2 = (NumericValue) o2;
			return Double.compare(n1.doubleValue(), n2.doubleValue());
		} else if (o1 instanceof NumericValue && !(o2 instanceof NumericValue)) {
			return 1;
		} else if (!(o1 instanceof NumericValue) && o2 instanceof NumericValue) {
			return -1;
		} else {
			return 0;
		}
	}

}
