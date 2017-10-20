package view;

import javax.swing.table.AbstractTableModel;

import model.folios.Folio;
import model.folios.Stock;

@SuppressWarnings("serial")
public class FolioTableModel extends AbstractTableModel {

	private static final int COLUMN_TICKER = 0;
	private static final int COLUMN_NAME = 1;
	private static final int COLUMN_SHARES = 2;
	private static final int COLUMN_COST = 3;
	private static final int COLUMN_PRICE = 4;
	private static final int COLUMN_VALUE = 5;
	private static final int COLUMN_PROFIT = 6;

	private String[] columnNames = { "Stock ID", "Name", "Shares", "Cost (" + CurrencyFormatter.DOLLAR + ")",
			"Price (" + CurrencyFormatter.DOLLAR + ")", "Value (" + CurrencyFormatter.DOLLAR + ")",
			"P/L (" + CurrencyFormatter.DOLLAR + ")" };
	private Folio folio;

	public FolioTableModel(Folio folio) {
		this.folio = folio;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return folio.getStocks().size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Stock stock = folio.getStocks().get(row);
		Object value;
		switch (col) {
		case COLUMN_TICKER:
			value = stock.getTicker();
			break;
		case COLUMN_NAME:
			value = stock.getName();
			break;
		case COLUMN_SHARES:
			value = stock.getShares();
			break;
		case COLUMN_COST:
			value = (double) stock.getCost() / 100;
			break;
		case COLUMN_PRICE:
			if (stock.isDataAvailable())
				value = (double) stock.getPrice() / 100;
			else
				value = "N/A";
			break;
		case COLUMN_VALUE:
			if (stock.isDataAvailable())
				value = (double) stock.getValue() / 100;
			else
				value = "N/A";
			break;
		case COLUMN_PROFIT:
			if (stock.isDataAvailable())
				value = (double) stock.getProfitLoss() / 100;
			else
				value = "N/A";
			break;
		default:
			throw new IllegalArgumentException("Column " + col + " does not exist in this table.");
		}
		return value;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		if (folio.getStocks().isEmpty())
			return Object.class;
		return getValueAt(0, col).getClass();
	}
}
