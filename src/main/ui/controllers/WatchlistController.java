package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Portfolio;
import model.Stock;
import model.Watchlist;

import java.math.BigDecimal;
import java.util.List;

public class WatchlistController extends Switchable {
    Portfolio portfolio;
    List<Watchlist> watchlists;

    @FXML ChoiceBox<String> watchlistChoiceBox;
    @FXML private TableView<Stock> watchlistView;
    @FXML private TableColumn<Stock, String> stockTickerColumn;
    @FXML private TableColumn<Stock, BigDecimal> currentPriceColumn;
    @FXML private TableColumn<Stock, BigDecimal> prevClosePriceColumn;
    @FXML private TableColumn<Stock, String> netChangeColumn;
    @FXML private TableColumn<Stock, String> percentChangeColumn;

    @FXML private TextField nameField;

    @FXML
    private void displayWatchlist() {
        String watchlistName = watchlistChoiceBox.getValue();
        if (watchlistName == null) {
            watchlistView.getItems().clear();
        } else {
            Watchlist watchlist = getWatchlist(watchlistName);
            setupTableColumns();
            watchlistView.setItems(getWatchlistStocks(watchlist));
        }
    }

    private void setupTableColumns() {
        stockTickerColumn.setCellValueFactory(new PropertyValueFactory<>("stockTicker"));
        currentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("stockPrice"));
        prevClosePriceColumn.setCellValueFactory(new PropertyValueFactory<>("previousClosingPrice"));
        netChangeColumn.setCellValueFactory(new PropertyValueFactory<>("netChange"));
        percentChangeColumn.setCellValueFactory(new PropertyValueFactory<>("percentChange"));
        watchlistView.refresh();
    }

    private ObservableList<Stock> getWatchlistStocks(Watchlist watchlist) {
        ObservableList<Stock> watchlistStocks = FXCollections.observableArrayList();
        for (Stock s: watchlist) {
            s.update();
        }
        watchlistStocks.addAll(watchlist.getStocksToWatch());
        return watchlistStocks;
    }

    @FXML
    private void createWatchlist() {
        String name = nameField.getText();
        if (name.equals("")) {
            generateAlert("Please enter a name for the watchlist", "No Name Entered");
        } else if (getWatchlist(name) != null) {
            generateAlert("Please enter a unique watchlist name.", "Watchlist Name Taken");
        } else {
            watchlists.add(new Watchlist(name));
            fillChoiceBox();
            nameField.setText("");
        }
    }

    @FXML
    private void deleteWatchlist() {
        String watchlistToDelete = watchlistChoiceBox.getValue();
        if (watchlistToDelete == null) {
            generateAlert("No watchlist available to delete", "Error");
        } else {
            Watchlist watchlist = getWatchlist(watchlistToDelete);
            watchlists.remove(watchlist);
            fillChoiceBox();
            displayWatchlist();
        }
    }

    @FXML
    private void removeStock() {
        Stock target = watchlistView.getSelectionModel().getSelectedItem();
        Watchlist watchlist = getWatchlist(watchlistChoiceBox.getValue());
        if (target == null) {
            String alertString = "Please select one of stocks below.";
            generateAlert(alertString, "No Stock was Selected");
        } else {
            watchlist.removeStock(target);
            displayWatchlist();
        }
    }

    @FXML
    private void switchToStockInfo() {
        Switchable.switchScene("Main");
        MainController controller = (MainController) Switchable.getController("Main");
        controller.portfolio = portfolio;
        controller.watchlists = watchlists;
        controller.fillChoiceBox();
    }

    @FXML
    private void switchToPortfolio() {
        Switchable.switchScene("Portfolio");
        PortfolioController controller = (PortfolioController) Switchable.getController("Portfolio");
        controller.portfolio = portfolio;
        controller.watchlists = watchlists;
        controller.displayPortfolio();
    }

    public void fillChoiceBox() {
        ObservableList<String> watchlistObservableList = FXCollections.observableArrayList();
        for (Watchlist watchlist : watchlists) {
            watchlistObservableList.add(watchlist.getListName());
        }
        watchlistChoiceBox.setItems(watchlistObservableList);
        watchlistChoiceBox.getSelectionModel().select(0);
    }

    private Watchlist getWatchlist(String name) {
        for (Watchlist watchlist : watchlists) {
            if (watchlist.getListName().equals(name)) {
                return watchlist;
            }
        }
        return null;
    }

    private void generateAlert(String message, String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.show();
    }
}
