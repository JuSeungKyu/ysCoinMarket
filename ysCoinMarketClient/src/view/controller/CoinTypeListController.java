package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import format.TypeInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import network.Client;
import view.userFxmlTag.CoinTypeTable;

public class CoinTypeListController extends Controller {
	@FXML
	public TableView<CoinTypeTable> viewMain;
	private ObservableList<CoinTypeTable> items;
	@FXML
	public TableColumn<CoinTypeTable, String> nameColumn;
	@FXML
	public TableColumn<CoinTypeTable, Integer> changeColumn;
	private Client client;


	public void initData(Object data) {
		this.client = (Client) client;
		System.out.println("C전달받음");
	}

	@Override//불러오기 성공 
	public void initialize(URL location, ResourceBundle resources) {
	
		items = FXCollections.observableArrayList();
		viewMain.setItems(items);
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
		changeColumn.setCellValueFactory(cellData -> cellData.getValue().getChange().asObject());

		System.out.println("종목별 리스트 출력");
		getTable();
		
	}

	public void getTable() {
//		TypeInfo[] typeInfo = this.client.getTypeInfo();
		TypeInfo[] typeInfo = {new TypeInfo("AAA", 1)};
		if (typeInfo == null) {
			System.out.println("coinTable 오류");
			return;
		}
		items = FXCollections.observableArrayList();
		viewMain.setItems(items);
		for (TypeInfo typeInfo2 : typeInfo) {
			CoinTypeTable c = new CoinTypeTable(typeInfo2.getName(), typeInfo2.getCurrentPrice());
			items.add(c);
			
		}
		
	}
}