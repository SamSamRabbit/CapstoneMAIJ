/**
 * 
 */
package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;

import astManager.ClassAST;
import astManager.MethodAST;
import astManager.PackageAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.NOSMetricVisitor;

/**
 * Number of Statements metric - counts the number of statements.
 * Does not include comments.
 * @author Jacob Botha
 *
 */
public class NOSMetric extends Metric {
	public NOSMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.PACKAGE, MetricLevel.CLASS, MetricLevel.METHOD);

	}
	
	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.ClassAST)
	 */
	@Override
	public void run(ClassAST clasAst) {
		if(this.alreadyDone(clasAst)) return;
		
		Integer[] value = new Integer[1];
		value[0] =  0;
		ArrayList<Range> poi = new ArrayList<Range>();
		
		clasAst.getNode().accept(new NOSMetricVisitor(poi), value);
		
		MetricResult result = new MetricResult( identifier, (double) value[0], poi);
		clasAst.getResults().put(identifier, result);
	}

	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.MethodAST)
	 */
	@Override
	public void run(MethodAST methAst) {
		if(this.alreadyDone(methAst)) return;
		
		Integer[] value = new Integer[1];
		value[0] =  0;
		ArrayList<Range> poi = new ArrayList<Range>();
		
		methAst.getNode().accept(new NOSMetricVisitor(poi), value);
		
		MetricResult result = new MetricResult( identifier, (double)value[0], poi);
		methAst.getResults().put(identifier, result);
	}

	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.PackageAST)
	 */
	@Override
	public void run(PackageAST packAst) {
		if(this.alreadyDone(packAst)) return;
		
		Integer[] value = new Integer[1];
		value[0] =  0;
		ArrayList<Range> poi = new ArrayList<Range>();
		
		Set<CompilationUnit> cus = packAst.getCus();
		NOSMetricVisitor nosVisitor = new NOSMetricVisitor(poi);
		for(CompilationUnit cu : cus) {
			cu.accept(nosVisitor, value);
		}
		
		MetricResult result = new MetricResult( identifier, (double)value[0]);
		packAst.getResults().put(identifier, result);
	}

}
