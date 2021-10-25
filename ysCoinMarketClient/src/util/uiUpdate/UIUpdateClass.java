package util.uiUpdate;

import javafx.application.Platform;

public abstract class UIUpdateClass {
	public void start() {
		Platform.runLater(()-> {
			update();
		});
	}
	
	public abstract void update();
}
