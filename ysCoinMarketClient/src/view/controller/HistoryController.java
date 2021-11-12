package view.controller;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import format.HistoryInfo;
import format.TransactionDetailsInfo;
import javafx.animation.AnimationTimer;
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
	private TableColumn<HistoryTable, Integer> tbOrdering;

	@FXML
	private TableColumn<HistoryTable, Integer> tbPenalty;

	@FXML
	private TableColumn<HistoryTable, Integer> tbPrice;

	@FXML
	private TableColumn<HistoryTable, String> tbOrder;

	@FXML
	private TableColumn<HistoryTable, String> tbTime;

	private ObservableList<HistoryTable> items;

	protected Client client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tbName.setCellValueFactory(new PropertyValueFactory<>("coin_id"));
		tbOrdering.setCellValueFactory(new PropertyValueFactory<>("ordering_amount"));
		tbPenalty.setCellValueFactory(new PropertyValueFactory<>("penalty_amount"));
		tbPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		tbOrder.setCellValueFactory(new PropertyValueFactory<>("order_type"));
		tbTime.setCellValueFactory(new PropertyValueFactory<>("time"));

		System.out.println("거래내역 스타트");
		AnimationTimer set = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				try {
					if (getHistoryInfoData())
						this.stop();
				} catch (Exception e) {
				}
			}
		};
		set.start();
	}

	@Override
	public void initData(Object client) {
		this.client = (Client) client;
		System.out.println("전달 완료");
	}

	// 새로고침
	public void refresh() {
		tbHistoryView.refresh();
	}

	private boolean getHistoryInfoData() {
		ArrayList<TransactionDetailsInfo> historyInfo = this.client.getTransactionDetailsData();
		if (historyInfo == null) {
			return false;
		}

		// 테이블에 넣기
		items = FXCollections.observableArrayList();
		items.add(new HistoryTable(historyInfo.coin_id, historyInfo.ordering_amount, historyInfo.penalty_amount)),
		historyInfo.price, historyInfo.order_type, historyInfo.time));
		tbHistoryView.setItems(items);
		
		return true;
	}

}
