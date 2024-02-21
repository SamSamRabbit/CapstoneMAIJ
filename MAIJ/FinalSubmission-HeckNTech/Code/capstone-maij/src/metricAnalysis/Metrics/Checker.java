package metricAnalysis.Metrics;

import java.util.Arrays;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

public class Checker extends Metric {

	public Checker() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}
	
	
	
	/*Find a value that reads or write a classified attribute (//secrecyvalue//)*/
	
	private double CAVal(TypeDeclaration<?> n) {
		double classifiedVal = 0.0;
	
		//predicted value for testdata?
		for(FieldDeclaration i : n.getFields()) {
			for(MethodDeclaration x : n.getMethods()) {
				if(i.toString().contains("/*secrecy*/")){
					String tempHold = i.getVariables().get(0).getName().toString();
					if(x.toString().contains(tempHold)) classifiedVal++;
				}
			}
		}//end forloops
		return classifiedVal;
	}//end CAVal function
	
	
	//work on this one??
	//find methods that do not interact with classified attiributes
	private double NCMethod(TypeDeclaration<?> n) {
		double classifiedVal = 0;
		
		//if contains this.+hold && public --> methodheader
		for(MethodDeclaration x : n.getMethods()) {
			for(FieldDeclaration i : n.getFields()) {
				if(i.isAnnotationPresent("secrecy")) {
					if(x.isPublic()) classifiedVal++;
				}
			}
			System.out.println("Classified Val is: " + classifiedVal);
		}
		
		return classifiedVal;
	}

	public void run(ClassAST clasAst) {
		//if it exists
		if(this.alreadyDone(clasAst)) return;
		
		System.out.println("Current path: " + clasAst.getNode().findCompilationUnit().get().getStorage().get().getPath());
		TypeDeclaration<?> n = clasAst.getNode();
		//get the values for CA and NCIA using their respective functions
		double CA = CAVal(n);
		double NCMVal = NCMethod(n);
		//find the result via NCIA over CA
		//double resultVal = NCMVal / CA;
		double resultVal = NCMVal;
		//Store the result within the MetricResult class ***Redefine this comment later
		MetricResult result = new MetricResult(this.identifier, resultVal);
		//log identifier + results
		clasAst.getResults().put(this.identifier, result);
	}
	
}
