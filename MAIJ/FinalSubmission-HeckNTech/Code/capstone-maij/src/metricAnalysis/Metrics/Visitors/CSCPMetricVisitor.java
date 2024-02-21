/*
 * Author: Sam
 */
package metricAnalysis.Metrics.Visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CSCPMetricVisitor extends VoidVisitorAdapter<Integer[]>{

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Integer[] arg) {
		super.visit(n, arg);
		
		if (n.isAnnotationPresent(critical.class)) {
			arg[0]++;
			if (n.getImplementedTypes().toString().contains("Serializable")) {
				arg[1]++;
			}
		}
	}
}
