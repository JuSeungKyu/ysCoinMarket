package view.userFxmlTag;

import javafx.beans.property.StringProperty;

public class HistoryTable {
	private StringProperty name;
	private StringProperty type;
	private StringProperty price;
	private StringProperty time;
	
	public HistoryTable(StringProperty name, StringProperty type, StringProperty price, StringProperty time) {
		super();
		this.name = name;
		this.type = type;
		this.price = price;
		this.time = time;
	}

	public StringProperty getName() {
		return name;
	}

	public void setName(StringProperty name) {
		this.name = name;
	}

	public StringProperty getType() {
		return type;
	}

	public void setType(StringProperty type) {
		this.type = type;
	}

	public StringProperty getPrice() {
		return price;
	}

	public void setPrice(StringProperty price) {
		this.price = price;
	}

	public StringProperty getTime() {
		return time;
	}

	public void setTime(StringProperty time) {
		this.time = time;
	}
	
}
