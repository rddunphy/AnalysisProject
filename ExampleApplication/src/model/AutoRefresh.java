package model;

public class AutoRefresh implements Runnable {
	
	private static final int TIMER = 30000;
	private Folio folio;
	
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
