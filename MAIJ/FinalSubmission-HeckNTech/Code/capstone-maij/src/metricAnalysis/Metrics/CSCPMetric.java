/*
 * Author: Sam
 * Critical Serialised Classes Proportion (CSCP)
 */
package metricAnalysis.Metrics;

import java.util.Arrays;
import java.util.Set;
import com.github.javaparser.ast.CompilationUnit;
import astManager.PackageAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.CSCPMetricVisitor;

public class CSCPMetric extends Metric {
	
	public CSCPMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.PACKAGE);
	}
	
	@Override
	public void run(PackageAST packAst) {
		
		if(this.alreadyDone(packAst)) return;
		
		Integer[] value = new Integer[2];
		value[0] = 0;
		value[1] = 1;
		
		CSCPMetricVisitor visitor = new CSCPMetricVisitor();
	
		Set<CompilationUnit> cus = packAst.getCus();
		
		for(CompilationUnit cu : cus) {
			cu.accept(visitor, value);
		}
		
		double cscp = (double) value[1] / (double) value[0];
		
		MetricResult result = new MetricResult( identifier, cscp);
		packAst.getResults().put(identifier, result);
		
	}	
}
