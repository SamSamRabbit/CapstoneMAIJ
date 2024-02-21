/**
 * 
 */
package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;

import com.github.javaparser.Range;

import astManager.ClassAST;
import astManager.MethodAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.ATFDMetricVisitor;

/**Access to Foreign Data Metric, Counts the number of times foreign
 * attributes are used, either directly or through accessor methods.
 * Foreign is taken anything that is not declared in the enclosing class.
 * @author Jacob Botha
 *
 */
public class ATFDMetric extends Metric {

	public ATFDMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.METHOD, MetricLevel.CLASS);
	}

	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.ClassAST)
	 */
	@Override
	public void run(ClassAST clasAst) {
		if(this.alreadyDone(clasAst)) return;
		
		/*
		 * Want the calculated value an the Point of Interest.
		 * Using an array of size 1 as a simple way to implement a mutable integer,
		 * since it needs to be updated inside the visitor.
		 */
		Integer[] value = new Integer[1];
		ArrayList<Range> poi = new ArrayList<Range>();
		value[0] = 0;
		
		/*
		 * Use the ATFDMetricVisitor to walk over the AST and compute results.
		 */
		clasAst.getNode().accept(new ATFDMetricVisitor(clasAst, poi), value);
		
		MetricResult result = new MetricResult(this.identifier, (double)value[0], poi);
		clasAst.getResults().put(this.identifier, result);
	}

	
	
	
	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.MethodAST)
	 */
	@Override
	public void run(MethodAST methAst) {
		if(this.alreadyDone(methAst)) return;
		
		ArrayList<Range> poi = new ArrayList<Range>();
		Integer[] value = new Integer[1];
		value[0] =  0;
				
		/*
		 * Use the ATFDMetricVisitor to walk over the AST and compute results.
		 */
		methAst.getNode().accept(new ATFDMetricVisitor(methAst.getParent(), poi), value);
		
		MetricResult result = new MetricResult(this.identifier, (double)value[0], poi);
		methAst.getResults().put(this.identifier, result);
	}
}

