package model;

import api.StockDataFromURL;
import parsers.StockDetailsParser;

import java.math.BigDecimal;
import java.util.Objects;

// Represents a single stock with name, ticker, current price and prev closing price
public class Stock {

    // Fields
    private String stockName;
    private String stockTicker;
    private BigDecimal stockPrice;
    private BigDecimal previousClosingPrice;
    private String netChange;
    private String percentChange;
    private boolean purchased;
    private BigDecimal purchasePrice;
    private int quantity = 0;
    private StockDetailsParser parser = new StockDetailsParser();

    // REQUIRES: name & ticker has non-zero length, price >= 0
    // EFFECTS: constructs stock with name, ticker and stock price;
    //          sets previousClosingPrice = 0, purchased = false,
    //          purchasedPrice = 0, quantity = 0

    public Stock(String name, String ticker, BigDecimal price) {
        this.stockName = name;
        this.stockTicker = ticker;
        this.stockPrice = price;

        this.previousClosingPrice = BigDecimal.ZERO;
        this.purchased = false;
        this.purchasePrice = BigDecimal.ZERO;
        this.quantity = 0;
    }

    public String getStockName() {
        return stockName;
    }

    public String getStockTicker() {
        return stockTicker;
    }

    public BigDecimal getStockPrice() {
        return stockPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getPreviousClosingPrice() {
        return previousClosingPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public boolean isPurchased() {
        return purchased;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setStockName(String name) {
        this.stockName = name;
    }

    public void setStockTicker(String ticker) {
        this.stockTicker = ticker; 
    }

    public void setStockPrice(BigDecimal price) {
        this.stockPrice = price;
    }

    public void setPreviousClosingPrice(BigDecimal closingPrice) {
        this.previousClosingPrice = closingPrice;
    }

    public void setPurchased(boolean b) {
        this.purchased = b;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNetChange() {
        return netChange;
    }

    public void setNetChange(String netChange) {
        this.netChange = netChange;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    // MODIFIES: this
    // EFFECTS: updates the fields stockPrice, previousClosingPrice, netChange and percentChange by making an api call
    //          and parsing the data
    public void update() {
        try {
            parser.parseStockData(StockDataFromURL.getStockData(this.stockTicker));
        } catch (Exception e) {
            // should not happen because ticker is always valid
        }
        this.stockPrice = parser.getStock().getStockPrice();
        this.previousClosingPrice = parser.getStock().getPreviousClosingPrice();
        this.netChange = parser.getStock().getNetChange();
        this.percentChange = parser.getStock().getPercentChange();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stock stock = (Stock) o;
        return stockName.equals(stock.stockName) && stockTicker.equals(stock.stockTicker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockName, stockTicker);
    }
}
