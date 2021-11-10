package view.userFxmlTag;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CoinTypeTable {
	private StringProperty name;
	private IntegerProperty change;
	
	public CoinTypeTable() {
		// X
	}
	
	public CoinTypeTable(String name, int change) {
		this.name = new SimpleStringProperty(name);
		this.change = new SimpleIntegerProperty(change);
	}

	public StringProperty getName() {
		return name;
	}

	public void setName(StringProperty name) {
		this.name = name;
	}

	public IntegerProperty getChange() {
		return change;
	}

	public void setChange(IntegerProperty change) {
		this.change = change;
	}
	

}
