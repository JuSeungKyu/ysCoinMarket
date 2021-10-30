package util;

import java.util.ArrayList;

import format.PriceInfo;

public class Util {
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
