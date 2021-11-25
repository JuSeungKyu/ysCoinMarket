package view.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import format.TransactionDetailsInfo;
import format.message.TransactionDetailsRequest;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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

		AnimationTimer set = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				try {
					if (!client.equals(null)) {
						client.addSendObject(new TransactionDetailsRequest());
						refresh();
						this.stop();
					}
				} catch (Exception e) {
				}
			}
		};
		set.start();
	}

	@Override
	public void initData(Object client) {
		this.client = (Client) client;
		System.out.println(this.client);
	}

	private boolean isRefresing;

	public void refresh() {
		if (isRefresing) {
			return;
		}
		isRefresing = true;
		
		client.addSendObject(new TransactionDetailsRequest());
		items = FXCollections.observableArrayList();
		items.clear();
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
		
		isRefresing = false;
	}

	private boolean getHistoryInfoData() {
		ArrayList<TransactionDetailsInfo> historyInfo = this.client.getTransactionDetailsData();
		if (historyInfo == null) {
			return false;
		}

		for (int i = 0; i < historyInfo.size(); i++) {
			items.add(new HistoryTable(historyInfo.get(i)));

		}
		tbHistoryView.setItems(items);

		return true;
	}

}
