package gui.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;

import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;

import astManager.BaseAST;
import astManager.Breadcrumb;
import astManager.ClassAST;
import astManager.InvalidOperationException;
import astManager.MethodAST;
import astManager.PackageAST;
import astManager.ProjectAST;
import gui.SceneBuilderMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import metricAnalysis.MetricResult;

public class SceneImportedController {

	@FXML
    private TreeView<BaseAST> heirarchyView;
    
    private InlineCssTextArea heirarchySourceText;
    private VirtualizedScrollPane<InlineCssTextArea> heirarchyScroll;
    
    @FXML
    private CheckBox displayToggel;

    @FXML
    private ComboBox<String> metricSelector;
    
    
    
    @FXML
    private BorderPane textPane;
    
    @FXML
    private ComboBox<ProjectAST> versionSelector;

	private ObservableList<String> metricSelectorList;
    
    /**method to convert the ranges stored in the MetricResult points of interest to the
	 * format int[] start, length.
	 * @param ranges List of ranges as in MetriResult pointsOfInterest.
	 * @param filePath File containing the node as a string.
	 * @return int array [start, length, start2, length2, ...] of ranges.
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public static int[] rangesToIntArray(List<Range> ranges, Path filePath) throws UnsupportedEncodingException, IOException {
		int length = 0, index = 0;
		int[] returnRanges = new int[ranges.size()*2];
		List<Range> tempRanges = new ArrayList<Range>(ranges);
		Comparator<Range> comparator = (Range range1, Range range2) -> {
			if(range1.begin.isBefore(range2.begin)) {
				return -1;
			} else if (range1.begin.isAfter(range2.begin)) {
				return 1;
			} else if(range1.end.isBefore(range2.end)) {
				return 1;
			} else if (range1.end.isAfter(range2.end)) {
				return -1;
			} else {
				return 0;
			}
		};
		tempRanges.sort(comparator);
		
	
		length = 0;
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath), "UTF8"));
		List<String> lines = new ArrayList<String>();
		String line = bufferReader.readLine();
		while(line != null) {
			lines.add(line.concat("\n"));
			line = bufferReader.readLine();
		}
			
		int[] charsToLine = new int[lines.size()];
		
		for(int i = 0; i < lines.size(); i++) {		
			charsToLine[i] = length;
			length += lines.get(i).length();
		}
		
		for(Range range : tempRanges) {
			returnRanges[index] = charsToLine[range.begin.line-1] + range.begin.column-1;
			returnRanges[index+1] = charsToLine[range.end.line-1] + range.end.column;		
			index += 2;
		}
		bufferReader.close();
		return returnRanges;
	}
    
    
    
    public SceneImportedController() {
    }



	/**
	 * Highlight the ranges for the points of interest if the Checkbox is checked and a metric is selected
	 */
	private void displayPOI() {
		if(displayToggel.isSelected() && metricSelector.getSelectionModel().getSelectedItem() != null) {
    		if(heirarchyView.getSelectionModel().isEmpty()) return;
        	BaseAST selectedAST = heirarchyView.getSelectionModel().getSelectedItem().getValue();
        	MetricResult result = selectedAST.getResults().get(metricSelector.getSelectionModel().getSelectedItem());
        	heirarchySourceText.clearStyle(0, heirarchySourceText.getText().length());
        	Path filePath;
        	String highlight = "-rtfx-background-color: aqua;";
        	String alternate = switchAlternateStyle("");
        	String style;
        	
			try {
				filePath = selectedAST.getNode().findCompilationUnit().get().getStorage().get().getPath();
				int[] intRange = SceneImportedController.rangesToIntArray(result.getPointsOfInterest(),filePath);
				List<Integer> ends = new ArrayList<Integer>();
				int begin, end;
	        	for(int i = 0; i < intRange.length; i +=2) {
	        		final Integer index = new Integer(i);
	        		
	        		begin = (int) (intRange[i]+ ends.stream().filter(p -> p < intRange[index]).count()*4);
	        		end = (int) (intRange[i+1]+ ends.stream().filter(p -> p < intRange[index+1]).count()*4);
	        		
					if(heirarchySourceText.getStyleAtPosition(begin).contains("aqua") || heirarchySourceText.getStyleAtPosition(begin).contains("yellow")) {
	        			style = alternate;
	        			alternate = switchAlternateStyle(alternate);        			
	        		}else {			
	        			style = highlight;
		        		highlight = switchStyle(highlight);
	        		}
					heirarchySourceText.setStyle(begin, end, style);
	        		heirarchySourceText.insertText(end, "    ");
        			heirarchySourceText.setStyle(end, end+3, style + " -rtfx-border-stroke-width: 1; -rtfx-border-stroke-color: black;");
        			ends.add(intRange[i+1]+1);
	        	}
			} catch (InvalidOperationException | IOException e) {
				
			}
    		
    	}
	}



