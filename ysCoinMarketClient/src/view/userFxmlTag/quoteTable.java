package view.userFxmlTag;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class quoteTable {
	private IntegerProperty normal;
	private IntegerProperty accumulate;
	private IntegerProperty order;
	public quoteTable(int normal , int accumulate,int order) {

		this.normal = new SimpleIntegerProperty(normal);
		this.accumulate = new SimpleIntegerProperty(accumulate);
		this.order = new SimpleIntegerProperty(order);
		
	}
	public IntegerProperty getNormal() {
		return normal;
	}
	public void setNormal(IntegerProperty normal) {
		this.normal = normal;
	}
	public IntegerProperty getAccumulate() {
		return accumulate;
	}
	public void setAccumulate(IntegerProperty accumulate) {
		this.accumulate = accumulate;
	}
	public IntegerProperty getOrder() {
		return order;
	}
	public void setOrder(IntegerProperty order) {
		this.order = order;
	}
}
