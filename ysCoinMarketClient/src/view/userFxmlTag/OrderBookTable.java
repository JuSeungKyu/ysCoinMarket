package view.userFxmlTag;

public class OrderBookTable {
	private int sellPrice;
	private int volume;
	private int buyPrice;
	
	public OrderBookTable(int sellPrice, int volume, int buyPrice) {
		this.sellPrice = sellPrice;
		this.volume = volume;
		this.buyPrice = buyPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}
	
	public int getSellPrice() {
		return this.sellPrice;
	}
	
	public int getVolume() {
		return this.volume;
	}
	
	public int getBuyPrice() {
		return this.buyPrice;
	}
	
}
