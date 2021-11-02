package format;

import java.io.Serializable;

public class TypeInfo implements Serializable {
	public String name;
	public int currentPrice;
	public int beforePrice;

	public TypeInfo(String name, int currentPrice, int beforePrice) {
		this.name = name;
		this.currentPrice = currentPrice;
		this.beforePrice = beforePrice;

	}

}
