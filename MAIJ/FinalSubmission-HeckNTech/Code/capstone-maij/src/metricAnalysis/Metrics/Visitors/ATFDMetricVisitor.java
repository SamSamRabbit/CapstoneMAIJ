package metricAnalysis.Metrics.Visitors;

import java.util.List;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

import astManager.BaseAST;
import astManager.ClassAST;

/**Visitor class used to walk over AST node and compute the metric value for Access To Foreign Data
 * @author Jacob Botha
 *
 */
public class ATFDMetricVisitor extends VoidVisitorAdapter<Integer[]>{
	/*
	 * We need to override visit methods for all the node types that we need to check.
	 * Ok, so anything in the local scope is not foreign, and anything declared in the enclosing
	 * class is not foreign.
	 * According to the JavaParser AST the only way to access foreign data is through a field access expression,
	 * method call expression or a method reference expression, so we need to override the visit method for these nodes.
	 */
	private ClassAST callingAst;
	private JavaParserFacade facade;
	private List<Range> poi;
	
	/**Constructor - sets the enclosing ClassAST for use in determining if something was declared locally.
	 * @param poi List to store ranges of nodes that affect the metric value
	 * @param classNode the ClassAST the metric is applied to if it is applied at the 
	 * class level or the enclosing ClassAST if the metric is applied at the method level.
	 */
	public ATFDMetricVisitor(ClassAST ast, List<Range> poi) {
		super();
		this.callingAst = ast;
		this.facade = BaseAST.getContainingProjectAST(callingAst).getSymbolSolverFacade();
		this.poi = poi;
	}
	
	/**Function to check whether a simple name is foreign - whether it was declared in the enclosing class or not.
	 * @param scope 
	 * @return
	 */
	private boolean handleScopeIsNameExpr(NameExpr scope) {
		try {
			/* 
			 * the facade.solve method resolves where the given node was declared, if there
			 * was no declaration such as when it is the name of a class like when you call a static method
			 * or field then it will be an empty optional.
			 */
			SymbolReference<? extends ResolvedValueDeclaration> resolved = this.facade.solve(scope);
			
			/*
			 * if it is solved then it was declared somewhere then we want to identify the class
			 * where it was declared and compare it to the class of 
			 * the BaseAST the metric is currently evaluating
			 */
			if(resolved.isSolved()) {
				ResolvedValueDeclaration h = resolved.getCorrespondingDeclaration();
				if(h.isEnumConstant()) {
					ResolvedType x = h.asEnumConstant().getType();
					return isForeignType(x.describe());
				}else if(h.isField()) {
					return isForeignType(h.asField().declaringType().getQualifiedName());
				}else if(h.isMethod()) {
					return isForeignType(h.asField().declaringType().getQualifiedName());
				}else if(h.isParameter()) {//if it was declared as a parameter it has to be local
					return false;
				}else if(h.isType()) {
					return isForeignType(h.asType().getQualifiedName());
				}else if(h.isVariable()) {//if it was declared as a variable it has to be local
					return false;
				}
			}else{ //if it wasn't declared somewhere, it must refer to a type - I think.
				ResolvedType x = this.facade.getType(scope);
				String qualName = x.describe();
				return isForeignType(qualName);
			}
		} catch (Exception e) {
			/*
			 *  IF the symbol couldn't be resolved, then it couldn't figure out where it was from
			 *  with the given typesolvers - meaning it has to be foreign. 
			 *  Or it was part of a package name. TODO
			 */
		     return true;
		}
		return true;
	}

	/**Function to check if the type is foreign or if it is the enclosing class (local).
	 * @param type you want to compare to the enclosing class - JavaParser Type
	 * @return true if foreign (not same as enclosing class) false otherwise.
	 */
	private boolean handleType(Type type) {
		try {
			ResolvedType x = this.facade.convertToUsage(type);
			String qualName = x.describe();
			return isForeignType(qualName);
		} catch (UnsolvedSymbolException e) {
			return true;
		}
	}

	/**Checks whether a method call is calling an accessor method.
	 * Uses naming conventions
	 * @param n the method call expresion to check
	 * @return true if it is an accessor, false otherwise.
	 */
	private boolean isAccessor(String name) {
		String fieldName;
		if ((name.startsWith("get") || name.startsWith("set")) && name.length() > 3) {
			fieldName = name.substring(3);
		} else if (name.startsWith("is") && name.length() > 2) {
			fieldName = name.substring(2);
		} else {
			return false;
		}
		//TODO naive implementation as it relies on naming convention, but not much more you can actually do?
		//is it possible to check whether the rest of the name matches a field of the declaring type.
		return true;
	}

