package model;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FolioTrackerTest {

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
		
		
		
		ft1.addFolio(folio1);
		ft1.addFolio(folio2);
		
		ft2.addFolio(folio1);
		ft2.addFolio(folio2);
		
		
	}
	
	@Test
	public void testGetFolios()
	{		
		List<Folio> f1 = ft1.getFolios();
		List<Folio> f2 = ft2.getFolios();

		for(int i = 0; i<f1.size(); i++)
		{
			assertEquals(f1.get(i).getName(), f2.get(i).getName());
			assertEquals(f1.get(i).getFileName(), f2.get(i).getFileName());			
		}		
		
	}
	
	@Test
	public void testAddNewFolio()
	{
		ft1.addNewFolio("Folio12345");
		boolean isFolioAdded = false;
		
		List<Folio> folio = ft1.getFolios();
		for(Folio f : folio)
		{
			if(f.getName().equals("Folio12345"))
			{
				isFolioAdded = true;
				break;
			}
		}
		
		assertTrue(isFolioAdded);
	}
	
	@Test
	public void testRemoveFolio()
	{
		
		ft1.removeFolio(folio2);		
		
		assertFalse(ft1.getFolios().contains(folio2));
		
		assertFalse(ft1.removeFolio(folio2));
		
	}
	
	@Test
	public void testGetQuote() throws WebsiteDataException, NoSuchTickerException, MethodException, IOException
	{
		Stock stock1 = new StockImpl("EZPW");	
		
		assertTrue(ft1.getQuote("EZPW").equals(stock1));
		
	}

}