	/**Displays the source code of the selected node in the text area
	 * @throws IOException
	 * @throws InvalidOperationException
	 */
	private void displaySourceCode() throws IOException, InvalidOperationException {
		String text = "";
		heirarchySourceText.clear();
    	heirarchySourceText.clearStyle(0, 0);
    	if(heirarchyView.getSelectionModel().isEmpty()) return;
    	BaseAST selectedAST = heirarchyView.getSelectionModel().getSelectedItem().getValue();
    	if(selectedAST.isPackageAST()) {
    		CompilationUnit cu = ((PackageAST) selectedAST).getPackageInfo();
    		if( cu != null) {
    			Path filePath = cu.getStorage().get().getPath();
        		text = readFileToString(filePath);
        		heirarchySourceText.appendText(text);
    		}
    	}else if(selectedAST.hasNode()) {   	
    		Path filePath = selectedAST.getNode().findCompilationUnit().get().getStorage().get().getPath();
    		text = readFileToString(filePath);
    		heirarchySourceText.appendText(text);
    		
    		ArrayList<Range> ranges = new ArrayList<Range>();
    		ranges.add(selectedAST.getNode().getRange().get());
    		int[] intRange = SceneImportedController.rangesToIntArray(ranges,filePath);
    		
    		heirarchySourceText.moveTo(intRange[0]);
    		heirarchySourceText.setStyle(intRange[0], intRange[0]+1, "-rtfx-background-color: blue; -fx-fill: white;");
    		heirarchySourceText.setStyle(intRange[1]-1, intRange[1], "-rtfx-background-color: blue; -fx-fill: white;");
    		heirarchySourceText.deselect();	
    	} else {
    		heirarchySourceText.moveTo(0);
    	}
    	
    	
    	//heirarchySourceText.setStyle(paragraph, "-rtfx-background-color: red;");
    	//heirarchySourceText.setStyle(0, 40, "-rtfx-background-color: blue;");
    	heirarchySourceText.requestFollowCaret();
    	displayPOI();
	}
    
