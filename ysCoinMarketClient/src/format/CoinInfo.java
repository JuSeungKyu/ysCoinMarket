package format;

public class CoinInfo {
	public String coinId = null;
	public byte coinDifficulty = 0;
	
	public CoinInfo(String coinId, byte coinDifficulty) {
		super();
		this.coinId = coinId;
		this.coinDifficulty = coinDifficulty;
	}

	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	public byte getCoinDifficulty() {
		return coinDifficulty;
	}

	public void setCoinDifficulty(byte coinDifficulty) {
		this.coinDifficulty = coinDifficulty;
	}
}
