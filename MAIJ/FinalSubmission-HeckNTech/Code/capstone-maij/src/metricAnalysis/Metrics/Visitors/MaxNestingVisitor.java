package metricAnalysis.Metrics.Visitors;

import java.util.List;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**Visitor that calculate the value for Max Nesting Metric.
 * @author Jacob Botha
 *
 */
public class MaxNestingVisitor extends VoidVisitorAdapter<Integer[]>{
	private List<Range> poi;
	public MaxNestingVisitor(List<Range> poi) {
		this.poi = poi;
	}

	/**Check current depth and updates max depth and point of interest if necessary.
	 * @param arg the array with current depth [1] and max depth[0]
	 * @param n the current node
	 */
	private void checkDepth(Integer[] arg, Node n) {
		if(arg[1] > arg[0]) { //record the max depth
			arg[0] = arg[1];
			this.poi.removeAll(this.poi);
		}
		if(arg[1] == arg[0]) { //want to have all points which are at max depth
			this.poi.add(n.getRange().get());
		}
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.expr.ConditionalExpr, java.lang.Object)
	 */
	@Override
	public void visit(ConditionalExpr n, Integer[] arg) {
		arg[1] +=  1; //increment as you move into the block
		checkDepth(arg, n);	
		super.visit(n, arg);
		arg[1] -=  1;  //decrement as you move back out of the block
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.DoStmt, java.lang.Object)
	 */
	@Override
	public void visit(DoStmt n, Integer[] arg) {
		arg[1] +=  1; 
		checkDepth(arg, n);	
		super.visit(n, arg);
		arg[1] -=  1; 
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ForeachStmt, java.lang.Object)
	 */
	@Override
	public void visit(ForeachStmt n, Integer[] arg) {
		arg[1] +=  1; 
		checkDepth(arg, n);	
		super.visit(n, arg);
		arg[1] -=  1; 
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ForStmt, java.lang.Object)
	 */
	@Override
	public void visit(ForStmt n, Integer[] arg) {
		arg[1] +=  1; 
		checkDepth(arg, n);	
		super.visit(n, arg);
		arg[1] -=  1; 
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.IfStmt, java.lang.Object)
	 */
	@Override
	public void visit(IfStmt n, Integer[] arg) {
		arg[1] +=  1; 
		checkDepth(arg, n);	
		super.visit(n, arg);
		arg[1] -=  1; 
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.SwitchStmt, java.lang.Object)
	 */
	@Override
	public void visit(SwitchStmt n, Integer[] arg) {
		arg[1] +=  1; 
		checkDepth(arg, n);	
		super.visit(n, arg);
		arg[1] -=  1; 
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.WhileStmt, java.lang.Object)
	 */
	@Override
	public void visit(WhileStmt n, Integer[] arg) {
		arg[1] +=  1; 
		checkDepth(arg, n);	
		super.visit(n, arg);
		arg[1] -=  1; 
	}
	
}