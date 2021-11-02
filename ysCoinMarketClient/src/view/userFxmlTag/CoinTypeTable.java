package view.userFxmlTag;

import javafx.beans.property.StringProperty;

public class CoinTypeTable {
	private StringProperty name;
	private StringProperty change;

	public CoinTypeTable(StringProperty name, StringProperty change) {
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
