package model.folios;

import model.*;
import model.io.MethodException;
import model.io.NoSuchTickerException;
import model.io.WebsiteDataException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * Created by Chris on 11/11/2016.
 */
public class FolioImpl extends Observable implements Folio {

	private String fileName;
	private String name;
	private FolioTracker ft;
	private List<Stock> stocks;
	private boolean updating;

	public FolioImpl(String name, FolioTracker ft, String fileName) {
		this.fileName = fileName;
		this.name = name;
		this.ft = ft;
		stocks = new ArrayList<>();
		updating = false;
		(new Thread(new AutoRefresh(this))).start();
	}

	@Override
	public List<Stock> getStocks() {
		return stocks;
	}

	@Override
	public boolean addStock(Stock s) {
		return stocks.add(s);
	}

	@Override
	public void buyStock(String ticker, long shares) {
		Stock stock;
		try {
			this.setChanged();
			for (Stock s : stocks) {
				if (s.getTicker().equals(ticker)) {
					s.update();
					s.buyShares(shares);
					notifyObservers();
					return;
				}
			}
			// if stock is new, get a quote and add it to the folio
			stock = ft.getQuote(ticker);
			stock.buyShares(shares);
			stocks.add(stock);
			notifyObservers();
		} catch (WebsiteDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTickerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getValue() {
		long total = 0;
		Iterator<Stock> it = stocks.iterator();
		while (it.hasNext()) {
			total += it.next().getValue();
		}
		return total;
	}

	@Override
	public long getProfitLoss() {
		long total = 0;
		Iterator<Stock> it = stocks.iterator();
		while (it.hasNext()) {
			total += it.next().getProfitLoss();
		}
		return total;
	}

	@Override
	public void sellStock(Stock stock, long shares) {
		for (Stock s : stocks) {
			if (s.equals(stock)) {
				s.sellShares(shares);

				if (s.getShares() == 0) {
					stocks.remove(s);
				}
				setChanged();
				notifyObservers();
				return;
			}
		}

	}

	@Override
	public Stock getStock(String ticker) {
		ticker = ticker.toUpperCase();
		for (Stock s : stocks) {
			if (s.getTicker().equals(ticker)) {
				return s;
			}
		}
		return null;
	}

	@Override
	public void updateStocks() {
		if (!updating) {
			updating = true;
			updateObservers("updating");
			new Thread(new UpdateStocksRunnable(this)).start();
		}
	}

	@Override
	public void updateObservers(String msg) {
		this.setChanged();
		if (msg == null) {
			this.notifyObservers();
		} else {
			this.notifyObservers(msg);
		}
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void setUpdating(boolean updating) {
		this.updating = updating;
	}

}
