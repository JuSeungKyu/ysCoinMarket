package view.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import network.Client;
import view.date.CoinTypeDate;

public class CoinTypeListController extends Controller {
	@FXML
	public TableView<CoinTypeDate> viewMain ;
	@FXML
	public TableColumn<CoinTypeDate, String> nameColumn;
	@FXML
	public TableColumn<CoinTypeDate, String> changeColumn;
	private Client client;
	@Override
	public void initData(Object data) {
		this.client = (Client) client;
		System.out.println("C전달받음");
	}
	public void getname() {

		
		
	}public void getchangelist() {
		
		
		
	}
	
	
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client.get_typedbname();
		client.get_typedbnum();
	}

	
	
	
	
	
}
