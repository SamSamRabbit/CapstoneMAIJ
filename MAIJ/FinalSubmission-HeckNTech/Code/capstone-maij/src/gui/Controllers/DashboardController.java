package gui.Controllers;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import astManager.ProjectAST;
import gui.SceneBuilderMain;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import metricAnalysis.Metrics.MetricLevel;

public class DashboardController {

    @FXML
    private TableView<Map.Entry<String, Map<Double, Double>>> resultsTable;

    @FXML
    private ComboBox<MetricLevel> levelSelector;

    @FXML
    private ComboBox<String> metricSelector;
    
    @FXML
    private TableColumn<Map.Entry<String, Map<Double, Double>>, String> identifierColumn;
    
    private SortedMap<Double, TableColumn<Map.Entry<String, Map<Double, Double>>, String>> valueColumns;
    
    /**returns the double as a string or returns 'null' if the value is null
     * @param value
     * @return null if value==null otherwise value.toString()
     */
    private String stringOrNull(Double value) {
    	if(value == null) return "null";
    	else return value.toString();
    }
    
    /**
	 * Populates the table with data fromt he selected metric.
	 */
	private void updateTable() {
		String identifier = metricSelector.getValue();
		MetricLevel level = levelSelector.getValue();
		Double value = null;
		
		TreeMap<String, Map<Double, Double>> display = new TreeMap<String,Map<Double, Double>>();
		
		if(level == MetricLevel.PROJECT) {
			for(String id : SceneBuilderMain.analysisManager.getSelectedMetrics(level)) {
				display.put(id, new TreeMap<Double, Double>());
				for(ProjectAST projAST : SceneBuilderMain.astManager.getAstStorage().values()) {
					value = null;
					try {
						value = projAST.getResults().get(id).getValue();
					} catch (Exception e) {}	
					display.get(id).put(projAST.getVersion(), value);
				}
			}
			resultsTable.setItems(FXCollections.observableArrayList(display.entrySet()));
		}	
		
		if(identifier == null || !SceneBuilderMain.analysisManager.metricIsSelected(identifier, level)) {
			resultsTable.setItems(FXCollections.observableArrayList(display.entrySet()));
			return;
		}
		
		if(level == MetricLevel.PACKAGE) {
			for(String PID : SceneBuilderMain.astManager.getAggregateHeirarchy().keySet()) {
				display.put(PID, new TreeMap<Double, Double>());
				for(ProjectAST projAST : SceneBuilderMain.astManager.getAstStorage().values()) {
					value = null;
					try {
						value = projAST.getPackages().get(PID).getResults().get(identifier).getValue();
					} catch (Exception e) {}	
					display.get(PID).put(projAST.getVersion(), value);
				}
			}
		}	
		
		if(level == MetricLevel.CLASS) {
			for(String PID : SceneBuilderMain.astManager.getAggregateHeirarchy().keySet()) {
				for(String CID : SceneBuilderMain.astManager.getAggregateHeirarchy().get(PID).keySet()) {
					display.put(PID.concat(".").concat(CID), new TreeMap<Double, Double>());
					for(ProjectAST projAST : SceneBuilderMain.astManager.getAstStorage().values()) {
						value = null;
						try {
							value = projAST.getPackages().get(PID).getClasses().get(CID).getResults().get(identifier).getValue();
						} catch (Exception e) {}	
						display.get(PID.concat(".").concat(CID)).put(projAST.getVersion(), value);
					}
				}		
			}
		}
		
		if(level == MetricLevel.METHOD) {
			for(String PID : SceneBuilderMain.astManager.getAggregateHeirarchy().keySet()) {
				for(String CID : SceneBuilderMain.astManager.getAggregateHeirarchy().get(PID).keySet()) {
					for(String MID : SceneBuilderMain.astManager.getAggregateHeirarchy().get(PID).get(CID)) {
						display.put(PID.concat(".").concat(CID).concat(".").concat(MID), new TreeMap<Double, Double>());
						for(ProjectAST projAST : SceneBuilderMain.astManager.getAstStorage().values()) {
							value = null;
							try {
								value = projAST.getPackages().get(PID).getClasses().get(CID).getMethods().get(MID).getResults().get(identifier).getValue();
							} catch (Exception e) {}
							display.get(PID.concat(".").concat(CID).concat(".").concat(MID)).put(projAST.getVersion(), value);
						}
					}
				}		
			}
		}
		
		
		resultsTable.setItems(FXCollections.observableArrayList(display.entrySet()));
	}
    
    @FXML
    void displayInTable(ActionEvent event) {
    	updateTable();
    }

	@FXML
    void initialize() {	
    	valueColumns = new TreeMap<Double, TableColumn<Map.Entry<String, Map<Double, Double>>, String>>();
    	for(Double version : SceneBuilderMain.astManager.getAstStorage().keySet()) {
    		valueColumns.put(version, new TableColumn<Map.Entry<String, Map<Double, Double>>, String>("Version ".concat(version.toString())));
    		resultsTable.getColumns().add(valueColumns.get(version));
    		valueColumns.get(version).setCellValueFactory(
					cellData -> new SimpleStringProperty(stringOrNull(cellData.getValue().getValue().get(version))));
    	}
    	
    	identifierColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
    	
    	levelSelector.setItems(FXCollections.observableArrayList(MetricLevel.values()));
    	levelSelector.getSelectionModel().select(0);
    	
    	updateMetricSelection();
    	 
    }
	
    @FXML
    void levelSelected(ActionEvent event) {
    	updateMetricSelection();
    }

	/**
	 * Updates the metric Selector combobox to have values consistent with the metric level and 
	 * the metrics selected in the analysis tab.
	 */
	public void updateMetricSelection() {
		MetricLevel metricLevel = levelSelector.getSelectionModel().getSelectedItem();
		if(metricLevel == MetricLevel.PROJECT) {
			metricSelector.setDisable(true);
			if(SceneBuilderMain.analysisManager.getSelectedMetrics(metricLevel).isEmpty()) {
				metricSelector.setPromptText("No Metrics Available For This Level");
			}else {
				metricSelector.setPromptText("Showing All Metrics, no need to select");
			}
			metricSelector.getSelectionModel().clearSelection();
		}else {
			metricSelector.setDisable(false);
			metricSelector.setPromptText("Please Select a Metric");
			List<String> metrics = SceneBuilderMain.analysisManager.getSelectedMetrics(metricLevel);
			metricSelector.setItems(FXCollections.observableList(metrics));
			if(metrics.isEmpty()) {
				metricSelector.setPromptText("No Metrics Available for this level");
			}
		}

		updateTable();
	}

}