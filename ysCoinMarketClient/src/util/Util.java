package util;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void newStage(String src, AnchorPane pane) {
		new Thread(() -> {
			Platform.runLater(() -> {
				Stage newStage = new Stage();
				Stage stage = (Stage) pane.getScene().getWindow();
				try {
					Parent main = FXMLLoader.load(getClass().getResource(src));
					Scene sc = new Scene(main);
					newStage.setTitle("YS Coin Market");
					newStage.setScene(sc);
					newStage.show();
					stage.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}).start();
	}
}
