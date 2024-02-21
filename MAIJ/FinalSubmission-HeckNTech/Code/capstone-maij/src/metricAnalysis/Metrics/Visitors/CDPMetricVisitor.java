package metricAnalysis.Metrics.Visitors;

import java.util.List;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CDPMetricVisitor extends VoidVisitorAdapter<Integer[]> {

	private List<Range> poi;
	
	public CDPMetricVisitor(List<Range> poi) {
		this.poi = poi;
	}
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		if(n.isAnnotationPresent(critical.class)) {
			arg[1] += 1;
		}
		this.poi.add(n.getRange().get());
	}
}
