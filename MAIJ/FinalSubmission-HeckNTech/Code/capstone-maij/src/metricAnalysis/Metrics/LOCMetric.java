/**
 * 
 */
package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import astManager.ClassAST;
import astManager.MethodAST;
import astManager.PackageAST;
import astManager.ProjectAST;
import metricAnalysis.MetricResult;
/**This metric calculates the number of lines of code for the given node.
 * Includes comments and depends on formatting.
 * May be applied at the Project, Package, Class or Method level.
 * @author Jacob Botha
 *
 */
public class LOCMetric extends Metric {
	
	/**
	 * Sets the identifier and the levels that the metric may be applied on
	 */
	public LOCMetric(){
		super();
		this.levels = Arrays.asList(MetricLevel.PROJECT, MetricLevel.PACKAGE, MetricLevel.CLASS, MetricLevel.METHOD);
	}

	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.ClassAST)
	 */
	@Override
	public void run(ClassAST clasAst) {
		/*
		 * Don't run it again if the result is already recorded.
		 * Prevents wasted computation if the results is needed by a different
		 * metric or the same metric at a higher level.
		 */
		if(this.alreadyDone(clasAst)) return;

		
		/*
		 * We are interested in recording both the number of lines of code
		 * and the location of points of interest that affected the value of the metric.
		 */
		Double loc = (double) 0;
		ArrayList<Range> poi = new ArrayList<Range>();
		
		
		/*
		 * Get the JavaParser Node from the wrapper AST
		 * In this case we can calculate the lines of code directly from the node's attributes - 
		 * check JavapParser Documentation for more details.
		 * Add the range of the node to the points of interest.
		 */
		TypeDeclaration<?> node = clasAst.getNode();
		loc = (double) (node.getEnd().get().line - node.getBegin().get().line + 1);
		poi.add(node.getRange().get());

		
		/*
		 * Create an MetricResult object with the calculated results and add it to the 
		 * node's resutls.
		 */
		MetricResult result = new MetricResult(this.identifier, loc, poi);
		clasAst.getResults().put(this.identifier, result);
	}

	
	
	
	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.MethodAST)
	 */
	@Override
	public void run(MethodAST methAst) {
		if(this.alreadyDone(methAst)) return;
		
		Double loc = (double) 0;
		ArrayList<Range> poi = new ArrayList<Range>();
		
		CallableDeclaration<?> node = methAst.getNode();
		loc = (double) (node.getEnd().get().line - node.getBegin().get().line + 1);
		poi.add(node.getRange().get());	
		
		MetricResult result = new MetricResult(this.identifier, loc, poi);
		methAst.getResults().put(this.identifier, result);
	}

	
	
	
	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.PackageAST)
	 */
	@Override
	public void run(PackageAST packAst) {
		if(this.alreadyDone(packAst)) return;
		
		/*
		 * No points of interest in this case as a package typically 
		 * spans multiple files.
		 */
		Double loc = (double) 0;
		MetricResult result;
		
		
		/*
		 * In this case directly working with the Compilation Units which
		 * represents the root node of the JavaParser AST.
		 */
		Set<CompilationUnit> cus = packAst.getCus();
		for(CompilationUnit cu : cus) {
			loc += cu.getEnd().get().line - cu.getBegin().get().line + 1;
		}
			
		result = new MetricResult( this.identifier, loc);
		packAst.getResults().put(this.identifier, result);
	}

	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.ProjectAST)
	 */
	@Override
	public void run(ProjectAST projAst) {
		if(this.alreadyDone(projAst)) return;
		
		Double loc = (double) 0;
		MetricResult result;
		
		/*
		 * Example of where the metric applied at a higher level uses the result
		 * of the metric applied at a lower level.
		 */
		Map<String, PackageAST> packages = projAst.getPackages();
		for(PackageAST packAst : packages.values()) {
			this.run(packAst);
			loc += packAst.getResults().get(this.identifier).getValue();
		}
		
		result = new MetricResult(this.identifier, loc);
		projAst.getResults().put(this.identifier, result);
	}
}
