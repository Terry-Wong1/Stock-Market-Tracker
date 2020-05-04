package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ui.controllers.MainController;
import ui.controllers.Switchable;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // create new empty Scene
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root);

        // switch scene
        Switchable.scene = scene;
        Switchable.switchScene("Main");

        // display scene
        primaryStage.setScene(scene);
        primaryStage.setTitle("Stock Market Tracker");
        primaryStage.getIcons().add(new Image("ui/icons/finance-icon-png-7.png"));
        primaryStage.show();

        // invokes save feature when stage is closed
        primaryStage.setOnCloseRequest(e -> {
            MainController controller = (MainController) Switchable.getController("Main");
            controller.savePortfolioData();
            controller.saveWatchlistsData();
            System.out.println("Data saved to file");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
