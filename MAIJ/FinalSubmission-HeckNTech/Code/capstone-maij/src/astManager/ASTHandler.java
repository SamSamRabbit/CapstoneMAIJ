/**
 * 
 */
package astManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import fileImport.PendingImport;
import metricAnalysis.MetricResult;

/**
 * Class to hold all the AST for the application in a form
 * that is convenient to evaluate software metrics on.
 * 
 * @author Jacob Botha
 *
 */
public class ASTHandler {
	private SortedMap<Double, ProjectAST> astStorage;
	private SortedMap<String, SortedMap<String, List<String>>> aggregateHeirarchy;

	/**Gives the package name of a compilation unit
	 * @param cu JavaParser CompilationUnit
	 * @return the package name or default if there is no package declaration
	 */
	public static String getPackageName(CompilationUnit cu) {
		String pName;
		if(cu.getPackageDeclaration().isPresent()) {
			pName = cu.getPackageDeclaration().get().getNameAsString();		
		} else {
			pName = "default";
		}
		return pName;
	}
	
	

	/**Builds the astStorage, by parsing all the versions
	 * @param pendingImports
	 */
	public ASTHandler(SortedMap<Double, PendingImport> pendingImports){
		astStorage = new TreeMap<Double, ProjectAST>();
		for (Double verNo : pendingImports.keySet()) {
			astStorage.put(verNo, new ProjectAST(verNo, pendingImports.get(verNo)));
		}
		
		generateAggregateHeirarchy();
	}

	/**
	 * Generates a combined heirarchy accross all versions, in a way that may be used to get a particular node from the ast storage.
	 */
	private void generateAggregateHeirarchy() {
		aggregateHeirarchy = new TreeMap<String, SortedMap<String, List<String>>>();
		
		for( Double version : astStorage.keySet()) {
			for(  PackageAST packAST : astStorage.get(version).getPackages().values()) {
				if(!aggregateHeirarchy.containsKey(packAST.getIdentifier())) {
					aggregateHeirarchy.put(packAST.getIdentifier(), new TreeMap<String, List<String>>());
				}
				
				for( ClassAST clasAST : packAST.getClasses().values()) {
					if(!aggregateHeirarchy.get(packAST.getIdentifier()).containsKey(clasAST.getIdentifier())) {
						aggregateHeirarchy.get(packAST.getIdentifier()).put(clasAST.getIdentifier(), new ArrayList<String>());
					}
					
					for(MethodAST methAST : clasAST.getMethods().values()) {
						if(!aggregateHeirarchy.get(packAST.getIdentifier()).get(clasAST.getIdentifier()).contains(methAST.getIdentifier())) {
							aggregateHeirarchy.get(packAST.getIdentifier()).get(clasAST.getIdentifier()).add(methAST.getIdentifier());
						}
					}
				}
				
			}
		}
	}

	/**
	 * @return the aggregateHeirarchy
	 */
	public SortedMap<String, SortedMap<String, List<String>>> getAggregateHeirarchy() {
		return aggregateHeirarchy;
	}
	
	/**
	 * @return the astStorage
	 */
	public SortedMap<Double, ProjectAST> getAstStorage() {
		return astStorage;
	}
	
	
	/**Retrieves the MetricResult object for the given metric on the given class
	 * @param metricIdentifier The identifier for the metric which results are wanted
	 * @param version the project version
	 * @param packageName the package name, fully qualified
	 * @param className the class name, as a simple name
	 * @return the corresponding MetricResult object, null if there is no such result
	 */
	public  MetricResult getClassResult(String metricIdentifier,Double version, String packageName, String className) {
		return astStorage.get(version).getPackages().get(packageName).getClasses().get(className).getResults().get(metricIdentifier);
	}
	
	/**Prints the files that encountered parse error to a string, each on its own line
	 * @return the string of filePaths
	 */
	public String getErrorsAsString() {
		SortedSet<Path> fails = new TreeSet<Path>();
		for( ProjectAST proj : astStorage.values()) {
			fails.addAll(proj.getErrors().get("Parse Fails").keySet());
		}
		StringBuilder string = new StringBuilder();
		for(Path path : fails) {
			string.append(path.toAbsolutePath());
			string.append("\n");
		}
		return string.toString();
	}
	
	/**Retrieves the MetricResult object for the given metric on the given method
	 * @param metricIdentifier The identifier for the metric which results are wanted
	 * @param version the project version
	 * @param packageName the package name, fully qualified
	 * @param className the class name, as a simple name
	 * @param methodSignature the method signature
	 * @return the corresponding MetricResult object, null if there is no such result
	 */
	public  MetricResult getMethodResult(String metricIdentifier,Double version, String packageName, String className, String methodSignature) {
		return astStorage.get(version).getPackages().get(packageName).getClasses().get(className).getMethods().get(methodSignature).getResults().get(metricIdentifier);
	}
	
	/**Retrieves the MetricResult object for the given metric on the given package
	 * @param metricIdentifier The identifier for the metric which results are wanted
	 * @param version the project version
	 * @param packageName the package name, fully qualified
	 * @return the corresponding MetricResult object, null if there is no such result
	 */
	public  MetricResult getPackageResult(String metricIdentifier,Double version, String packageName) {
		return astStorage.get(version).getPackages().get(packageName).getResults().get(metricIdentifier);
	}
	
	/**Retrieves the MetricResult object for the given metric on the project version
	 * @param metricIdentifier The identifier for the metric which results are wanted
	 * @param version the project version
	 * @return the corresponding MetricResult object, null if there is no such result
	 */
	public MetricResult getProjectResult(String metricIdentifier, Double version) {
		return astStorage.get(version).getResults().get(metricIdentifier);
	}
}
