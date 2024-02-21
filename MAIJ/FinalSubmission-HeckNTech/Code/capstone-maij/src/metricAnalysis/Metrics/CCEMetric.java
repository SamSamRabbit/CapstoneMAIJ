/*
 * Author: Sam
 * Critical Classes Extensibility (CCE)
 */
package metricAnalysis.Metrics;

import java.util.Arrays;
import java.util.Set;
import com.github.javaparser.ast.CompilationUnit;
import astManager.PackageAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.CCEMetricVisitor;

public class CCEMetric extends Metric {
	
	public CCEMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.PACKAGE);
	}
	
	
	@Override
	public void run(PackageAST packAst) {
		
		if(this.alreadyDone(packAst)) return;
		
		Integer[] value = new Integer[2];
		value[0] = 0;
		value[1] = 0;
		
		CCEMetricVisitor visitor = new CCEMetricVisitor();
		
		Set<CompilationUnit> cus = packAst.getCus();
		
		for(CompilationUnit cu : cus) {
			cu.accept(visitor, value);
		}
		
		double cce = (double) value[1] / (double) value[0];
		
		MetricResult result = new MetricResult( identifier, cce);
		packAst.getResults().put(identifier, result);
	}

}
