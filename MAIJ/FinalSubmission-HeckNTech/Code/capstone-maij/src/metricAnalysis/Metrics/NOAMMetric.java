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

/**Number of Accessor Methods - Checks how many accessor methods (getters, setters)
 * there are in a class - currently implemented based on naming conventions.
 * @author Jacob Botha
 */
public class NOAMMetric extends Metric {

	public NOAMMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}

	/**Check whether the given method is an accessor method using naming conventions
	 * Has to start with get, set or is and have the rest of the name be identical
	 * to a field in the class.
	 * @param x The method declaration to be checked
	 * @param n The containing Class node
	 * @return true if the method name matches a getter/setter name, false otherwise
	 */
	private boolean isAccessorMethod(MethodDeclaration x, TypeDeclaration<?> n) {
		String name = x.getNameAsString();
		String fieldName;
		if ((name.startsWith("get") || name.startsWith("set")) && name.length() > 3) {
			fieldName = name.substring(3);
		} else if (name.startsWith("is") && name.length() > 2) {
			fieldName = name.substring(2);
		} else {
			return false;
		}
		
		
		/*
		 * The field declaration covers the whole line - modefier and everything.
		 * The variable Declarator covers the type name = value; part
		 * The variable name is the name part.
		 */
		for(FieldDeclaration field : n.getFields()) {
			for(VariableDeclarator var : field.getVariables()) {
				if(var.getNameAsString().toLowerCase().equals(fieldName.toLowerCase())) {
					return true;
				}
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.ClassAST)
	 */
	@Override
	public void run(ClassAST clasAst) {
		if(this.alreadyDone(clasAst)) return;
		
		Integer value = 0;
		ArrayList<Range> poi = new ArrayList<Range>();
		
		
		/*
		 * Working directly with the node for this one.
		 * Can get the methods directly. Only interested in public methods and need
		 * to check them for being accessor Methods.
		 */
		TypeDeclaration<?> n = clasAst.getNode();
		for(MethodDeclaration x : n.getMethods()) {
			if(x instanceof NodeWithAccessModifiers) {
				EnumSet<Modifier> mods = ((NodeWithAccessModifiers<?>) x).getModifiers();
				if(mods.contains(Modifier.PUBLIC) && isAccessorMethod(x, n)) {
					value++;
					poi.add(x.getRange().get());
				}

			}
		}			
			
		MetricResult result = new MetricResult( identifier, (double)value, poi);
		clasAst.getResults().put(identifier, result);
	}
}
