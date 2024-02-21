package metricAnalysis.Metrics;

import java.util.Arrays;
import java.util.ArrayList;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

public class CMAIMetric extends Metric {
	
	ArrayList<String> CAList = new ArrayList<String>();
	ArrayList<MethodDeclaration> MMList = new ArrayList<MethodDeclaration>();
	
	public CMAIMetric() {
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

	private double MMVal(TypeDeclaration<?> n) {
		double mm = 0.0;
		for (MethodDeclaration x : n.getMethods()) {
			if (x.getName().toString().contains("Set") || x.getName().toString().contains("set")) {
				MMList.add(x);
				mm++;
			}
		}
		return mm;
	}
	
	private double ACAVal(TypeDeclaration<?> n) {
		double aca = 0.0;

		for (MethodDeclaration x : MMList) {
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
		MMList.clear();
		
		//System.out.println("Current path: " + clasAst.getNode().findCompilationUnit().get().getStorage().get().getPath());
		TypeDeclaration<?> n = clasAst.getNode();
		
		double mm = MMVal(n);
		double ca = CAVal(n);
		double cmai = ACAVal(n) / (mm * ca);
		
		MetricResult result = new MetricResult(this.identifier, cmai);
		clasAst.getResults().put(this.identifier, result);
	}
		
}
