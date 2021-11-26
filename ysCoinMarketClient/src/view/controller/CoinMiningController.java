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
	private Label type;
	@FXML
	private Label time;
	@FXML
	private Label number;
	@FXML
	private Label frequency;
	@FXML
	private Button closeBtn;

	private Util util = new Util();
	private Client client = null;
	private CoinInfo coin = null;
	private boolean isReady = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Thread coinMiningTimingThread = new Thread(() -> {

			long startTime = System.currentTimeMillis() + 32400000;
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

			Timestamp ts = new Timestamp(System.currentTimeMillis() - startTime);
			do {
				new UIUpdateClass() {

					@Override
					public void update() {
						ts.setTime(System.currentTimeMillis() - startTime);
						time.setText("경과 시간 : " + sdf.format(ts));
						System.out.println(time.getText());
					}
				}.start();
			} while (util.sleep(1000));
		});

		System.out.println("setTime : " + time.getText());

		Thread coinMiningThread = new Thread(() -> {

			while (!isReady) {
				System.out.println("Waiting");
				if (!util.sleep(1000)) {
					break;
				}
			}

			System.out.println("isReady");

			coin.getCoinId();
			new UIUpdateClass() {

				@Override
				public void update() {
					type.setText(coin.getCoinId());
					System.out.println(type.getText());
				}
			}.start();

			System.out.println("setType : " + type.getText());

			new UIUpdateClass() {
				@Override
				public void update() {
					number.setText("0");
				}
			}.start();
			System.out.println("setNumber : " + number.getText());

			String PreviousHash = client.getHash();
			Block previousBlock = new Block(PreviousHash, coin.getCoinDifficulty());

			System.out.println("Block : " + previousBlock.getValidHashString());

			while (true) {
				Block block = new Block(previousBlock.getValidHashString(), coin.getCoinDifficulty());
				previousBlock = block;
				new UIUpdateClass() {
					@Override
					public void update() {
						number.setText(Integer.toString(Integer.parseInt(number.getText()) + 1));
					}
				}.start();
				client.addBlock(block.getPrevHashString());
			}
		});

		Main.ThreadList.add(coinMiningTimingThread);
		Main.ThreadList.add(coinMiningThread);
		coinMiningTimingThread.start();
		coinMiningThread.start();

	}

	@Override
	public void initData(Object data) {
		this.client = (Client) data;
		this.coin = new CoinInfo(this.client.getCurrentCoinId(), this.client.getCurrentCoinDifficulty());
		System.out.println("client ready");
		isReady = true;
	}

}
