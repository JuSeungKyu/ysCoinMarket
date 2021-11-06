package view.controller;

import java.net.URL;
import java.sql.Time;
import java.util.List;
import java.util.ResourceBundle;

import format.HistoryInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import network.Client;
import util.StageControll;
import view.userFxmlTag.HistoryTable;

public class HistoryController extends Controller {
	@FXML
	private AnchorPane root;

	@FXML
	private TableView<HistoryTable> tbHistoryView;

	@FXML
	private TableColumn<HistoryTable, String> tbName;

	@FXML
	private TableColumn<HistoryTable, String> tbType;

	@FXML
	private TableColumn<HistoryTable, Integer> tbPrice;

	@FXML
	private TableColumn<HistoryTable, Time> tbTime;

	@FXML
	private TableColumn<HistoryTable, Integer> tbWhether;

	private ObservableList<HistoryTable> items;

	private Client client;

	// 새로고침

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tbName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tbType.setCellValueFactory(new PropertyValueFactory<>("type"));
		tbPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		tbTime.setCellValueFactory(new PropertyValueFactory<>("time"));
		tbWhether.setCellValueFactory(new PropertyValueFactory<>("whether"));

		System.out.println("거래내역 스타트");

		items = FXCollections.observableArrayList();
		// items.add(new HistoryTable(name, type, price, time, whether))
		// items.add(new HistoryTable("테스트", "테스트", 1234, null, 1213));
		tbHistoryView.setItems(items);
		// getHistoryInfo();

	}

	@Override
	public void initData(Object client) {
		this.client = (Client) client;
		System.out.println("전달 완료");
	}

	private void getHistoryInfoData() {
		HistoryInfo historyInfo = this.client.getHistoryInfoData();
		if (historyInfo == null) {
			return;
		}

		// 테이블에 넣기
		// List<HistoryInfo> HistoryList = client.;
		items = FXCollections.observableArrayList();
		tbHistoryView.setItems(items);
	}

	public void backScene() {
		
	}

}
