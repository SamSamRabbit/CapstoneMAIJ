package gui.Controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import gui.MetricList;
import gui.SceneBuilderMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import metricAnalysis.Metrics.MetricLevel;

public class AnalysisController {

    @FXML
    private Button analyzeButton;

    @FXML
    private TextArea analasysDisplay;
    
    @FXML
    private BorderPane metricSelectionPane;
    
    @FXML
    private Button selectMetricButton;
    
    @FXML
    private MenuItem csvMethods;

    @FXML
    private MenuItem projectCSV;
    
    @FXML
    private MenuItem classCSV;

    @FXML
    private MenuItem packageCSV;

	private MetricList metricList;
	

	/**Saves the String as a CSV file, with the user having to pick a save location.
	 * @param results 
	 * @param fileName 
	 * 
	 */
	private void saveAsCSV(String results) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
		//fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV", "*.csv"));
    	File selection = fileChooser.showSaveDialog(SceneBuilderMain.primaryStage);
    	if(selection == null) return;
    	try {
			PrintWriter out = new PrintWriter(selection);
			out.print(results);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	
	}

	/**
     * Sets the selected metrics in the analysis Manager to be equal to the metrics selected with the checkboxes.
     */
    void confirmSelectedMetrics() {
    	SceneBuilderMain.analysisManager.cleanMetrics();
    	
    	for (Entry<String, Map<MetricLevel, CheckBox>> entry : metricList.getMetricCheckBoxList().entrySet()) {
    		for(MetricLevel level : entry.getValue().keySet()) {
    	    	if(entry.getValue().get(level).isSelected()) {
    	    		SceneBuilderMain.analysisManager.addMetric(entry.getKey(), level);   		
    	    	}
    		}
	    }
    }
	
    @FXML
    void exportClassCSV(ActionEvent event) {
    	SceneBuilderMain.analysisManager.runMetrics();
    	String results = SceneBuilderMain.analysisManager.getResultsHandler().getResultsAsCSVClass();
    	analasysDisplay.setText(results);
    	saveAsCSV(results);
    }

    @FXML
    void exportMethodsCSV(ActionEvent event) {
    	SceneBuilderMain.analysisManager.runMetrics();
    	String results = SceneBuilderMain.analysisManager.getResultsHandler().getResultsAsCSVMethod();
    	analasysDisplay.setText(results);
    	saveAsCSV(results);
    }

    @FXML
    void exportPackageCSV(ActionEvent event) {
    	SceneBuilderMain.analysisManager.runMetrics();
    	String results = SceneBuilderMain.analysisManager.getResultsHandler().getResultsAsCSVPackage();
    	analasysDisplay.setText(results);
    	saveAsCSV(results);
    }
	
    @FXML
	void exportProjectCSV(ActionEvent event) {
		SceneBuilderMain.analysisManager.runMetrics();
    	String results = SceneBuilderMain.analysisManager.getResultsHandler().getResultsAsCSVProject();
    	analasysDisplay.setText(results); 	
    	saveAsCSV(results);
	}

    @FXML
    void initialize() {
    	metricList = new MetricList();
        VBox metricSelect = metricList.getMetricCheckBoxes();
    	metricSelectionPane.setCenter(metricSelect);    	
    	for (Entry<String, Map<MetricLevel, CheckBox>> entry : metricList.getMetricCheckBoxList().entrySet()) {
    		for(MetricLevel level : entry.getValue().keySet()) {
	    		entry.getValue().get(level).setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e ) {
						confirmSelectedMetrics();
					}
				});
	    		
	    		if(SceneBuilderMain.analysisManager.metricIsSelected(entry.getKey(), level)) {
    				entry.getValue().get(level).setSelected(true);
    			}
    		}
	    }
    	
    	SceneBuilderMain.analysisManager.runMetrics();
    	String results = SceneBuilderMain.analysisManager.getResultsHandler().getResultsAsString();
    	analasysDisplay.setText(results);
    	
    }
    
    @FXML
    public void runAnalysis(ActionEvent event) { 	
    	if(
			SceneBuilderMain.analysisManager.getSelectedMetrics(MetricLevel.METHOD).isEmpty()
			&& SceneBuilderMain.analysisManager.getSelectedMetrics(MetricLevel.PACKAGE).isEmpty()
			&& SceneBuilderMain.analysisManager.getSelectedMetrics(MetricLevel.CLASS).isEmpty()
			&& SceneBuilderMain.analysisManager.getSelectedMetrics(MetricLevel.PROJECT).isEmpty()
			){
    		SceneBuilderMain.displayAlert("Invalid Selection", "Please select a metric to run", "You need at least one metric selected to run an analysis", AlertType.WARNING);
    		return;
    	}
    	SceneBuilderMain.analysisManager.runMetrics();
    	String results = SceneBuilderMain.analysisManager.getResultsHandler().getResultsAsString();
    	analasysDisplay.setText(results);
    }
}