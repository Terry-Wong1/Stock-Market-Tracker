package persistence;

import model.Portfolio;
import model.Stock;
import model.Watchlist;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DataSaverTest {
    Stock stockOne;
    Stock stockTwo;
    Stock stockThree;
    Portfolio testPortfolio;
    Watchlist testWatchlist;

    @BeforeEach
    void runBefore() {
        stockOne = new Stock("Apple", "AAPL", new BigDecimal(319.61));
        stockTwo = new Stock("Facebook", "FB", new BigDecimal(207.19));
        stockThree = new Stock("Amazon", "AMZN", new BigDecimal(2150.80));

        testPortfolio = new Portfolio();
        testWatchlist = new Watchlist("Watchlist 1");
    }

    @Test
    void testSavePortfolioData() {
        testPortfolio.purchaseStock(stockThree, 4);
        JSONObject testPortfolioData = DataSaver.savePortfolioData(testPortfolio);
        assertEquals("Portfolio", testPortfolioData.get("name"));
        assertEquals(testPortfolio.getBalance(), testPortfolioData.get("balance"));
        assertEquals(testPortfolio.getSize(), testPortfolioData.get("size"));
        JSONArray stocksOwned = (JSONArray) testPortfolioData.get("stocks owned");
        JSONObject stockOwned = (JSONObject) stocksOwned.get(0);
        assertEquals(stockThree.getStockTicker(), stockOwned.get("ticker"));
        assertEquals(stockThree.getQuantity(), stockOwned.get("quantity"));
        assertEquals(stockThree.getStockPrice(), stockOwned.get("price"));
        assertEquals(stockThree.getPurchasePrice(), stockOwned.get("purchase price"));
        assertEquals(stockThree.getStockName(), stockOwned.get("stock name"));
    }

    @Test
    void testSaveWatchlistData() {
        testWatchlist.addStock(stockOne);
        JSONObject testWatchlistData = DataSaver.saveWatchlistData(testWatchlist);
        assertEquals(testWatchlist.getListName(), testWatchlistData.get("name"));
        assertEquals(testWatchlist.getSize(), testWatchlistData.get("size"));
        JSONArray stocksWatched = (JSONArray) testWatchlistData.get("stocks");
        JSONObject stockOneData = (JSONObject) stocksWatched.get(0);
        assertEquals(stockOne.getStockTicker(), stockOneData.get("ticker"));
        assertEquals(stockOne.getStockPrice(), stockOneData.get("price"));
        assertEquals(stockOne.getStockName(), stockOneData.get("stock name"));
    }

}
