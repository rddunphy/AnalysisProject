package model;

import model.Folio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 11/11/2016.
 */
public class FolioLoader {

	private String path;
	private static final String FOLIO_LIST = "folioList.txt";
	private FolioTracker ft;

	public FolioLoader(FolioTracker ft) {
		this.ft = ft;
		File f = new File(System.getProperty("java.class.path"));
		File dir = f.getAbsoluteFile().getParentFile();
		path = dir.toString() + File.separator + ".foliotrackerfiles" + File.separator;
	}

	private Folio readFolioFile(String fileName) throws IOException {
		FileReader fr = new FileReader(path + fileName);
		BufferedReader br = new BufferedReader(fr);

		String newLine;

		String folioName = br.readLine().trim();
		Folio folio = new FolioImpl(folioName, ft, fileName);

		while ((newLine = br.readLine()) != null) {
			String[] splits = newLine.split(";");
			String ticker = splits[0];
			String name = splits[1];
			int shares = Integer.parseInt(splits[2]);
			int cost = Integer.parseInt(splits[3]);
			Stock stock = new StockImpl(ticker, name, shares, cost);
			folio.addStock(stock);
		}
		br.close();
		return folio;
	}

	/*
	 * Currently requires that the name for the folio given in the first line of
	 * the folio text file is the same as the text files name Not optimal but I
	 * don't know a way around this
	 */

	public void saveFolioFiles(List<Folio> folios) {
		try {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileWriter flw = new FileWriter(path + FOLIO_LIST);
			BufferedWriter folioListWriter = new BufferedWriter(flw);
			for (Folio f : folios) {
				try {
					folioListWriter.write(f.getFileName());
					folioListWriter.newLine();
					FileWriter fw = new FileWriter(path + f.getFileName());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(f.getName());
					bw.newLine();
					for (Stock s : f.getStocks()) {
						bw.write(s.getTicker() + ";" + s.getName() + ";" + s.getShares() + ";" + s.getCost());
						bw.newLine();
					}
					bw.close();
				} catch (IOException e) {}
			}
			folioListWriter.close();
		} catch (IOException e) {}

	}

	private List<String> getFolioFiles() throws IOException {
		FileReader fread = new FileReader(path + FOLIO_LIST);
		BufferedReader bread = new BufferedReader(fread);

		String line;
		List<String> filenames = new ArrayList<>();
		while ((line = bread.readLine()) != null) {
			if (line.trim() != "")
				filenames.add(line);
		}

		bread.close();
		return filenames;
	}

	public List<Folio> loadFolios() throws IOException {
		List<Folio> folios = new ArrayList<>();
		for (String filename : getFolioFiles()) {
			try {
				folios.add(readFolioFile(filename));
			} catch (Exception e) {}
		}
		return folios;
	}

}
