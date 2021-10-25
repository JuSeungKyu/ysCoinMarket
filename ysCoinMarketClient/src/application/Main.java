package application;
	
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import util.StageControll;
import util.Util;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	public static ArrayList<Thread> ThreadList = new ArrayList<Thread>();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/fxml/Login.fxml"));
			AnchorPane ap = (AnchorPane)loader.load();
			Scene scene = new Scene(ap);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			new StageControll().setCloseEventHandler(primaryStage);
			primaryStage.show();
			primaryStage.setTitle("login form");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
