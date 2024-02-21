/**
 * 
 */
package astManager;

import java.util.SortedMap;
import java.util.TreeMap;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;

import metricAnalysis.MetricResult;

/**AST wrapper for AST needded to evaluate class level metrics
 * @author Jacob Botha
 *
 */
public class ClassAST extends BaseAST{
	/**
	 * Map method/constructor Signature to Method AST
	 */
	private SortedMap<String, MethodAST> methods;

	private String className;

	/**Creates ClassAST from the given node with the given parent
	 * @param node
	 * @param parent
	 */
	public ClassAST(TypeDeclaration<?> node, BaseAST parent) {
		this.methods = new TreeMap<String, MethodAST>();
		this.node = node;
		processMethods();
		this.results = new TreeMap<String, MetricResult>();
		className = node.getNameAsString();
		this.parent = parent;
	}

	/**Processes the methods contained in the class adding them as 
	 * MethodAST objects. Methods and Constructors are considered to be
	 * methods for the purpose of metric evaluation.
	 * @param node
	 */
	@SuppressWarnings("unchecked")
	private void processMethods() {
		for (BodyDeclaration<?> node: (NodeList<BodyDeclaration<?>>) this.node.asTypeDeclaration().getMembers()) {
			if(node.isCallableDeclaration()) { //covers constructors and methods
				//added javaprser for method
				CallableDeclaration<?> callNode = (CallableDeclaration<?>) node;
				this.methods.put(callNode.getSignature().toString(), new MethodAST(callNode, this));
			}
		}
	}

	/* (non-Javadoc)
	 * @see astManager.BaseAST#getBreadcrumb()
	 */
	@Override
	public Breadcrumb getBreadcrumb() {
		return new Breadcrumb(this.getParent().getParent().getVersion(), this.getParent().getIdentifier(), this.getIdentifier(), null);
	}


	/* (non-Javadoc)
	 * @see astManager.BaseAST#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return this.className;
	} 
	/**
	 * @return the methods
	 */
	public SortedMap<String, MethodAST> getMethods() {
		return this.methods;
	}
	
	/* (non-Javadoc)
	 * @see astManager.BaseAST#getNode()
	 */
	@Override
	public TypeDeclaration<?> getNode() {
		return (TypeDeclaration<?>) this.node;
	}

	/* (non-Javadoc)
	 * @see astManager.BaseAST#getParent()
	 */
	@Override
	public PackageAST getParent() {
		return (PackageAST) this.parent;
	}

	public boolean isAnnotationDeclaration() {
		return this.node.isAnnotationDeclaration();
	}

	/**Whether this AST represents an ClassOrInterfaceDeclaration
	 * @return true if this.node is an ClassOrInterfaceDeclaration
	 */
	public boolean isClassOrInterface() {
		return this.node.isClassOrInterfaceDeclaration();
	}

	/** Whether this BaseAST holds an JavaParser EnumDeclaration
	 * in its node field
	 * @return true if this.node is an EnumDeclaration
	 */
	public boolean isEnum() {
		return this.node.isEnumDeclaration();
	}
	


}
