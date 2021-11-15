package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import format.CoinInfo;
import format.message.PreviousHashRequest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import network.Client;
import util.Util;

public class CoinMiningController extends Controller {
	
	@FXML
	private Label type;
	@FXML
	private Label time;
	@FXML
	private Label number;
	@FXML
	private Label frequency;
	
	private Client client;
	private CoinInfo coin = null;
	private PreviousHashRequest preHashReq;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimationTimer setType = new AnimationTimer() {
		    @Override
		    public void handle(long timestamp) {
        		if(!coin.getCoinId().isEmpty()) {
        			type.setText("채굴 종목 : " + coin.getCoinId());
        			this.stop();
        		}
		    }
		};
		setType.start();
		long startTime = System.currentTimeMillis() + 32400000;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		Timestamp ts = new Timestamp(System.currentTimeMillis() - startTime);
		AnimationTimer updateTime = new AnimationTimer() {
		    @Override
		    public void handle(long timestamp) {
		    	ts.setTime(System.currentTimeMillis() - startTime);
		    	time.setText("경과 시간 : " + sdf.format(ts));
		    }
		};
		
		updateTime.start();
		
		Thread coinMiningTHread = new Thread(() -> {
			while(coin == null);
			
			this.preHashReq = new PreviousHashRequest(this.coin.getCoinId());
			
			this.client.addSendObject(preHashReq);
			
			while(client.getHash() == null) {
				System.out.println(client.getHash());
			}
			
			String PreviousHash = client.getHash();
			
			System.out.println("coin type : " + coin.getCoinId() + ", previous hash : " + PreviousHash);
		});

		Main.ThreadList.add(coinMiningTHread);
		coinMiningTHread.start();
	}

	@Override
	public void initData(Object data) {
		this.client = (Client) data;
		this.coin = new CoinInfo(this.client.getCurrentCoinId(), this.client.getCurrentCoinDifficulty());
	}
	
}
