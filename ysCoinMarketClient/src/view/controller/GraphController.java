package view.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import application.Main;
import format.PriceInfo;
import format.message.History;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import network.Client;
import util.Util;

public class GraphController extends Controller {

	@FXML
	Canvas graph;
	private GraphicsContext gc;
	private SimpleDateFormat minuteFormat = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		for (; count < pi.length && pi[count] != null; count++) {
			if (high < pi[count].highPrice) {
				high = pi[count].highPrice;
			}
			if (low > pi[count].lowPrice) {
				low = pi[count].lowPrice;
			}
		}

//		텍스트 그릴 영역을 뺸 너비와 높이
		int w = (int) graph.getWidth()-80;
		int h = (int) graph.getHeight()-40;
		
		if(h <= 0 || w <= 0) {
			return;
		}
		
		int priceScale = (int) (high - low) / h;
		
		if(priceScale == 0) {
			return;
		}
		
		int rectScale = (int) w/count;
		
		gc.clearRect(0, 0, graph.getWidth(), graph.getHeight());
		canvasDrawLine(0, h, w, h);
		canvasDrawLine(w, 0, w, h);
		gc.setTextAlign(TextAlignment.CENTER);
		for(short i = 0; i < count; i++) {
			int x = w-(rectScale*i+rectScale/2);
//			그래프 그림 그리기
			if(pi[i].startPrice > pi[i].closePrice) {
				gc.setFill(Color.BLUE);
				canvasDrawLine(x, h-(pi[i].lowPrice-low)/priceScale, x, h-(pi[i].highPrice-low)/priceScale);
				gc.fillRect(w-rectScale*(i+1), h-(pi[i].startPrice-low)/priceScale, rectScale, ((pi[i].startPrice-low)/priceScale) - ((pi[i].closePrice-low)/priceScale) );
			} else {
				gc.setFill(Color.RED);
				canvasDrawLine(x, h-(pi[i].lowPrice-low)/priceScale, x, h-(pi[i].highPrice-low)/priceScale);
				gc.fillRect(w-rectScale*(i+1), h-(pi[i].closePrice-low)/priceScale, rectScale, ((pi[i].closePrice-low)/priceScale) - ((pi[i].startPrice-low)/priceScale) );
			}
			
//			그래프 시간 텍스트 그리기
			if(i%4 == 0) {
				gc.setFill(Color.BLACK);
				gc.setFont(new Font(12));
				gc.fillText(minuteFormat.format(pi[i].time), x, h+30);
				canvasDrawLine(x, h+10, x, h);
			}
			
		}

//		그래프 가격 그리기
		gc.setFill(Color.BLACK);
		gc.setTextAlign(TextAlignment.LEFT);
		for(byte i=0; i<8; i++) {
			int y = Math.round((h - h /8 * i));
			canvasDrawLine(w, y, w+10, y);
			gc.fillText(Integer.toString(low + (high - low)/8*i), w+15, y);
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
