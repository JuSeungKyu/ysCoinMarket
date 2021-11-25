package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import format.Bool;
import format.message.LoginRequest;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import network.Client;
import util.Util;
import view.userFxmlTag.ToggleSwitch;

public class LoginController extends Controller {
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

	private Util util;
	private Client client;
	private Bool isLoding = new Bool(false);

	@Override
	public void initData(Object data) {
		this.client = (Client) data;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ToggleSwitch btn = new ToggleSwitch("로그인", "회원가입",
				"-fx-background-color: #ffffff; -fx-text-fill:#c8c8c8; -fx-border-color:#c8c8c8;",
				"-fx-background-color: #e6e6ff; -fx-text-fill:#6735fb; -fx-border-color:#6735fb;");

		btn.switchOnProperty().addListener((obser, oldV, newV) -> {
			this.submit.setText(btn.switchOnProperty().get() ? "회원가입" : "로그인");
		});

		switchPane.getChildren().add(btn);
		util = new Util();
	}

	public void joinOrLogin() {
		if (isLoding.getValue()) {
			return;
		}
		isLoding.setValue(true);

		String id = this.id.getText();
		String pw = this.pw.getText();

		if (id.equals(null) || pw.equals(null) || id.length() == 0 || pw.length() == 0) {
			util.alert("경고", "잘못된 입력", "모든 필드를 채웠는지 확인해주십시오");
			isLoding.setValue(false);
			return;
		}

		if (id.length() > 20) {
			util.alert("경고", "잘못된 입력", "너무 긴 아이디 입니다. (최대 20글자)");
			isLoding.setValue(false);
			return;
		}
		
		if (pw.length() > 50) {
			util.alert("경고", "잘못된 입력", "너무 긴 비밀번호 입니다. (최대 50글자)");
			isLoding.setValue(false);
			return;
		}

		this.client = new Client(isLoding);
		this.client.setRoot(root);
		this.client.addSendObject(new LoginRequest(id, pw, this.submit.getText().equals("로그인")));
	}

	public Client getClient() {
		return this.client;
	}
}
