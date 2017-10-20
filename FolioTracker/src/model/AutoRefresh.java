package model;

import model.folios.Folio;

public class AutoRefresh implements Runnable {
	
	private static final int TIMER = 30000;
	private Folio folio;

	class InternalClass {
		public int internalMethod() {
			int x = 3;
			int y = 4;
			return x + y;
		}
	}

	public AutoRefresh(Folio folio) {
		this.folio = folio;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(TIMER);
				folio.updateStocks();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}

}
