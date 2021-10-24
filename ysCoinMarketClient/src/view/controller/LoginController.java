package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.userFxmlTag.ToggleSwitch;

public class LoginController implements Initializable {
	@FXML
	AnchorPane root;
	@FXML
	Pane switchPane;
	@FXML
	TextField id;
	@FXML
	TextField pw;
	@FXML
	Button submit;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        ToggleSwitch btn = new ToggleSwitch("로그인", "회원가입", 
        		"-fx-background-color: #ffffff; -fx-text-fill:#c8c8c8; -fx-border-color:#c8c8c8;",
        		"-fx-background-color: #e6e6ff; -fx-text-fill:#6735fb; -fx-border-color:#6735fb;"
        );
        
        btn.switchOnProperty().addListener((obser, oldV, newV) -> {
        	this.submit.setText(btn.switchOnProperty().get() ? "회원가입" : "로그인");
		});
        
        switchPane.getChildren().add(btn);
	}
	
	public void joinOrLogin() {
		String id = this.id.getText();
		String pw = this.pw.getText();
		
	}
}
