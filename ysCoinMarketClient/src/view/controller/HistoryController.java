package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import format.message.History;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import network.Client;

public class HistoryController extends Controller{
	private Client client = new Client();
	
	@FXML
	private Button backBtn;

	@FXML
	private TableView<?> tvHistoryView;
	
	@FXML
	private TableColumn<?, ?> tcEvent;
	
	@FXML
	private TableColumn<?, ?> tcType;
	
	@FXML
	private TableColumn<?, ?> tcPrice;
	
	@FXML
	private TableColumn<?, ?> tcTime;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("거래내역 스타트");
		
	}
	
	@Override
	public void initData(Object client) {
		this.client = (Client) client;
		System.out.println("전달 완료");
	}
	
	private void getHistory() {
		
	}

	private void ChangeScene() {
		//팝업? 체인지?
	}

}
