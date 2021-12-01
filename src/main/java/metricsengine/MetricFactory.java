package metricsengine;

import java.io.Serializable;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public interface MetricFactory extends Serializable {
	Metric getMetric();
}
