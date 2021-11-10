package view.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import application.Main;
import format.PriceInfo;
import format.message.History;
import format.message.UpdateGraphRange;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
	Canvas graph;

	private GraphicsContext gc;
	private SimpleDateFormat minuteFormat = new SimpleDateFormat("HH:mm:ss");
	private Client client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("graphStart");
		gc = graph.getGraphicsContext2D();
		
		LongProperty lastUpdateTime = new SimpleLongProperty(0);
		AnimationTimer timer = new AnimationTimer() {
		    @Override
		    public void handle(long timestamp) {
	        	try {
					getHistory();
				} catch (Exception e) {}
				sendGraphRangeUpdateRequst();
		    }
		};
		timer.start();
		setEvent();
	}

	public void initData(Object client) {
		this.client = (Client) client;
		System.out.println("전달받음");
	}

	protected short beforeMousePointX = 0;
	protected short historyLength = 0;
	protected float movementX = 0;
	
	private void setEvent() {
		// 마우스 휠 이벤트
		this.graph.setOnScroll((ScrollEvent event) -> {
			short[] arr = { 0, 0 };
			if (event.getDeltaY() > 0) {
				arr[1] = 1;
			} else {
				arr[1] = -1;
			}
			this.client.addSendObject(new UpdateGraphRange(arr));
		});

		// 마우스 그래그 이벤트
		this.graph.setOnMousePressed((MouseEvent event)->{
			this.beforeMousePointX = (short) event.getX();
		});
		
		this.graph.setOnMouseDragged((MouseEvent event)->{
			this.movementX += (event.getX() - beforeMousePointX) / (this.graph.getWidth() / this.historyLength);
			this.beforeMousePointX = (short) event.getX();
		});
	}
	
	private void sendGraphRangeUpdateRequst() {
		if(Math.abs(this.movementX) > 1) {
			this.client.addSendObject(new UpdateGraphRange( 
					(short) Math.round(movementX)
			));
			this.movementX = 0;
		}
	}

	private void getHistory() {
		History history = this.client.getHistory();
		if (history == null) {
			return;
		}

		PriceInfo[] pi = history.info;
		this.historyLength = (short) pi.length;

		int[] temp = getHighPriceAndLowPrice(pi);
		int high = temp[0];
		int low = temp[1];

//		텍스트 그릴 영역을 뺸 너비와 높이
		int w = (int) graph.getWidth() - 80;
		int h = (int) graph.getHeight() - 40;

		if (h <= 0 || w <= 0) {
			return;
		}

		int priceScale = (int) (high - low) / h;

		if (priceScale == 0) {
			return;
		}

//		테두리 그리기, 그래프 초기화
		gc.clearRect(0, 0, graph.getWidth(), graph.getHeight());
		canvasDrawLine(0, h, w, h);
		canvasDrawLine(w, 0, w, h);
		gc.setTextAlign(TextAlignment.CENTER);

//		그래프 그리기
		drawCandleGraph(pi, w, h, priceScale, high, low);

//		그래프 가격 그리기
		drawPrice(high, low, h, w);
	}

	private void drawPrice(int high, int low, int h, int w) {
		gc.setFill(Color.BLACK);
		gc.setTextAlign(TextAlignment.LEFT);
		for (byte i = 0; i < 8; i++) {
			int y = Math.round((h - h / 8 * i));
			canvasDrawLine(w, y, w + 10, y);
			gc.fillText(Integer.toString(low + (high - low) / 8 * i), w + 15, y);
		}
	}

	private int[] getHighPriceAndLowPrice(PriceInfo[] pi) {
		int[] output = { pi[0].highPrice, pi[0].lowPrice };
		for (short i = 0; i < pi.length; i++) {
			if (output[0] < pi[i].highPrice) {
				output[0] = pi[i].highPrice;
			}
			if (output[1] > pi[i].lowPrice) {
				output[1] = pi[i].lowPrice;
			}
		}
		return output;
	}

	private void drawCandleGraph(PriceInfo[] pi, int w, int h, int priceScale, int high, int low) {
		int rectScale = (int) w / pi.length;
		float interval = (float) (pi.length / 4);

		for (short i = 0; i < pi.length; i++) {
			int x = w - (rectScale * i + rectScale / 2);
//			그래프 그림 그리기
			if (pi[i].startPrice > pi[i].closePrice) {
				gc.setFill(Color.BLUE);
				canvasDrawLine(x, h - (pi[i].lowPrice - low) / priceScale, x, h - (pi[i].highPrice - low) / priceScale);
				gc.fillRect(w - rectScale * (i + 1), h - (pi[i].startPrice - low) / priceScale, rectScale,
						((pi[i].startPrice - low) / priceScale) - ((pi[i].closePrice - low) / priceScale));
			} else {
				gc.setFill(Color.RED);
				canvasDrawLine(x, h - (pi[i].lowPrice - low) / priceScale, x, h - (pi[i].highPrice - low) / priceScale);
				gc.fillRect(w - rectScale * (i + 1), h - (pi[i].closePrice - low) / priceScale, rectScale,
						((pi[i].closePrice - low) / priceScale) - ((pi[i].startPrice - low) / priceScale));
			}

//			그래프 시간 텍스트 그리기
			if (i % interval == 0) {
				gc.setFill(Color.BLACK);
				gc.setFont(new Font(12));
				gc.fillText(minuteFormat.format(pi[i].time), x, h + 30);
				canvasDrawLine(x, h + 10, x, h);
			}

		}
	}

	private void canvasDrawLine(int x1, int y1, int x2, int y2) {
		gc.beginPath();
		gc.moveTo(x1, y1);
		gc.lineTo(x2, y2);
		gc.stroke();
	}

}