package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import network.Client;
import view.userFxmlTag.quoteControllerTable;;

public class quoteController extends Controller {
	@FXML
	public TableView<quoteControllerTable> viewMain;
	private ObservableList<quoteControllerTable> items;
	@FXML
	public TableColumn<quoteControllerTable, String> normalColumn;
	@FXML
	public TableColumn<quoteControllerTable, Integer> accumulateColumn;
	@FXML
	public TableColumn<quoteControllerTable, Integer> orderColumn;
	private Client client;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void initData(Object data) {
		this.client = (Client) data;
	}

}
