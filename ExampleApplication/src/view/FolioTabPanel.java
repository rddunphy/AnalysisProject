package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import model.Folio;

@SuppressWarnings("serial")
public class FolioTabPanel extends JPanel implements Observer {

	private Folio folio;
	private JTable table;
	private JLabel totalValueLabel;
	private JLabel totalProfitLabel;
	private JLabel statusLabel;
	private Color defaultForegroundColor;

	public FolioTabPanel(Folio folio) {
		this.folio = folio;
		// Because apparently it's not black:
		defaultForegroundColor = new JLabel().getForeground();
		folio.addObserver(this);
		this.setLayout(new BorderLayout());
		this.add(buildHeaderPanel(), BorderLayout.PAGE_START);
		this.add(buildTablePane(), BorderLayout.CENTER);
		this.add(buildStatusPanel(), BorderLayout.PAGE_END);
		updateHeaderPanel();
	}

	private Component buildStatusPanel() {
		// Status panel is a bar along the bottom of the frame containing a
		// JLabel with information about the application's status.
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 18));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		return statusPanel;
	}

	private Component buildTablePane() {
		// Table pane is a JScrollPane containing the main data table.
		FolioTableModel model = new FolioTableModel(folio);
		table = new JTable(model);
		table.setCellSelectionEnabled(false);
		JScrollPane tablePane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		// Columns containing values in pounds need to be rendered with two
		// decimal places:
		TableColumnModel m = table.getColumnModel();
		for (int col = 3; col <= 6; col++) {
			// Columns 3 to 6 contain currency values.
			m.getColumn(col).setCellRenderer(new CurrencyTableCellRenderer(defaultForegroundColor));
		}
		// Code for sorting the table by first column, in ascending order:
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		for (int col = 0; col < table.getColumnCount(); col++) {
			sorter.setComparator(col, new FolioTableModelComparator<Object>());
		}
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		return tablePane;
	}

	private Component buildHeaderPanel() {
		// Header panel is a JPanel containing labels with information about the
		// folio such as the name and total value
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());
		JLabel titleLabel = new JLabel(folio.getName());
		titleLabel.setFont(titleLabel.getFont().deriveFont(18.0f));
		titleLabel.setBorder(new EmptyBorder(5, 10, 5, 5));
		totalValueLabel = new JLabel();
		totalValueLabel.setBorder(new EmptyBorder(5, 5, 0, 10));
		totalProfitLabel = new JLabel();
		totalProfitLabel.setBorder(new EmptyBorder(5, 5, 5, 10));
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.PAGE_AXIS));
		totalValueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		totalProfitLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		dataPanel.add(totalValueLabel);
		dataPanel.add(totalProfitLabel);
		headerPanel.add(titleLabel, BorderLayout.LINE_START);
		headerPanel.add(dataPanel, BorderLayout.LINE_END);
		return headerPanel;
	}

	private void updateHeaderPanel() {
		// Update the values for the total value and profits of the folio
		totalValueLabel.setText("Total value: " + CurrencyFormatter.format(folio.getValue()));
		long totalProfit = folio.getProfitLoss();
		if (totalProfit >= 0) {
			totalProfitLabel.setForeground(defaultForegroundColor);
		} else {
			totalProfitLabel.setForeground(Color.RED);
		}
		totalProfitLabel.setText("Total P/L: " + CurrencyFormatter.format(totalProfit));
	}

	public Folio getFolio() {
		return folio;
	}

	public void setStatusLabel(String status) {
		statusLabel.setText(status);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == null || arg.getClass() != String.class)
			arg = "";
		String str = (String) arg;
		switch (str) {
		case "updating":
			setStatusLabel("Loading data from web...");
			break;
		case "updated":
			setStatusLabel("");
			break;
		default:
			((AbstractTableModel) table.getModel()).fireTableDataChanged();
			updateHeaderPanel();
			break;
		}
	}
}
