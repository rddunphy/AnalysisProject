
package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import model.folios.Folio;
import model.folios.FolioTracker;
import model.folios.Stock;
import model.io.MethodException;
import model.io.NoSuchTickerException;
import model.io.WebsiteDataException;

@SuppressWarnings("serial")
public class PrimaryFrame extends JFrame implements View, Observer {

    public static final String NAME = "Folio Tracker";
    public static final String AUTHORS = "Omer Abdullah, Salman Ahmed, Khizer Asghar, Chris Conway, R. David Dunphy";

    private JMenuBar menuBar;
    private JTabbedPane contentPane;
    private FolioTracker ft;

    public PrimaryFrame(FolioTracker ft, ActionListener actionListener, WindowListener windowListener) {
        this.setTitle(NAME);
        this.ft = ft;
        ft.addObserver(this);
        setSizeAndCentre(600, 450);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(windowListener);
        this.menuBar = new PrimaryMenuBar(this, actionListener);
        this.setJMenuBar(menuBar);
        contentPane = new JTabbedPane();
        this.setContentPane(contentPane);
        this.setVisible(true);
    }

    @Override
    public void exit() {
        ft.saveFolios();
        System.exit(0);
    }

    @Override
    public void buyStock() {
        JPanel input = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField inTicker = new JTextField();
        input.add(new JLabel("Ticker:"));
        input.add(inTicker);

    	FolioTabPanel tab = getActiveTab();
    	if (tab == null) {
    		JOptionPane.showMessageDialog(this, "Create a folio to buy stocks!", "Cannot buy stocks", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
        while (true) {
            int option = JOptionPane.showConfirmDialog(this, input, "Buy Stocks", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    Stock addingSt = ft.getQuote(inTicker.getText());
                    if(!addingSt.isDataAvailable())
                        throw new NoSuchTickerException(addingSt.getTicker());
                    while (true) {
                        JPanel addInput = new JPanel(new GridLayout(0, 1, 2, 2));
                        JLabel tickerAmount = new JLabel("Ticker: " + addingSt.getTicker() + " Price: "
                                + CurrencyFormatter.format(addingSt.getPrice()));
                        addInput.add(tickerAmount);
                        JTextField amount = new JTextField();
                        addInput.add(new JLabel(("Amount")));
                        addInput.add(amount);
                        int nextOption = JOptionPane.showConfirmDialog(null, addInput, "Buy Stocks",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (nextOption == JOptionPane.OK_OPTION) {
                            Folio currentFolio = tab.getFolio();
                            int num_shares;
                            try {
                                num_shares = Integer.parseInt(amount.getText());
                                if (num_shares > 0) {
                                    currentFolio.buyStock(addingSt.getTicker(), num_shares);
                                    return;
                                }else{
                                    printErrorMessage("Invalid number:" + num_shares + ", please keep it above 0");
                                }
                            } catch (NumberFormatException e) {
                                printErrorMessage("Please enter a valid number of shares to purchase");
                            }
                        } else {
                            return;
                        }
                    }
                } catch (IOException | MethodException e) {
                    e.printStackTrace();
                } catch (NoSuchTickerException e) {
                    printErrorMessage("Ticker: '" + e.getMessage() + "' does not exist, try again");
                } catch (WebsiteDataException e) {
                    printErrorMessage(
                            "Error connecting to site, please check  your internet connection or try again later");
                } catch (NumberFormatException e) {
                    printErrorMessage("Invalid ticker, please try again");
                }
            } else {
                return;
            }
        }
    }

    public void printErrorMessage(String err) {
        JOptionPane.showMessageDialog(null, err, "Error", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void openFolio(Folio f) {
        int i = getTabIndex(f);
        if (i == -1) {
            i = contentPane.getTabCount();
            addFolioTab(f);
        }
        contentPane.setSelectedIndex(i);
    }

    @Override
    public void newFolio() {
        String name = (String) JOptionPane.showInputDialog(this, "Please enter a name for your folio:",
                "Create new folio", JOptionPane.PLAIN_MESSAGE, null, null, null);
        // If a name was returned, create and open the new folio:
        if ((name != null) && (name.length() > 0)) {
            openFolio(ft.addNewFolio(name));
        }
    }

    @Override
    public FolioTracker getFolioTracker() {
        return ft;
    }

    @Override
    public void updateValues() {
        for (int i = 0; i < contentPane.getTabCount(); i++) {
            FolioTabPanel p = (FolioTabPanel) contentPane.getComponentAt(i);
            p.getFolio().updateStocks();
        }
    }
    
    @Override
    public void showErrorMessage(String title, String msg) {
    	JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }
    
    @Override
    public void update(Observable o, Object arg) {
    	if (arg != null && arg.equals("load")) {
            for (Folio folio : ft.getFolios()) {
                addFolioTab(folio);
            }
            this.updateValues();
    	}
    }

    /**
     * Add a new tab to the content pane with data from the specified folio.
     *
     * @param folio The folio
     */
    private void addFolioTab(Folio folio) {
        // Add the tab to the content pane and add the title panel:
        int index = contentPane.getTabCount();
        String title = folio.getName();
        JPanel panel = new FolioTabPanel(folio);
        contentPane.insertTab(title, null, panel, null, index);
        contentPane.setTabComponentAt(index, new TabComponent(contentPane, title));
    }

    /**
     * Set frame size and centre on screen.
     */
    private void setSizeAndCentre(int w, int h) {
        this.setSize(new Dimension(w, h));
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width / 2) - (w / 2);
        int y = (screen.height / 2) - (h / 2);
        this.setLocation(x, y);
        this.setMinimumSize(new Dimension(300, 200));
    }

    /**
     * Get the panel in the currently active tab.
     *
     * @return The panel
     */
    private FolioTabPanel getActiveTab() {
        return (FolioTabPanel) contentPane.getSelectedComponent();
    }

    /**
     * Return the index of the tab displaying a given folio, or -1 if none
     * exists.
     *
     * @param f The folio
     * @return The index
     */
    private int getTabIndex(Folio f) {
        for (int i = 0; i < contentPane.getTabCount(); i++) {
            FolioTabPanel p = (FolioTabPanel) contentPane.getComponentAt(i);
            if (p.getFolio().equals(f))
                return i;
        }
        return -1;
    }

    @Override
    public void sellStock() {
        JPanel input = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField inTicker = new JTextField();
        input.add(new JLabel("Ticker:"));
        input.add(inTicker);
    	FolioTabPanel tab = getActiveTab();
    	if (tab == null) {
    		JOptionPane.showMessageDialog(this, "Create a folio to buy stocks!", "Cannot buy stocks", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
        while (true) {
            int option = JOptionPane.showConfirmDialog(this, input, "Sell Stocks", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    Folio f = getActiveTab().getFolio();
                    Stock removingSt = f.getStock(inTicker.getText());
                    while (true) {
                        JPanel addInput = new JPanel(new GridLayout(0, 1, 2, 2));
                        JLabel tickerAmount = new JLabel("Ticker: " + inTicker.getText() + " Price: "
                                + CurrencyFormatter.format(removingSt.getPrice()) + " Shares " + removingSt.getShares());

                        addInput.add(tickerAmount);
                        JTextField amount = new JTextField();
                        addInput.add(new JLabel(("Amount")));
                        addInput.add(amount);
                        int nextOption = JOptionPane.showConfirmDialog(null, addInput, "Sell Stocks",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (nextOption == JOptionPane.OK_OPTION) {
                            try {
                                int num = Integer.parseInt(amount.getText());
                                Folio currentFolio = tab.getFolio();
                                if(num > 0 && num <= currentFolio.getStock(inTicker.getText()).getShares()) {
                                    currentFolio.sellStock(removingSt, num);
                                    break;
                                }else{
                                    printErrorMessage("Please enter a number between 1 and "+currentFolio.getStock(inTicker.getText()).getShares());
                                }
                            } catch (NumberFormatException e) {
                                printErrorMessage("Try again with a valid number");
                            }
                        } else {
                            break;
                        }
                    }
                    break;

                } catch (NullPointerException ex) {
                    printErrorMessage("This stock does not exist in your folio.");
                }
            } else {
                break;
            }
        }

    }

	@Override
	public void displayHelp() {
		JOptionPane.showMessageDialog(this, "GIYF", "Help", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void displayAbout() {
		JOptionPane.showMessageDialog(this, NAME + " created by " + AUTHORS, "About " + NAME, JOptionPane.INFORMATION_MESSAGE);
	}

}