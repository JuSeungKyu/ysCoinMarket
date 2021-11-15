package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import format.CoinInfo;
import format.message.BuyRequest;
import format.message.SellRequest;
import format.message.TransactionDetailsRequest;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import network.Client;
import util.SetEventUtil;
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
	@FXML
	private Button CoinMiningBtn;
	
	private Client client;

	public void initData(Object client) {
		this.client = (Client) client;
		System.out.println("전달받음");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println(11);
		loadPage();

		SetEventUtil seu = new SetEventUtil();
		seu.setNumericField(count);
		seu.setNumericField(price);
	}

	public void CoinMining() {
		CoinMiningBtn.setDisable(true);
		
		new StageControll().newStage("/view/fxml/CoinMining.fxml", root, client, false, true);
	}
	
	public void history() {
		new StageControll().newStage("/view/fxml/History.fxml", root, client, false);
		client.addSendObject(new TransactionDetailsRequest());
	}

	public void loadPage() {
		StageControll sc = new StageControll();
		Util util = new Util();
		new UIUpdateThread() {
			@Override
			public void update() {
				while (true) {
					util.sleep(100);
					if (client != null) {
						sc.addFxmlChildren(pane2, "/view/fxml/Graph.fxml", client);
						sc.addFxmlChildren(pane1, "/view/fxml/CoinTypeList.fxml", client);
						break;
					}
				}
			}
		}.start();
	}
	
	public boolean textValidation(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@FXML
	private TextField count;
	@FXML
	private TextField price;
	
	private void transaction(String type) {
		String countText = count.getText();
		String priceText = price.getText();
		
		if(!textValidation(countText) || !textValidation(priceText)) {
			new Util().alert("경고", "올바르지않은 입력입니다", "다시 입력해주세요");
			return;
		}

		int count = Integer.parseInt(countText);
		int price = Integer.parseInt(priceText);

		client.addSendObject(type.equals("구매") ? 
				new BuyRequest(client.getCurrentCoinId(), price, count)
				: new SellRequest(client.getCurrentCoinId(), price, count));
	}
	
	public void buy() {
		transaction("구매");
	}
	
	public void sell() {
		transaction("판매");
	}
}