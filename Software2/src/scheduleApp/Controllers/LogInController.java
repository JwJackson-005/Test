package scheduleApp.Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import scheduleApp.DBAccess.AppointmentDAO;
import scheduleApp.DBAccess.UserDAO;
import scheduleApp.ModelClasses.Appointment;
import scheduleApp.utils.BusinessTimeUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The LogInController class is used to handle the program logic for the user while viewing the Login form. It
 * has functionality to log in the user with proper credentials, or deny the user with invalid credentials.
 * @author Jason Jackson
 */
public class LogInController implements Initializable {

    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;

    @FXML private Label username;
    @FXML private Label password;
    @FXML private Label loginTitle;
    @FXML private Label regionLabel;

    @FXML private Button logInButton;
    @FXML private Button exitButton;

    private ResourceBundle rb = ResourceBundle.getBundle("scheduleApp.lang", Locale.getDefault());

    /**
     * This method initializes the form and sets the appropriate language text for all labels and buttons
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginTitle.setText(rb.getString("loginTitle"));
        username.setText(rb.getString("username"));
        password.setText(rb.getString("password"));
        regionLabel.setText(rb.getString("region") + ": " + ZoneId.systemDefault());

        logInButton.setText(rb.getString("login"));
        exitButton.setText(rb.getString("exit"));

    }

    /**
     * This method is called when the log in button is clicked. It will attempt to verify the user and either display
     * and error message if the username and password does not match a user in the database, or log the user in to the
     * program if a match is found. On success, it will check if the user has an appointment coming in the next 15
     * minutes.
     * @param event
     * @throws IOException
     */
    public void logInButtonClicked(ActionEvent event) throws IOException
    {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        boolean outcome;

        if (outcome = UserDAO.verifyUser(username, password)){
            checkUpcomingAppointments();
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("../fxml/MainForm.fxml"));
            Parent mainParent = mainLoader.load();
            Scene mainScene = new Scene(mainParent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(mainScene);
            window.setTitle(rb.getString("mainTitle"));
            window.show();
        }

        else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText(rb.getString("loginError"));
            a.show();
        }

        recordLoginActivity(username, outcome);
    }

    /**
     * This method checks if there are any upcoming appointments for a successfully logged in user. If one is found, it
     * will display the appointment ID, Date, and Time in an Alert box. If there is no upcoming appointment, it will
     * display a message letting the user know that no appointment is upcoming.
     */
    private void checkUpcomingAppointments() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        boolean upcomingAppointmentFound = false;
        ObservableList<Appointment> appointments = appointmentDAO.getAll();

        for(Appointment appointment : appointments) {
            if (!appointment.getStartTime().isBefore(LocalDateTime.now()) &&
                    appointment.getStartTime().isBefore(LocalDateTime.now().plusMinutes(15))) {
                Alert upcomingAppointment = new Alert(Alert.AlertType.INFORMATION);
                upcomingAppointment.setContentText(rb.getString("upcomingAppointment") +
                        "\nAppointment ID: " + String.valueOf(appointment.getId()) +
                        "\nDate: " + appointment.getStartTime().toLocalDate() +
                        "\nTime: " + appointment.getStartTime().toLocalTime());
                upcomingAppointment.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                upcomingAppointment.setTitle(rb.getString("upComingAppointmentTitle"));
                upcomingAppointment.show();
                upcomingAppointmentFound = true;
                break;
            }
        }

        if(!upcomingAppointmentFound) {
            Alert noUpcomingAppointment = new Alert(Alert.AlertType.INFORMATION);
            noUpcomingAppointment.setContentText(rb.getString("noUpComingAppointment"));
            noUpcomingAppointment.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            noUpcomingAppointment.setTitle(rb.getString("noUpComingAppointmentTitle"));
            noUpcomingAppointment.show();
        }
    }

    /**
     * This method will log any attempt at logging in to a text file. The log includes the username, the date, the time,
     * and whether or not the attempt was successful.
     * @param username the username that attempted to log in
     * @param outcome whether the log in was successful or not
     */
    private void recordLoginActivity(String username, boolean outcome) {
        try {
            File loginFile = new File("login_activity.txt");
            if (!loginFile.exists()) {
                loginFile.createNewFile();
            }
            Writer output = new FileWriter(loginFile, true);
            ZonedDateTime utcTime = BusinessTimeUtil.localToUTC(LocalDateTime.now());
            String utcString = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).format(utcTime);
            String successOrReject = (outcome? "SUCCESSFUL":"DENIED");
            output.write("Login attempt by: " + username + " " + successOrReject + " At: " + utcString +"\n");
            output.close();
        }
        catch (IOException e) {
            System.out.println("Could not write to login file");
            e.printStackTrace();
        }
    }

    /**
     * This method exits the program when the exit button is clicked.
     */
    public void cancelButtonClicked()
    {
        System.exit(-1);
    }


}
