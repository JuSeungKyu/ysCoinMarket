package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import network.Client;
import view.userFxmlTag.CoinTypetable;

public class CoinTypeListController extends Controller {
	@FXML
	public TableView<CoinTypetable> viewMain;
	@FXML
	public TableColumn<CoinTypetable, String> nameColumn;
	@FXML
	public TableColumn<CoinTypetable, String> changeColumn;
	private Client client;

	@Override
	public void initData(Object data) {
		this.client = (Client) client;
		System.out.println("C전달받음");
	}

	public void getName() {
		client.getTypeDBName();

	}

	public void getChangeList() {

		String sNum = client.getTypeDBNum();// DB coin_type last_price 추출 부탁
		String sYesterCoin = client.getYesterdayCoin();
		int coin = Integer.parseInt(sNum);
		int yesterCoin = Integer.parseInt(sYesterCoin);// String변경
		System.out.println("coin :" + coin + "\r\n" + "yesterday coin:" + yesterCoin);// 확인 지워야 할것
		double eve = coin / yesterCoin;

		double changeNum = eve / yesterCoin * 100;// changeNum = 값

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client.getTypeDBName();
		client.getTypeDBNum();
	}

}