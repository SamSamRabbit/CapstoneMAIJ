package metricAnalysis.Metrics;

import java.util.Arrays;


import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

/*
 * Author: Sam
 */

/*
	Classified Class Data Accessibility
*/
public class CCDAMetric extends Metric {
	
	public CCDAMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}
	
	private double CAVal(TypeDeclaration<?> n) {
		double classifiedVal = 0.0;
		for(FieldDeclaration x : n.getFields()) if(x.isAnnotationPresent("underline")) classifiedVal++;
		return classifiedVal;
	}
	
	private double NCCAVal(TypeDeclaration<?> n) {
		double classifiedVal = 0.0;
		for(FieldDeclaration x : n.getFields()) if(x.isAnnotationPresent("underline") && x.isStatic() && x.isPublic()) classifiedVal++;
		return classifiedVal;
	}

	@Override
	public void run(ClassAST classAst) {
		
		if (this.alreadyDone(classAst)) {
			return;
		}
		
		TypeDeclaration<?> n = classAst.getNode();

		double CA = CAVal(n);
		double NCCA = NCCAVal(n);
		double resultVal = NCCA / CA;

		MetricResult result = new MetricResult(this.identifier, resultVal);
		classAst.getResults().put(this.identifier, result);		
	}
}
