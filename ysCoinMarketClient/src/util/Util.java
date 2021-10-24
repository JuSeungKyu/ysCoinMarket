package util;

import java.io.IOException;

import application.Main;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Client;
import view.controller.Controller;

public class Util {
	public void alert(String title, String header, String content) {
		new Thread(() -> {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(title);
				alert.setHeaderText(header);
				alert.setContentText(content);

				alert.showAndWait();
			});
		}).start();
	}

	public boolean sleep(int time) {
		try {
			Thread.sleep(time);
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void newStage(String src, AnchorPane pane, Object sendData, boolean close) {
		new Thread(() -> {
			Platform.runLater(() -> {
				Stage newStage = new Stage();
				Stage stage = (Stage) pane.getScene().getWindow();
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource(src));
					
					Parent main = loader.load();
					Scene sc = new Scene(main);
					newStage.setTitle("YS Coin Market");
					newStage.setScene(sc);
					setCloseEventHandler(newStage);

					Controller newController = loader.getController();
					newController.initData(sendData);
					
					newStage.show();
					
					
					if(close) {
						stage.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}).start();
	}
	
	public void setCloseEventHandler(Stage stage) {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent evt) {
				System.out.println("종료");
				for(int i = 0; i < Main.ThreadList.size(); i++) {
					Main.ThreadList.get(i).interrupt();
				}
			}
		});
	}
}
