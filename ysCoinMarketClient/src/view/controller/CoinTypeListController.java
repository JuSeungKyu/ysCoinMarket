package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import format.TypeInfo;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import network.Client;
import view.userFxmlTag.CoinTypeTable;

public class CoinTypeListController extends Controller {
	@FXML
	public TableView<CoinTypeTable> viewMain;
	@FXML
	public TableColumn<CoinTypeTable, String> nameColumn;
	@FXML
	public TableColumn<CoinTypeTable, String> changeColumn;
	private Client client;

	@Override
	public void initData(Object data) {
		this.client = (Client) client;
		System.out.println("C전달받음");
	}

	public void getTable() {
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}