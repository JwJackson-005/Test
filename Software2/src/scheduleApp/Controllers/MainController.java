package scheduleApp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import scheduleApp.DBAccess.UserDAO;
import scheduleApp.Main;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The MainController class handles the program logic when the user is viewing the Main Form. It routes the user to the
 * correct forms to accomplish their tasks. The user can select to view the Customers, Appointments, or Reports forms,
 * or they have the option to log out to switch users.
 * @author Jason Jackson
 */
public class MainController implements Initializable {

    @FXML
    private Label userLabel;
    @FXML
    private Label versionLabel;
    @FXML
    private Label mainTitle;

    @FXML
    private Button customersButton;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button logoutButton;

    private ResourceBundle rb;

    /**
     * Initializes the main form, including all labels and buttons. This will be localized into whatever language the
     * user logged in with.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = ResourceBundle.getBundle("scheduleApp.lang", Locale.getDefault());

        mainTitle.setText(rb.getString("mainTitle"));
        customersButton.setText(rb.getString("customers"));
        appointmentsButton.setText(rb.getString("appointments"));
        reportsButton.setText(rb.getString("reports"));
        logoutButton.setText(rb.getString("logout"));
        userLabel.setText(rb.getString("user") + " " + UserDAO.getLoggedInUser().getUsername());
        versionLabel.setText(rb.getString("version") + " " + Main.versionNumber);
    }

    /**
     * This method is called when the user clicks on the customers button. It will route the user to the Customers Form.
     * @param event
     * @throws IOException
     */
    public void customerButtonClicked(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/CustomersForm.fxml"));
        Parent parent = loader.load();
        Scene customerScene = new Scene(parent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(customerScene);
        window.setTitle(rb.getString("customers"));
        window.show();
    }

    /**
     * This method is called when the user clicks on the appointments button. It will route the user to the Appointments
     * Form.
     * @param event
     * @throws IOException
     */
    public void appointmentsButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AppointmentsForm.fxml"));
        Parent parent = loader.load();
        Scene customerScene = new Scene(parent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(customerScene);
        window.setTitle(rb.getString("appointments"));
        window.show();
    }

    /**
     * This method is called when the user clicks on the reports button. It will route the user to the Reports Form.
     * @param event
     * @throws IOException
     */
    public void reportsButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ReportsForm.fxml"));
        Parent parent = loader.load();
        Scene customerScene = new Scene(parent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(customerScene);
        window.setTitle(rb.getString("reports"));
        window.show();
    }

    /**
     * This method is called when the user clicks on the Log Out button. It will route the user back to the Login Form.
     * @param event
     * @throws IOException
     */
    public void logOutButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/LogInForm.fxml"));
        Parent parent = loader.load();
        Scene customerScene = new Scene(parent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(customerScene);
        window.setTitle(rb.getString("logout"));
        window.show();
    }

}