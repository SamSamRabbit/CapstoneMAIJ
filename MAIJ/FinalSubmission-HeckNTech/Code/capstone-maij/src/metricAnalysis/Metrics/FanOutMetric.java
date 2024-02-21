/**
 * 
 */
package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.Range;

import astManager.ClassAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.FanOutMetricVisitor;

/**Fan Out Metric - Defined as the number of other classes referenced by a class.
 * Currently doesn't check field access or method calls, due to how the parser works.
 * @author Jacob Botha
 *
 */
public class FanOutMetric extends Metric {

	public FanOutMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}

	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.ClassAST)
	 */
	@Override
	public void run(ClassAST clasAst) {
		if(this.alreadyDone(clasAst)) return;
		
		/*
		 * Need to extract the point of interest and unique references to foreign types.
		 * using a visitor to walk over the AST node and record the unique references.
		 */
		Set<String> referenced = new HashSet<String>();
		ArrayList<Range> poi = new ArrayList<Range>();
		
		clasAst.getNode().accept(new FanOutMetricVisitor(clasAst, poi), referenced);
		
		MetricResult result = new MetricResult(this.identifier, (double)referenced.size(), poi);
		clasAst.getResults().put(this.identifier, result);
	}
}
