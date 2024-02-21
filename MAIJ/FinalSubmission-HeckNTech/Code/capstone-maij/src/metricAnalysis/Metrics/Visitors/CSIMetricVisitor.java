package metricAnalysis.Metrics.Visitors;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CSIMetricVisitor extends VoidVisitorAdapter<HashMap<String, ArrayList<String>>> {

	ArrayList<String> list;
	ArrayList<String> superList;
	ArrayList<String> inheritList;
	
	public CSIMetricVisitor() {
		
	}
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, HashMap<String, ArrayList<String>> arg) {
		super.visit(n, arg);
		//superclass, criticalClass, noOfInherit
		//first, find the amount of classes that inherit the super 
		if(n.getExtendedTypes() != null) {
			//we can get the super classes and the inheritable ones within here.
			inheritList = new ArrayList<String>(arg.get("noOfInherit"));
			//add all the class that inherit off A super.
			if(!(inheritList.contains(n.getNameAsString()))) {
				inheritList.add(n.getNameAsString());
				arg.put("noOfInherit", inheritList);
			}
			superList = new ArrayList<String>(arg.get("superclass"));
			String s = n.getExtendedTypes().toString();
			s = s.substring(1, s.length()-1);
			if(!(superList.contains(s))){
				superList.add(s);
				arg.put("superclass", superList);
				System.out.println("this was entered");
			}
		}
		if(n.isAnnotationPresent("critical")) {
			list = new ArrayList<String>(arg.get("criticalClass"));
			list.add(n.getNameAsString());
			arg.put("criticalClass", list);
		}
//		list = new ArrayList<String>(arg.get("subclass"));
//		System.out.println("accessed " + n.getNameAsString());
//		list.add(n.getNameAsString());
//		arg.put("subclass", list);
		
	}
}
