
package ctrlr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.View;

public class PrimaryActionListener implements ActionListener {
	
	private View view;

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "quit":
			view.exit();
			break;
        case "buy":
        	view.buyStock();
        	break;
        case "new":
        	view.newFolio();
        	break;
        case "sell":
        	view.sellStock();
        	break;
        case "update":
        	view.updateValues();
        	break;
        case "help":
        	view.displayHelp();
        	break;
        case "about":
        	view.displayAbout();
        	break;
		default:
			// Command not implemented
			return;
		}
	}
	
	public void setView(View view) {
		this.view = view;
	}
}

