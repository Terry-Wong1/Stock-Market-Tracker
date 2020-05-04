package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;

// Represents a list of stocks currently owned
public class Portfolio implements Iterable<Stock> {

    // Fields
    private HashSet<Stock> stocksOwned;
    private BigDecimal balance;

    // EFFECTS: creates a new set to hold purchased stocks, initial balance is 0
    public Portfolio() {
        stocksOwned = new HashSet<Stock>();
        balance = BigDecimal.ZERO;
    }

    // REQUIRES: shares >= 0
    // MODIFIES: this and stock
    // EFFECTS: adds stock to stocksOwned, updates purchased / purchasePrice / quantity
    //          fields in stock and updates portfolio balance
    public void purchaseStock(Stock stock, int shares) {
        if (stocksOwned.contains(stock)) {
            int totalShares = getStock(stock).getQuantity() + shares;
            getStock(stock).setQuantity(totalShares);

            // update portfolio balance
            balance = balance.add(stock.getPurchasePrice().multiply(BigDecimal.valueOf(shares)));
        } else {
            stocksOwned.add(stock);
            stock.setPurchased(true);
            stock.setPurchasePrice(stock.getStockPrice());
            stock.setQuantity(shares);

            // update portfolio balance
            balance = balance.add(stock.getPurchasePrice().multiply(BigDecimal.valueOf(shares)));
        }
    }

    // REQUIRES: stock to be in stocksOwned & shares <= quantity
    // MODIFIES: this and stock
    // EFFECTS: if shares = quantity, then removes stock from stocksOwned and updates fields in stock accordingly
    //          otherwise if shares < quantity, stock remains in stocksOwned and reduces quantity field in stock
    //          and updates portfolio balance

    public void sellStock(Stock stock, int shares) {
        if (stock.getQuantity() == shares) {
            // update balance
            balance = balance.subtract(stock.getPurchasePrice().multiply(BigDecimal.valueOf(shares)));

            stocksOwned.remove(stock);
            stock.setPurchased(false);
            stock.setPurchasePrice(BigDecimal.ZERO);
            stock.setQuantity(0);
        } else {
            int updatedQuantity = stock.getQuantity() - shares;
            stock.setQuantity(updatedQuantity);

            // update balance
            balance = balance.subtract(stock.getPurchasePrice().multiply(BigDecimal.valueOf(shares)));
        }
    }

    public Stock getStock(Stock stock) {
        for (Stock s: stocksOwned) {
            if (s.equals(stock)) {
                return s;
            }
        }
        return null;
    }

    // EFFECTS: checks if given stock is in stocksOwned
    public boolean contains(Stock stock) {
        return stocksOwned.contains(stock);
    }

    // EFFECTS: returns the size of stocksOwned
    public int getSize() {
        return stocksOwned.size();
    }

    // EFFECTS: returns balance (book cost) of portfolio
    public BigDecimal getBalance() {
        return balance;
    }

    public HashSet<Stock> getStocksOwned() {
        return stocksOwned;
    }

    @Override
    public Iterator<Stock> iterator() {
        return stocksOwned.iterator();
    }
}
