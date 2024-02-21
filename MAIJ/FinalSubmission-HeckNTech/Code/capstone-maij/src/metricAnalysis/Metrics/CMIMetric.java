package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import astManager.ClassAST;
import astManager.PackageAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.CAIMetricVisitor;
import metricAnalysis.Metrics.Visitors.CMIMetricVisitor;

public class CMIMetric extends Metric {
	private ArrayList<String> superclass = new ArrayList<String>();
	private ArrayList<String> subclass = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> classes = new HashMap<>();
	double cai = 0.0;
	
	public CMIMetric() {
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
		//change this for classified methods
		
		//first, we search to check if method is classified
		//super class check for a classified attribute.. 
		//then check to see if superclass CA's within subclass?
		//do once for superclass
//		if(superclass.size() > 0) {
//			for(int i = 0; i < superclass.size() - 1; i++) {
//				ClassAST ast = classes.get(superclass.get(i));
//				for(MethodDeclaration n : ast.getNode().getMethods()) {
//					for(FieldDeclaration x : ast.getNode().getFields()) {
//						if(x.isAnnotationPresent("secrecy")) {
//							if(n.toString().contains(x.getVariable(i).toString())) {
//								ca++;
//							}
//						}
//					}
//				}
//			}
//		}
//		// again for subclass
//		if((subclass.size() > 0)) {
//			for(int i = 0; i < subclass.size() -1 ; i++) {
//				ClassAST ast = classes.get(subclass.get(i));
//				for(MethodDeclaration n : ast.getNode().getMethods()) {
//					for(FieldDeclaration x : ast.getNode().getFields()) {
//						if(x.isAnnotationPresent("secrecy")) {
//							if(n.toString().contains(x.getVariable(i).toString())) {
//								ca++;
//							}
//						}
//					}
//				}
//			}
		System.out.println("The superclass size is: " + superclass.size());
		System.out.println("The superclass contents is: " + superclass);
		for(int i = 0; i < superclass.size() - 1;i++) {
			System.out.println("current for loop counter is: " + i);
			ClassAST superAST = classes.get(superclass.get(i));
			for(MethodDeclaration n : superAST.getNode().getMethods()) {
				for(FieldDeclaration x: superAST.getNode().getFields()) {
					System.out.println("The variable name is: " + x.getVariable(i).getNameAsString());
					if(x.isAnnotationPresent("secrecy") && n.toString().contains(x.getVariable(i).getNameAsString())) {
						ca++;
					}
				}
			}
		}
		System.out.println("this is the subclass: " + superclass);
		System.out.println("The result is: " + ca);
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
			for (MethodDeclaration n : ast.getNode().getMethods()) {
				if (n.isAnnotationPresent("secrecy")) {
					ai++;
					System.out.println("val" + ai);
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
		
		
		classes.put("superclass", superclass);
		classes.put("subclass", subclass);
		
		
		CMIMetricVisitor visitor = new CMIMetricVisitor();
		
		Set<CompilationUnit> cus = packAst.getCus();
		
		for(CompilationUnit cu : cus) {
			cu.accept(visitor, classes);
		}
		System.out.println(classes.size());
		
		superclass = new ArrayList<String>(classes.get("superclass"));
		subclass = new ArrayList<String>(classes.get("subclass"));
	
		double ca = CAVal(packAst);
		double ai = AIVal(packAst);
		
		cai = ai / ca;
		
		
		
		MetricResult result = new MetricResult(identifier, cai);
		packAst.getResults().put(identifier, result);

	}
}
