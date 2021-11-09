package view.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

import format.TypeInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import network.Client;
import db.JDBC;
import view.userFxmlTag.CoinTypeTable;

public class CoinTypeListController extends Controller {
	@FXML
	public TableView<CoinTypeTable> viewMain;
	private ObservableList<CoinTypeTable> items;
	@FXML
	public TableColumn<CoinTypeTable, String> nameColumn;
	@FXML
	public TableColumn<CoinTypeTable, String> changeColumn;
	private Client client;

	public void initData(Object data) {
		this.client = (Client) client;
		System.out.println("C 전달받음");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		changeColumn.setCellValueFactory(new PropertyValueFactory<>("change"));

		System.out.println("종목별 리스트 출력");
		items = FXCollections.observableArrayList();
		viewMain.setItems(items);

	}

	public void getTable() {
		TypeInfo[] typeInfo = this.client.getTypeInfo();
		if (typeInfo == null) {
			return;

		}

		items = FXCollections.observableArrayList();
		viewMain.setItems(items);

	}
}