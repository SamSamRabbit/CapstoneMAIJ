/**
 * 
 */
package astManager;

import java.util.TreeMap;

import com.github.javaparser.ast.body.CallableDeclaration;

import metricAnalysis.MetricResult;
/**AST wrapper for ast needed to evaluate method level metrics
 * @author Jacob Botha
 *
 */
public class MethodAST extends BaseAST{
	
	private String methodSignature;

	/**Constructs MethodAST fromt the given node with the given parent
	 * @param node
	 * @param parent
	 */
	public MethodAST(CallableDeclaration<?> node, BaseAST parent){
		this.node = node;
		this.results = new TreeMap<String, MetricResult>();
		this.methodSignature = node.getSignature().toString();
		this.parent = parent;
	}

	/* (non-Javadoc)
	 * @see astManager.BaseAST#getBreadcrumb()
	 */
	@Override
	public Breadcrumb getBreadcrumb() {
		return new Breadcrumb(this.getParent().getParent().getParent().getVersion(), this.getParent().getParent().getIdentifier(), this.getParent().getIdentifier(), this.getIdentifier());
	}


	/* (non-Javadoc)
	 * @see astManager.BaseAST#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return methodSignature;
	}

	/* (non-Javadoc)
	 * @see astManager.BaseAST#getNode()
	 */
	@Override
	public CallableDeclaration<?> getNode() {
		return (CallableDeclaration<?>)this.node;
	}

	/* (non-Javadoc)
	 * @see astManager.BaseAST#getParent()
	 */
	@Override
	public ClassAST getParent() {
		return (ClassAST) this.parent;
	}

	/**Whether this BaseAST holds an JavaParser ConstructorDeclaration
	 * in its node field
	 * @return true if this.node is an ConstructorDeclaration
	 */
	public boolean isConstructor() {
		return this.node.isConstructorDeclaration();
	}
	
	/**Whether this BaseAST holds an JavaParser MethodDeclaration
	 * in its node field
	 * @return true if this.node is an MethodDeclaration
	 */
	public boolean isMethod() {
		return this.node.isMethodDeclaration();
	}
}
