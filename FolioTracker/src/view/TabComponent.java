package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class TabComponent extends JPanel {

	/**
	 * Construct a JPanel containing the title of the tab and a button for closing the tab.
	 * @param pane The pane in which the tab is located
	 * @param title The title of the tab
	 */
	public TabComponent(JTabbedPane pane, String title) {
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		JLabel titleLabel = new JLabel(title);
		JButton closeButton = new JButton("x");
		int size = 16;
		closeButton.setPreferredSize(new Dimension(size, size));
		closeButton.setContentAreaFilled(false);
		closeButton.setFocusable(false);
		closeButton.setBorder(BorderFactory.createEtchedBorder());
		closeButton.setBorderPainted(true);
		TabComponent t = this;
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pane.removeTabAt(pane.indexOfTabComponent(t));
			}
		});
		this.add(titleLabel, BorderLayout.WEST);
		// Add some space between the label and the button:
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		this.add(closeButton, BorderLayout.EAST);
	}
}
