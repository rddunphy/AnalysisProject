package model.folios;

import model.io.IQuote;
import model.io.Quote;

public class StockImpl implements Stock {

	private String ticker;
	private String name;
	private long shares;
	private long cost;
	private long price;
	private boolean dataAvailable;

	public StockImpl(String ticker, String name, long shares, long cost) {
		this.ticker = ticker.toUpperCase();
		this.name = name;
		this.shares = shares;
		this.cost = cost;
		this.price = -1;
		this.dataAvailable = false;
	}

	public StockImpl(String ticker) {
		this(ticker, ticker, 0, 0);
	}

	@Override
	public String getTicker() {
		return ticker;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getShares() {
		return shares;
	}

	@Override
	public void buyShares(long shares) {
		this.shares += shares;
		this.cost += shares * price;
	}

	@Override
	public void sellShares(long shares) {
		if (shares > this.shares)
			shares = this.shares;
		this.shares -= shares;
		this.cost -= shares * price;
	}

	@Override
	public long getCost() {
		return cost;
	}

	@Override
	public long getPrice() {
		return price;
	}

	@Override
	public long getValue() {
		return shares * price;
	}

	@Override
	public long getProfitLoss() {
		return getValue() - cost;
	}

	@Override
	public boolean isDataAvailable() {
		return dataAvailable;
	}

	@Override
	public void update() {
		IQuote quote = new Quote(false);
		try {
			quote.setValues(ticker);
			this.price = (long) (quote.getLatest() * 100);
			this.name = quote.getName();
			this.dataAvailable = true;
		} catch (Exception e) {
			this.price = -1;
			this.dataAvailable = false;
		}
	}
	
	@Override
	public int hashCode() {
		return ticker.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass())
			return false;
		Stock stock = (Stock) o;
		return this.ticker.equals(stock.getTicker());
	}
	
	@Override
	public String toString() {
		return ticker + " (" + name + ") x " + shares + " at cost " + cost;
	}

}