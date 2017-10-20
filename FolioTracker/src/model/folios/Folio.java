package model.folios;

import java.util.List;
import java.util.Observer;

/**
 * Created by Chris on 11/11/2016.
 */
public interface Folio {

	/**
	 * Get the file name that this folio will be saved under.
	 * 
	 * @return The file name
	 */
	public String getFileName();

	/**
	 * @return A list of stocks contained in this folio
	 */
	public List<Stock> getStocks();

	/**
	 * Find a stock with a given ticker symbol. Returns null if no such stock is
	 * present.
	 * 
	 * @param ticker
	 *            The ticker symbol
	 * @return The stock found in this folio
	 */
	public Stock getStock(String ticker);

	/**
	 * Add a new stock to this folio. If stock is already present, does nothing.
	 * 
	 * @param s
	 *            The stock
	 * @return True iff this was modified
	 */
	public boolean addStock(Stock s);

	/**
	 * Buy shares in a given stock.
	 * 
	 * @param ticker
	 *            The ticker symbol of the stock to buy
	 * @param shares
	 *            The number of shares to buy
	 */
	public void buyStock(String ticker, long shares);

	/**
	 * Get the name of this folio.
	 * 
	 * @return The name
	 */
	public String getName();

	/**
	 * @return The total value of all holdings in this folio
	 */
	public long getValue();

	/**
	 * @return The total profits or losses of all holdings in this folio
	 */
	public long getProfitLoss();

	/**
	 * Add an observer to the folio.
	 * 
	 * @param o
	 *            The observer
	 */
	public void addObserver(Observer o);

	/**
	 * Sell shares of a given stock.
	 * 
	 * @param stock
	 *            The stock to sell shares of
	 * @param shares
	 *            The number of shares to sell
	 */
	public void sellStock(Stock stock, long shares);

	/**
	 * Get new quotes for all stocks and update values accordingly.
	 */
	public void updateStocks();

	/**
	 * Set changed and update observers.
	 * 
	 * @param msg
	 *            The message to pass to the observers (null for no message)
	 */
	public void updateObservers(String msg);

	/**
	 * Set whether this folio is in the process of updating stock info or not.
	 * 
	 * @param updating
	 */
	public void setUpdating(boolean updating);
}
