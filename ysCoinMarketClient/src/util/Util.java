package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.uiUpdate.UIUpdateThread;

public class Util {
	public void alert(String title, String header, String content) {
		new UIUpdateThread() {
			@Override
			public void update() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(title);
				alert.setHeaderText(header);
				alert.setContentText(content);

				alert.showAndWait();
			}
		}.start();
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
}
