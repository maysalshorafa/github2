package com.pos.leaders.leaderspossystem.CashBox;

public class JBInterface {

	/**
	 * Open CashBox ��Ǯ��
	 * */
	public static void openCashBox() {
		new Thread(openBox).start();
	}

	/**
	 * Close CashBox �ر�Ǯ��
	 * */
	public static boolean closeCashBox() {
		int result = GpioControl.activate(GpioControl.qx_o, false);
		if (result == 0)
			return true;
		else
			return false;
	}
	
	public static Runnable openBox = new Runnable() {
		public void run() {
			try {
				System.err.println("open box");
				GpioControl.activate(GpioControl.qx_o, true);
				Thread.currentThread().sleep(500);
				GpioControl.activate(GpioControl.qx_o, false);
				System.err.println("close box");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
}