	/**Determines whether the scope of a field access expression, method call expression or method reference expression
	 * is foreign i.e. wasn't declared locally or in the enclosing class. The function traverses down to the base of the
	 * scope and determines where it was declared.
	 * Currently treating static imports as if they were local.
	 * @param scope the scope to be examined - JavaParser Expression
	 * @return true if foreign false otherwise
	 */
	private boolean isForeign(Expression scope) {
		/*
		 * TODO - what if the bottom of the scope is like com.github
		 * like the start of the package name - probably won't resolve so will treat it like foreign
		 * 
		 */
		while(true) {	
			if(scope.isThisExpr() || scope.isSuperExpr()) {	//if the scope starts with this. or super. then it is local
				return false;
			}else if(scope.isFieldAccessExpr()) { //if it is a field access expression then keep traversing
				scope = scope.asFieldAccessExpr().getScope();		
				continue;
			}else if(scope.isMethodCallExpr()) {
				if(scope.asMethodCallExpr().getScope().isPresent()) { //if the method call has scope, keep traversing
					scope = scope.asMethodCallExpr().getScope().get();				
					continue;
				}else {//methodcall with no scope - so in local namespace, therefore local
					//TODO handle static import scenario
					return false;
				}
			}else if(scope.isNameExpr()){
				return handleScopeIsNameExpr(scope.asNameExpr());
				
			}else if(scope.isEnclosedExpr()){
				scope = scope.asEnclosedExpr().getInner();
				continue;
			}else if(scope.isCastExpr()) {
				scope = scope.asCastExpr().getExpression();
			}else if(scope.isClassExpr()) {
				Type type = scope.asClassExpr().getType();
				return handleType(type);
			}else if(scope.isObjectCreationExpr()) {
				Type type = scope.asObjectCreationExpr().getType();
				return handleType(type);
			}else if(scope.isTypeExpr()) {
				Type type = scope.asTypeExpr().getType();
				return handleType(type);
			}else if(scope.isArrayAccessExpr()) {
				scope = scope.asArrayAccessExpr().getName();
				continue;
			} else if(scope.isLiteralExpr()) {//literals are local.
				return false;
			} else if(scope.isAssignExpr()) {//continue with the target (left hand side)
				scope = scope.asAssignExpr().getTarget();
				continue;
			}
			else {
				return true;
			}
		}
	}

	/**Compares the qualified name of a type to the enclosing class to determine if it is foreign.
	 * @param qualName qualified name of a type as a string
	 * @return true if it is different from the enclosing class, false otherwise.
	 */
	private boolean isForeignType(String qualName) {
		TypeDeclaration<?> t = (this.callingAst.getNode());
		ResolvedReferenceTypeDeclaration r = this.facade.getTypeDeclaration(t);
		//TODO fix for nested classes - referencing a nested class is not foreign (maybe - figure that out first).
		if(r.getQualifiedName().equals(qualName)) {
			return false; //is not foreign
		} else return true; //is foreign
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.expr.FieldAccessExpr, java.lang.Object)
	 */
	@Override
	public void visit(FieldAccessExpr n, Integer[] arg) {	
		super.visit(n, arg);
		if(isForeign(n.getScope())) {
			arg[0] += 1;
			poi.add(n.getRange().get());
			//TODO will overcount, everything lower in the scope will also increment - might want to leave it that way
		}	
	}

	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.expr.MethodCallExpr, java.lang.Object)
	 */
	@Override
	public void visit(MethodCallExpr n, Integer[] arg) {
		super.visit(n, arg);
		if(n.traverseScope().isPresent() && isForeign(n.traverseScope().get()) && isAccessor(n.getNameAsString())) {
			arg[0] += 1;
			poi.add(n.getRange().get());
			//TODO will overcount, everything lower in the scope will also increment - might want to leave it that way
		}
	}
	
	/* (non-Javadoc)
	 * @see com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.github.javaparser.ast.expr.MethodReferenceExpr, java.lang.Object)
	 */
	@Override
	public void visit(MethodReferenceExpr n, Integer[] arg) {
		if(isForeign(n.getScope()) && isAccessor(n.getIdentifier())) {
			arg[0] += 1;
			poi.add(n.getRange().get());
			//TODO will overcount, everything lower in the scope will also increment - might want to leave it that way
		}
		super.visit(n, arg);
	}
	
}