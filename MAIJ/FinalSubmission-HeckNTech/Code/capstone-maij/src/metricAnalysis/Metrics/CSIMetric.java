package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import astManager.ClassAST;
import astManager.PackageAST;
import metricAnalysis.MetricResult;
import metricAnalysis.Metrics.Visitors.CSIMetricVisitor;

/*
 * 
 * Author: Jesse Studin
 * Metric: CSIMetric (accompanying CSIVisitorMetric
 * Subject: Capstone
 */
public class CSIMetric extends Metric {
	private ArrayList<String> superclass = new ArrayList<String>();
	private ArrayList<String> criticalClass = new ArrayList<String>();
	private ArrayList<String> noOfInherit = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> classes = new HashMap<>();
	double cai = 0.0;
	
	public CSIMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.PACKAGE);	
	}
	
	/*
	 *	the number of classified attributes 
	 *	which can be inherited in a hierarchy 
	 */
	
	private double CCVal(PackageAST packast) {
		//find the CC Value.
		//create an array with the current criticalclasses that could be a superclass.
		SortedMap<String, ClassAST> classes = packast.getClasses();

		for(int i = 0; i < superclass.size(); i++) {	
			if(superclass.contains(criticalClass.get(i))) {
				criticalClass.remove(i);
				i--;
			}
		} //that should leave criticalClass with the potential inheritable critical classes.
		Map<String, ArrayList<String>> allTypes = new HashMap<>();
		
		for(int i = 0; i < criticalClass.size(); i++) {
			ClassAST ast = classes.get(criticalClass.get(i));
			for(MethodDeclaration n : ast.getNode().getMethods()) {
				for(FieldDeclaration x : ast.getNode().getFields()) {
					//we want to push all data types to the data types list, and quickly put it the hashmap.
					ArrayList<String> dataTypes = new ArrayList<String>();
					dataTypes.add(x.getVariable(0).getTypeAsString());
					allTypes.put(criticalClass.get(i), dataTypes);
				}
			}
		}//we should now have all data types with their corresponding classes.
		//now we just need to find the extra superclasses ontop of the already superclass
		//a straight iterative loop through each value, this would be more efficient via a search algorithm, 
		//create an array to hold the newly found datatypes within each class/method
		for(String className : allTypes.keySet()) {
			ArrayList<String> dataType = allTypes.get(className);
			for(int i = 0; i < dataType.size(); i++) {
				//checks if the datatype is used within the current critical classes.
				if(criticalClass.contains(dataType.get(i))) {
					//then checks if superclass does not have the key (the original class)
					if(!(superclass.contains(className))) {
						//adds it to the super class list.
						superclass.add(className);
					}
				}
			}
			
		}
		//here we add on the extra superclass found
		double ccVal = superclass.size() - 1;
		return ccVal;
	}
	
	@Override
	public void run(PackageAST packAst) {
		
		//CSI Metric is Complete
		
		if(this.alreadyDone(packAst)) return;
		
		//clear all 3 arrayLists and HashMaps
		classes.clear();
		superclass.clear();
		criticalClass.clear();
		noOfInherit.clear();
		
		classes.put("superclass", superclass);
		classes.put("criticalClass", criticalClass);
		classes.put("noOfInherit", noOfInherit);
		
		
		CSIMetricVisitor visitor = new CSIMetricVisitor();
		
		Set<CompilationUnit> cus = packAst.getCus();
		
		for(CompilationUnit cu : cus) {
			cu.accept(visitor, classes);
		}
		System.out.println(classes.size());
		
		//get the values onto usable arrayLists
		superclass = new ArrayList<String>(classes.get("superclass"));
		criticalClass = new ArrayList<String>(classes.get("criticalClass"));
		noOfInherit = new ArrayList<String>(classes.get("noOfInherit"));
		
		//number of classes which may inherit from critical superclass.
		//
		
		double ca = criticalClass.size() - 1;
		double cc = CCVal(packAst);
		double noI = criticalClass.size(); //I use critical class twice, the second one is all the possible inheritable critical classes.
		//System.out.println("work please: " + ca * cc);
		double demoninator = ca * cc;
		cai = noI / demoninator;

		MetricResult result = new MetricResult(identifier, cai);
		packAst.getResults().put(identifier, result);

	}
}
