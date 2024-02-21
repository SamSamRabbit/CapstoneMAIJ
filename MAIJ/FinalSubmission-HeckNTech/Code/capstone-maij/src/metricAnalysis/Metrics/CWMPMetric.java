package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.SortedMap;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import astManager.ClassAST;
import astManager.PackageAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.CWMPMetricVisitor;

public class CWMPMetric extends Metric {
	
	public CWMPMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.PACKAGE);
	}
	
	
	//count classified methods
	private double CMVal(PackageAST packast) {
	
		Double cm = 0.0;
		Double cwm = 0.0;
		
		SortedMap<String, ClassAST> classes = packast.getClasses();
		ArrayList<String> ca = new ArrayList<String>();
		
		for (Entry<String, ClassAST> entry : classes.entrySet()) {
			ClassAST ast = entry.getValue();
			ca.clear();
			
			for (FieldDeclaration f : ast.getNode().getFields()) {
				if (f.isAnnotationPresent(secrecy.class)) {
					ca.add(f.getVariable(0).toString());
				}
			}
			
			if (ca.size() > 0) {
				for (MethodDeclaration m : ast.getNode().getMethods()) {
					for (int i = 0; i < ca.size(); i++) {
						if (m.toString().contains(ca.get(i))) {
							cm++;
							CWMPMetricVisitor visitor = new CWMPMetricVisitor(ca);
							Integer[] t = new Integer[1];
							t[0] = 0;
							m.accept(visitor, t);
							if (t[0] > 0) {
								cwm++;
							}
							break;
						}
					}
				}
			}
		}
	
		return cwm/cm;
	}
	
	@Override
	public void run(PackageAST packAst) {
		
		if(this.alreadyDone(packAst)) return;		
		
		
		MetricResult result = new MetricResult(identifier, CMVal(packAst));
		packAst.getResults().put(identifier, result);
	}

}
