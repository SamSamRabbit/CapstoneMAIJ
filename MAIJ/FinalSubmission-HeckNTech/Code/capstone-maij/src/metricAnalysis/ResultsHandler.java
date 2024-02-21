package metricAnalysis;

import java.util.Map;

import astManager.ASTHandler;
import astManager.ProjectAST;
import metricAnalysis.Metrics.Metric;

/**Class used to parse the results data into a useable format after the analysis has been run.
 * @author Jacob Botha
 *
 */
public class ResultsHandler {
	private ASTHandler astHandler;
	private AnalysisHandler analysisHandler; 
	private Map<String, Metric> projectMetrics;
	private Map<String, Metric> packageMetrics;
	private Map<String, Metric> classMetrics;
	private Map<String, Metric> methodMetrics;
	private String DELIMITER = ",";
	
	public ResultsHandler(ASTHandler handler, AnalysisHandler analyzer, 
			Map<String, Metric> methodMetrics, Map<String, Metric> classMetrics, 
			Map<String, Metric> packageMetrics, Map<String, Metric> projectMetrics) {
		
		this.astHandler = handler;
		this.analysisHandler = analyzer;
		this.methodMetrics = methodMetrics;
		this.classMetrics = classMetrics;
		this.packageMetrics = packageMetrics;
		this.projectMetrics = projectMetrics;
	}
	
	
	/**Gives the results of the class metrics as a CSV string
	 * @return
	 */
	public String getResultsAsCSVClass() {	
		String value;
		String csv = "";
		
		csv = csv.concat("Metric Identifier,Package Identifier,Class Identifier");
		for (ProjectAST projAST : astHandler.getAstStorage().values()) {
			csv = csv.concat(DELIMITER).concat(projAST.getIdentifier());
		}
		csv = csv.concat("\n");
		for(String identifier : classMetrics.keySet()) {
			for(String PID: astHandler.getAggregateHeirarchy().keySet()) {
				for (String CID : astHandler.getAggregateHeirarchy().get(PID).keySet()) {
						csv = csv.concat(identifier).concat(DELIMITER).concat(PID).concat(DELIMITER).concat(CID);
						for (ProjectAST projAST : astHandler.getAstStorage().values()) {
							value = null;
							try {
								value = projAST.getPackages().get(PID).getClasses().get(CID).getResults()
										.get(identifier).getValue().toString();
							} catch (Exception e) {}
							if (value == null)
								csv = csv.concat(DELIMITER);
							else
								csv = csv.concat(DELIMITER).concat(value.toString());
						}
						csv = csv.concat("\n");	
				}
			}

		}
		return csv;
		
	}
	
	/**Gives the results of the method metrics as a CSV String
	 * @return
	 */
	public String getResultsAsCSVMethod() {
		
		String value;
		String csv = "";
		
		csv = csv.concat("Metric Identifier,Package Identifier,Class Identifier,Method Identifier");
		for (ProjectAST projAST : astHandler.getAstStorage().values()) {
			csv = csv.concat(DELIMITER).concat(projAST.getIdentifier());
		}
		csv = csv.concat("\n");
		
		for(String identifier : methodMetrics.keySet()) {
			for(String PID: astHandler.getAggregateHeirarchy().keySet()) {
				for (String CID : astHandler.getAggregateHeirarchy().get(PID).keySet()) {
					for (String MID : astHandler.getAggregateHeirarchy().get(PID).get(CID)) {
						csv = csv.concat(identifier).concat(DELIMITER).concat(PID).concat(DELIMITER).concat(CID).concat(DELIMITER+"\"").concat(MID).concat("\"");
						for (ProjectAST projAST : astHandler.getAstStorage().values()) {
							value = null;
							try {
								value = projAST.getPackages().get(PID).getClasses().get(CID).getMethods().get(MID).getResults()
										.get(identifier).getValue().toString();
							} catch (Exception e) {}
							if (value == null)
								csv = csv.concat(DELIMITER);
							else
								csv = csv.concat(DELIMITER).concat(value.toString());
						}
						csv = csv.concat("\n");

					}
				}
			}
		}
		return csv;
	}
	
	/**Gives the resutls of the package level metrics as a CSV string
	 * @return
	 */
	public String getResultsAsCSVPackage() {
		String value;
		String csv = "";

		for(String identifier : packageMetrics.keySet()) {
			csv = csv.concat("Metric Identifier,Package Identifier");
			for (ProjectAST projAST : astHandler.getAstStorage().values()) {
				csv = csv.concat(DELIMITER).concat(projAST.getIdentifier());
			}
			csv = csv.concat("\n");
			
			for(String PID: astHandler.getAggregateHeirarchy().keySet()) {
						csv = csv.concat(identifier).concat(DELIMITER).concat(PID);
						for (ProjectAST projAST : astHandler.getAstStorage().values()) {
							value = null;
							try {
								value = projAST.getPackages().get(PID).getResults()
										.get(identifier).getValue().toString();
							} catch (Exception e) {}
							if (value == null)
								csv = csv.concat(DELIMITER);
							else
								csv = csv.concat(DELIMITER).concat(value.toString());
						}
						csv = csv.concat("\n");	
			}
		}
		return csv;
	}
	
