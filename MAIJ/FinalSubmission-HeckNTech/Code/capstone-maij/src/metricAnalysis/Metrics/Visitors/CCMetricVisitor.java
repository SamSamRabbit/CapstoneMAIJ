package metricAnalysis.Metrics.Visitors;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CCMetricVisitor extends VoidVisitorAdapter<HashMap<String, ArrayList<String>>> {
	ArrayList<String> list;
	
	public void visit(ClassOrInterfaceDeclaration n, HashMap<String, ArrayList<String>> arg) {
		super.visit(n, arg);
		
		System.out.println("get name: " + n.getNameAsString());
		
//		n.get
//		
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
