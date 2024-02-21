/**
 * 
 */
package astManager;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;

import metricAnalysis.MetricResult;

/**AST wrapper for AST needed to evaluate project level metrics
 * @author Jacob Botha
 *
 */
public class PackageAST extends BaseAST{
	final private String packageName;

	/**
	 * map of class name to ClassAST
	 */
	private SortedMap<String, ClassAST> classes;

	/**
	 * keep the JavaParser CompilationUnits that make up this package
	 */
	private Set<CompilationUnit> cus;

	private CompilationUnit packageInfo;

	/**Create a package AST with the given name and the given parent
	 * @param Name
	 * @param parent ProjectAST that contains this one
	 */
	public PackageAST(String Name, BaseAST parent) {
		this.classes = new TreeMap<String, ClassAST>();
		this.cus = new HashSet<CompilationUnit>();
		this.packageName = Name;
		this.results = new TreeMap<String, MetricResult>();
		this.parent = parent;
	}



	/* (non-Javadoc)
	 * @see astManager.BaseAST#getBreadcrumb()
	 */
	@Override
	public Breadcrumb getBreadcrumb() {
		return new Breadcrumb(this.getParent().getVersion(), this.getIdentifier(), null, null);
	}

	
	
	/**
	 * @return the classes
	 */
	public SortedMap<String, ClassAST> getClasses() {
		return this.classes;
	}

	/**
	 * @return the cus
	 */
	public Set<CompilationUnit> getCus() {
		return this.cus;
	}
	/**
	 * @return the packageName
	 */
	@Override
	public String getIdentifier() {
		return this.packageName;
	}
	/**
	 * @return the packageInfo
	 */
	public CompilationUnit getPackageInfo() {
		return packageInfo;
	}

	/* (non-Javadoc)
	 * @see astManager.BaseAST#getParent()
	 */
	@Override
	public ProjectAST getParent() {
		return (ProjectAST) this.parent;
	}



	/**Processes the classes contained inside the cu adding them to the astStorage as ClassAST objects
	 * classes, interfaces, enums and annotation types are considered to be classes for metric evaluation.
	 * This will pick up any nested classes as well.
	 * as ClassAST objects
	 * @param cu JavaParser CompilationUnit
	 */
	public void processClasses(CompilationUnit cu) {
		
		if(cu.getStorage().get().getFileName().equals("package-info.java")) {
			this.packageInfo = cu;
		}else {
			this.cus.add(cu);
		}
		for(TypeDeclaration<?> node : cu.findAll(TypeDeclaration.class)) {
			this.classes.put(node.getNameAsString(), new ClassAST(node, this));
		}
	}
	
	
}
