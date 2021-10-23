package formet;

import java.sql.Time;

public class PriceInfo {
	int closePrice = 0;
	int startPrice = 0;
	int highPrice = 0;
	int lowPrice = 0;
	Time time;
	
	public PriceInfo(int startPrice, int closePrice, int highPrice, int lowPrice, Time time) {
		this.closePrice = closePrice;
		this.startPrice = startPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.time = time;
	}
}
