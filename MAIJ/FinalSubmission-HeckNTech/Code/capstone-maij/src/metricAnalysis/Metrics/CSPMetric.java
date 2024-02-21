package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import com.github.javaparser.ast.CompilationUnit;
import astManager.PackageAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.CSPMetricVisitor;

public class CSPMetric extends Metric {

	private ArrayList<String> undefine = new ArrayList<String>();
	private ArrayList<String> superclass = new ArrayList<String>();
	private ArrayList<String> subclass = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> classes = new HashMap<>();
	
	
	public CSPMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.PACKAGE);
	}
	
	public double CCVal() {
		
		for (int i = 0; i < undefine.size(); i++) {
			if (!(superclass.contains(undefine.get(i)))) {
				undefine.remove(i);
				i--;
			}
		}
		
		return (double) (undefine.size() + subclass.size());

	}
	
	public double CSCVal() {
		
		return (double) undefine.size();
	}

	@Override
	public void run(PackageAST packAst) {
		
		if(this.alreadyDone(packAst)) return;
		
		double csp = 0.0;
		double cc = 0.0;
		double csc = 0.0;
		
		undefine.removeAll(undefine);
		superclass.removeAll(superclass);
		subclass.removeAll(subclass);
		
		System.out.println(undefine);
		System.out.println(superclass);
		System.out.println(subclass);
		
		classes.put("undefine", undefine);
		classes.put("superclass", superclass);
		classes.put("subclass", subclass);
		
		CSPMetricVisitor visitor = new CSPMetricVisitor();
		
		Set<CompilationUnit> cus = packAst.getCus();
		
		for(CompilationUnit cu : cus) {
			cu.accept(visitor, classes);
		}
		
		undefine = new ArrayList<String>(classes.get("undefine"));
		superclass = new ArrayList<String>(classes.get("superclass"));
		subclass = new ArrayList<String>(classes.get("subclass"));
		
		if (superclass.size() > 0) {
			cc = CCVal();
			csc = CSCVal();
		}
		
		csp = csc / cc;

		MetricResult result = new MetricResult(identifier, csp);
		packAst.getResults().put(identifier, result);
		
	}

}
