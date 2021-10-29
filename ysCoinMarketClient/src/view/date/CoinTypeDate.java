package view.date;

import javafx.beans.property.StringProperty;

public class CoinTypeDate {
	private StringProperty name;
	private StringProperty change;

	public CoinTypeDate(StringProperty name, StringProperty change) {
		this.name = name;
		this.change = change;
	}

	public StringProperty nameProperty() {
		return name;
	}

	public StringProperty changeProperty() {
		return change;
	}

}
