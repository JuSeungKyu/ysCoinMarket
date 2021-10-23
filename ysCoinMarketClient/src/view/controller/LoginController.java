package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import view.userFxmlTag.ToggleSwitch;

public class LoginController implements Initializable {
	@FXML
	AnchorPane root;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        ToggleSwitch btn = new ToggleSwitch("로그인", "회원가입", 
        		"-fx-background-color: #8c8cbe; -fx-text-fill:black; -fx-background-radius: 4;",
        		"-fx-background-color: rgb(220, 220, 255); -fx-text-fill:black; -fx-background-radius: 4;"
        );
        
        btn.switchOnProperty().addListener((obser, oldV, newV) -> {
    		System.out.println((btn.switchOnProperty().get() ? "회원가입" : "로그인"));
		});
        
        root.getChildren().add(btn);
	}
}
