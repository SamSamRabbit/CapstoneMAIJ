/**
 * 
 */
package astManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import fileImport.PendingImport;
import metricAnalysis.MetricResult;

/**AST wrapper for ast needed to evaluate project level metrics
 * @author Jacob Botha
 *
 */
public class ProjectAST extends BaseAST {
	
	
	/**
	 * JavaParserSymbolSolver TypeSolver
	 */
	private CombinedTypeSolver typeSolver;
	private Double version;
	
	/**
	 * Map of package name to PackageAST
	 */
	private SortedMap<String, PackageAST> packages;
	private SortedMap<String, SortedMap<Path, Exception>> errors;
	private Path directory;

	/**
	 * @return the directory
	 */
	public Path getDirectory() {
		return directory;
	}

	/**Basic constructor creates an ProjectAST with the given version number
	 * holding the AST parsed from the files in pendingImport
	 * @param versionNo
	 * @param pendingImport 
	 */
	public ProjectAST(Double versionNo, PendingImport pendingImport) {
		this.version = versionNo;
		this.parent = null;
		this.directory = pendingImport.getSelectedDirectory();
		
		this.packages = new TreeMap<String, PackageAST>();
		
		
		this.results = new TreeMap<String, MetricResult>();
			
		this.typeSolver = new CombinedTypeSolver();
		this.typeSolver.add(new ReflectionTypeSolver());
		this.typeSolver.add(new ReflectionTypeSolver(false));
		errors = this.parseImport(pendingImport);
	}

	/**Adds JarTypeSolvers to the combined type solver for use with JavaSymbolSolver
	 * @param pendingImport List of .jar files
	 * @return Map of file paths with exceptions if they threw an exception
	 * @throws IOException 
	 */
	private SortedMap<Path, Exception> addJarTypeSolvers(List<Path> jarFiles) {
		SortedMap<Path, Exception> jarFails = new TreeMap<Path, Exception>();
		for(Path p : jarFiles) {
			try {
				this.typeSolver.add(new JarTypeSolver(p));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				jarFails.put(p, e);
			}
		}
		return jarFails;
	}

	/**Adds JavaParserTypeSolvers to the combined type solver for use 
	 * with JavaParserSymbolSolver
	 * 
	 * @param sourceRoots - set of root directories for the source code
	 * 						if the package is com.github.etc must be the 
	 * 						directory containing the folder com
	 */
	private void addSourceTypeSolvers(Set<Path> sourceRoots) {
		for(Path p : sourceRoots) {
			this.typeSolver.add(new JavaParserTypeSolver(p));
		}
	}

	/** Processes the pendingImport which represent a version of the source code to be analyzed.
	 *  Parsing it into a ProjectAST.
	 * @param pendingImport
	 * @return [.java parse failures, .jar parse failures], the file path is mapped to the exception thrown.
	 */
	private SortedMap<String, SortedMap<Path, Exception>> parseImport(PendingImport pendingImport) {
		Set<Path> sourceRoots = new HashSet<Path>();
		SortedMap<Path, Exception> parseFails = new TreeMap<Path, Exception>();
		SortedMap<Path, Exception> jarFails;
		CompilationUnit cu;
		
		for(Path path: pendingImport.getJavaFiles()) {
			try {
				cu = JavaParser.parse(path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				parseFails.put(path, e);
				continue;
			}			
			try {
				sourceRoots.add(cu.getStorage().get().getSourceRoot());
			} catch (Exception ignore) {
				//TODO probably means there is a naming clash between filesystem and package
				//ignore for now, but could figure out how to get the source root some other way later.
			}					
			processCU(cu);
		}
		
		addSourceTypeSolvers(sourceRoots);
		jarFails = addJarTypeSolvers(pendingImport.getJarFiles());	
		SortedMap<String, SortedMap<Path, Exception>> failsMap = new TreeMap<String, SortedMap<Path, Exception>>();
		failsMap.put("Parse Fails", parseFails);
		failsMap.put("Jar Fails", jarFails);
		
		return failsMap;
	}
	/**processes the CompilationUnit - adding it the the astStorage
	 * as part of a PackageAST object. If the package already exists simply add
	 * the cu to it, if it does not then it first create the package.
	 * @param cu
	 */
	
	private void processCU(CompilationUnit cu) {
		String pName = ASTHandler.getPackageName(cu);
		if(!this.packages.containsKey(pName)) {
			this.packages.put(pName, new PackageAST(pName, this));
		}
		this.packages.get(pName).processClasses(cu);
	}
	
	/* (non-Javadoc)
	 * @see astManager.BaseAST#getBreadcrumb()
	 */
	@Override
	public Breadcrumb getBreadcrumb() {
		return new Breadcrumb(this.getVersion(), null, null, null);
	}

	/**
	 * @return the errors
	 */
	public SortedMap<String, SortedMap<Path, Exception>> getErrors() {
		return errors;
	}

	/* (non-Javadoc)
	 * @see astManager.BaseAST#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return "Version ".concat(getVersion().toString());
	}

	/**
	 * @return the packages
	 */
	public SortedMap<String, PackageAST> getPackages() {
		return this.packages;
	}

	/**
	 * @return the typeSolver
	 */
	public JavaParserFacade getSymbolSolverFacade() {
		return JavaParserFacade.get(this.typeSolver);
	}

	/**
	 * @return the version
	 */
	public Double getVersion() {
		return this.version;
	}

}
