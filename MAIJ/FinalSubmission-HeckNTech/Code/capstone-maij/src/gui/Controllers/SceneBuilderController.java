package gui.Controllers;

import java.io.IOException;

import astManager.InvalidOperationException;
import gui.SceneBuilderMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import metricAnalysis.Metrics.MetricLevel;

public class SceneBuilderController {
	
	private String aboutText = "This is a tool used to run metric analysis on java project, with the "
			+ "emphasis being running the same analysis across multiple versions of "
			+ "same project. \n"
			+ "\n"
			+ "Start by importing the project versions, then clicking on the confirm button. "
			+ "From here you may select the metrics and generate the data in the analysis tab "
			+ "where you may also export the data as csv files. Once the analysis is run you may "
			+ "view the data in the Overview tab. The project tab lets you browse through the source "
			+ "code and if the data was generated lets you view points of interest for metrics that support "
			+ "it.";	

    @FXML
    private MenuItem aboutMenuButton;
	
    @FXML
    private Button analysisButton;
       
    @FXML
    private Button projectButton;

	@FXML
    private BorderPane mainView;
	
    @FXML
    private Button dashboardButton;
	
    public SceneBuilderController() {
    	
    }
    
    @FXML
    void initialize() throws IOException {
    	SceneBuilderMain.mainView = mainView;
    	SceneBuilderMain.switchSceneImportConent();
    }
    
    @FXML
    void showAbout(ActionEvent event) {
    	String headerText = "MAIJ - Metric Analysis and Identification for Java";
    	String title = "About This Program";
    	AlertType alertType = AlertType.INFORMATION;
    	String contentText = aboutText;
    	  	
		SceneBuilderMain.displayAlert(title, headerText, contentText, alertType);
    }
    
    @FXML
    void switchToAnalysis(ActionEvent event) throws IOException {
    	if(SceneBuilderMain.analysisManager == null) {
    		SceneBuilderMain.displayAlert("No Source Imported", "No Source Code has been imported", "Please hit the confirm button to import the source code.", AlertType.WARNING);
    		return;
    	}
    	SceneBuilderMain.switchSceneAnalysis();
    }
    
    @FXML
    void switchToDashboard(ActionEvent event) throws IOException {
    	if(SceneBuilderMain.astManager == null) {
    		SceneBuilderMain.displayAlert("No Source Imported", "No Source Code has been imported", "Please hit the confirm button to import the source code.", AlertType.WARNING);
    		return;
    	}
    	if(SceneBuilderMain.analysisManager == null) {
    		SceneBuilderMain.displayAlert("No Analysis Run Yet", "No analsys has been run", "Please run an analysis for the selected metrics to generate data", AlertType.WARNING);
    		return;
    	}
    	if(SceneBuilderMain.analysisManager.getSelectedMetrics(MetricLevel.PROJECT).isEmpty()
    			&& SceneBuilderMain.analysisManager.getSelectedMetrics(MetricLevel.PACKAGE).isEmpty()
    			&& SceneBuilderMain.analysisManager.getSelectedMetrics(MetricLevel.CLASS).isEmpty()
    			&& SceneBuilderMain.analysisManager.getSelectedMetrics(MetricLevel.METHOD).isEmpty()
    			) {
    		SceneBuilderMain.displayAlert("No Metrics Selected", "Please Select a Metric", "You need to have a metric selected and an analysis run in the Metric"
        			+ " Analysis tab before you may look at the data.", AlertType.WARNING);
    		return;
    	}
    	SceneBuilderMain.analysisManager.runMetrics();
    	SceneBuilderMain.switchSceneDashboard();
    }

    @FXML
    void switchToProjectExplorer(ActionEvent event) throws IOException, InvalidOperationException {
    	if(SceneBuilderMain.astManager == null) {
    		SceneBuilderMain.displayAlert("No Source Imported", "No Source Code has been imported", "Please hit the confirm button to import the source code.", AlertType.WARNING);
    		return;
    	}
    	SceneBuilderMain.switchSceneImported();
    }
}




