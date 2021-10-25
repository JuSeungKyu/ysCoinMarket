package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import formet.PriceInfo;
import formet.message.History;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import network.Client;
import util.Util;

public class GraphController extends Controller {

	@FXML
	Canvas graph;
	private GraphicsContext gc;

	private Client client;

	public void initData(Object client) {
		this.client = (Client) client;
		System.out.println("전달받음");
	}

	private void getHistory() {
		History history = this.client.getHistory();
		if (history == null) {
			return;
		}

		PriceInfo[] pi = history.info;

		int high = pi[0].highPrice;
		int low = pi[0].lowPrice;
		short count = 1;
		for (; pi[count] != null; count++) {
			if (high < pi[count].highPrice) {
				high = pi[count].highPrice;
			}
			if (low > pi[count].lowPrice) {
				low = pi[count].lowPrice;
			}
		}

		int w = (int) graph.getWidth();
		int h = (int) graph.getHeight();
		
		if(h == 0 || w== 0) {
			return;
		}
		
		int priceScale = (int) (high - low) / h;
		
		if(priceScale == 0) {
			return;
		}
		
		int rectScale = (int) w/count;
		
		gc.clearRect(0, 0, w, h);
		for(short i = 0; i < count; i++) {
			if(pi[i].startPrice > pi[i].closePrice) {
				gc.setFill(Color.BLUE);
				canvasDrawLine(w-(rectScale*i+rectScale/2), (pi[i].lowPrice-low)/priceScale, w-(rectScale*i+rectScale/2), (pi[i].highPrice-low)/priceScale);
				gc.fillRect(w-rectScale*i, (pi[i].startPrice-low)/priceScale, rectScale, ((pi[i].startPrice-low)/priceScale) - ((pi[i].closePrice-low)/priceScale) );
			} else {
				gc.setFill(Color.RED);
				canvasDrawLine(w-(rectScale*i+rectScale/2), (pi[i].lowPrice-low)/priceScale, w-(rectScale*i+rectScale/2), (pi[i].highPrice-low)/priceScale);
				gc.fillRect(w-rectScale*i, (pi[i].closePrice-low)/priceScale, rectScale, ((pi[i].closePrice-low)/priceScale) - ((pi[i].startPrice-low)/priceScale) );
			}
		}
	}

	public void canvasDrawLine(int x1, int y1, int x2, int y2) {
		gc.beginPath();
		gc.moveTo(x1, y1);
        gc.lineTo(x2, y2);
		gc.stroke();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("graphStart");
		gc = graph.getGraphicsContext2D();

		Thread graphDrawThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Util util = new Util();
				while (true) {
					if (!util.sleep(100)) {
						break;
					}
					getHistory();
				}
			}
		});
		Main.ThreadList.add(graphDrawThread);
		graphDrawThread.start();
	}
}
