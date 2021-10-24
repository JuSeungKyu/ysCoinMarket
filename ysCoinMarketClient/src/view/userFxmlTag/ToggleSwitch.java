package view.userFxmlTag;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ToggleSwitch extends HBox {

	private Label label1 = new Label();
	private Label label2 = new Label();
	private String style1;
	private String style2;

	private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);

	public SimpleBooleanProperty switchOnProperty() {
		return switchedOn;
	}

	private void init(String text1, String text2) {
		label1.setText(text1);
		label2.setText(text2);

		getChildren().addAll(label1, label2);
		label2.setOnMouseClicked((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		label1.setOnMouseClicked((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		setStyle();
		bindProperties();
	}

	private void setStyle() {
		// Default Width
		setWidth(300);
		setHeight(45);
		label1.setAlignment(Pos.CENTER);
		label2.setAlignment(Pos.CENTER);
		label1.setStyle(style2);
		label2.setStyle(style1);
		setAlignment(Pos.CENTER_LEFT);
	}

	private void bindProperties() {
		label1.prefWidthProperty().bind(widthProperty().divide(2));
		label1.prefHeightProperty().bind(heightProperty());
		label2.prefWidthProperty().bind(widthProperty().divide(2));
		label2.prefHeightProperty().bind(heightProperty());
	}

	public ToggleSwitch(String text1, String text2, String style1, String style2) {
		this.style1 = style1;
		this.style2 = style2;
		
		init(text1, text2);
		switchedOn.addListener((obser, oldV, newV) -> {
			if (newV) {
				label1.setStyle(style1);
				label2.setStyle(style2);
			} else {
				label1.setStyle(style2);
				label2.setStyle(style1);
			}
		});
	}
}