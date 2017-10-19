package model;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

public interface FolioTracker {
	
	public void loadFolios() throws IOException;

	public List<Folio> getFolios();
	
	public boolean addFolio(Folio folio);
	
	public Folio addNewFolio(String name);
	
	public boolean removeFolio(Folio folio);

	public Stock getQuote(String ticker) throws WebsiteDataException, NoSuchTickerException, MethodException, IOException;

	public void addObserver(Observer o);
	
	public void saveFolios();
}
