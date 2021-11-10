package format;

import java.io.Serializable;

public class TypeInfo implements Serializable {
	public String name;
	public int currentPrice;

	public TypeInfo(String name, int currentPrice) {
		this.name = name;
		this.currentPrice = currentPrice;
	}
}
