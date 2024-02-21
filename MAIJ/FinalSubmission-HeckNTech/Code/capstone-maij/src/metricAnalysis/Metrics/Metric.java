/**
 * 
 */
package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.List;

import astManager.BaseAST;
import astManager.ClassAST;
import astManager.MethodAST;
import astManager.PackageAST;
import astManager.ProjectAST;

/**Base Class for a metric that all metrics should extend.
 * @author Jacob Botha
 *
 */
public abstract class Metric {
	protected String identifier;
	protected List<MetricLevel> levels;
	
	/**
	 * Constructor, sets the identifier
	 */
	public Metric() {
		this.identifier = this.getClass().getSimpleName();
		this.levels = new ArrayList<MetricLevel>();
	}
	
	/**Checks whether the metric has already been applied to the given AST node.
	 * @param ast AST Node you want to check
	 * @return true if it has already been calculated, false otherwise
	 */
	public boolean alreadyDone(BaseAST ast) {
		if(ast.getResults().containsKey(this.getIdentifier())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**Returns the unique identifier for this metric.
	 * @return the identifier of the metric
	 */
	public String getIdentifier(){
		return this.identifier;
	}
	/**Gives the different levels at which the metric may be applied
	 * @return the levels
	 */
	public List<MetricLevel> getLevels(){
		return this.levels;
	}
	
	/**Runs the metric at a class level, does nothing if it is not implemented at this level
	 * @param clasAst
	 */
	public  void run(ClassAST clasAst) {
		return;
	}
	/**runs the metric at a method level, does nothing if it is not implemented at this level
	 * @param methAst
	 */
	public  void run(MethodAST methAst) {
		return;
	}
	
	/**Runs the metric at a package level, does nothing if it is not implemented at this level
	 * @param packAst
	 */
	public  void run(PackageAST packAst) {
		return;
	}
	
	/**Runs the metric at a project level, Does nothing if it is not implemented at this level.
	 * @param projAst
	 */
	public  void run(ProjectAST projAst) {
		return;
	}
}
