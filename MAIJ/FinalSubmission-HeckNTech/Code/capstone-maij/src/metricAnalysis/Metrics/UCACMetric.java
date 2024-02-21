package metricAnalysis.Metrics;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import astManager.ClassAST;
import metricAnalysis.MetricResult;
import java.util.Arrays;
import java.util.List;

public class UCACMetric extends Metric{

	public UCACMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}
	
	//“The ratio of the number of classes which contain classified methods that access classified attributes but are
	//never used by other classes to the total number of critical classes in the program”
	
	//methods to use
	//determine whether a class contains Classified methods that access classified attributes, but is never used
	//by other classes
	public double getClassifiedMethods() {
		
		return 0.0;
	}
	
	//end the methods used
	public void run(ClassAST clasAst) {
		if(this.alreadyDone(clasAst)) return;
	
		TypeDeclaration<?> n = clasAst.getNode();
		//getVariables().get(0).getName().toString()
		//Class<? extends TypeDeclaration>
		Class<? extends TypeDeclaration> clss = n.getClass();
		
		double resultVal = 20.0;
		MetricResult result = new MetricResult(this.identifier, resultVal);
		clasAst.getResults().put(this.identifier, result);
	}
}
