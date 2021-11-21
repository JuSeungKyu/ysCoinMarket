package view.userFxmlTag;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class OrderBookTable {
	private IntegerProperty sellPrice;
	private IntegerProperty volume;
	private IntegerProperty buyPrice;
	
	public OrderBookTable(int sellPrice, int volume, int buyPrice) {
		this.sellPrice = new SimpleIntegerProperty(sellPrice);
		this.volume = new SimpleIntegerProperty(volume);
		this.buyPrice = new SimpleIntegerProperty(buyPrice);
	}

	public void setSellPrice(IntegerProperty sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	public void setVolume(IntegerProperty volume) {
		this.volume = volume;
	}
	
	public void setBuyPrice(IntegerProperty buyPrice) {
		this.buyPrice = buyPrice;
	}
	
	public IntegerProperty getSellPrice() {
		return this.sellPrice;
	}
	
	public IntegerProperty getVolume() {
		return this.volume;
	}
	
	public IntegerProperty getBuyPrice() {
		return this.buyPrice;
	}
	
}
