
package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StockTest {
	Stock stock1;
	Stock stock2;
	Stock stock3;
	Stock stock4;
	Stock stock5;
	Object temp;
	String x = "bahlegday";
	
	@Before
	public void setUp()
	{
		stock1 = new StockImpl("GOOGL", "Alphabet", 0, 0);
		stock2 = new StockImpl("GOOGL", "Alphabet", 0, 0);
		stock3 = new StockImpl("GOOGL", "Alphabet", 0, 0);
		stock4 = new StockImpl("AMZN", "Amazon", 0, 0);
		stock5 = new StockImpl("GOOGL", "Alphabet", 0, 0);
		temp = null;
	}
	
	@Test
	public void stockEqualsSymmetric() {
	
		assertTrue(stock1.equals(stock2) && stock2.equals(stock1));
	}
	
	@Test
	public void stockEqualsTransitive()
	{
		assertTrue(stock1.equals(stock2) && stock2.equals(stock3) && stock3.equals(stock1));
	}
	
	@Test
	public void stockEqualsConsistent()
	{
		assertTrue(stock1.equals(stock1) && stock1.equals(stock1));
	}
	
	@Test
	public void stockEqualsReflexive()
	{
		
	}
	
	@Test
	public void testGetName()
	{
		assertTrue(stock1.getName().equals(stock2.getName()));
		
	}
	
	@Test
	public void testGetShares()
	{
		assertTrue(stock1.getShares() == stock2.getShares());
		
	}
	
	@Test
	public void testGetCost()
	{
		assertTrue(stock1.getCost() == stock2.getCost());
		
	}
	
	@Test
	public void testGetPrice()
	{
		assertTrue(stock1.getPrice() == stock2.getPrice());
		
	}
	
	@Test
	public void testGetValue()
	{
		assertTrue(stock1.getValue() == stock2.getValue());
		
	}
	
	@Test
	public void testToString()
	{
		assertTrue(stock1.toString().equals(stock1.getTicker() + " (" + stock1.getName() + ") x " + stock1.getShares() + " at cost " + stock1.getCost()));
	}
	
	@Test
	public void testNull()
	{
		assertFalse(stock1.equals(temp));
	}
	
	@Test
	public void testEquals()
	{
		assertFalse(stock1.equals(x));
	}
	
	@Test
	public void testHashCode()
	{
		assertTrue(stock1.hashCode() == stock2.hashCode());
		assertFalse(stock1.hashCode() == stock4.hashCode());
		
	}
	
	@Test
	public void testProfitLoss()
	{
		assertTrue(stock1.getProfitLoss() == stock2.getProfitLoss());
		
	}
	
	@Test
	public void testBuyShares()
	{
		stock1.buyShares(10);
		stock2.buyShares(10);
		assertTrue(stock1.getShares() == stock2.getShares());
		
	}
	
	@Test
	public void testSellShares()
	{
		stock1.buyShares(10);
		stock1.sellShares(5);
		stock2.sellShares(5);
		assertTrue(stock1.getShares() == 5);
		
	}
	
}
