package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import network.Client;
import util.StageControll;
import util.Util;
import util.uiUpdate.UIUpdateThread;

public class MainFxcontroller extends Controller {
	@FXML
	private AnchorPane root;
	@FXML
	private AnchorPane pane1;
	@FXML
	private AnchorPane pane2; // 그래프

	private Client client;

	public void initData(Object client) {
		this.client = (Client) client;
		System.out.println("전달받음");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println(11);
		loadPage();
	}

	public void loadPage() {
		StageControll sc = new StageControll();
		Util util = new Util();
		new UIUpdateThread() {
			@Override
			public void update() {
				while (true) {
					util.sleep(100);
					if(client != null) {
						sc.addFxmlChildren(pane2, "/view/fxml/Graph.fxml", client);
						sc.addFxmlChildren(pane1, "/view/fxml/CoinTypeList.fxml", client);
						break;
					}
				}
			}
		}.start();
	}
}