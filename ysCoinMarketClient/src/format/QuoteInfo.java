package format;

import java.io.Serializable;

public class QuoteInfo implements Serializable {
	public int normal;
	public int accumulate;
	public int order;

	public QuoteInfo(int normal, int accumulate, int order) {
		this.normal = normal;
		this.accumulate = accumulate;
		this.order = order;
	}
}
