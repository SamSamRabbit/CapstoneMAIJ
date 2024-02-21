package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import metricAnalysis.AnalysisHandler;
import metricAnalysis.Metrics.MetricLevel;

/**List of checkboxes to display all the available metrics+level combinations.
 * @author Jacob Botha
 *
 */
public class MetricList {
	static Set<String> SelectedMetricsSet;
	static Map<String, Map<MetricLevel,CheckBox>> metricCheckBoxList = new HashMap<String, Map<MetricLevel,CheckBox>>();
	Set<String> metricNamesSet = AnalysisHandler.availableMetrics();
	VBox metricCheckBoxes;

	public MetricList() {
		VBox layout = new VBox();
		List<String> names = new ArrayList<String>(metricNamesSet);
		names.sort(null);
		for(String s : names) {
			metricCheckBoxList.put(s, new HashMap<MetricLevel, CheckBox>());
			for(MetricLevel level : AnalysisHandler.getLevels(s)) {
				CheckBox CB = new CheckBox(s.concat(level.toString()));
				CB.getStylesheets().add(this.getClass()
						.getResource("checkBoxStyle.css").toExternalForm());
				//String background = "-fx-background-color: #dcf6f6;";
				//CB.setStyle(background);
				metricCheckBoxList.get(s).put(level, CB);	
				layout.getChildren().add(metricCheckBoxList.get(s).get(level));
			}
		}
		layout.setPadding(new Insets(10));
		layout.setSpacing(10);
		this.metricCheckBoxes = layout;
	}

	/**
	 * @return the metricCheckBoxes
	 */
	public VBox getMetricCheckBoxes() {
		String style = "-fx-background-color: #4faec2, -fx-border-color: #4faec2;";
		metricCheckBoxes.setStyle(style);
		metricCheckBoxes.setMinWidth(500);
		metricCheckBoxes.setPrefHeight(1000);
		return metricCheckBoxes;
	}
	
	/**
	 * @return the metricCheckBoxList
	 */
	public Map<String, Map<MetricLevel,CheckBox>> getMetricCheckBoxList() {
		return metricCheckBoxList;
	}

}
