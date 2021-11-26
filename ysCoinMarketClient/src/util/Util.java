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
			return false;
		}
	}
	
	public int getIndexById(ObservableList<Node> list, String str) {
		int index = -1;
		
		for (int i = 0; i < list.size(); i++) {
			String nodeId = list.get(i).getId(); 
			if(nodeId != null ? nodeId.equals(str) : false) {
				index = i;
				return index;
			}
		}
		
		return index;
	}
}
