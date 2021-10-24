package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import formet.message.History;
import javafx.fxml.Initializable;
import util.Util;

public class GraphController extends Controller{

	protected History history;
	public void initData(Object history) {
		this.history = (History) history;
    }
	
	private void getHistory() {
		if(this.history != null) {
			System.out.println(this.history.toString());
		} else {
			System.out.println("null");
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("graphStart");
		Thread graphDrawThread = new Thread(new Runnable() {
			@Override
			public void run() {
				getHistory();
			}
		});
		Main.ThreadList.add(graphDrawThread);
		graphDrawThread.start();
	}
}