	/**Gives the results of the Project level metrics as a CSV string
	 * @return
	 */
	public String getResultsAsCSVProject() {
		String value;
		String csv = "";

		for(String identifier : projectMetrics.keySet()) {
			
			csv = csv.concat("Metric Identifier");
			for (ProjectAST projAST : astHandler.getAstStorage().values()) {
				csv = csv.concat(DELIMITER).concat(projAST.getIdentifier());
			}
			csv = csv.concat("\n");
			csv = csv.concat(identifier);
			for (ProjectAST projAST : astHandler.getAstStorage().values()) {
				value = null;
				try {
					value = projAST.getResults()
							.get(identifier).getValue().toString();
				} catch (Exception e) {}
				if (value == null)
					csv = csv.concat(DELIMITER);
				else
					csv = csv.concat(DELIMITER).concat(value.toString());
			}
			csv = csv.concat("\n");	
			
			csv = csv.concat("\n\n");
		}
		return csv;
	}
	
	
	/**Give all the metric data in a string - not a pretty format
	 * @return
	 */
	public String getResultsAsString() {
		String value;
		String results = "PROJECT METRICS\n";
		
		for(String identifier : projectMetrics.keySet()) {
			results = results.concat(identifier.concat("\t\t\t\t"));
			for(ProjectAST projAST : astHandler.getAstStorage().values()) {
				value = null;
				try {
					value = projAST.getResults().get(identifier).getValue().toString();		
				} catch (Exception e) {}
				if(value == null) results = results.concat("null,\t");
				else results = results.concat(value.toString()).concat(",\t");
			}
			results = results.concat("\n");
		}
		results = results.concat("\n");
		
		results = results.concat("PACKAGE METRICS \n");
		for(String identifier : packageMetrics.keySet()) {
			results = results.concat(identifier.concat("\n"));
			for(String PID: astHandler.getAggregateHeirarchy().keySet()) {
				results = results.concat(PID.concat("\t\t\t\t"));
				for(ProjectAST projAST : astHandler.getAstStorage().values()) {
					value = null;
					try {
						value = projAST.getPackages().get(PID).getResults().get(identifier).getValue().toString();		
					} catch (Exception e) {}
					if(value == null) results = results.concat("null,\t");
					else results = results.concat(value.toString()).concat(",\t");
				}
				results = results.concat("\n");
			}
			results = results.concat("\n");
		}
		results = results.concat("\n");
		
		results = results.concat("CLASS METRICS \n");
		for(String identifier : classMetrics.keySet()) {
			results = results.concat(identifier.concat("\n"));
			for(String PID: astHandler.getAggregateHeirarchy().keySet()) {
				for (String CID : astHandler.getAggregateHeirarchy().get(PID).keySet()) {
					results = results.concat(PID.concat(".")).concat(CID).concat("\t\t\t\t");
					for (ProjectAST projAST : astHandler.getAstStorage().values()) {
						value = null;
						try {
							value = projAST.getPackages().get(PID).getClasses().get(CID).getResults().get(identifier).getValue().toString();
						} catch (Exception e) {}
						if (value == null)
							results = results.concat("null,\t");
						else
							results = results.concat(value.toString()).concat(",\t");
					}
					results = results.concat("\n");
				}
				results = results.concat("\n");
			}
		}
		results = results.concat("\n");
		
		results = results.concat("METHOD METRICS \n");
		for(String identifier : methodMetrics.keySet()) {
			results = results.concat(identifier.concat("\n"));
			for(String PID: astHandler.getAggregateHeirarchy().keySet()) {
				for (String CID : astHandler.getAggregateHeirarchy().get(PID).keySet()) {
					for (String MID : astHandler.getAggregateHeirarchy().get(PID).get(CID)) {
						results = results.concat(PID.concat(".")).concat(CID).concat(".").concat(MID).concat("\t\t\t\t");
						for (ProjectAST projAST : astHandler.getAstStorage().values()) {
							value = null;
							try {
								value = projAST.getPackages().get(PID).getClasses().get(CID).getMethods().get(MID).getResults()
										.get(identifier).getValue().toString();
							} catch (Exception e) {}
							if (value == null)
								results = results.concat("null,\t");
							else
								results = results.concat(value.toString()).concat(",\t");
						}
						results = results.concat("\n");
					}
				}
			}
			results = results.concat("\n");
		}
		results = results.concat("\n");
		
		return results;
	}
}
