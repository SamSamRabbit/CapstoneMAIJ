package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAccessModifiers;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

public class CAAIMetric extends Metric {
	
	ArrayList<String> CAList = new ArrayList<String>();
	ArrayList<MethodDeclaration> AMList = new ArrayList<MethodDeclaration>();
	
	public CAAIMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}
	
	private double CAVal(TypeDeclaration<?> n) {
		
		double ca = 0.0;
		
		for (FieldDeclaration x : n.getFields()) {
			if(x.isAnnotationPresent(secrecy.class)) {
				CAList.add(x.getVariables().toString().substring(1, x.getVariables().toString().length()-2));
				ca++;
			}
		}
		return ca;
	}
	
	private double AMVal(TypeDeclaration<?> n) {
		
		double am = 0.0;
		for (MethodDeclaration x : n.getMethods()) {
			if (x.getName().toString().contains("Get") || x.getName().toString().contains("get")) {
				AMList.add(x);
				am++;
			}
		}
		return am;
	}
	
	private double ACAVal(TypeDeclaration<?> n) {
		double aca = 0.0;

		for (MethodDeclaration x : AMList) {
			for (String s : CAList) {
				if (x.getChildNodes().toString().contains(s)) {
					aca++;
				}
			}
		}
		
		return aca;
	}

	
	
	public void run(ClassAST clasAst) {
		
		if(this.alreadyDone(clasAst)) return;
		
		CAList.clear();
		AMList.clear();
		
		TypeDeclaration<?> n = clasAst.getNode();
		
		double am = AMVal(n);
		double ca = CAVal(n);
		double caai = ACAVal(n) / (am * ca);
		
		MetricResult result = new MetricResult(this.identifier, caai);
		clasAst.getResults().put(this.identifier, result);
	}
	
	
}
