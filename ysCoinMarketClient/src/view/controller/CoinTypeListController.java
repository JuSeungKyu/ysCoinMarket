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
	public TableView<CoinTypetable> viewMain ;
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
	public void getname() {
		client.get_typedbname();
		
		
	}public void getchangelist() {
		
		String snum = client.get_typedbnum();//DB coin_type  last_price 추출 부탁
		String syestercoin = client.get_yesterdaycoin();
		int coin = Integer.parseInt(snum);
		int yestercoin = Integer.parseInt(syestercoin);//String변경
		System.out.println("coin :"+coin+"\r\n"+"yesterday coin:"+yestercoin);//확인  지워야 할것 
		double eve = coin / yestercoin;
		
		double changenum = eve /yestercoin* 100;//changenum = 값
		
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client.get_typedbname();
		client.get_typedbnum();
	}
	
}