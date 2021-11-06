package view.userFxmlTag;

import java.sql.Time;

public class HistoryTable {
	private String name;
	private String type;
	private int price;
	private Time time;
	private int whether; //된건지 안된건지
	
	public HistoryTable(String name, String type, int price, Time time, int whether) {
		super();
		this.name = name;
		this.type = type;
		this.price = price;
		this.time = time;
		this.whether = whether;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public int getWhether() {
		return whether;
	}

	public void setWhether(int whether) {
		this.whether = whether;
	}
	
}
