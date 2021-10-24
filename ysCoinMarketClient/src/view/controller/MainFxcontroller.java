package view.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import network.Client;


public class MainFxcontroller extends Controller{
	@FXML
    private AnchorPane root;
	@FXML
    private AnchorPane pane1;
	@FXML
    private AnchorPane pane2;
	
	private Client client;
	public void initData(Object client) {
		this.client = (Client)client;
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println(11);
		loadPage();
	}

    public void loadPage() {
        try {
        	AnchorPane graph = FXMLLoader.load(getClass().getResource("../fxml/Graph.fxml"));
        	pane2.getChildren().add(graph);
        	pane1.getChildren().add(FXMLLoader.load(getClass().getResource("../fxml/CoinTypeList.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}