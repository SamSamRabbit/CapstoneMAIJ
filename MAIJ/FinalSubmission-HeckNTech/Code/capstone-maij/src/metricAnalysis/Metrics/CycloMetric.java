/**
 * 
 */
package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;

import com.github.javaparser.Range;

import astManager.MethodAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.CycloMetricVisitor;

/**Cyclomatic Complexity Metric
 * Calculates the number of independent paths through a block of code.
 * 
 * Methods have a base complexity of 1.
 * Every control flow statement adds 1. (if, case, catch, throw, do, while, for, break, continue, ?:)
 * else, finally and default don't count.
 * Boolean && and || adds 1 because of short circuit evaluation.
 * 
 * definition taken from https://pmd.github.io/pmd-6.7.0/pmd_java_metrics_index.html#cyclomatic-complexity-cyclo
 * @author Jacob Botha
 */
public class CycloMetric extends Metric {
	public CycloMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.METHOD);
	}


	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.BaseAST)
	 */
	@Override
	public void run(MethodAST methAst) {
		if(this.alreadyDone(methAst)) return;
		
		/*
		 * Want the calculated value and points of interest.
		 * Methods have a base complexity of 1.
		 * Using a visitor to calculate the metric.
		 */
		Integer[] value = new Integer[1];
		value[0] =  1;
		ArrayList<Range> poi = new ArrayList<Range>();
		poi.add(methAst.getNode().getRange().get());
		methAst.getNode().accept(new CycloMetricVisitor(poi), value);
		
		/*
		 * Add the results to the MethodAST the metric is being calculated on.
		 */
		MetricResult result = new MetricResult( identifier, (double) value[0], poi);
		methAst.getResults().put(identifier, result);
	}

}
