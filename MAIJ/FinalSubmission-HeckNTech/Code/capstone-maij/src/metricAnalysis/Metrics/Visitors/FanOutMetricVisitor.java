package metricAnalysis.Metrics.Visitors;

import java.util.List;
import java.util.Set;

import com.github.javaparser.Range;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import astManager.BaseAST;
import astManager.ClassAST;

/**Visitor Class to calculate Fan Out Metric Value
 * Doesn't count field access or method calls, only places where the parser puts a ClassOrInterfaceType.
 * @author Jacob Botha
 *
 */
public class FanOutMetricVisitor extends VoidVisitorAdapter<Set<String>>{

	private ClassAST callingAst;
	private JavaParserFacade facade;
	private List<Range> poi;

	public FanOutMetricVisitor(ClassAST clasAst, List<Range> poi) {
		super();
		this.callingAst = clasAst;
		this.facade = BaseAST.getContainingProjectAST(callingAst).getSymbolSolverFacade();
		this.poi = poi;
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.type.ClassOrInterfaceType, java.lang.Object)
	 */
	@Override
	public void visit(ClassOrInterfaceType n, Set<String> arg) {
		String name = n.getNameAsString();
		if(!name.equals(callingAst.getIdentifier()) && !arg.contains(name)){
			arg.add(name);
			poi.add(n.getRange().get());
		}
		
		n.getName().accept(this, arg);
        //n.getScope().ifPresent(l -> l.accept(this, arg)); //don't want to travel up the scope into the package name.
        n.getTypeArguments().ifPresent(l -> l.forEach(v -> v.accept(this, arg)));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
	}
}