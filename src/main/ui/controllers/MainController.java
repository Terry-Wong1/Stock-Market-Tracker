package ui.controllers;

import api.StockDataFromURL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import model.Portfolio;
import model.Stock;
import model.Watchlist;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import parsers.StockDetailsParser;
import parsers.exceptions.QueryLimitExceededException;
import parsers.exceptions.StockParseException;
import persistence.DataSaver;
import persistence.Reader;
import persistence.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*Represents a controller to handle user events */

public class MainController extends Switchable {
    public static final String PORTFOLIO_DATA = "./data/portfolio.json";
    public static final String WATCHLISTS_DATA = "./data/watchlists.json";
    private Reader reader = new Reader();
    private Writer writer;
    private Stock stock;
    Portfolio portfolio = new Portfolio();
    List<Watchlist> watchlists = new ArrayList<>();
    StockDetailsParser parser = new StockDetailsParser();

    @FXML private Text stockTicker;
    @FXML private Text stockPrice;
    @FXML private Text priceChange;
    @FXML private Text percentChange;
    @FXML private Text dayLow;
    @FXML private Text dayHigh;
    @FXML private Text tradeVolume;
    @FXML private Text previousClose;
    @FXML private Text openPrice;

    @FXML private TextField stockSearch;
    @FXML private TextField quantityField;
    @FXML private ChoiceBox<String> watchlistChoiceBox;


    @FXML
    private void initialize() {
        loadPortfolioData();
        loadWatchlistData();
        fillChoiceBox();
    }

    private void loadPortfolioData() {
        try {
            portfolio = reader.readPortfolio(new FileReader(PORTFOLIO_DATA));
        } catch (IOException e) {
            System.out.println("No previous portfolio data to load");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadWatchlistData() {
        try {
            watchlists = reader.readWatchlists(new FileReader(WATCHLISTS_DATA));
        } catch (IOException e) {
            System.out.println("No previous watchlists data found");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void fillChoiceBox() {
        ObservableList<String> watchlistObservableList = FXCollections.observableArrayList();
        for (Watchlist watchlist : watchlists) {
            watchlistObservableList.add(watchlist.getListName());
        }
        watchlistChoiceBox.setItems(watchlistObservableList);
        watchlistChoiceBox.getSelectionModel().select(0);
    }

    public void savePortfolioData() {
        try {
            writer = new Writer(new File(PORTFOLIO_DATA));
            JSONObject portfolioData = DataSaver.savePortfolioData(portfolio);
            writer.write(portfolioData.toJSONString());
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save portfolio data to " + PORTFOLIO_DATA);
        }
    }

    public void saveWatchlistsData() {
        try {
            writer = new Writer(new File(WATCHLISTS_DATA));
            JSONArray watchlistsData = new JSONArray();
            for (Watchlist w : watchlists) {
                JSONObject watchlistData = DataSaver.saveWatchlistData(w);
                watchlistsData.add(watchlistData);
            }
            writer.write(watchlistsData.toJSONString());
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save watchlists data to " + WATCHLISTS_DATA);
        }
    }

    @FXML
    private void buyStock() {
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            generateAlert("Please input a quantity greater than 0.", "Invalid quantity");
        }
        if (stock == null) {
            String alertString = "Please search for a stock ticker before hitting the buy stock button";
            generateAlert(alertString, "No Stock was found");
        } else if (quantity > 0) {
            portfolio.purchaseStock(stock, quantity);
            quantityField.setText("");
        }
    }

    @FXML
    private void addToWatchlist() {
        int index = watchlistChoiceBox.getSelectionModel().getSelectedIndex();
        Watchlist watchlist = watchlists.get(index);
        String alertString = "Please search for a stock ticker before adding it to watchlist.";
        if (stock == null) {
            generateAlert(alertString, "No Stock was found");
        } else if (watchlist != null) {
            watchlist.addStock(stock);
        }
    }

    @FXML
    private void findStock() {
        String ticker = stockSearch.getText();
        String responseContent = StockDataFromURL.getStockData(ticker);
        try {
            parser.parseStockData(responseContent);
            this.stock = parser.getStock();
            displayStockInformation();
        } catch (StockParseException e) {
            generateAlert("No such stock exists", "Invalid Ticker Entered");
        } catch (QueryLimitExceededException q) {
            generateAlert("Please wait 1 minute, before trying again.", "Number of queries exceeded");
        }
    }

    @FXML
    private void switchToPortfolio() {
        Switchable.switchScene("Portfolio");
        PortfolioController controller = (PortfolioController) Switchable.getController("Portfolio");
        controller.portfolio = portfolio;
        controller.watchlists = watchlists;
        controller.displayPortfolio();
    }

    @FXML
    private void switchToWatchlists() {
        Switchable.switchScene("Watchlist");
        WatchlistController controller = (WatchlistController) Switchable.getController("Watchlist");
        controller.portfolio = portfolio;
        controller.watchlists = watchlists;
        controller.fillChoiceBox();
    }

    private void generateAlert(String message, String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.show();
    }

    private void displayStockInformation() {
        stockTicker.setText(stock.getStockTicker());
        String price = stock.getStockPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
        stockPrice.setText("$" + price);
        if (Double.parseDouble(stock.getNetChange()) < 0) {
            priceChange.setText(stock.getNetChange());
            priceChange.setFill(Color.RED);
            percentChange.setText("(" + stock.getPercentChange() + "%" + ")");
            percentChange.setFill(Color.RED);
        } else {
            priceChange.setText(stock.getNetChange());
            priceChange.setFill(Color.GREEN);
            percentChange.setText("(" + stock.getPercentChange() + "%" + ")");
            percentChange.setFill(Color.GREEN);
        }

        openPrice.setText(parser.getOpenPrice());
        dayLow.setText(parser.getDayLow());
        dayHigh.setText(parser.getDayHigh());
        tradeVolume.setText(parser.getVolume());
        String prevClose = stock.getPreviousClosingPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
        previousClose.setText(prevClose);
    }

}
