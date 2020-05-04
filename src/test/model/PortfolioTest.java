package model;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {
    // Fields
    Portfolio testPortfolio;
    Stock stockOne;
    Stock stockTwo;
    Stock stockThree;

    @BeforeEach
    public void runBefore() {
        stockOne = new Stock("Apple", "AAPL", new BigDecimal(319.61));
        stockTwo = new Stock("Facebook", "FB", new BigDecimal(207.19));
        stockThree = new Stock("Amazon", "AMZN", new BigDecimal(2150.80));

        testPortfolio = new Portfolio();
    }

    @Test
    public void testConstructor() {
        assertEquals(BigDecimal.ZERO, testPortfolio.getBalance());
        assertEquals(0, testPortfolio.getSize());
    }

    @Test
    public void testPurchaseStock() {
        testPortfolio.purchaseStock(stockOne, 10);
        assertTrue(testPortfolio.contains(stockOne));
        assertFalse(testPortfolio.contains(stockTwo));
        assertTrue(stockOne.isPurchased());
        assertEquals(new BigDecimal(319.61).setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getPurchasePrice());
        assertEquals(10, stockOne.getQuantity());
    }

    @Test
    public void testPurchaseMultipleStocks() {
        testPortfolio.purchaseStock(stockOne, 3);
        testPortfolio.purchaseStock(stockTwo, 5);

        assertTrue(testPortfolio.contains(stockOne));
        assertFalse(testPortfolio.contains(stockThree));
        assertTrue(stockOne.isPurchased());
        assertEquals(new BigDecimal(319.61).setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getPurchasePrice());
        assertEquals(3, stockOne.getQuantity());

        assertTrue(testPortfolio.contains(stockTwo));
        assertTrue(stockTwo.isPurchased());
        assertEquals(new BigDecimal(207.19).setScale(2, BigDecimal.ROUND_HALF_DOWN), stockTwo.getPurchasePrice());
        assertEquals(5, stockTwo.getQuantity());
    }

    @Test
    public void testPurchaseOwnedStock() {
        testPortfolio.purchaseStock(stockOne, 3);
        testPortfolio.purchaseStock(stockOne, 5);
        assertTrue(testPortfolio.contains(stockOne));
        assertTrue(stockOne.isPurchased());
        assertEquals(new BigDecimal(319.61).setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getPurchasePrice());
        assertEquals(8, stockOne.getQuantity());
    }

    @Test
    public void testSellAllStock() {
        testPortfolio.purchaseStock(stockThree, 2);
        testPortfolio.sellStock(stockThree, 2);

        assertFalse(testPortfolio.contains(stockThree));
        assertFalse(stockThree.isPurchased());
        assertEquals(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_DOWN), stockThree.getPurchasePrice());
        assertEquals(0, stockThree.getQuantity());
    }

    @Test
    public void testSellPartialStock() {
        testPortfolio.purchaseStock(stockThree, 5);
        testPortfolio.sellStock(stockThree, 1);

        assertTrue(testPortfolio.contains(stockThree));
        assertTrue(stockThree.isPurchased());
        assertEquals(new BigDecimal(2150.80).setScale(2, BigDecimal.ROUND_HALF_DOWN), stockThree.getPurchasePrice());
        assertEquals(4, stockThree.getQuantity());
    }

    @Test
    public void testGetBalance() {
        assertEquals(BigDecimal.ZERO, testPortfolio.getBalance());
        testPortfolio.purchaseStock(stockOne, 10);

        BigDecimal price = stockOne.getPurchasePrice();
        BigDecimal shares = BigDecimal.valueOf(stockOne.getQuantity());
        BigDecimal balance = price.multiply(shares);
        assertEquals(balance, testPortfolio.getBalance());
    }

    @Test
    public void testGetStock() {
        testPortfolio.purchaseStock(stockOne, 10);
        testPortfolio.purchaseStock(stockTwo, 5);
        assertEquals(testPortfolio.getStock(stockOne), stockOne);
        assertEquals(testPortfolio.getStock(stockTwo), stockTwo);
        assertEquals(testPortfolio.getStock(stockThree), null);
    }

    @Test
    public void testGetStocksOwned() {
        testPortfolio.purchaseStock(stockTwo, 5);
        HashSet<Stock> testSet = testPortfolio.getStocksOwned();
        assertFalse(testSet.isEmpty());
        assertTrue(testSet.contains(stockTwo));
        assertFalse(testSet.contains(stockOne));
        assertEquals(1, testSet.size());
    }

    @Test
    public void testIterator() {
        testPortfolio.purchaseStock(stockOne, 3);
        Iterator<Stock> iterator = testPortfolio.iterator();
        while (iterator.hasNext()) {
           Stock testStock = iterator.next();
           assertEquals(testStock, stockOne);
           assertEquals(3, testStock.getQuantity());
           assertEquals(stockOne.getStockPrice(), testStock.getStockPrice());
        }
    }
}
