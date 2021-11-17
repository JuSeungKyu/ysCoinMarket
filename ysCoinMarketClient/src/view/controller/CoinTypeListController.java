package view.controller;

import java.net.URL;

import java.util.ResourceBundle;

import javax.naming.NameClassPair;

import format.TypeInfo;
import format.message.CoinTypeChange;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
		this.client = (Client) data;
	}

	@Override // 불러오기 성공
	public void initialize(URL location, ResourceBundle resources) {
		viewMain.setItems(items);
		System.out.println("tableStart");
		items = FXCollections.observableArrayList();
		viewMain.setItems(items);
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
		changeColumn.setCellValueFactory(cellData -> cellData.getValue().getChange().asObject());

		viewMain.setOnMouseClicked((MouseEvent e) -> {
			int index = Math.round((Math.round(e.getY()) - 30) / 30);
			
			if(index < 0 || index >= items.size()) {
				return;
			} 
			
			client.addSendObject(new CoinTypeChange(nameColumn.getCellData(index)));
		});
		
		AnimationTimer setType = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				try {
					getTable();
				} catch (Exception e) {}
			}
		};
		setType.start();
	}

	public boolean getTable() {
		TypeInfo[] typeInfo = this.client.getTypeInfoList();
		if (typeInfo.length == 0) {
			return false;
		}
		items.clear();
		for (TypeInfo typeInfo2 : typeInfo) {
			CoinTypeTable c = new CoinTypeTable(typeInfo2.name, typeInfo2.currentPrice);
			items.add(c);

		}

		return true;
	}
	
	@FXML
	public void rowFromTable() {
//		viewMain.getSelectionModel().getSelectedItem()
	
	}
}