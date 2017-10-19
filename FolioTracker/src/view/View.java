package view;

import model.Folio;
import model.FolioTracker;

public interface View {
	
	public void exit();
	
	public void buyStock();
	
	public FolioTracker getFolioTracker();
	
	public void openFolio(Folio f);
	
	public void newFolio();
	
	public void sellStock();
	
	public void updateValues();

	public void showErrorMessage(String title, String msg);

	public void displayHelp();

	public void displayAbout();

}
