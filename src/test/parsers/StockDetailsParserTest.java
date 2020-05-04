package parsers;

import model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parsers.exceptions.QueryLimitExceededException;
import parsers.exceptions.StockParseException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class StockDetailsParserTest {
    String sampleResponse = "{\n" +
            "    \"Global Quote\": {\n" +
            "        \"01. symbol\": \"IBM\",\n" +
            "        \"02. open\": \"112.0000\",\n" +
            "        \"03. high\": \"113.8100\",\n" +
            "        \"04. low\": \"110.1700\",\n" +
            "        \"05. price\": \"110.9300\",\n" +
            "        \"06. volume\": \"6305552\",\n" +
            "        \"07. latest trading day\": \"2020-03-31\",\n" +
            "        \"08. previous close\": \"112.9300\",\n" +
            "        \"09. change\": \"-2.0000\",\n" +
            "        \"10. change percent\": \"-1.7710%\"\n" +
            "    }\n" +
            "}";

    String invalidResponse = "Hello World";

    String errorMessage = "{    \"Error Message\": \"Invalid API call. Please retry or " +
            "visit the documentation (https://www.alphavantage.co/documentation/) for GLOBAL_QUOTE.\"}";

    String limitExceededResponse = "{    \"Note\": \"Thank you for using Alpha Vantage! " +
            "Our standard API call frequency is 5 calls per minute and 500 calls per day. " +
            "Please visit https://www.alphavantage.co/premium/ if you would like to target a higher API call frequency.\"}";

    Stock testStock;
    StockDetailsParser parser;

    @BeforeEach
    void runBefore() {
        parser = new StockDetailsParser();
    }

    @Test
    void testParseStockDataValidResponse() {
        try {
            testStock = new Stock("IBM", "IBM", new BigDecimal("110.9300"));
            parser.parseStockData(sampleResponse);
            assertEquals(testStock, parser.getStock());
            assertEquals("112.00", parser.getOpenPrice());
            assertEquals("113.81", parser.getDayHigh());
            assertEquals("110.17", parser.getDayLow());
            assertEquals("6305552", parser.getVolume());
            assertEquals("-2.00", parser.getStock().getNetChange());
            assertEquals("-1.77", parser.getStock().getPercentChange());
        } catch (StockParseException e) {
            fail();
        } catch (QueryLimitExceededException q) {
            fail();
        }
    }

    @Test
    void testParseStockDataInvalidResponse() {
        try {
            parser.parseStockData(invalidResponse);
            fail();
        } catch (StockParseException e) {
            // expected
        } catch (QueryLimitExceededException q) {
            fail();
        }
    }

    @Test
    void testParseStockDataErrorResponse() {
        try {
            parser.parseStockData(errorMessage);
            fail();
        } catch (StockParseException e) {
            // expected
        } catch (QueryLimitExceededException q) {
            fail();
        }
    }

    @Test
    void testParseStockDataQueryLimitExceeded() {
        try {
            parser.parseStockData(limitExceededResponse);
            fail();
        } catch (StockParseException e) {
            fail();
        } catch (QueryLimitExceededException q) {
            // expected
        }
    }
}
