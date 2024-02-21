package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import astManager.ClassAST;
import astManager.PackageAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.CAIMetricVisitor;
import metricAnalysis.Metrics.Visitors.CCCMetricVisitor;
import metricAnalysis.Metrics.Visitors.CCMetricVisitor;

public class CCCMetric extends Metric{

	private ArrayList<String> superclass = new ArrayList<String>();
	private ArrayList<String> subclass = new ArrayList<String>();
	private ArrayList<String> sizeclass = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> classes = new HashMap<>();
	double cai = 0.0;
	
	public CCCMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.PACKAGE);	
	}
	
	/*
	 *	the number of classified attributes 
	 *	which can be inherited in a hierarchy 
	 */
	private double CAVal(PackageAST packast) {
		double ca = 0;
		SortedMap<String, ClassAST> classes = packast.getClasses();
		
		
		//hash map to get all classes?
		for (int i = 0; i < superclass.size(); i++) {
			System.out.println("we are at this now: " + i);
			ClassAST ast = classes.get(superclass.get(i));
			for(MethodDeclaration n : ast.getNode().getMethods()) {
				for(FieldDeclaration x : ast.getNode().getFields()) {
					System.out.println(n.getBody().get().getStatement(0).getChildNodes().get(0).toString()); 
					System.out.println("the variable Type: " + x.getVariable(0).getTypeAsString()); 
					System.out.println("the range: " + n.getRange());
						if(x.isAnnotationPresent("secrecy") && n.toString().contains(x.getVariable(0).getNameAsString())) {
							System.out.println("entered");
						}
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
				if (n.isAnnotationPresent("secrecy")) {
					ai++;
				}
			}
		}
		
		return ai;
	}
	
	@Override
	public void run(PackageAST packAst) {
		
		//ONE COMPLETE MULTICLASS METRIC
		
		if(this.alreadyDone(packAst)) return;
		
		//clear all 3 arrayLists and HashMaps
		classes.clear();
		superclass.clear();
		subclass.clear();
		
		System.out.println("classes size: " + classes.size());
		
		classes.put("superclass", superclass);
		classes.put("subclass", subclass);
		
		CCCMetricVisitor visitor = new CCCMetricVisitor();
		CCMetricVisitor cvisitor = new CCMetricVisitor();
		
		Set<CompilationUnit> cus = packAst.getCus();
		sizeclass = new ArrayList<String>();
		for(CompilationUnit cu : cus) {
			cu.accept(visitor, classes);
		}
		superclass = new ArrayList<String>(classes.get("superclass"));
		subclass = new ArrayList<String>(classes.get("subclass"));
		//now superclass contains all the classes (can rename the variable later, bad practice :/ 
		System.out.println("here " + superclass.size());
		double ca = CAVal(packAst);
		double ai = AIVal(packAst);
		
		cai = ai / ca;
		
		
		
		MetricResult result = new MetricResult(identifier, cai);
		packAst.getResults().put(identifier, result);
	}
}
