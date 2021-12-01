package metricsengine.numeric_value_metrics;

import metricsengine.Metric;
import metricsengine.MetricFactory;

public class ProjectEvaluationFactory implements MetricFactory {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -4383950032132050649L;

	public ProjectEvaluationFactory() {
	}

	@Override
	public Metric getMetric() {
		return ProjectEvaluation.getInstance();
	}

}
