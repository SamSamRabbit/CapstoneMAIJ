package gui.Controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

import astManager.InvalidOperationException;
import gui.SceneBuilderMain;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import metricAnalysis.AnalysisHandler;

public class ImportContentController {

	private ObservableList<Map.Entry<Double, Path>> pendingImports;

	@FXML
    private Button browseButton;
	
    @FXML
    private Button selectButton;
    
    @FXML
    private Button confirmButton;
    
    @FXML
    private ListView<Path> includedFiles;
    
    @FXML
    private TableColumn<Map.Entry<Double, Path>, String> versionColumn;

    @FXML
    private TableColumn<Map.Entry<Double, Path>, String> directoryColumn;

    @FXML
    private TableView<Map.Entry<Double, Path>> importsTable;
    
    @FXML
    private Spinner<Integer> versionSelectSpinner;
    
    @FXML
    private TextField chosenDirectory;

    public ImportContentController() {
		// TODO Auto-generated constructor stub
	}
    
    @FXML
    void confirmInput(ActionEvent event) throws IOException, InvalidOperationException {
    	SceneBuilderMain.astManager = SceneBuilderMain.importManager.confirmImport();
    	SceneBuilderMain.analysisManager = new AnalysisHandler(SceneBuilderMain.astManager);
    	
    	String errorsAsString = SceneBuilderMain.astManager.getErrorsAsString();
    	if(!errorsAsString.isEmpty()) {
    		SceneBuilderMain.displayAlert("Import Errors", "The following files failed to import and will be ignored",
    			errorsAsString, AlertType.ERROR);
    	}
    	
    	SceneBuilderMain.switchSceneImported();
		
    }

    @FXML
    void displayFiles(MouseEvent event) {
    	if(!importsTable.getSelectionModel().isEmpty()){
    		SceneBuilderMain.importManager.selectImport(importsTable.getSelectionModel().getSelectedItem().getKey());
			chosenDirectory.setText(importsTable.getSelectionModel().getSelectedItem().getValue().toString());
			includedFiles.setItems(FXCollections.observableList(SceneBuilderMain.importManager.getJavaFiles()));
    	}
    }
    
    @FXML
    void initialize() {    
		if (versionSelectSpinner != null) {
			SpinnerValueFactory<Integer> versionSelectValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
					1, 99999, 1);
			versionSelectSpinner.setValueFactory(versionSelectValueFactory);
		}
		if (versionColumn != null) {
			versionColumn.setCellValueFactory(
					cellData -> new SimpleStringProperty(cellData.getValue().getKey().toString()));
		}
		if (directoryColumn != null) {
			directoryColumn.setCellValueFactory(
					cellData -> new SimpleStringProperty(cellData.getValue().getValue().toString()));
		}
		
		
    }
    
    
    @FXML
    void openDirectoryChooser(ActionEvent event) {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	File selectedFile = directoryChooser.showDialog(SceneBuilderMain.primaryStage);
    	if(selectedFile == null) return;
    	chosenDirectory.setText(selectedFile.getAbsolutePath());
    	try {
			SceneBuilderMain.importManager.selectImport(chosenDirectory.getText());
			if(SceneBuilderMain.importManager.getSelectedImport().getJavaFiles().isEmpty()) {
				selectButton.setDisable(true);
				SceneBuilderMain.displayAlert("Invalid Directory", "No .java files detected", "The program can only work with java projects", AlertType.WARNING);			
			}else {
				selectButton.setDisable(false);
			}
		} catch (IOException e) {
			chosenDirectory.setText("");
			selectButton.setDisable(true);
		}
    	
    	includedFiles.setItems(FXCollections.observableList(SceneBuilderMain.importManager.getJavaFiles()));
    	
    }
    
    @FXML
    void removeVersion(ActionEvent event) {
    	SceneBuilderMain.importManager.removeVersion(importsTable.getSelectionModel().getSelectedItem().getKey());
    	pendingImports = FXCollections.observableList(new ArrayList<Map.Entry<Double, Path>>(SceneBuilderMain.importManager.getPendingVersions().entrySet()));  
    	importsTable.setItems(pendingImports);
    	if(importsTable.getItems().isEmpty()) {
    		confirmButton.setDisable(true);
    	}
    }
    
    @FXML
    void selectForImport(ActionEvent event) {
    	SceneBuilderMain.importManager.addVersion((double) versionSelectSpinner.getValue());
    	pendingImports = FXCollections.observableList(new ArrayList<Map.Entry<Double, Path>>(SceneBuilderMain.importManager.getPendingVersions().entrySet()));  
    	importsTable.setItems(pendingImports);
		chosenDirectory.setText(SceneBuilderMain.importManager.getSelectedImport().getSelectedDirectory().toString());
		confirmButton.setDisable(false);
		versionSelectSpinner.increment();
    }
}
