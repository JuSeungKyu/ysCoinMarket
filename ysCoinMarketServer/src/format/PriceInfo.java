package format;

import java.io.Serializable;
import java.sql.Time;

public class PriceInfo implements Serializable{
	public int closePrice;
	public int startPrice;
	public int highPrice;
	public int lowPrice;
	public Time time;
	
	public PriceInfo(int startPrice, int closePrice, int highPrice, int lowPrice, Time time) {
		this.closePrice = closePrice;
		this.startPrice = startPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.time = time;
	}
}
