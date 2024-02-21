/**
 * 
 */
package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;

import com.github.javaparser.Range;

import astManager.MethodAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.MaxNestingVisitor;

/**
 * The maximum nesting depth in the code
 * Depth increases for (if-else, switch, do, while, for, ?:)
 * @author Jacob Botha
 *
 */
public class MaxNestingMetric extends Metric { 

	public MaxNestingMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.METHOD);
	}

	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.MethodAST)
	 */
	@Override
	public void run(MethodAST methAst) {
		if(this.alreadyDone(methAst)) return;
		
		
		/*
		 * for the visitor we need to keep track of a max value and a current value
		 * Also the points of interest
		 * Using a visitor to walk over the AST Node and keep track of the depth.
		 */
		Integer[] value = new Integer[2]; 
		value[0] =  0;
		value[1] =  0;
		ArrayList<Range> poi = new ArrayList<Range>();
		
		
		methAst.getNode().accept(new MaxNestingVisitor(poi), value);
		
		MetricResult result = new MetricResult( identifier, (double) value[0], poi);
		methAst.getResults().put(identifier, result);
	}

}