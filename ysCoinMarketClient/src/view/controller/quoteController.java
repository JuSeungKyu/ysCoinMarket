package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import format.QuoteInfo;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import network.Client;
import view.userFxmlTag.quoteTable;;

public class quoteController extends Controller {
	@FXML
	public TableView<quoteTable> viewMain;
	private ObservableList<quoteTable> items;
	@FXML
	public TableColumn<quoteTable, Integer> normalColumn;
	@FXML
	public TableColumn<quoteTable, Integer> accumulateColumn;
	@FXML
	public TableColumn<quoteTable, Integer> orderColumn;
	private Client client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		{
			viewMain.setItems(items);
			System.out.println("tableStart");
			items = FXCollections.observableArrayList();
			viewMain.setItems(items);
			normalColumn.setCellValueFactory(cellData -> cellData.getValue().getNormal().asObject());
			accumulateColumn.setCellValueFactory(cellData -> cellData.getValue().getAccumulate().asObject());
			orderColumn.setCellValueFactory(cellData -> cellData.getValue().getOrder().asObject());

			AnimationTimer setType = new AnimationTimer() {
				@Override
				public void handle(long timestamp) {
					try {
						getquote();
					} catch (Exception e) {
//						e.printStackTrace();
					}

				}

			};
			setType.start();
		}
	}

	public boolean getquote() {
		QuoteInfo[] quoteInfo = this.client.getQuoteInfoList();
		if (quoteInfo.length == 0) {
			return false;
		}
		items.clear();
		for (QuoteInfo quoteInfo2 : quoteInfo) {
			quoteTable c = new quoteTable(quoteInfo2.normal, quoteInfo2.accumulate, quoteInfo2.order);
			items.add(c);
		}
		return true;
	}

	public void initData(Object data) {
		this.client = (Client) data;
	}

}
