package parsers;

import model.Stock;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import parsers.exceptions.QueryLimitExceededException;
import parsers.exceptions.StockParseException;

import java.math.BigDecimal;

/* Parses stock data obtained from http call to AlphaVantage */
public class StockDetailsParser {
    private Stock stock;
    private String openPrice;
    private String dayHigh;
    private String dayLow;
    private String volume;
    private JSONParser parser = new JSONParser();

    public void parseStockData(String responseContent) throws StockParseException, QueryLimitExceededException {
        try {
            JSONObject response = (JSONObject) parser.parse(responseContent);
            if (response.containsKey("Error Message")) {
                throw new StockParseException();
            } else if (response.containsKey("Note")) {
                throw new QueryLimitExceededException();
            }
            JSONObject stockData = (JSONObject) response.get("Global Quote");
            updateFields(stockData);
            makeStockFromData(stockData);
        } catch (ParseException e) {
            throw new StockParseException();
        }
    }

    private void makeStockFromData(JSONObject stockData) {
        String stockTicker =  (String) stockData.get("01. symbol");
        BigDecimal stockPrice = new BigDecimal((String) stockData.get("05. price"));
        BigDecimal prevClose = new BigDecimal((String) stockData.get("08. previous close"));
        Stock stock = new Stock(stockTicker, stockTicker, stockPrice);
        stock.setPreviousClosingPrice(prevClose);

        String netChange = (String) stockData.get("09. change");
        netChange = netChange.substring(0, netChange.length() - 2);
        stock.setNetChange(netChange);

        String percentChange = (String) stockData.get("10. change percent");
        percentChange = percentChange.substring(0, percentChange.length() - 3);
        stock.setPercentChange(percentChange);

        this.stock = stock;
    }

    private void updateFields(JSONObject stockData) {
        String openPrice = (String) stockData.get("02. open");
        this.openPrice = openPrice.substring(0, openPrice.length() - 2);
        String dayHigh = (String) stockData.get("03. high");
        this.dayHigh = dayHigh.substring(0, dayHigh.length() - 2);
        String dayLow = (String) stockData.get("04. low");
        this.dayLow = dayLow.substring(0, dayLow.length() - 2);
        this.volume = (String) stockData.get("06. volume");
    }

    public Stock getStock() {
        return stock;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public String getDayHigh() {
        return dayHigh;
    }

    public String getDayLow() {
        return dayLow;
    }

    public String getVolume() {
        return volume;
    }
}
