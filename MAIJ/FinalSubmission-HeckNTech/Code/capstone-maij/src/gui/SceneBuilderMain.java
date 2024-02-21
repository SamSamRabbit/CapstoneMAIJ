package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import astManager.ASTHandler;
import astManager.InvalidOperationException;
import fileImport.SourceImportManager;
import gui.Controllers.AnalysisController;
import gui.Controllers.DashboardController;
import gui.Controllers.ImportContentController;
import gui.Controllers.SceneBuilderController;
import gui.Controllers.SceneImportedController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import metricAnalysis.AnalysisHandler;


/**Main entry point of the program, also contains the model for the GUI that ties everything together.
 * @author Jacob Botha
 *
 */
public class SceneBuilderMain extends Application {

	public static Stage primaryStage;
	public static BorderPane mainView;
	public static SourceImportManager importManager;
	
	public static ASTHandler astManager;
	public static AnalysisHandler analysisManager;
	private static GridPane afterImport;
	private static GridPane importContent;
	private static GridPane analysis;
	private static GridPane dashboard;
	private static SceneImportedController sceneImportedController;
	private static AnalysisController sceneAnalysisController;
	private static ImportContentController sceneImportContentController;
	private static DashboardController sceneDashboardController;
	public SceneBuilderController mainController;
	private static Image image = new Image((new File("Logo.png")).toURI().toString(), 100, 100, true, true);
	
	/**Display the standard Alert with the given values set as the title, header, content and type.
	 * Also uses the custom Logo as the graphic.
	 * @param title
	 * @param headerText
	 * @param contentText
	 * @param alertType
	 */
	public static void displayAlert(String title, String headerText, String contentText, AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title); 	
		alert.setHeaderText(headerText);
    	ImageView graphic = new ImageView(image);
    	alert.setGraphic(graphic);
		alert.setContentText(contentText);
		alert.getDialogPane().setMinWidth(800);
    	alert.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static void switchSceneAnalysis() throws IOException {
		if (analysis == null) {
			URL resource = SceneBuilderMain.class.getResource("Analysis.fxml");
			FXMLLoader loader = new FXMLLoader(resource);
			analysis = loader.load();
			sceneAnalysisController = loader.getController();
		}
		mainView.setCenter(analysis);
	}
	
	public static void switchSceneDashboard() throws IOException {
		//sceneAnalysisController.runAnalysis(new ActionEvent());
		if (dashboard == null) {
			URL resource = SceneBuilderMain.class.getResource("DashBoard.fxml");
			FXMLLoader loader = new FXMLLoader(resource);
			dashboard = loader.load();
			sceneDashboardController = loader.getController();
		}
		mainView.setCenter(dashboard);
		sceneDashboardController.updateMetricSelection();
	}
	
	public static void switchSceneImportConent() throws IOException {
		if (importContent == null) {
			URL resource = SceneBuilderMain.class.getResource("ImportContent.fxml");
			FXMLLoader loader = new FXMLLoader(resource);
			importContent = loader.load();
			sceneImportContentController = loader.getController();
		}
		mainView.setCenter(importContent);
	}
	
	public static void switchSceneImported() throws IOException, InvalidOperationException {
		if(afterImport == null) {
			URL resource = SceneBuilderMain.class.getResource("SceneImported.fxml");
			FXMLLoader loader = new FXMLLoader(resource);
			afterImport = loader.load();
			sceneImportedController = loader.getController();
		} else {
			sceneImportedController.displaySourceCode(null);
		}
		mainView.setCenter(afterImport);
	}
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.getIcons().add(image);
		SceneBuilderMain.primaryStage = primaryStage;
		URL resource = SceneBuilderMain.class.getResource("SceneBuilder.fxml");
		FXMLLoader loader = new FXMLLoader(resource);
		Parent root = loader.load();
		mainController = loader.getController();
		importManager = new SourceImportManager();
		Scene scene = new Scene(root);
		
		
		primaryStage.setTitle("MAIJ: Metric Analysis and Identification for Java");
		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(900);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
}
