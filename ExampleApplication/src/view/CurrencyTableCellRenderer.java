package view;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class CurrencyTableCellRenderer extends DefaultTableCellRenderer {

	private static final Format formatter = new DecimalFormat("#0.00");
	private Color defaultForegroundColor;
	
	public CurrencyTableCellRenderer(Color defaultForegroundColor) {
		this.defaultForegroundColor = defaultForegroundColor;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		this.setHorizontalAlignment(JLabel.RIGHT);
		try {
			if ((double) value < 0) {
				this.setForeground(Color.RED);
			} else {
				this.setForeground(defaultForegroundColor);
			}
			value = formatter.format((Number) value);
		} catch (ClassCastException e) {
			// Value is not a number, so don't format as one.
			this.setForeground(defaultForegroundColor);
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}