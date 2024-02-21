package metricAnalysis.Metrics.Visitors;

import java.util.List;

import com.github.javaparser.Range;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsStmt;
import com.github.javaparser.ast.modules.ModuleOpensStmt;
import com.github.javaparser.ast.modules.ModuleProvidesStmt;
import com.github.javaparser.ast.modules.ModuleRequiresStmt;
import com.github.javaparser.ast.modules.ModuleUsesStmt;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**Visitor to count the number of statments in a node
 * Does not include comments.
 * @author Jacob Botha
 *
 */
public class NOSMetricVisitor extends VoidVisitorAdapter<Integer[]>{
	private List<Range> poi;

	public NOSMetricVisitor(List<Range> poi) {
		this.poi = poi;
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.body.AnnotationDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(AnnotationDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.AssertStmt, java.lang.Object)
	 */
	@Override
	public void visit(AssertStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.BreakStmt, java.lang.Object)
	 */
	@Override
	public void visit(BreakStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(ClassOrInterfaceDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.body.ConstructorDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(ConstructorDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ContinueStmt, java.lang.Object)
	 */
	@Override
	public void visit(ContinueStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.DoStmt, java.lang.Object)
	 */
	@Override
	public void visit(DoStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.EmptyStmt, java.lang.Object)
	 */
	@Override
	public void visit(EmptyStmt n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.body.EnumConstantDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(EnumConstantDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.body.EnumDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(EnumDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt, java.lang.Object)
	 */
	@Override
	public void visit(ExplicitConstructorInvocationStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ExpressionStmt, java.lang.Object)
	 */
	@Override
	public void visit(ExpressionStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}
	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.body.FieldDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(FieldDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ForeachStmt, java.lang.Object)
	 */
	@Override
	public void visit(ForeachStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ForStmt, java.lang.Object)
	 */
	@Override
	public void visit(ForStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.IfStmt, java.lang.Object)
	 */
	@Override
	public void visit(IfStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.ImportDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(ImportDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.body.InitializerDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(InitializerDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.LocalClassDeclarationStmt, java.lang.Object)
	 */
	@Override
	public void visit(LocalClassDeclarationStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.body.MethodDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(MethodDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.modules.ModuleDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(ModuleDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.modules.ModuleExportsStmt, java.lang.Object)
	 */
	@Override
	public void visit(ModuleExportsStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.modules.ModuleOpensStmt, java.lang.Object)
	 */
	@Override
	public void visit(ModuleOpensStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.modules.ModuleProvidesStmt, java.lang.Object)
	 */
	@Override
	public void visit(ModuleProvidesStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.modules.ModuleRequiresStmt, java.lang.Object)
	 */
	@Override
	public void visit(ModuleRequiresStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.modules.ModuleUsesStmt, java.lang.Object)
	 */
	@Override
	public void visit(ModuleUsesStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.PackageDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(PackageDeclaration n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ReturnStmt, java.lang.Object)
	 */
	@Override
	public void visit(ReturnStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.SwitchEntryStmt, java.lang.Object)
	 */
	@Override
	public void visit(SwitchEntryStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.SwitchStmt, java.lang.Object)
	 */
	@Override
	public void visit(SwitchStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.SynchronizedStmt, java.lang.Object)
	 */
	@Override
	public void visit(SynchronizedStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.ThrowStmt, java.lang.Object)
	 */
	@Override
	public void visit(ThrowStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.TryStmt, java.lang.Object)
	 */
	@Override
	public void visit(TryStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.UnparsableStmt, java.lang.Object)
	 */
	@Override
	public void visit(UnparsableStmt n, Integer[] arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.stmt.WhileStmt, java.lang.Object)
	 */
	@Override
	public void visit(WhileStmt n, Integer[] arg) {
		super.visit(n, arg);
		arg[0] += 1;
		this.poi.add(n.getRange().get());
	}
	
	
}