package metricAnalysis.Metrics.Visitors;

import java.util.ArrayList;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CWMPMetricVisitor extends VoidVisitorAdapter<Integer[]> {
	
	ArrayList<String> list;
	
	public CWMPMetricVisitor(ArrayList<String> ca) {
		list = new ArrayList<String>(ca);
	}
	
	@Override
	public void visit(MethodCallExpr n, Integer[] arg) {
		super.visit(n, arg);
		
		if (n.getScope().toString().contains("System.out")) {
			for (int i = 0; i < list.size(); i++) {
				if (n.toString().contains(list.get(i))) {
					arg[0]++;
					break;
				}
			}
		}		
	}

}
