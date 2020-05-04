package persistence;

/* Utilizes json-simple written by Yidong Fang.
   Obtained from https://code.google.com/archive/p/json-simple/ */


import model.Portfolio;
import model.Stock;
import model.Watchlist;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// A reader that will read portfolio and watchlist data from a JSON file
public class Reader {
    // Fields
    private JSONParser parser;

    // EFFECT: constructs a new reader
    public Reader() {
        parser = new JSONParser();
        // stub

    }

    // EFECTS: returns portfolio parsed from JSON file
    public Portfolio readPortfolio(FileReader file) throws IOException, ParseException {
        Portfolio portfolio = new Portfolio();

        // read file as JSON object
        JSONObject portfolioData = (JSONObject) parser.parse(file);
        JSONArray portfolioStocks = (JSONArray) portfolioData.get("stocks owned");

        for (int i = 0; i < portfolioStocks.size(); i++) {
            // parse data
            JSONObject stockData = (JSONObject) portfolioStocks.get(i);
            String name = (String) stockData.get("stock name");
            String ticker = (String) stockData.get("ticker");
            BigDecimal price = new BigDecimal((double) stockData.get("price"));
            BigDecimal purchasePrice = new BigDecimal((double) stockData.get("purchase price"));
            int quantity =  Integer.valueOf(stockData.get("quantity").toString());

            // create stock with above parsed data
            Stock stock = new Stock(name, ticker, price);
            stock.setPurchasePrice(purchasePrice);

            // add stock to portfolio
            portfolio.purchaseStock(stock, quantity);
        }
        return portfolio;
    }

    // EFFECTS: returns a list of watchlists parsed from JSON file
    public List<Watchlist> readWatchlists(FileReader file) throws IOException, ParseException {
        List<Watchlist> watchlists = new ArrayList<>();

        // read file as JSONArray
        JSONArray watchlistsData = (JSONArray) parser.parse(file);

        for (int i = 0; i < watchlistsData.size(); i++) {
            // parse data from each watchlist
            JSONObject watchlistData = (JSONObject) watchlistsData.get(i);
            String name = (String) watchlistData.get("name");
            Watchlist watchlist = new Watchlist(name);
            JSONArray stocksData = (JSONArray) watchlistData.get("stocks");

            // loop through stocksData array and add to each watchlist
            for (int j = 0; j < stocksData.size(); j++) {
                JSONObject stockData = (JSONObject) stocksData.get(j);
                String stockName = (String) stockData.get("stock name");
                String stockTicker = (String) stockData.get("ticker");
                BigDecimal stockPrice = new BigDecimal((double) stockData.get("price"));
                Stock stock = new Stock(stockName, stockTicker, stockPrice);
                watchlist.addStock(stock);
            }

            watchlists.add(watchlist);
        }

        return watchlists;
    }

}
