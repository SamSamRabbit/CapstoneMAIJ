package unitTesting;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import astManager.ASTHandler;
import fileImport.SourceImportManager;
import metricAnalysis.AnalysisHandler;

class MetricTests {
	
	SourceImportManager importManager = new SourceImportManager();
	ASTHandler astHandler;
	AnalysisHandler analyzer;
	
	@Before
	void loadData() {
		try {
		importManager.selectImport("./testData");
		} catch (Exception e) {
			e.printStackTrace();
		}
		importManager.addVersion(1.0);
		astHandler = importManager.confirmImport();
		analyzer= new AnalysisHandler(astHandler);
		analyzer.runMetrics();
		System.out.print(analyzer.getResultsHandler().getResultsAsCSVMethod());
	}

	@Test
	void test() {
		//fail("Not yet implemented");
		//assertTrue()
	}

}