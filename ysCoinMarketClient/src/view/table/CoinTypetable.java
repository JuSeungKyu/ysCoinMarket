package view.table;

import javafx.beans.property.StringProperty;

public class CoinTypetable {
	private StringProperty name;
	private StringProperty change;

	public CoinTypetable(StringProperty name, StringProperty change) {
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
