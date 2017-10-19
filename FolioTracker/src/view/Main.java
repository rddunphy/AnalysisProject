package view;

import java.io.IOException;

import ctrlr.PrimaryActionListener;
import ctrlr.PrimaryWindowListener;
import model.FolioTracker;
import model.FolioTrackerImpl;

public class Main {

	public static void main(String[] args) {

		PrimaryActionListener al = new PrimaryActionListener();
		PrimaryWindowListener wl = new PrimaryWindowListener();

		FolioTracker ft = new FolioTrackerImpl();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				View frame = new PrimaryFrame(ft, al, wl);
				al.setView(frame);
				wl.setView(frame);
				try {
					ft.loadFolios();
				} catch (IOException e) {
					String msg = "Folios could not be loaded.\n";
					msg += "Folio list file may be either missing or corrupted.\n";
					msg += "If this is your first time using FolioTracker, create a folio to start.";
					frame.showErrorMessage("Problem loading folios", msg);
				}
			}
		});

	}

}
