package view.userFxmlTag;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CoinTypeTable {
	private StringProperty name;
	private StringProperty change;
	
	public CoinTypeTable() {
		// X
	}
	
	public CoinTypeTable(String name, String change) {
		this.name = new SimpleStringProperty(name);
		this.change = new SimpleStringProperty(change);
	}

	public StringProperty getName() {
		return name;
	}

	public void setName(StringProperty name) {
		this.name = name;
	}

	public StringProperty getChange() {
		return change;
	}

	public void setChange(StringProperty change) {
		this.change = change;
	}
	
}
