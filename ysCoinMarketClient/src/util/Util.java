package util;

import javafx.collections.ObservableList;
import javafx.scene.Node;
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
			System.out.println("스레드 종료");
			return false;
		}
	}
	
	public int getIndexById(ObservableList<Node> list, String str) {
		int index = -1;
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getId().equals(str)) {
				index = i;
				return index;
			}
		}
		
		return index;
	}
}
