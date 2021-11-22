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

	private Client client;
	private CoinInfo coin = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {


		AnimationTimer setType = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				if (!coin.getCoinId().isEmpty()) {
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
				client.addBlock();
			}
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
