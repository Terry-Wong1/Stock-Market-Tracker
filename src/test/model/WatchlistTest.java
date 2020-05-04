package model;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class WatchlistTest {

    // Fields
    Watchlist testWatchlist;
    Stock stockOne;
    Stock stockTwo;

    @BeforeEach
    public void runBefore() {
        stockOne = new Stock("Apple", "AAPL", new BigDecimal(319.61));
        stockTwo = new Stock("Facebook", "FB", new BigDecimal(207.19));

        testWatchlist = new Watchlist("Watchlist 1");
    }

    @Test
    public void testConstructor() {
        assertEquals("Watchlist 1", testWatchlist.getListName());
        assertEquals(0, testWatchlist.getSize());
    }

    @Test
    public void testAddStock() {
        testWatchlist.addStock(stockOne);
        assertTrue(testWatchlist.contains(stockOne));
        assertFalse(testWatchlist.contains(stockTwo));
    }

    @Test
    public void testRemoveStock() {
        testWatchlist.addStock(stockOne);
        testWatchlist.addStock(stockTwo);
        testWatchlist.removeStock(stockTwo);
        assertTrue(testWatchlist.contains(stockOne));
        assertFalse(testWatchlist.contains(stockTwo));
    }

    @Test
    public void testSetListName() {
        assertEquals("Watchlist 1", testWatchlist.getListName());
        testWatchlist.setListName("Watchlist 2");
        assertEquals("Watchlist 2", testWatchlist.getListName());
    }

    @Test
    public void testGetStocksToWatch() {
        testWatchlist.addStock(stockOne);
        HashSet<Stock> testSet = testWatchlist.getStocksToWatch();
        assertFalse(testSet.isEmpty());
        assertEquals(1, testSet.size());
        assertTrue(testSet.contains(stockOne));
        assertFalse(testSet.contains(stockTwo));
    }

    @Test
    public void testIterator() {
        testWatchlist.addStock(stockTwo);
        Iterator<Stock> iterator = testWatchlist.iterator();
        while (iterator.hasNext()) {
            Stock testStock = iterator.next();
            assertEquals("Facebook", testStock.getStockName());
            assertEquals("FB", testStock.getStockTicker());
            assertEquals(testStock, stockTwo);
            assertEquals(stockTwo.getStockPrice(), testStock.getStockPrice());
        }
    }


 }
