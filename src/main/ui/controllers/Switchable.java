package ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;

/* */
public abstract class Switchable {
    public static Scene scene;
    public static final HashMap<String, Switchable> CONTROLLERS = new HashMap<>();
    private Parent root;

    public static Switchable add(String name) {
        // look inside HashMap to see if controller already exists
        Switchable controller = CONTROLLERS.get(name);

        // if controller doesn't exist yet; load it from FXML file
        if (controller == null) {
            try {
                FXMLLoader loader = new FXMLLoader(Switchable.class.getResource(name + ".fxml"));
                Parent root = loader.load();
                controller = loader.getController();
                controller.setRoot(root);
                CONTROLLERS.put(name, controller);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return controller;
    }

    public static void switchScene(String name) {
        Switchable controller = CONTROLLERS.get(name);

        if (controller == null) {
            controller = add(name);
        }

        if (controller != null) {
            if (scene != null) {
                scene.setRoot(controller.getRoot());
                controller.getRoot().requestFocus();
            }
        }
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public Parent getRoot() {
        return root;
    }

    public static Switchable getController(String name) {
        return CONTROLLERS.get(name);
    }
}
