package model;

import static org.junit.Assert.*;

import model.folios.*;
import org.junit.Before;
import org.junit.Test;

public class FolioTest {
	Folio folio1;
	Folio folio2;
	FolioTracker ft1;
	FolioTracker ft2;
	Stock stock1;
	
	@Before
	public void setUp(){
		ft1 = new FolioTrackerImpl();
		ft2 = new FolioTrackerImpl();
		folio1 = new FolioImpl("Folio 1", ft1, "file1");
		folio2 = new FolioImpl("Folio 2", ft2, "file2");
		stock1 = new StockImpl("GOOGL", "Alphabet Inc.", 50, 500);
	}
	
	@Test
	public void testName(){
		//test the name of the folio matches the name of the folio created
		assertEquals("Folio 1", folio1.getName());
	}
	
	@Test
	public void testFileName(){
		//test the file name held in the folio is correct
		assertEquals("file1", folio1.getFileName());
		assertEquals("file2", folio2.getFileName());
	}
	
	@Test
	public void testAddStock(){
		//test if we can successfuly add stocks to our folio and that it actually gets added
		assertTrue(folio1.addStock(stock1));
		//test if stock1 was successfuly added to the folios list
		assertTrue(folio1.getStocks().contains(stock1));
	}
	
	@Test
	public void testGetStock(){
		//test if we can get back the stock we added by its ticker
		folio1.addStock(stock1);
		assertEquals(stock1, folio1.getStock(stock1.getTicker()));
		//test for non existent stock by ticker
		assertNull(folio1.getStock("fake"));
	}
	
	@Test
	public void testBuyStocks(){
		//testing if we can buy valid stocks and ensure they actually get added successfully
		folio1.buyStock("FB", 50);
		assertNotNull(folio1.getStock("FB"));
		assertEquals(50, folio1.getStock("FB").getShares());
		//test buying more of an existing stock
		folio1.buyStock("FB", 50);
		assertEquals(100, folio1.getStock("FB").getShares());
	}
	
	@Test
	public void sellStock(){
		folio1.buyStock("GOOGL", 100);
		//test selling half of our stock first
		folio1.sellStock(folio1.getStock("GOOGL"), 50);
		assertEquals(50, folio1.getStock("GOOGL").getShares());
		//test if we can sell all of our remaining stock and the stock object gets removed from the folio
		folio1.sellStock(folio1.getStock("GOOGL"), 50);
		assertNull(folio1.getStock("GOOGL"));
	}
}
