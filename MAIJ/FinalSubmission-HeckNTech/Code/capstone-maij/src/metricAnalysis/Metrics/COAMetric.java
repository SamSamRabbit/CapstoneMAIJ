package metricAnalysis.Metrics;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import astManager.ClassAST;
import javassist.compiler.ast.MethodDecl;
import metricAnalysis.MetricResult;

public class COAMetric extends Metric {
	
	private ClassAST clasAst;


	public COAMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}
	
	
	/*
	 * Finds the CM Value(number of classified methods)
	 */
	
	private double CMVal(TypeDeclaration<?> n) {
		
		double value = 0.0;
		String lastUsed = "";
		for(MethodDeclaration  y : n.getMethods()) {
			for(FieldDeclaration x : n.getFields()) {
				if(!lastUsed.contentEquals(y.toString())) {
					if(x.isAnnotationPresent("secrecy")) {
						String attr = x.getVariables().get(0).getName().toString();
						if(y.toString().cotnains(attr)) {
							value++;
							lastUsed = y.toString();
						}
					}
				}
			}
		}
		return value;
	}//end CMVal function
	
	/*
	 * Find the total for NCM(number of non-private classified methods)
	 */
	//made edit to this function, was finding the public classified attribute, meant to find public on method
	//it can only read each method once.. - store each read string method into a arraylist<string>(orlist) and check if it exists within it before doing calc
	
	private double NCMVal(TypeDeclaration<?> n) {
		double value = 0.0;
		String lastUsed = "";
		for(MethodDeclaration y : n.getMethods()) {
			for(FieldDeclaration x : n.getFields()) {
				if(!lastUsed.contentEquals(y.toString())) {
					if(y.isPublic()) {
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
		}//end for loops
		return value;
	}//end NCMVal function
	
	
	public void run(ClassAST clasAst) {
		//if it exists
		if(this.alreadyDone(clasAst)) return;
		
		TypeDeclaration<?> n = clasAst.getNode();
		//get the values for CM and NCM using their respective functions
		double CM = CMVal(n);
		double NCM = NCMVal(n);
		//find the result via NCM over CM
		double resultVal = NCM / CM;
		//Store the result within the MetricResult class ***Redefine this comment later
		MetricResult result = new MetricResult(this.identifier, resultVal);
		//log identifier + results
		clasAst.getResults().put(this.identifier, result);
	}
}