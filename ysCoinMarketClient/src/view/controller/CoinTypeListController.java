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
		System.out.println("C전달받음");
	}

	public void getTable() {
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		items = FXCollections.observableArrayList();
		viewMain.setItems(items);
		listUp();
		
	}

	public void listUp() {
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
		changeColumn.setCellValueFactory(cellData -> cellData.getValue().getChange());

		JDBC db = new JDBC("localhost", "yscoin", "root", "");
		Connection con = db.con;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM `coin_type` ORDER BY 1 ASC";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String name = rs.getString("name");
				String change = rs.getString("change");

				CoinTypeTable gs = new CoinTypeTable(name, change);
				((List<CoinTypeTable>) viewMain).add(gs);
			}
		} catch (Exception e) {
			System.out.println("오류");
		}
	}


}