/*
 * Author: Sam
 * Classified Attributes Inheritance (CAI)
 * The ratio of the number of classified attributes which can be inherited in a hierarchy 
 * to the total number of classified attributes in that hierarchy
 */
package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import astManager.ClassAST;
import astManager.PackageAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.CAIMetricVisitor;

public class CAIMetric extends Metric {

	private ArrayList<String> superclass = new ArrayList<String>();
	private ArrayList<String> subclass = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> classes = new HashMap<>();
	double cai = 0.0;
	
	public CAIMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.PACKAGE);	
	}
	
	/*
	 *	the number of classified attributes 
	 *	which can be inherited in a hierarchy 
	 */
	private double CAVal(PackageAST packast) {
		
		for (int i = 0; i < subclass.size(); i++) {
			
			if (superclass.contains(subclass.get(i))) {
				subclass.remove(i);
				i--;
			}
		}
		
		double ca = 0.0;
		SortedMap<String, ClassAST> classes = packast.getClasses();
		
		for (int i = 0; i < subclass.size(); i++) {
			ClassAST ast = classes.get(subclass.get(i));
			for (FieldDeclaration n : ast.getNode().getFields()) {
				if (n.isAnnotationPresent(secrecy.class)) {
					ca++;
				}
			}
		}
		
		for (int i = 0; i < superclass.size(); i++) {
			ClassAST ast = classes.get(superclass.get(i));
			for (FieldDeclaration n : ast.getNode().getFields()) {
				if (n.isAnnotationPresent(secrecy.class)) {
					ca++;
				}
			}
		}
			
		return ca;
	}
	
	/*
	 *	the total number of classified attributes 
	 *	in that hierarchy
	 */
	private double AIVal(PackageAST packast) {
		
		double ai = 0.0;
		SortedMap<String, ClassAST> classes = packast.getClasses();
		
		for (int i = 0; i < superclass.size(); i++) {
			ClassAST ast = classes.get(superclass.get(i));
			for (FieldDeclaration n : ast.getNode().getFields()) {
				if (n.isAnnotationPresent(secrecy.class)) {
					ai++;
				}
			}
		}
		
		return ai;
	}
	
	@Override
	public void run(PackageAST packAst) {
		
		if(this.alreadyDone(packAst)) return;
		
		classes.put("superclass", superclass);
		classes.put("subclass", subclass);
		
		CAIMetricVisitor visitor = new CAIMetricVisitor();
		
		Set<CompilationUnit> cus = packAst.getCus();
		
		for(CompilationUnit cu : cus) {
			cu.accept(visitor, classes);
		}
		
		superclass = new ArrayList<String>(classes.get("superclass"));
		subclass = new ArrayList<String>(classes.get("subclass"));
		
		double ca = CAVal(packAst);
		double ai = AIVal(packAst);
		
		cai = ai / ca;
		
		MetricResult result = new MetricResult( identifier, cai);
		packAst.getResults().put(identifier, result);
	}
}
