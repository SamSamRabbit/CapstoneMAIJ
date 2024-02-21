/**
 * 
 */
package astManager;

import java.util.SortedMap;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.comments.Comment;

import metricAnalysis.MetricResult;

/**Base class for the wrapper classes holding the JavaParser AST
 * @author Jacob Botha
 *
 */
public abstract class BaseAST {
	protected SortedMap<String, MetricResult> results;
	protected BaseAST parent;
	protected BodyDeclaration<?> node;
	
	
	/**Gets the ProjectAST associated with this BaseAST
	 * follows the tree back to the root.
	 * @param ast the BaseAST you want the ProjectAST of
	 * @return the ProjectAST that contains ast or ast if ast is a ProjectAST
	 */
	public static ProjectAST getContainingProjectAST(BaseAST ast) {
		BaseAST container = ast;	
		while(!container.isProjectAST()) {
			try {
				container = container.getParent();
			} catch (InvalidOperationException e) {
				// This will never be reached as only ProjectAST doesn't have a parent
				e.printStackTrace();
			}
		}
		//relies on the current structure of ProjectAST being the highest
		return (ProjectAST) container;
	}
	
	/**
	 * @return the Breadcrumb pointing to this node
	 */
	public abstract Breadcrumb getBreadcrumb();


	/**
	 * @return the identifier used for the node.
	 * "Version #", package name, class name and method signature respectively
	 */
	public abstract String getIdentifier();
	
	
	/**Gets the JavaParser AST Node associated with this AST.
	 * @return the node as a BodyDeclaration
	 * @throws InvalidOperationException if the AST doesn't have an associated node
	 */
	public BodyDeclaration<?> getNode() throws InvalidOperationException {
		if(this.hasNode()){
			return this.node;
		}else {
			throw  new InvalidOperationException();
		}
	}
	
	/**gets the parent BaseAST associated with this BaseAST
	 * @return the parent
	 * @throws InvalidOperationException 
	 */
	public BaseAST getParent() throws InvalidOperationException {
		if(this.hasParent()) {
			return this.parent;
		}else {
			throw new InvalidOperationException();
		}
	}

	/**
	 * @return the results
	 */
	public SortedMap<String, MetricResult> getResults() {
		return this.results;
	}
	
	/**Checks if this AST has a node
	 * @return true if this.node is not null
	 */
	public boolean hasNode() {
		if(this.node == null) {
			return false;
		} else {
			return true;
		}
	}
	
	
	/**Checks if this BaseAST has a parent
	 * Same as !isProjectAST() as only a ProjectAST does not have a parent
	 * @return true if this.parent is not null
	 */
	public boolean hasParent() {
		if(this.parent == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @return true if this BaseAST is a ClassAST
	 */
	public boolean isClassAST() {
		return this instanceof ClassAST;
	}
	
	
	/**
	 * @return true if this BaseAST is a MethodAST
	 */
	public boolean isMethodAST() {
		return this instanceof MethodAST;
	}
	
	/**
	 * @return true if this BaseAST is a PackageAST
	 */
	public boolean isPackageAST() {
		return this instanceof PackageAST;
	}
	
	/**
	 * @return true if this BaseAST is a ProjectAST
	 */
	public boolean isProjectAST() {
		return this instanceof ProjectAST;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getIdentifier();
	}
	
}