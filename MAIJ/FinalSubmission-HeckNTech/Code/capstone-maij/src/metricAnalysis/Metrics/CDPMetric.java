/*
 * Critical Design Proportion (CDP)
 * The ratio of number of critical classes to the total number
	of classes in a design
 */

package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import astManager.PackageAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.CDPMetricVisitor;

public class CDPMetric extends Metric{
	
	public CDPMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.PACKAGE);	
	}
	
	@Override
	public void run(PackageAST packAst) {
		
		if(this.alreadyDone(packAst)) return;
		
		Integer[] value = new Integer[2];
		value[0] = 0;
		value[1] = 0;
		ArrayList<Range> poi = new ArrayList<Range>();
		
		Set<CompilationUnit> cus = packAst.getCus();
		
		CDPMetricVisitor visitor = new CDPMetricVisitor(poi);
		for(CompilationUnit cu : cus) {
			cu.accept(visitor, value);
		}
		
		
		double classes = value[0];			//total number of classes
		double critical = value[1];			//number of critical classes
		double cdp = critical / classes;
		
		MetricResult result = new MetricResult( identifier, cdp);
		packAst.getResults().put(identifier, result);
		
	}
}
