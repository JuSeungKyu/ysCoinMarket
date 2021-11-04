package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import network.Client;
import view.userFxmlTag.HistoryTable;

public class HistoryController extends Controller{
	
	@FXML
	private Button backBtn;

	@FXML
	private TableView<HistoryTable> tvHistoryView;
	
	@FXML
	private TableColumn<HistoryTable, String> tcName;
	
	@FXML
	private TableColumn<HistoryTable, String> tcType;
	
	@FXML
	private TableColumn<HistoryTable, String> tcPrice;
	
	@FXML
	private TableColumn<HistoryTable, String> tcTime;
	
	private ObservableList<HistoryTable> items;

	private Client client;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		items = FXCollections.observableArrayList();
		tvHistoryView.setItems(items);
		//?
		System.out.println("거래내역 스타트");
		
	}
	
	@Override
	public void initData(Object client) {
		this.client = (Client) client;
		System.out.println("전달 완료");
	}
	
	private void get() {
		
	}

	private void ChangeScene() {
		//팝업? 체인지?
	}

}
