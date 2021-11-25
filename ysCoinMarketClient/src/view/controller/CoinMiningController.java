package view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import coin.Block;
import format.CoinInfo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import network.Client;
import util.Util;
import util.uiUpdate.UIUpdateClass;
import util.uiUpdate.UIUpdateThread;

public class CoinMiningController extends Controller {

	@FXML
	private Label typeLbl;
	@FXML
	private Label timeLbl;
	@FXML
	private Label numberLbl;
	@FXML
	private Label frequencyLbl;
	@FXML
	private Button closeBtn;

	private Client client = null;
	private CoinInfo coin = null;
	private boolean isReady = false;

	public boolean setType(String type) {
		new UIUpdateThread() {
			
			@Override
			public void update() {
				typeLbl.setText(type);
			}
		}.start();
		
		return true;
	}
	
	public boolean setTime() {
		long startTime = System.currentTimeMillis() + 32400000;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		Timestamp ts = new Timestamp(System.currentTimeMillis() - startTime);
		new UIUpdateThread() {
			
			@Override
			public void update() {
				while(new Util().sleep(1000)) {
					ts.setTime(System.currentTimeMillis() - startTime);
					timeLbl.setText("경과 시간 : " + sdf.format(ts));
				}
			}
		}.start();
		
		return true;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		while(!isReady);
		

		setType(coin.getCoinId());
		setTime();

		Thread coinMiningTHread = new Thread(() -> {
			new UIUpdateClass() {
				@Override
				public void update() {
					numberLbl.setText("0");
				}
			}.start();
			
			while (client == null && coin == null)
				;
			while (client.getHash() == null) {
				System.out.println(client.getHash());
			}

			String PreviousHash = client.getHash();
			Block previousBlock = new Block(PreviousHash, coin.getCoinDifficulty());

			System.out.println("Block : " + previousBlock.getValidHashString());

			while (true) {
				Block block = new Block(previousBlock.getValidHashString(), coin.getCoinDifficulty());
				previousBlock = block;
				new UIUpdateClass() {
					@Override
					public void update() {
						numberLbl.setText(Integer.toString(Integer.parseInt(numberLbl.getText()) + 1));
					}
				}.start();
				client.addBlock(block.getPrevHashString());
			}
		});

		Main.ThreadList.add(coinMiningTHread);
		coinMiningTHread.start();

	}

	@Override
	public void initData(Object data) {
		this.client = (Client) data;
		this.coin = new CoinInfo(this.client.getCurrentCoinId(), this.client.getCurrentCoinDifficulty());
		isReady = true;
	}

}
