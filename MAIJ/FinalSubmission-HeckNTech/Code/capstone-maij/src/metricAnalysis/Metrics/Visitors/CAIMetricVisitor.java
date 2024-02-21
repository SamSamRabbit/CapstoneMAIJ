/*
 * Author: Sam
 */

package metricAnalysis.Metrics.Visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class CAIMetricVisitor extends VoidVisitorAdapter<HashMap<String, ArrayList<String>>> {

	ArrayList<String> list;
	
	public CAIMetricVisitor() {
		
	}
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, HashMap<String, ArrayList<String>> arg) {
		super.visit(n, arg);
		
		if (n.getExtendedTypes().isNonEmpty()) {
			list = new ArrayList<String>(arg.get("subclass"));
			list.add(n.getNameAsString());
			arg.put("subclass", list);
			
			String s = n.getExtendedTypes().toString();
			s = s.substring(1, s.length()-1);
			list = new ArrayList<String>(arg.get("superclass"));
			
			if (!(list.contains(s))) {
				list.add(s);
				arg.put("superclass", list);
			}
		}
	}
}
