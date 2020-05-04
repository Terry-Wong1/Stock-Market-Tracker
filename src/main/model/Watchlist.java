package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;

// Represents a list of stocks to watch
public class Watchlist implements Iterable<Stock> {

    // Fields
    private HashSet<Stock> stocksToWatch;
    private String listName;

    // EFFECTS: creates new watchlist with given name
    public Watchlist(String name) {
        stocksToWatch = new HashSet<Stock>();
        this.listName = name;
    }

    // REQUIRES: stock is not in stocksToWatch
    // MODIFIES: this
    // EFFECTS:  adds stock to the set if not already there, otherwise does nothing
    public void addStock(Stock stock) {
        stocksToWatch.add(stock);
    }

    // REQUIRES: stock is in stocksToWatch
    // MODIFIES: this
    // EFFECTS: removes stock from set
    public void removeStock(Stock stock) {
        stocksToWatch.remove(stock);
    }

    // EFFECTS: returns true if given stock is currently in stocksToWatch
    public boolean contains(Stock stock) {
        return stocksToWatch.contains(stock);
    }

    // getter
    public String getListName() {
        return this.listName;
    }

    // getter
    public int getSize() {
        return stocksToWatch.size();
    }

    // setter
    public void setListName(String newName) {
        this.listName = newName;
    }

    public HashSet<Stock> getStocksToWatch() {
        return stocksToWatch;
    }

    @Override
    public Iterator<Stock> iterator() {
        return stocksToWatch.iterator();
    }
}
