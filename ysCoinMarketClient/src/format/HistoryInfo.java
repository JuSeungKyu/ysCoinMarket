package format;

import java.io.Serializable;
import java.sql.Time;

public class HistoryInfo implements Serializable {
	public String name;
	public String type;
	public int price;
	public Time time;
	public int whether; //된건지 안된건지~~~
	
	public HistoryInfo(String name, String type, int price, Time time, int whether) {
		super();
		this.name = name;
		this.type = type;
		this.price = price;
		this.time = time;
		this.whether = whether;
	}
	
	
}
