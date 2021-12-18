package scheduleApp.Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import scheduleApp.DBAccess.AppointmentDAO;
import scheduleApp.DBAccess.ContactDAO;
import scheduleApp.DBAccess.ReportDAO;
import scheduleApp.DBAccess.UserDAO;
import scheduleApp.ModelClasses.Appointment;
import scheduleApp.ModelClasses.Contact;
import scheduleApp.ModelClasses.Report;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;

/**
 * The ReportsController class handles the program logic for the Reports form. It allows the user to view three different
 * reports tied to the appointments in the database, which the user can slightly customize to their needs.
 * @author Jason Jackson
 */
public class ReportsController implements Initializable {

    @FXML private Label userLabel;

    @FXML private TableView<Report> report1TableView;
    @FXML private TableColumn<Report, Month> monthColumn;
    @FXML private TableColumn<Report, Integer> typeColumn;
    @FXML private TableColumn<Report, Integer> countColumn;

    @FXML private TableView<Appointment> report2TableView;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn2;
    @FXML private TableColumn<Appointment, String> titleColumn2;
    @FXML private TableColumn<Appointment, String> descriptionColumn2;
    @FXML private TableColumn<Appointment, String> typeColumn2;
    @FXML private TableColumn<Appointment, LocalDateTime> startColumn2;
    @FXML private TableColumn<Appointment, LocalDateTime> endColumn2;
    @FXML private TableColumn<Appointment, Integer> customerIdColumn2;

    @FXML private ComboBox<Contact> contactComboBox;

    @FXML private TableView<Appointment> report3TableView;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn3;
    @FXML private TableColumn<Appointment, String> titleColumn3;
    @FXML private TableColumn<Appointment, String> descriptionColumn3;
    @FXML private TableColumn<Appointment, String> typeColumn3;
    @FXML private TableColumn<Appointment, LocalDateTime> startColumn3;
    @FXML private TableColumn<Appointment, LocalDateTime> endColumn3;
    @FXML private TableColumn<Appointment, String> contactColumn3;
    @FXML private TableColumn<Appointment, Integer> customerIdColumn3;

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    private AppointmentDAO appointmentDAO;
    private ContactDAO contactDAO;
    private ReportDAO reportDAO;

    /**
     * This method initializes the Reports Form and populates all three tabs' TableViews, Labels, ComboBoxes and Date
     * Pickers. Furthermore, it sets up a listener for the two DatePickers in report 3 using a lambda expression.
     * LAMBDA JUSTIFICATION: This lambda was probably the most useful lambda because it simplified the parameters and
     * logic needed for the date picker dramatically. It makes use of the oldValue and newValue with error checking
     * to make sure that the newly picked date is valid.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userLabel.setText("Logged in as: " + UserDAO.getLoggedInUser().getUsername());

        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));


        appointmentIdColumn2.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn2.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn2.setCellValueFactory(new PropertyValueFactory<>("description"));
        startColumn2.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn2.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        typeColumn2.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerIdColumn2.setCellValueFactory(new PropertyValueFactory<>("customerId"));


        appointmentIdColumn3.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn3.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn3.setCellValueFactory(new PropertyValueFactory<>("description"));
        startColumn3.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn3.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        typeColumn3.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerIdColumn3.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        contactColumn3.setCellValueFactory(new PropertyValueFactory<>("contactName"));


        reportDAO = new ReportDAO();
        contactDAO = new ContactDAO();
        appointmentDAO = new AppointmentDAO();

        contactComboBox.setItems(contactDAO.getAll());
        contactComboBox.getSelectionModel().select(0);


        report1TableView.setItems(reportDAO.getAll());
        report2TableView.setItems(appointmentDAO.getAllByContact(contactComboBox.getSelectionModel().getSelectedItem().getId()));
        initializeTimePeriodReport();

        startDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (!newValue.isAfter(endDatePicker.getValue())) { // start date is <= end date
                startDatePicker.setValue(newValue);
                report3TableView.setItems(appointmentDAO.getAllByDates(startDatePicker.getValue(), endDatePicker.getValue()));
            }
            else { // start date after end date
                startDatePicker.setValue(oldValue);
                invalidDateAlert();
            }
        });

        endDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (!newValue.isBefore(startDatePicker.getValue())) { // end date is >= start date
                endDatePicker.setValue(newValue);
                report3TableView.setItems(appointmentDAO.getAllByDates(startDatePicker.getValue(), endDatePicker.getValue()));
            }
            else { // end date before start date
                endDatePicker.setValue(oldValue);
                invalidDateAlert();
            }
        });
    }

    /**
     * This method is called whenever the lambda expression determines that the new date picked by either the
     * start or end date is invalid. It displays an error message to the user.
     */
    private void invalidDateAlert() {
        Alert invalidDatesAlert = new Alert(Alert.AlertType.WARNING);
        invalidDatesAlert.setContentText("Invalid Dates given. Start date must be the same day or earlier than the End Date");
        invalidDatesAlert.setTitle("Invalid Dates");
        invalidDatesAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        invalidDatesAlert.show();
    }

    /**
     * Method called whenever the contact ComboBox is changed. This updates the second report's tableView to the
     * schedule of the appropriate contact.
     */
    public void contactComboChanged() {
        Contact newSelection = contactComboBox.getSelectionModel().getSelectedItem();
        report2TableView.setItems(appointmentDAO.getAllByContact(newSelection.getId()));
    }

    /**
     * This method initializes the DatePickers for report 3 and initializes the TableView to include all appointments.
     */
    private void initializeTimePeriodReport() {
        ObservableList<Appointment> allAppointmentsByDates = appointmentDAO.getAllByDates(null, null);

        startDatePicker.setValue(allAppointmentsByDates.get(0).getStartTime().toLocalDate());
        endDatePicker.setValue(allAppointmentsByDates.get(allAppointmentsByDates.size() - 1).getEndTime().toLocalDate());

        report3TableView.setItems(allAppointmentsByDates);
    }

    /**
     * This method returns the user back to the Main Form when they click the Exit Button.
     * @param event
     * @throws IOException
     */
    public void exitButtonClicked(ActionEvent event) throws IOException
    {

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("../fxml/MainForm.fxml"));
        Parent mainParent = mainLoader.load();
        Scene mainScene = new Scene(mainParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(mainScene);
        window.show();
    }
}
