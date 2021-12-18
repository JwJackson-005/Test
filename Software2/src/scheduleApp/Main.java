package scheduleApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduleApp.utils.DBConnection;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Driver class for the program
 */
public class Main extends Application {

    /**
     * The constant for the current version number.
     */
    public static double versionNumber = 1.00;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("./fxml/LogInForm.fxml"));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("scheduleApp.lang", Locale.getDefault());
        primaryStage.setTitle(resourceBundle.getString("login"));
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        DBConnection.startConnection();

        //Locale.setDefault(new Locale("fr", "France"));
        launch(args);

        DBConnection.closeConnection();
    }
}
