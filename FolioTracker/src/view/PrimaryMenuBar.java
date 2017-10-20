
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import model.folios.Folio;

@SuppressWarnings("serial")
public class PrimaryMenuBar extends JMenuBar implements Observer {
	
	private View view;
	private ActionListener actionListener;
	
	private JMenu fileMenu;
	private JMenu folioSubmenu;
	private JMenu stockMenu;
	private JMenu helpMenu;

	public PrimaryMenuBar(View view, ActionListener actionListener) {
		this.view = view;
		this.actionListener = actionListener;
		view.getFolioTracker().addObserver(this);
		
		folioSubmenu = new JMenu("Open folio");
		folioSubmenu.setMnemonic('O');
		//populateFolioSubmenu();
		
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		fileMenu.add(createMenuItem("New folio", "new", 'N', "ctrl N"));
		fileMenu.add(folioSubmenu);
		fileMenu.addSeparator();
		fileMenu.add(createMenuItem("Quit", "quit", 'Q', "alt F4"));
		this.add(fileMenu);
		
		stockMenu = new JMenu("Stock");
		stockMenu.setMnemonic('S');
		stockMenu.add(createMenuItem("Buy stock", "buy", 'B', "ctrl B"));
		stockMenu.add(createMenuItem("Sell stock", "sell", 'S', "ctrl shift B"));
		stockMenu.add(createMenuItem("Update values", "update", 'U', "ctrl U"));
		this.add(stockMenu);
		
		helpMenu = new JMenu("?");
		helpMenu.setMnemonic('?');
		helpMenu.add(createMenuItem("Help", "help", 'H', "F1"));
		helpMenu.add(createMenuItem("About", "about", 'A'));
		this.add(helpMenu);
	}

	private JMenuItem createMenuItem(String name, String action, char mnemonic, String accelerator) {
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(actionListener);
		item.setActionCommand(action);
		if (mnemonic != '\0')
			item.setMnemonic(mnemonic);
		if (accelerator != null)
			item.setAccelerator(KeyStroke.getKeyStroke(accelerator));
		return item;
	}

	private JMenuItem createMenuItem(String name, String action, char mnemonic) {
		return createMenuItem(name, action, mnemonic, null);
	}
	
	public void populateFolioSubmenu() {
		for (Folio f : view.getFolioTracker().getFolios()) {
			JMenuItem item = new JMenuItem(f.getName());
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					view.openFolio(f);
				}});
			folioSubmenu.add(item);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		folioSubmenu.removeAll();
		populateFolioSubmenu();
	}
}
