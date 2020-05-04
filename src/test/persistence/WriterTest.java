package persistence;

import model.Portfolio;
import model.Stock;
import model.Watchlist;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WriterTest {

    // Fields
    private static final String TEST_PORTFOLIO = "./data/testPortfolio.json";
    private static final String TEST_WATCHLIST = "./data/testWatchlist.json";
    private Writer testWriter;
    private Reader testReader = new Reader();
    private Portfolio portfolio;
    private Watchlist watchlist;
    private Stock stockOne = new Stock("Microsoft", "MSFT", new BigDecimal(170.89));
    private Stock stockTwo = new Stock("Intel", "INTC", new BigDecimal(61.76));
    private Stock stockThree = new Stock ("Advanced Micro Devices", "AMD", new BigDecimal(49.12));

    @BeforeEach
    void runBefore() throws FileNotFoundException, SecurityException {
        //setup portfolio
        portfolio = new Portfolio();
        portfolio.purchaseStock(stockOne, 5);

        //setup watchlist
        watchlist = new Watchlist("Watchlist 1");
        watchlist.addStock(stockTwo);
        watchlist.addStock(stockThree);
    }

    @Test
    void testWritePortfolio() throws FileNotFoundException {
        // call helper function
        JSONObject portfolioDetails = DataSaver.savePortfolioData(portfolio);

        // write data
        testWriter = new Writer(new File(TEST_PORTFOLIO));
        testWriter.write(portfolioDetails.toJSONString());
        testWriter.flush();
        testWriter.close();

        // read data back to verify expected values
        Portfolio readPortfolio = null;
        try {
            readPortfolio = testReader.readPortfolio(new FileReader(TEST_PORTFOLIO));
            assertEquals(1, readPortfolio.getSize());
            BigDecimal portfolioBal = stockOne.getStockPrice().multiply(new BigDecimal(5));
            assertEquals(portfolioBal, readPortfolio.getBalance());

            // NOT an elegant testing solution - ask TA about ways to improve this (how to match objects)
            for (Stock s: readPortfolio) {
                assertEquals(stockOne.getStockName(), s.getStockName());
                assertEquals(stockOne.getStockTicker(), s.getStockTicker());
                assertEquals(stockOne.getStockPrice(), s.getStockPrice());
                assertEquals(stockOne.getQuantity(), s.getQuantity());
                assertTrue(s.isPurchased());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWriteWatchlists() throws FileNotFoundException {
        JSONArray watchlists = new JSONArray();
        watchlists.add(DataSaver.saveWatchlistData(watchlist));

        // write data
        testWriter = new Writer(new File(TEST_WATCHLIST));
        testWriter.write(watchlists.toJSONString());
        testWriter.flush();
        testWriter.close();

        // read data back to verify correctness
        List<Watchlist> readWatchlists = null;
        try {
            readWatchlists = testReader.readWatchlists(new FileReader(TEST_WATCHLIST));
            assertFalse(readWatchlists.isEmpty());
            assertEquals(1, readWatchlists.size());
            assertEquals("Watchlist 1", readWatchlists.get(0).getListName());
            assertEquals(2, readWatchlists.get(0).getSize());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
