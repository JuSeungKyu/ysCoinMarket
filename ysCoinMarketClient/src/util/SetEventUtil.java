package util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class SetEventUtil {
	// 텍스트 필드에 숫자 밖에 입력 못하게 하는 이벤트
	public void setNumericField(TextField field) {
		field.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	field.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
	}
}
