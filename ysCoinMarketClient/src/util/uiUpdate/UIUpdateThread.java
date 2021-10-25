package util.uiUpdate;

import application.Main;

public abstract class UIUpdateThread extends UIUpdateClass{
//	fxml 스레드 동시성 때매 이 클래스를 사용하지 않고 다른 스레드에서 UI를 변경하면 안됨
	@Override
	public void start() {
		Thread t = new Thread(()->{
			super.start();
		});
		Main.ThreadList.add(t);
		t.start();
	}
}
