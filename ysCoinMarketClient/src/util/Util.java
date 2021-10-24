package util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Util {
	public void alert(String title, String header, String content) {
		new Thread(()->{
			Platform.runLater(()->{
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
}
