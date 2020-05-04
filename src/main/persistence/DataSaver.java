package persistence;

import model.Portfolio;
import model.Stock;
import model.Watchlist;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/* Abstract class for creating JSON objects of given portfolio and watchlist */
public abstract class DataSaver {

    public static JSONObject savePortfolioData(Portfolio portfolio) {
        // create JSON object to represent portfolio data
        JSONObject data = new JSONObject();
        data.put("name", "Portfolio");
        data.put("balance", portfolio.getBalance());
        data.put("size", portfolio.getSize());

        JSONArray stocksOwned = new JSONArray();
        for (Stock stock : portfolio) {
            JSONObject stockDetails = getJsonObject(stock);
            stockDetails.put("purchase price", stock.getPurchasePrice());
            stockDetails.put("quantity", stock.getQuantity());
            stocksOwned.add(stockDetails);
        }

        data.put("stocks owned", stocksOwned);
        return data;
    }

    public static JSONObject saveWatchlistData(Watchlist watchlist) {
        JSONObject data = new JSONObject();
        data.put("name", watchlist.getListName());
        data.put("size", watchlist.getSize());

        JSONArray stocksWatched = new JSONArray();
        for (Stock stock : watchlist) {
            JSONObject stockDetails = getJsonObject(stock);
            stocksWatched.add(stockDetails);
        }

        data.put("stocks", stocksWatched);
        return data;
    }

    private static JSONObject getJsonObject(Stock stock) {
        JSONObject stockDetails = new JSONObject();
        stockDetails.put("stock name", stock.getStockName());
        stockDetails.put("ticker", stock.getStockTicker());
        stockDetails.put("price", stock.getStockPrice());
        return stockDetails;
    }
}
