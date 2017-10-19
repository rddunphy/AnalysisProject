package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class FolioTrackerImpl extends Observable implements FolioTracker {

	private List<Folio> folios;
	private FolioLoader loader;

	public FolioTrackerImpl() {
		folios = new ArrayList<>();
		loader = new FolioLoader(this);
	}

	@Override
	public List<Folio> getFolios() {
		return this.folios;
	}

	@Override
	public boolean addFolio(Folio folio) {
		return folios.add(folio);
	}

	/**
	 * Generates a new file name from the current time stamp.
	 * 
	 * @return The file name
	 */
	private String generateFileName() {
		long i = System.currentTimeMillis();
		return "f" + Long.toString(i) + ".folio";
	}

	@Override
	public Folio addNewFolio(String name) {
		Folio f = new FolioImpl(name, this, generateFileName());
		addFolio(f);
		setChanged();
		notifyObservers();
		return f;
	}

	@Override
	public boolean removeFolio(Folio folio) {
		if (folios.remove(folio)) {
			return true;
		}
		return false;
	}

	@Override
	public Stock getQuote(String ticker)
			throws WebsiteDataException, NoSuchTickerException, MethodException, IOException {
		Stock s = new StockImpl(ticker);
		s.update();
		return s;
	}

	@Override
	public void saveFolios() {
		loader.saveFolioFiles(folios);
	}

	@Override
	public void loadFolios() throws IOException {
		folios = loader.loadFolios();
		setChanged();
		notifyObservers("load");
	}

}
