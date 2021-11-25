package view.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import network.Client;
import view.userFxmlTag.OrderBookTable;

public class OrderBookController extends Controller{
	@FXML
	public TableView<OrderBookTable> orderBookTable;
	@FXML
	public TableColumn<OrderBookTable, Integer> sellPriceColumn;
	@FXML
	public TableColumn<OrderBookTable, Integer> volumnColumn;
	@FXML
	public TableColumn<OrderBookTable, Integer> buyPriceColumn;
	
	private ObservableList<OrderBookTable> items;

	protected Client client;
	
	@Override
	public void initData(Object client) {
		this.client = (Client) client;
		System.out.println("orderBook start");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		items = FXCollections.observableArrayList();
		orderBookTable.setItems(items);
		
		sellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
		volumnColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));
		buyPriceColumn.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
		
		AnimationTimer setBook = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				try {
					drawTable();
				} catch (Exception e) {}
			}
		};
		setBook.start();
	}
	
	public void drawTable() {
		ArrayList<int[]> data = getInfo();
		items.clear();
		for(byte i = 0; i < data.size(); i++) {
			OrderBookTable c = new OrderBookTable(data.get(i)[0], data.get(i)[1], data.get(i)[2]);
			items.add(c);
		}
		System.out.println(items.size());
	}
	
	public ArrayList<int[]> getInfo() {
		return this.client.getOrderBook();
	}
}
