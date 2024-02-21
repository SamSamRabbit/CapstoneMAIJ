package metricAnalysis.Metrics;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

public class UACAMetric extends Metric {
	
	/*
	* Finds the ratio of the number of classified methods to the total number of methods in a given class. (CMW)
	* Aim to measure the weight of methods in a class which potentially interact with any classified attributes in a particular class.
	* @author Jesse Studin
	*/

	
		
	public UACAMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}
	
	/*
	 * Finds the M value
	 * Sort the total number of methods in that class
	 */
	
	//just trying to get the variable name of each one..
	
	
	//ratio of
	//number of classified attributes that are assigned but never used.
	//total number of classified attributes
	private double CAVal(TypeDeclaration<?> n) {
		double result = 0;
		for(FieldDeclaration x : n.getFields()) if(x.isAnnotationPresent("secrecy")) result++;
		return result;
	}
	//number of classified attirbutes that are assigned but NOT used.
     private double CANUVal(TypeDeclaration<?> n) {
    	 double result = 0;
    	 //lets see if contain does anything
    	 for(FieldDeclaration x : n.getFields()) {
    		 
    		 //so get the current field and check if it is contained with method.
    		 //String currentFieldPre = x.toString();
    		 //String currentField = currentFieldPre.replaceAll("\\[", "").replaceAll("\\]", "");
    		 String currentField = x.getVariables().get(0).getName().toString();

    		 // now i have the variable without brackets, can check whether the methods contain this. if so return 1;
    		 //check whether it isn't use.
    		 for(MethodDeclaration y : n.getMethods()) {
    			 String methodString = y.toString();
    			 if(x.isAnnotationPresent("secrecy") == true) {
	    			 if(methodString.contains(currentField) == false) result++;

    			 }
    		 }
    	 }
    	 System.out.println("The result was : " + result);
    	 return result;
	}
	public void run(ClassAST clasAst) {
		if(this.alreadyDone(clasAst)) return;
	
		TypeDeclaration<?> n = clasAst.getNode();
		double CANUVal = CANUVal(n);
		double CAVal = CAVal(n);
		double resultVal = CANUVal / CAVal; //finds the ratio
		MetricResult result = new MetricResult(this.identifier, resultVal);
		clasAst.getResults().put(this.identifier, result);
	}
}


