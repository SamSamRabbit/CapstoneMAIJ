/**
 * 
 */
package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.MethodDeclaration;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

/**Number of methods metric - calculate how many methods a class has - does not count constructors.
 * @author Jacob Botha
 *
 */
public class NOMMetric extends Metric {
	
	public NOMMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}

	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.ClassAST)
	 */
	@Override
	public void run(ClassAST clasAst) {
		if(this.alreadyDone(clasAst)) return;
		
		Integer value = 0;
		value =  clasAst.getNode().getMethods().size();	
		
		ArrayList<Range> poi = new ArrayList<Range>();
		for( MethodDeclaration x : clasAst.getNode().getMethods()) {
			poi.add(x.getRange().get());
		}

		MetricResult result = new MetricResult( identifier, (double) value, poi);
		clasAst.getResults().put(identifier, result);
	}
}
