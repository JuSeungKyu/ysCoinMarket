package view.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import application.Main;
import format.PriceInfo;
import format.message.History;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import network.Client;
import util.Util;
import view.userFxmlTag.ToggleSwitch;

public class GraphController extends Controller {
	@FXML
	Pane switchPane;
	@FXML
	RadioButton date;
	@FXML
	RadioButton hour;
	@FXML
	RadioButton minute;
	
	@FXML
	Canvas graph;
	private GraphicsContext gc;
	private SimpleDateFormat minuteFormat = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Client client;
	
	private boolean graphType = false;
	private byte graphBlockType = 0;

	ToggleGroup toggleGroup = new ToggleGroup();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("graphStart");
		gc = graph.getGraphicsContext2D();
		btnSet();
		blockGroupSet();

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
		
		
		gc.clearRect(0, 0, graph.getWidth(), graph.getHeight());
		canvasDrawLine(0, h, w, h);
		canvasDrawLine(w, 0, w, h);
		gc.setTextAlign(TextAlignment.CENTER);
		
//		그래프 그리기
		if(this.graphType) {
			drawCurvedLineGraph(count, pi, w, h, priceScale, low);
		} else {
			drawCandleGraph(count, pi, w, h, priceScale, high, low);
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
	
	private void drawCurvedLineGraph(short count, PriceInfo[] pi, int w, int h, int priceScale, int low) {
		int oneblockScale = (int) w/count;
		for(short i = 0; i < count-1; i++) {
//			그래프 그림 그리기
			int x = w-(oneblockScale*i+oneblockScale/2);
			int nextX = w-(oneblockScale*(i+1)+oneblockScale/2);
			
			canvasDrawLine(x, h-(pi[i].closePrice-low)/priceScale, nextX, h-(pi[i+1].closePrice-low)/priceScale);
			gc.fillOval(x-4, h-(pi[i].closePrice-low)/priceScale-4, 8, 8);
			
//			그래프 시간 텍스트 그리기
			if(i%4 == 0) {
				gc.setFill(Color.BLACK);
				gc.setFont(new Font(12));
				gc.fillText(minuteFormat.format(pi[i].time), x, h+30);
				canvasDrawLine(x, h+10, x, h);
			}
		}
		
		gc.fillOval(w-(oneblockScale*(count-1)+oneblockScale/2)-4, h-(pi[count-1].closePrice-low)/priceScale-4, 8, 8);
	}
	
	private void drawCandleGraph(short count, PriceInfo[] pi, int w, int h, int priceScale, int high, int low) {
		int rectScale = (int) w/count;
		
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
	}

	private void canvasDrawLine(int x1, int y1, int x2, int y2) {
		gc.beginPath();
		gc.moveTo(x1, y1);
        gc.lineTo(x2, y2);
		gc.stroke();
	}
	
	private void btnSet() {
		ToggleSwitch btn = new ToggleSwitch("촛불 그래프", "꺾은선 그래프", 
        		"-fx-background-color: #ffffff; -fx-text-fill:#c8c8c8; -fx-border-color:#c8c8c8;",
        		"-fx-background-color: #e6e6ff; -fx-text-fill:#6735fb; -fx-border-color:#6735fb;"
        );
		
        btn.switchOnProperty().addListener((obser, oldV, newV) -> {
        	graphType = btn.switchOnProperty().get();
		});
        
        switchPane.getChildren().add(btn);
	}
		
	private void blockGroupSet() {
		minute.setSelected(true);
		date.setToggleGroup(toggleGroup);
		hour.setToggleGroup(toggleGroup);
		minute.setToggleGroup(toggleGroup);
	}
	
	public void blockUpdate() {
		client.changeHistoryBlock(((Node) toggleGroup.getSelectedToggle()).getId());
	}
}
