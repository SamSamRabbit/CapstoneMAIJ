package metricAnalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import astManager.ASTHandler;
import astManager.ClassAST;
import astManager.MethodAST;
import astManager.PackageAST;
import astManager.ProjectAST;
import metricAnalysis.Metrics.*;

/**
 * Access point for everything related to choosing source input, choosing
 * metrics, running the metrics and putting the result in a usable format.
 * 
 * @author Jacob Botha
 *
 */
public class AnalysisHandler {
	private static final Map<String,Class<?>> registeredMetrics;
	static {
		Map<String, Class<?>> aMap = new HashMap<String, Class<?>>();
		aMap.put(new ATFDMetric().getIdentifier(), ATFDMetric.class);
		aMap.put(new CycloMetric().getIdentifier(), CycloMetric.class);
		aMap.put(new LOCMetric().getIdentifier(), LOCMetric.class);
		aMap.put(new MaxNestingMetric().getIdentifier(), MaxNestingMetric.class);
		aMap.put(new NOPAMetric().getIdentifier(), NOPAMetric.class);
		aMap.put(new NOSMetric().getIdentifier(), NOSMetric.class);
		aMap.put(new NOAMMetric().getIdentifier(), NOAMMetric.class);
		aMap.put(new FanOutMetric().getIdentifier(), FanOutMetric.class);
		aMap.put(new CAIWMetric().getIdentifier(), CAIWMetric.class);
		aMap.put(new CCDAMetric().getIdentifier(), CCDAMetric.class);
		aMap.put(new Checker().getIdentifier(), Checker.class);
		aMap.put(new CIDAMetric().getIdentifier(), CIDAMetric.class);
		aMap.put(new CMWMetric().getIdentifier(), CMWMetric.class);
		aMap.put(new COAMetric().getIdentifier(), COAMetric.class);
		aMap.put(new UACAMetric().getIdentifier(), UACAMetric.class);
		aMap.put(new UCAMMetric().getIdentifier(), UCAMMetric.class);
		aMap.put(new CSPMetric().getIdentifier(), CSPMetric.class);
		aMap.put(new CDPMetric().getIdentifier(), CDPMetric.class);
		aMap.put(new CAIMetric().getIdentifier(), CAIMetric.class);
		aMap.put(new CCEMetric().getIdentifier(), CCEMetric.class);
		aMap.put(new CSCPMetric().getIdentifier(), CSCPMetric.class);
		aMap.put(new CCCMetric().getIdentifier(), CCCMetric.class);
		aMap.put(new CMIMetric().getIdentifier(), CMIMetric.class);
		aMap.put(new CCPCMetric().getIdentifier(), CCPCMetric.class);
		aMap.put(new CSIMetric().getIdentifier(), CSIMetric.class);
		aMap.put(new CWMPMetric().getIdentifier(), CWMPMetric.class);
		//Register new metrics here in the same format as Above
	
		registeredMetrics = Collections.unmodifiableMap(aMap);
	}
	private ASTHandler astHandler; 
	private ResultsHandler resultsHandler;
	private Map<String, Metric> projectMetrics;
	private Map<String, Metric> packageMetrics;
	private Map<String, Metric> classMetrics;
	private Map<String, Metric> methodMetrics;
	private boolean done;
	
	/**Creates a new instance of the metric with the given identifier
	 * @param identifier
	 * @return new instance of the Metric object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static Metric createMetric(String identifier) throws InstantiationException, IllegalAccessException {
		return (Metric) registeredMetrics.get(identifier).newInstance();
	}

	/**gives the identifiers of the available metrics.
	 * @return Set containing the identifiers of the available metrics.
	 */
	static public Set<String> availableMetrics() {
		return registeredMetrics.keySet();
	}

