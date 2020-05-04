package persistence;

import model.Portfolio;
import model.Stock;
import model.Watchlist;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ReaderTest {
    // Fields
    private static final String READ_PORTFOLIO_FILE = "./data/testReadPortfolioFile.json";
    private static final String READ_WATCHLIST_FILE = "./data/testReadWatchlistFile.json";
    private Reader testReader;


    @BeforeEach
    void runBefore() {
        testReader = new Reader();
    }

    @Test
    void testReadPortfolioFile() {
        try {
            Portfolio portfolio = testReader.readPortfolio(new FileReader(READ_PORTFOLIO_FILE));
            assertEquals(2, portfolio.getSize());
            BigDecimal balance = BigDecimal.ZERO;
            for (Stock s : portfolio) {
                balance = balance.add(s.getPurchasePrice().multiply(new BigDecimal(s.getQuantity())));
            }
            assertEquals(balance, portfolio.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testReadWatchlistFile() {
        try {
            List<Watchlist> watchlists = testReader.readWatchlists(new FileReader(READ_WATCHLIST_FILE));
            assertEquals("Hello World", watchlists.get(0).getListName());
            assertEquals("foo bar", watchlists.get(1).getListName());
            assertEquals(2, watchlists.get(0).getSize());
            assertEquals(1, watchlists.get(1).getSize());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testIOExceptionReadPortfolio() {
        try {
            Object testObject = testReader.readPortfolio(new FileReader("./data/path/does/not/exist.json"));
        } catch (IOException e) {
            // expected result
        } catch (ParseException e) {
            fail("Parse Exception is unexpected");
        }
    }

    @Test
    void testParseExceptionReadPortfolio() {
        try {
            Object testObject = testReader.readPortfolio(new FileReader("./data/malformedFile.json"));
        } catch (IOException e) {
            fail("IOException unexpected");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    void testIOExceptionReadWatchlists() {
        try {
            Object testObject = testReader.readWatchlists(new FileReader("./data/file/does/not/exist.json"));
        } catch (IOException e) {
            // expected
        } catch (ParseException e) {
            fail("ParseException is unexpected");
        }
    }
    @Test
    void testParseExceptionReadWatchlists() {
        try {
            Object testObject = testReader.readWatchlists(new FileReader("./data/malformedFile.json"));
        } catch (IOException e) {
            fail("IOException is unexpected");
        } catch (ParseException e) {
            // expected
        }
    }
}
