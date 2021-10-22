package formet;

public class PriceInfo {
	int closePrice = 0;
	int startPrice = 0;
	int highPrice = 0;
	int lowPrice = 0;
	
	public PriceInfo(int closePrice, int startPrice, int highPrice, int lowPrice) {
		this.closePrice = closePrice;
		this.startPrice = startPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
	}
}