	/**Give the list of applicable level that this metric may be applied to.
	 * @param identifier
	 * @return the list of levels (MetricLevel constants)
	 */
	public static List<MetricLevel> getLevels(String identifier){
		try {
			return createMetric(identifier).getLevels();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<MetricLevel>();
	}
	
	/**
	 * Basic Constructor
	 */
	public AnalysisHandler(ASTHandler handler) {
		this.astHandler = handler;
		this.methodMetrics = new HashMap<String, Metric>();
		this.classMetrics = new HashMap<String, Metric>();
		this.packageMetrics = new HashMap<String, Metric>();
		this.projectMetrics = new HashMap<String, Metric>();
		this.resultsHandler = new ResultsHandler(handler, this,
				this.methodMetrics, this.classMetrics, this.packageMetrics, this.projectMetrics);
		this.done = false;
	}
	
	/**Add the metric with the given identifier to the metrics to run at the class level
	 * @param identifier
	 */
	private void addClassMetric(String identifier) {
		try {
			this.classMetrics.put(identifier, AnalysisHandler.createMetric(identifier));
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDone(false);
	}
	
	/**Add the metric with the given identifier to the metrics to run at the method level
	 * @param identifier
	 */
	private void addMethodMetric(String identifier) {
		try {
			this.methodMetrics.put(identifier,AnalysisHandler.createMetric(identifier));
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDone(false);
	}
	
	/**Add the metric with the given identifier to the metrics to run at the package level
	 * @param identifier
	 */
	private void addPackageMetric(String identifier) {
		try {
			this.packageMetrics.put(identifier,AnalysisHandler.createMetric(identifier));
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDone(false);
	}
	
	/**Add the metric with the given identifier to the metrics to run at the project level
	 * @param identifier
	 */
	private void addProjectMetric(String identifier) {
		try {
			this.projectMetrics.put(identifier,AnalysisHandler.createMetric(identifier));
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDone(false);
	}
	
	/**removes the class metric with the given identifier - will no longer be run
	 * @param identifier
	 */
	private void removeClassMetric(String identifier) {
		this.classMetrics.remove(identifier);
	}
	
	/**removes the method metric with the given identifier - will no longer be run
	 * @param identifier
	 */
	private void removeMethodMetric(String identifier) {
		this.methodMetrics.remove(identifier);
	}
	
	/**removes the package metric with the given identifier - will no longer be run
	 * @param identifier
	 */
	private void removePackageMetric(String identifier) {
		this.packageMetrics.remove(identifier);
	}
	
	/**removes the project metric with the given identifier - will no longer be run
	 * @param identifier
	 */
	private void removeProjectMetric(String identifier) {
		this.projectMetrics.remove(identifier);
	}
	
	/**Runs the selected class level metrics - those in this.classMetrics
	 * @param clasAst
	 */
	private void runClassLevelMetrics(ClassAST clasAst) {
		
		for(Metric metric : classMetrics.values()) {
			metric.run(clasAst);
		}
	}
	
	/**runs the selected method level metrics - thos in this.methodMetrics
	 * @param methAst
	 */
	private void runMethodLevelMetrics(MethodAST methAst) {
		for(Metric metric : methodMetrics.values()) {
			metric.run(methAst);
		}
	}
	
	/**Runs the selected package levels metrics - those  in this.packageMetrics
	 * @param packAst
	 */
	private void runPackageLevelMetrics(PackageAST packAst) {
		for(Metric metric : packageMetrics.values()) {
			metric.run(packAst);
		}
	}
	
	/**Runs the selected project level metrics - those in this.projectMetrics
	 * @param projAst
	 */
	private void runProjectLevelMetrics(ProjectAST projAst) {
		for(Metric metric : projectMetrics.values()) {
			metric.run(projAst);

		}
	}
	
	/**Add the given metric at the given level so that it will be run at the level
	 * during runAnalysis()
	 * @param identifier
	 * @param level
	 */
	public void addMetric(String identifier, MetricLevel level) {
		if(level == MetricLevel.METHOD) addMethodMetric(identifier);
		if(level == MetricLevel.CLASS) addClassMetric(identifier);
		if(level == MetricLevel.PACKAGE) addPackageMetric(identifier);
		if(level == MetricLevel.PROJECT) addProjectMetric(identifier);
		this.setDone(false);
	}
	
	/**
	 * Empties the selected metrics so that no metrics will be run during 
	 * runAnalysis()
	 */
	public void cleanMetrics() {
		projectMetrics.clear();
		packageMetrics.clear();
		classMetrics.clear();
		methodMetrics.clear();
	}

	/**Returns the attached results handler
	 * @return
	 */
	public ResultsHandler getResultsHandler() {
		return this.resultsHandler;
	}
	
	/**Returns the identifiers of all the metrics selected for the given level
	 * those that will be run during runAnalysis();
	 * @param level
	 * @return The list of metric identifiers
	 */
	public List<String> getSelectedMetrics(MetricLevel level){
		if(level == MetricLevel.METHOD) return new ArrayList<String>(methodMetrics.keySet());
		if(level == MetricLevel.CLASS) return new ArrayList<String>(classMetrics.keySet());
		if(level == MetricLevel.PACKAGE) return new ArrayList<String>(packageMetrics.keySet());
		if(level == MetricLevel.PROJECT) return new ArrayList<String>(projectMetrics.keySet());
		return null;
	}

	/**Whether the selected metric have been run
	 * @return the done
	 */
	public boolean isDone() {
		return done;
	}

	/** Check whether the given metric is selected at the given level
	 * @param identifier
	 * @param level
	 * @return true if it is selected, false otherwise
	 */
	public boolean metricIsSelected(String identifier, MetricLevel level) {
		if(level == MetricLevel.METHOD) return methodMetrics.containsKey(identifier);
		if(level == MetricLevel.CLASS) return classMetrics.containsKey(identifier);
		if(level == MetricLevel.PACKAGE) return packageMetrics.containsKey(identifier);
		if(level == MetricLevel.PROJECT) return projectMetrics.containsKey(identifier);
		return false;
	}
	
	/** Removes the given metric from the given level
	 * @param identifier
	 * @param level
	 */
	public void removeMetric(String identifier, MetricLevel level) {
		if(level == MetricLevel.METHOD) removeMethodMetric(identifier);
		if(level == MetricLevel.CLASS) removeClassMetric(identifier);
		if(level == MetricLevel.PACKAGE) removePackageMetric(identifier);
		if(level == MetricLevel.PROJECT) removeProjectMetric(identifier);
	}

	/**
	 * Runs the chosen metrics on the chosen input. For now runs Lines of Code
	 * metric on all Compilation Units.
	 */
	public void runMetrics() {	
		for(ProjectAST projAst: astHandler.getAstStorage().values()) {
			for(PackageAST packAst: projAst.getPackages().values()) {
				for(ClassAST clasAst: packAst.getClasses().values()) {
					for(MethodAST methAst: clasAst.getMethods().values()) {
						runMethodLevelMetrics(methAst);
					}
					runClassLevelMetrics(clasAst);
				}
				runPackageLevelMetrics(packAst);
			}
			runProjectLevelMetrics(projAst);
		}
		this.setDone(true);
	}
	
	/**
	 * @param done the done to set
	 */
	public void setDone(boolean done) {
		this.done = done;
	}
}