    /**Reads the file at filePath to a string.
	 * @param filePath
	 * @return the conent of the file as a string
	 * @throws IOException
	 */
	private String readFileToString(Path filePath) throws IOException {
		String content;// = new String(Files.readAllBytes(filePath),Charset.forName("UTF8"));
		
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath), "UTF8"));
		content = bufferReader.lines().collect(Collectors.joining("\n"));
		bufferReader.close();
		return content;
	}



	/**Function to switch between two alternate colour for the highlight
	 * @param highlight
	 * @return the style as a string
	 */
	private String switchAlternateStyle(String highlight) {
		if(highlight.contains("-rtfx-background-color: khaki;")) {
    		return "-rtfx-background-color: lawngreen; -fx-fill: black;";
    	}else {
    		return "-rtfx-background-color: khaki; -fx-fill: black;";
    	}
	}
    
	/**Function to switch between two primary colours for the highlight
     * @param highlight
     * @return the style as a string
     */
    private String switchStyle(String highlight) {
    	if(highlight.contains("-rtfx-background-color: aqua;")) {
    		return "-rtfx-background-color: yellow; -fx-fill: black;";
    	}else {
    		return "-rtfx-background-color: aqua; -fx-fill: black;";
    	}
	}
	
    /**
	 * Updates the metric Combobox with the available metrics that have points of interes on the selected node.
	 */
	private void updateMetricSelection() {
		String selectedMetric = metricSelector.getSelectionModel().getSelectedItem();
    	metricSelectorList.clear();
    	if(!heirarchyView.getSelectionModel().isEmpty()) {
    		BaseAST selectedAST = heirarchyView.getSelectionModel().getSelectedItem().getValue();
	    	if(selectedAST.hasNode()) {
		    	for( Entry<String, MetricResult> entry: selectedAST.getResults().entrySet()) {
		    		if(entry.getValue().hasPointsOfInterest()) {
		    			metricSelectorList.add(entry.getKey());
		    		}
		    	}
	    	}
	    	if(metricSelectorList.contains(selectedMetric)) {
	    		metricSelector.getSelectionModel().select(selectedMetric);
	    	}
	    	if(metricSelectorList.isEmpty()) {
	    		metricSelector.setPromptText("No POI data available");
	    	}else {
	    		metricSelector.setPromptText("Select a Metric");
	    	}
    	}
	}



	@FXML
    void boxTicked(ActionEvent event) throws IOException, InvalidOperationException {
		displaySourceCode();
    	
    }
    

	@FXML
    void initialize() {	
    	versionSelector.setItems(FXCollections.observableList(new ArrayList<ProjectAST>( SceneBuilderMain.astManager.getAstStorage().values())));
    	versionSelector.getSelectionModel().select(0);
    	
    	heirarchySourceText = new InlineCssTextArea();
    	
    	heirarchySourceText.setStyle("-fx-background-color: #dcf6f6;");
    	
    	heirarchySourceText.setEditable(false);
    	heirarchyScroll = new VirtualizedScrollPane<InlineCssTextArea>(heirarchySourceText);
    	textPane.setCenter(heirarchyScroll);
    	
		if(heirarchyView != null) {
			TreeItem<BaseAST> rootItem = new TreeItem<BaseAST>();
			heirarchyView.setRoot(rootItem);
			TreeItem<BaseAST> packItem, classItem, methItem;
			ProjectAST proj = versionSelector.getValue();
			for(PackageAST pack : proj.getPackages().values()) {
				packItem = new TreeItem<BaseAST>(pack);
				rootItem.getChildren().add(packItem);
				for(ClassAST clas : pack.getClasses().values()) {
					classItem = new TreeItem<BaseAST>(clas);
					packItem.getChildren().add(classItem);
					for(MethodAST meth : clas.getMethods().values()) {
						methItem = new TreeItem<BaseAST>(meth);
						classItem.getChildren().add(methItem);
					}
				}
			}	
		}	
		metricSelectorList = FXCollections.observableList(new ArrayList<String>());
		metricSelector.setItems(metricSelectorList);
		
		heirarchyView.getStylesheets().add(this.getClass()
						.getResource("TreeView.css").toExternalForm());
    }

    @FXML
    void metricSelected(ActionEvent event) throws IOException, InvalidOperationException {
    	displaySourceCode();
    }
    

    
    @FXML
    void SwitchToVersion(ActionEvent event) throws IOException, InvalidOperationException {
    	TreeItem<BaseAST> selection = heirarchyView.getSelectionModel().getSelectedItem();
    	Breadcrumb crumb;
    	if(selection != null) {
    		crumb = selection.getValue().getBreadcrumb();
    	}else {
    		crumb = new Breadcrumb(null, null, null, null);
    	}
    	MultipleSelectionModel<TreeItem<BaseAST>> selectionModel = heirarchyView.getSelectionModel();
    	
    	TreeItem<BaseAST> rootItem = new TreeItem<BaseAST>();
		heirarchyView.setRoot(rootItem);
		TreeItem<BaseAST> packItem, classItem, methItem;
		ProjectAST proj = versionSelector.getValue();
		for(PackageAST pack : proj.getPackages().values()) {
			
			packItem = new TreeItem<BaseAST>(pack);
			rootItem.getChildren().add(packItem);
			
			if(pack.getIdentifier().equals(crumb.getPackageID())) {
				selectionModel.clearSelection();
				selectionModel.select(packItem);;
			}
			
			for(ClassAST clas : pack.getClasses().values()) {
				classItem = new TreeItem<BaseAST>(clas);
				packItem.getChildren().add(classItem);
				
				if(clas.getIdentifier().equals(crumb.getClassID())) {
					packItem.setExpanded(true);
					selectionModel.clearSelection();
					selectionModel.select(classItem);;
				}
				for(MethodAST meth : clas.getMethods().values()) {
					methItem = new TreeItem<BaseAST>(meth);
					classItem.getChildren().add(methItem);
					if(meth.getIdentifier().equals(crumb.getMethodID())) {
						classItem.setExpanded(true);
						selectionModel.clearSelection();
						selectionModel.select(methItem);;
					}
				}
			}
		}
		
		updateMetricSelection();
		displaySourceCode();
    }



	@FXML
    public void displaySourceCode(MouseEvent event) throws IOException, InvalidOperationException { 		
    	updateMetricSelection();
    	displaySourceCode();  	
    }
}