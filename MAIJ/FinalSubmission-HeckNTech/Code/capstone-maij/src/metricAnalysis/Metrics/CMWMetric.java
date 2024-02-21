/*
* Finds the ratio of the number of classified methods to the total number of methods in a given class. (CMW)
* Aim to measure the weight of methods in a class which potentially interact with any classified attributes in a particular class.
* @author Lizzie
*/

package metricAnalysis.Metrics;
import java.util.Arrays;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

public class CMWMetric extends Metric {
	
	public CMWMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}
	
	/*
	 * Finds the M value
	 * Sort the total number of methods in that class
	 */
	
	private double MVal(TypeDeclaration<?> n) {
		double methodsVal = n.getMethods().size();
		return methodsVal;
	}
	
	/*
	 * Finds the total for CM value
	 * Sort the number of methods which may interact in any form with classified attributes in a class.
	 */
	
     private double CMVal(TypeDeclaration<?> n) {
		double value = 0.0;
		String lastUsed = "";
		for(MethodDeclaration y : n.getMethods()) {
			for(FieldDeclaration x : n.getFields()) {
				if(!lastUsed.contentEquals(y.toString())) {
					if(x.isAnnotationPresent("secrecy")) {
						String attr = x.getVariables().get(0).getName().toString();
						if(y.toString().contains(attr)) {
							value++;
							lastUsed = y.toString();
						}
					}
				}
			}
		}
		return value;
	}
	public void run(ClassAST clasAst) {
		if(this.alreadyDone(clasAst)) return;
		
		System.out.println("Current path: " + clasAst.getNode().findCompilationUnit().get().getStorage().get().getPath());
		TypeDeclaration<?> n = clasAst.getNode();
		double M = MVal(n);
		double CM = CMVal(n);
		double resultVal = CM / M;
		MetricResult result = new MetricResult(this.identifier, resultVal);
		clasAst.getResults().put(this.identifier, result);
	}
}