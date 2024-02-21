package metricAnalysis.Metrics.Visitors;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CCCMetricVisitor extends VoidVisitorAdapter<HashMap<String, ArrayList<String>>> {
	ArrayList<String> list;
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, HashMap<String, ArrayList<String>> arg) {
		super.visit(n, arg);
		
		list = new ArrayList<String>(arg.get("superclass"));
		System.out.println("The order they were collected : " + n.getNameAsString());
		list.add(n.getNameAsString());
		System.out.println("This is the annotation: " + n.getAnnotationByName("critical").isPresent());
		arg.put("superclass", list);
		
		//find whether it has a critical annotation, after doing so, put the class into an array to use later. b
		
//		if (n.getExtendedTypes().isNonEmpty()) {
//			list = new ArrayList<String>(arg.get("subclass"));
//
//			list.add(n.getNameAsString());
//			arg.put("subclass", list);
//			System.out.println("this is the extended type: " + n.getExtendedTypes().toString());
//			String s = n.getExtendedTypes().toString();
//			s = s.substring(1, s.length()-1);
//			list = new ArrayList<String>(arg.get("superclass"));
//			
//			if (!(list.contains(s))) {
//				list.add(s);
//				arg.put("superclass", list);
//			}
//		}
	}
}
