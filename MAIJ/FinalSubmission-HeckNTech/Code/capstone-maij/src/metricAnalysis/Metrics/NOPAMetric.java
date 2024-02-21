package metricAnalysis.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAccessModifiers;

import astManager.ClassAST;
import metricAnalysis.MetricResult;

/**Number of Public Attributes - counts anything that is public, methods, fields, constructors etc.
 * @author Jacob Botha
 *
 */
public class NOPAMetric extends Metric {	
	public NOPAMetric() {
		super();
		this.levels = Arrays.asList(MetricLevel.CLASS);
	}


	/* (non-Javadoc)
	 * @see metricAnalysis.internalMetrics.Metric#run(astManager.ClassAST)
	 */
	@Override
	public void run(ClassAST clasAst) {
		if(this.alreadyDone(clasAst)) return;
		
		Integer value = 0;
		ArrayList<Range> poi = new ArrayList<Range>();
		
		
		TypeDeclaration<?> n = clasAst.getNode();
		for(BodyDeclaration<?> x : n.getMembers()) {
			if(x instanceof NodeWithAccessModifiers) {
				EnumSet<Modifier> mods = ((NodeWithAccessModifiers<?>) x).getModifiers();
				if(mods.contains(Modifier.PUBLIC)) {
					value++;
					poi.add(x.getRange().get());
				}
			}
		}			
			
		MetricResult result = new MetricResult( identifier, (double) value, poi);
		clasAst.getResults().put(identifier, result);
	}

}
