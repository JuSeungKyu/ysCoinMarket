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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.uiUpdate.UIUpdateThread;
import view.controller.Controller;

public class StageControll {
	public void newStage(String src, AnchorPane pane, Object sendData, boolean close) {
		new UIUpdateThread() {
			@Override
			public void update() {
				Stage newStage = new Stage();
				Stage stage = (Stage) pane.getScene().getWindow();
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource(src));

					Parent main = loader.load();
					Scene sc = new Scene(main);
					newStage.setTitle("YS Coin Market");
					newStage.setScene(sc);

					Controller newController = loader.getController();
					newController.initData(sendData);

					newStage.show();

					if (close) {
						setCloseEventHandler(newStage);
						stage.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void setCloseEventHandler(Stage stage) {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent evt) {
				System.out.println("종료");
				for (int i = 0; i < Main.ThreadList.size(); i++) {
					Main.ThreadList.get(i).interrupt();
				}
			}
		});
	}

	public Controller addFxmlChildren(Pane pane, String src, Object sendData) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(src));
			pane.getChildren().add(loader.load());

			Controller newController = loader.getController();
			newController.initData(sendData);
			return newController;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
