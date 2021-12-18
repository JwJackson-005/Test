package mainForm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the main driver class of the Inventory System. It launches the main method which will pass
 * along into the MainController class via the start() method.
 */



public class Main extends Application {

    /**
     * This is the function that will create and load the starting JavaFX window.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
        primaryStage.setTitle("Software 1 Inventory System");
        primaryStage.setScene(new Scene(root, 800, 350));
        primaryStage.show();
    }

    /**
     * Driver function for the program.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
