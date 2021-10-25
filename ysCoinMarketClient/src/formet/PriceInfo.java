package formet;

import java.io.Serializable;
import java.sql.Time;

public class PriceInfo implements Serializable{
	public int closePrice = 0;
	public int startPrice = 0;
	public int highPrice = 0;
	public int lowPrice = 0;
	public Time time;
	
	public PriceInfo(int startPrice, int closePrice, int highPrice, int lowPrice, Time time) {
		this.closePrice = closePrice;
		this.startPrice = startPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.time = time;
	}
}
