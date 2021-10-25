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
//		this.history = (History) history;
		System.out.println("전달받음");
    }
	
	private void getHistory() {
		if(this.history == null) {
			return;
		}
		
		System.out.println(this.history.toString());
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("graphStart");
		Thread graphDrawThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Util util = new Util();
				while(true) {
					util.sleep(10);
					getHistory();
				}
			}
		});
		Main.ThreadList.add(graphDrawThread);
		graphDrawThread.start();
	}
}
