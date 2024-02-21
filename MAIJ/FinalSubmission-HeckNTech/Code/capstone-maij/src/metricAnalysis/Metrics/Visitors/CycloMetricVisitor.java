package metricAnalysis.Metrics.Visitors;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.Range;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**Visitor used to walk over the AST node and calculate the Cyclometric Complexity metric.
 * @author Jacob Botha
 *
 */
public class CycloMetricVisitor extends VoidVisitorAdapter<Integer[]>{
	/*
	 * Relatively simple metric to implement - override the visit methods for each relevant statment
	 * and increment the calculated value.
	 */
	List<Range> poi;
	public CycloMetricVisitor(ArrayList<Range> poi) {
		this.poi = poi;
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.expr.BinaryExpr, java.lang.Object)
	 */
	@Override
	public void visit(BinaryExpr n, Integer[] arg) {
		BinaryExpr.Operator op = n.getOperator();
		if(op.equals(BinaryExpr.Operator.AND) || op.equals(BinaryExpr.Operator.OR)) {
			arg[0] = arg[0] + 1;
			poi.add(n.getRange().get());
		}
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.BreakStmt, java.lang.Object)
	 */
	@Override
	public void visit(BreakStmt n, Integer[] arg) {
		arg[0] = arg[0] + 1;
		poi.add(n.getRange().get());
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.CatchClause, java.lang.Object)
	 */
	@Override
	public void visit(CatchClause n, Integer[] arg) {
		arg[0] = arg[0] + 1;
		poi.add(n.getRange().get());
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.expr.ConditionalExpr, java.lang.Object)
	 */
	@Override
	public void visit(ConditionalExpr n, Integer[] arg) {
		arg[0] = arg[0] + 1;
		poi.add(n.getRange().get());
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ContinueStmt, java.lang.Object)
	 */
	@Override
	public void visit(ContinueStmt n, Integer[] arg) {
		arg[0] = arg[0] + 1;
		poi.add(n.getRange().get());
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.DoStmt, java.lang.Object)
	 */
	@Override
	public void visit(DoStmt n, Integer[] arg) {
		arg[0] = arg[0] + 1;
		poi.add(n.getRange().get());
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ForeachStmt, java.lang.Object)
	 */
	@Override
	public void visit(ForeachStmt n, Integer[] arg) {
		arg[0] = arg[0] + 1;
		poi.add(n.getRange().get());
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ForStmt, java.lang.Object)
	 */
	@Override
	public void visit(ForStmt n, Integer[] arg) {
		arg[0] = arg[0] + 1;
		poi.add(n.getRange().get());
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.IfStmt, java.lang.Object)
	 */
	@Override
	public void visit(IfStmt n, Integer[] arg) {
		arg[0] = arg[0] + 1;
		poi.add(n.getRange().get());
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.SwitchEntryStmt, java.lang.Object)
	 */
	@Override
	public void visit(SwitchEntryStmt n, Integer[] arg) {
		if(n.getLabel().isPresent()) { //the default case has no label
			arg[0] = arg[0] + 1;
			poi.add(n.getRange().get());
		}
		super.visit(n, arg);
		
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ThrowStmt, java.lang.Object)
	 */
	@Override
	public void visit(ThrowStmt n, Integer[] arg) {
		arg[0] = arg[0] + 1;
		poi.add(n.getRange().get());
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.WhileStmt, java.lang.Object)
	 */
	@Override
	public void visit(WhileStmt n, Integer[] arg) {
		arg[0] = arg[0] + 1;
		poi.add(n.getRange().get());
		super.visit(n, arg);
	}
	
}