package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Portfolio;
import model.Stock;
import model.Watchlist;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioController extends Switchable {
    Portfolio portfolio;
    List<Watchlist> watchlists;

    @FXML private TableView<Stock> portfolioView;
    @FXML private TableColumn<Stock, String> stockTickerColumn;
    @FXML private TableColumn<Stock, BigDecimal> stockPurchasePriceColumn;
    @FXML private TableColumn<Stock, Integer> stockQuantityColumn;
    @FXML private TableColumn<Stock, BigDecimal> currentPriceColumn;
    @FXML private TableColumn<Stock, String> netChangeColumn;
    @FXML private TableColumn<Stock, String> percentChangeColumn;
    @FXML private Text balanceText;
    @FXML private TextField quantityField;

    @FXML
    private void switchToStockInfo() {
        Switchable.switchScene("Main");
        MainController controller = (MainController) Switchable.getController("Main");
        controller.portfolio = portfolio;
        controller.watchlists = watchlists;
    }

    @FXML
    private void switchToWatchlists() {
        Switchable.switchScene("Watchlist");
        WatchlistController controller = (WatchlistController) Switchable.getController("Watchlist");
        controller.portfolio = portfolio;
        controller.watchlists = watchlists;
        controller.fillChoiceBox();
    }

    public void displayPortfolio() {
        // displays portfolio
        setupTableColumns();
        portfolioView.setItems(getPortfolioStocks());
        balanceText.setText("$" + portfolio.getBalance().toString());
        balanceText.setFill(Color.valueOf("51B349"));
    }

    private void setupTableColumns() {
        stockTickerColumn.setCellValueFactory(new PropertyValueFactory<>("stockTicker"));
        stockPurchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        stockQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("stockPrice"));
        netChangeColumn.setCellValueFactory(new PropertyValueFactory<>("netChange"));
        percentChangeColumn.setCellValueFactory(new PropertyValueFactory<>("percentChange"));
        portfolioView.refresh();
    }

    private ObservableList<Stock> getPortfolioStocks() {
        ObservableList<Stock> portfolioStocks = FXCollections.observableArrayList();
        for (Stock s: portfolio) {
            s.update();
        }
        portfolioStocks.addAll(portfolio.getStocksOwned());
        return portfolioStocks;
    }

    @FXML
    private void buyStock() {
        Stock target = portfolioView.getSelectionModel().getSelectedItem();
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            generateAlert("Please input a quantity greater than 0.", "Invalid quantity");
        }
        if (target == null) {
            String alertString = "Please select one of the sample stocks below before hitting the Buy Stock button.";
            generateAlert(alertString, "No Stock was Selected");
        } else if (quantity > 0) {
            portfolio.purchaseStock(target, quantity);
            quantityField.setText("");
            displayPortfolio();
        }
    }

    @FXML
    private void sellStock() {
        Stock target = portfolioView.getSelectionModel().getSelectedItem();
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                throw new NumberFormatException();
            } else if (quantity > target.getQuantity()) {
                throw new Exception();
            }
        } catch (NumberFormatException e) {
            generateAlert("Please input a quantity greater than 0.", "Invalid quantity");
        } catch (Exception e) {
            generateAlert("Cannot sell more stocks than you currently own.", "Invalid Quantity");
        }
        if (target == null) {
            String alertString = "Please select one of the stocks below.";
            generateAlert(alertString, "No Stock was Selected");
        } else if (quantity <= target.getQuantity() && quantity > 0) {
            portfolio.sellStock(target, quantity);
            quantityField.setText("");
            displayPortfolio();
        }
    }

    private void generateAlert(String message, String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.show();
    }

}
