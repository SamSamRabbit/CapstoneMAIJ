/*
 * Finds the ratio of non-private classified instance attributes to the number of classified attributes within a class. (CIDA)
 * Aim to change this to not allow comments, but until further deliberation is met, is hard to find the secrecy value without 'secrecy' tag
 * @author Jesse Studin
 */

package metricAnalysis.Metrics;
import java.util.Arrays;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.ibm.icu.text.DecimalFormat;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

public class CIDAMetric extends Metric {
	
	public CIDAMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}
	
	/*
	 * Finds the CA Value
	 * Sorts each field into x (for-each) and if "/*secrecy exists, then CA is +=1, then returns CA
	 */
	
	
	private double CAVal(TypeDeclaration<?> n) {
		double classifiedVal = 0.0;
		//checks if the current field contains the secrecy comment, if so CA is icreased by one
		for(FieldDeclaration x : n.getFields()) if(x.isAnnotationPresent("secrecy")) classifiedVal++; //end for-if loop
		return classifiedVal;
	}//end CAVal function
	
	/*
	 * Find the total for NCIA
	 * Saves each field to X and finds whether it contains the word "public".. if so adds 1 to classifiedVal, when complete, returns the value
	 */
	private double NCIAVal(TypeDeclaration<?> n) {
		double classifiedVal = 0.0;
		//checks whether the current field has the secrecy comment and is set to public, if so, NCIA is increased by one.
		for(FieldDeclaration x : n.getFields()) if(x.isAnnotationPresent("secrecy") && x.isPublic()) classifiedVal++;
		return classifiedVal;
	}//end NCIAVal function
	public void run(ClassAST clasAst) {
		//if it exists
		if(this.alreadyDone(clasAst)) return;
		TypeDeclaration<?> n = clasAst.getNode();
		//CA and NCIA Results
		double CA = CAVal(n);
		double NCIA = NCIAVal(n);
		//end results
		DecimalFormat df = new DecimalFormat("####0.00");
		double resultVal = Double.valueOf(df.format(NCIA / CA));
		//Store the result within the MetricResult class ***Redefine this comment later
		MetricResult result = new MetricResult(this.identifier, resultVal);
		//log identifier + results
		clasAst.getResults().put(this.identifier, result);
	}
}
