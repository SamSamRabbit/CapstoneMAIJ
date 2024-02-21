package metricAnalysis.Metrics;

import java.util.Arrays;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.ibm.icu.text.DecimalFormat;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

/*
 * Author: 
 */

public class CAIWMetric extends Metric {
	public CAIWMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}
	
	//may interact with classified attributes
	//total number of methods that which could have access to all attributes
	
	//each time method contains one classified value, value increase++.
	private double CAMethVal(TypeDeclaration<?> n) {
		double classifiedVal = 0.0;
		for(FieldDeclaration x : n.getFields()) {
			for(MethodDeclaration y : n.getMethods()) {
				if(x.isAnnotationPresent("secrecy")) {
					String varName = x.getVariables().get(0).getName().toString();
					System.out.println(varName);
					if(y.toString().contains(varName)) classifiedVal++;
				}
			}
		}
		return classifiedVal;
	}
	
	
	//checks if all attributes is accessed within all methods
	private double NCAMethVal(TypeDeclaration<?> n) {
		double classifiedVal = 0.0;
		for(FieldDeclaration x : n.getFields()) {
			for(MethodDeclaration i : n.getMethods()){
				String varName = x.getVariables().get(0).getName().toString();
				if(i.toString().contains(varName)) classifiedVal++;
			}
		}
		return classifiedVal;
	}
	
	public void run(ClassAST clasAst) {
		//if it exists
		if(this.alreadyDone(clasAst)) return;
		TypeDeclaration<?> n = clasAst.getNode();
		//get the values for CA and NCIA using their respective functions
		//find the result via NCIA over CA
		DecimalFormat df = new DecimalFormat("####0.00");
		double resultVal = Double.valueOf(df.format(CAMethVal(n) / NCAMethVal(n)));
		//Store the result within the MetricResult class ***Redefine this comment later
		MetricResult result = new MetricResult(this.identifier, resultVal);
		//log identifier + results
		clasAst.getResults().put(this.identifier, result);
	}
	
}
