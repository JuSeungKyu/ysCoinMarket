package view.userFxmlTag;

public class HistoryTable {
	public String coin_id; // 이름
	public int ordering_amount; // 주문량
	public int penalty_amount; // 채결량
	public int price; //가격
	public String order_type; // 주문 종류(매수, 매도)
	public String time; // 시간(어차피 정렬되어서 전달받기 때문에 정렬할 필요 없음)
	
	public HistoryTable(String coin_id, int ordering_amount, int penalty_amount, int price, String order_type,
			String time) {
		super();
		this.coin_id = coin_id;
		this.ordering_amount = ordering_amount;
		this.penalty_amount = penalty_amount;
		this.price = price;
		this.order_type = order_type;
		this.time = time;
	}
	
	public String getCoin_id() {
		return coin_id;
	}
	public void setCoin_id(String coin_id) {
		this.coin_id = coin_id;
	}
	public int getOrdering_amount() {
		return ordering_amount;
	}
	public void setOrdering_amount(int ordering_amount) {
		this.ordering_amount = ordering_amount;
	}
	public int getPenalty_amount() {
		return penalty_amount;
	}
	public void setPenalty_amount(int penalty_amount) {
		this.penalty_amount = penalty_amount;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	
}
