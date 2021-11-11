package view.controller;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import format.TypeInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	private TypeInfo type;

	String st = type.getName();
	int num = type.getCurrentPrice();
	
	
	public void initData(Object data) {
		this.client = (Client) client;
		System.out.println("C전달받음");
	}

	@Override // 불러오기 성공
	public void initialize(URL location, ResourceBundle resources) {

		items = FXCollections.observableArrayList();
		viewMain.setItems(items);
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
		changeColumn.setCellValueFactory(cellData -> cellData.getValue().getChange().asObject());

		System.out.println("종목별 리스트 출력");
		this.client.getTypeInfoList();
		for (int i = 0; i <num ; i++) {
			getTable(st, num);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		
	}

	public void getTable(String name, int currentPrice) {
//		TypeInfo[] typeInfo = this.client.getTypeInfo();
		TypeInfo[] typeInfo = { new TypeInfo(name, currentPrice) };
		if (typeInfo == null) {
			System.out.println("coinTable 오류");
			return;
		}
		items = FXCollections.observableArrayList();
		viewMain.setItems(items);
		for (TypeInfo typeInfo2 : typeInfo) {
			CoinTypeTable c = new CoinTypeTable(typeInfo2.name, typeInfo2.currentPrice);
			items.add(c);

		}

	}
}