package model;

public interface Stock {

	/**
	 * Get the ticker symbol of the stock.
	 * 
	 * @return The ticker symbol
	 */
	public String getTicker();

	/**
	 * Get the name of the stock.
	 * 
	 * @return The name
	 */
	public String getName();

	/**
	 * Get the number of shares in the stock.
	 * 
	 * @return The number of shares
	 */
	public long getShares();

	/**
	 * Buy shares at the current price.
	 * 
	 * @param shares
	 *            The number of shares to buy
	 */
	public void buyShares(long shares);

	/**
	 * Sell shares at the current price.
	 * 
	 * @param shares
	 *            The number of shares to sell
	 */
	public void sellShares(long shares);

	/**
	 * Get the total cost of the holding.
	 * 
	 * @return The cost
	 */
	public long getCost();

	/**
	 * Get the current price per stock.
	 * 
	 * @return The price
	 */
	public long getPrice();

	/**
	 * Get the total value of the holding.
	 * 
	 * @return The value
	 */
	public long getValue();

	/**
	 * Get the total profit or lost on the holding. Losses are represented by
	 * negative values.
	 * 
	 * @return The profit or loss
	 */
	public long getProfitLoss();

	/**
	 * Get a new quote for this stock and update all derived values accordingly.
	 */
	public void update();

	/**
	 * Returns true iff recent data from the server is available for this stock.
	 * 
	 * @return True iff data is available
	 */
	public boolean isDataAvailable();
}
