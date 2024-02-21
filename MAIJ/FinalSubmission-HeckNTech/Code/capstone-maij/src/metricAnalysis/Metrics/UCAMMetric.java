package metricAnalysis.Metrics;

import java.util.Arrays;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

public class UCAMMetric extends Metric {

	public UCAMMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}
	
	
	//number
	//of classified methods that access a classified attribute but are never called by other methods
	public double CANUVal(TypeDeclaration<?> n) {

		//check if classified method accesses a classified attribute, then check if that method is accessed by non C
		double result = 0;
		
		for(MethodDeclaration y : n.getMethods()) {
			String currMethodNameY = y.getNameAsString();
			String currMethodY = y.toString();
			for(FieldDeclaration x : n.getFields()) {
				String currField = x.getVariables().get(0).getName().toString();
				System.out.println("Current class is: " + n.getNameAsString());
				for(MethodDeclaration z : n.getMethods()) {
					//check if method y accesses field
					if(y.isAnnotationPresent("secrecy") && x.isAnnotationPresent("secrecy")) {
						if(currMethodY.contains(currField)) {
							if(z.toString().contains(currMethodNameY) && z.isAnnotationPresent("secrecy") == false && z.toString().contentEquals(currMethodY) == false) result++; // end third if loop
						}//end second if loop
					}//end first if loop
				}//end 3rd for loop
			}//end second for loop
		}//end first for loop
		return result;
	}
	
	public double CAVal(TypeDeclaration<?> n, Class<? extends TypeDeclaration> className) {
		double result = 0;
		for(MethodDeclaration x : n.getMethods()) if(x.isAnnotationPresent("secrecy") && x.isPublic()) result++;
		return result;
	}
	
	public void run(ClassAST clasAst) {
		if(this.alreadyDone(clasAst)) return;
	
		TypeDeclaration<?> n = clasAst.getNode();
		//getVariables().get(0).getName().toString()
		//Class<? extends TypeDeclaration>
		Class<? extends TypeDeclaration> clss = n.getClass();
		double CANUVal = CANUVal(n);
		double CAVal = CAVal(n, clss);
		double resultVal = CANUVal / CAVal; //finds the ratio
		MetricResult result = new MetricResult(this.identifier, resultVal);
		clasAst.getResults().put(this.identifier, result);
	}
}
