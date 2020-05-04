package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parsers.StockDetailsParser;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {
    Stock stockOne;
    BigDecimal stockOnePrice;
    BigDecimal zero;
    BigDecimal stockOneNewPrice;

    @BeforeEach
    public void runBefore() {
        stockOnePrice = new BigDecimal(319.61);
        zero = BigDecimal.ZERO;
        stockOneNewPrice = new BigDecimal(1508.79);

        stockOne = new Stock("Apple", "AAPL", stockOnePrice);
    }

    @Test
    public void testConstructor() {
        assertEquals("Apple", stockOne.getStockName());
        assertEquals("AAPL", stockOne.getStockTicker());
        assertEquals(stockOnePrice.setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getStockPrice());

        assertEquals(zero.setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getPreviousClosingPrice());
        assertFalse(stockOne.isPurchased());
        assertEquals(zero.setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getPurchasePrice());
        assertEquals(0, stockOne.getQuantity());
    }

    @Test
    public void testUpdateStockName() {
        assertEquals("Apple", stockOne.getStockName());
        stockOne.setStockName("Google");
        assertEquals("Google", stockOne.getStockName());
    }

    @Test
    public void testUpdateTicker() {
        assertEquals("AAPL", stockOne.getStockTicker());
        stockOne.setStockTicker("GOOG");
        assertEquals("GOOG", stockOne.getStockTicker());
    }

    @Test
    public void testUpdateStockPrice() {
        assertEquals(stockOnePrice.setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getStockPrice());
        stockOne.setStockPrice(stockOneNewPrice);
        assertEquals(stockOneNewPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getStockPrice());
    }

    @Test
    public void testUpdateClosingPrice() {
        assertEquals(zero.setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getPreviousClosingPrice());

        BigDecimal prevClosingPrice = new BigDecimal(321.55);
        stockOne.setPreviousClosingPrice(prevClosingPrice);
        assertEquals(prevClosingPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getPreviousClosingPrice());
    }

    @Test
    public void testUpdatePurchasedStatus() {
        assertFalse(stockOne.isPurchased());
        stockOne.setPurchased(true);
        assertTrue(stockOne.isPurchased());
    }

    @Test
    public void testUpdatePurchasePrice() {
        assertEquals(zero.setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getPurchasePrice());
        stockOne.setPurchasePrice(stockOnePrice);
        assertEquals(stockOnePrice.setScale(2, BigDecimal.ROUND_HALF_DOWN), stockOne.getPurchasePrice());
    }

    @Test
    public void testUpdateQuantity() {
        assertEquals(0, stockOne.getQuantity());
        stockOne.setQuantity(10);
        assertEquals(10, stockOne.getQuantity());
    }

    @Test
    public void testNotEquals() {
        Stock correctNameWrongTicker = new Stock("Apple", "APPLE", new BigDecimal(1234.0));
        Stock wrongNameCorrectTicker = new Stock("Microsoft", "AAPL", BigDecimal.TEN);
        String testString = "Hello World";
        assertFalse(stockOne.equals(correctNameWrongTicker));
        assertFalse(stockOne.equals(wrongNameCorrectTicker));
        assertFalse(correctNameWrongTicker.equals(stockOne));
        assertFalse(wrongNameCorrectTicker.equals(stockOne));
        assertFalse(stockOne.equals(testString));
        assertFalse(stockOne.equals(null));
    }

    @Test
    public void testEquals() {
        Stock theSame = new Stock("Apple", "AAPL", BigDecimal.ZERO);
        assertTrue(theSame.equals(stockOne));
        assertTrue(stockOne.equals(theSame));
        assertTrue(theSame.equals(theSame));
        assertTrue(stockOne.equals(stockOne));
    }

    @Test
    public void testHashCode() {
        Stock theSame = new Stock("Apple", "AAPL", BigDecimal.ZERO);
        Stock notTheSame = new Stock("APple", "AAPL", BigDecimal.ZERO);
        int hashCode = theSame.hashCode();
        assertEquals(hashCode, stockOne.hashCode());
        assertNotEquals(notTheSame.hashCode(), stockOne.hashCode());
    }

    @Test
    public void testGetNetChange() {
        stockOne.setNetChange("-0.56");
        assertEquals("-0.56", stockOne.getNetChange());
    }

    @Test
    public void testSetNetChange() {
        assertEquals(null, stockOne.getNetChange());
        stockOne.setNetChange("-45.89");
        assertEquals("-45.89", stockOne.getNetChange());
    }

    @Test
    public void testGetPercentChange() {
        stockOne.setPercentChange("-6.86");
        assertEquals("-6.86", stockOne.getPercentChange());
    }

    @Test
    public void testSetPercentChange() {
        assertEquals(null, stockOne.getPercentChange());
        stockOne.setPercentChange("-15.85");
        assertEquals("-15.85", stockOne.getPercentChange());
    }

}
