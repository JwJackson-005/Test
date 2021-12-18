package scheduleApp.Controllers;

import javafx.collections.FXCollections;
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
import javafx.stage.Stage;
import scheduleApp.DBAccess.AppointmentDAO;
import scheduleApp.DBAccess.ContactDAO;
import scheduleApp.DBAccess.CustomerDAO;
import scheduleApp.DBAccess.UserDAO;
import scheduleApp.ModelClasses.*;
import scheduleApp.utils.BusinessTimeUtil;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The AppointmentsController class is the Controller class used to handle the program logic while the user is viewing
 * the Appointments Form. It interfaces heavily with the AppointmentsDAO class to pull data from the Model classes in
 * the database.
 * @author Jason Jackson
 */

public class AppointmentsController implements Initializable {

    @FXML private TableView<Appointment> appointmentTableView;

    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, String> contactColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> startTimeColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> endTimeColumn;
    @FXML private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML private Label userLabel;
    @FXML private TextField appointmentIdTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField typeTextField;
    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<LocalTime> endTimeComboBox;
    @FXML private ComboBox<Customer> customerIdComboBox;

    @FXML private Button addModifyButton;
    @FXML private Button deleteButton;

    @FXML private ToggleGroup weekOrMonth;
    @FXML private RadioButton weeklyRadioButton;
    @FXML private RadioButton monthlyRadioButton;
    @FXML private RadioButton allRadioButton;

    private AppointmentDAO appointmentDAO;

    /**
     * This method initializes all of the form fields, sets up the proper tableView column factories, and creates
     * a listener for the Appointments TableView using a lambda expression.
     * LAMBDA JUSTIFICATION: The lambda is very helpful in this method because it quickly and cleanly sets up the
     * listener for the TableView to update the form fields and change the addModify button to the modify state. If
     * not for using this lambda, I would have had to pass multiple values to one or more methods.
     * @param url the url
     * @param resourceBundle the resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText("Logged in as: " + UserDAO.getLoggedInUser().getUsername());

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        appointmentDAO = new AppointmentDAO();

        deleteButton.setDisable(true);

        weekOrMonth = new ToggleGroup();
        weekOrMonth.getToggles().add(weeklyRadioButton);
        weekOrMonth.getToggles().add(monthlyRadioButton);
        weekOrMonth.getToggles().add(allRadioButton);
        weekOrMonth.selectToggle(weeklyRadioButton);
        populateTableView();

        ContactDAO contactDAO = new ContactDAO();
        contactComboBox.setItems(contactDAO.getAll());

        CustomerDAO customerDAO = new CustomerDAO();
        customerIdComboBox.setItems(customerDAO.getAll());

        populateTimeComboBoxes();

        appointmentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                addModifyButton.setText("Modify");
                deleteButton.setDisable(false);

                appointmentIdTextField.setText(String.valueOf(newSelection.getId()));
                titleTextField.setText(newSelection.getTitle());
                descriptionTextField.setText(newSelection.getDescription());
                locationTextField.setText(newSelection.getLocation());
                startDatePicker.setValue(newSelection.getStartTime().toLocalDate());
                endDatePicker.setValue(newSelection.getEndTime().toLocalDate());
                typeTextField.setText(newSelection.getType());
                startTimeComboBox.getSelectionModel().select(newSelection.getStartTime().toLocalTime());
                endTimeComboBox.getSelectionModel().select(newSelection.getEndTime().toLocalTime());

                for (int i = 0; i < contactComboBox.getItems().size(); i++) {
                    Contact currentContact = contactComboBox.getItems().get(i);
                    if (currentContact.getId() == newSelection.getContactId()) {
                        contactComboBox.setValue(currentContact);
                        break;
                    }
                }

                for (int i = 0; i < customerIdComboBox.getItems().size(); i++) {
                    Customer currentCustomer = customerIdComboBox.getItems().get(i);
                    if (currentCustomer.getId() == newSelection.getCustomerId()) {
                        customerIdComboBox.setValue(currentCustomer);
                        break;
                    }
                }
            }
        });
    }

    /**
     * This method is called to populate the Start Time and End Time combo boxes with times starting at 00:00 to 23:45
     * in 15 minute intervals.
     */
    private void populateTimeComboBoxes() {

        for(int i = 0; i < 24; i++) {
            for(int j = 0; j < 60; j+=15) {
                startTimeComboBox.getItems().add(LocalTime.of(i, j, 0));
                endTimeComboBox.getItems().add(LocalTime.of(i, j, 0));
            }
        }
    }

    /**
     * This method checks whether the addModify button is currently showing add or if it shows modify, then routes
     * the program to the appropriate functionality.
     */
    public void addOrModifyClicked() {
        if (addModifyButton.getText().compareTo("Add") == 0)
            addButtonClicked();
        else // Modify Clicked
            modifyButtonClicked();
    }

    /**
     * This method is called when the user has filled out the form manually, does not have an appointment selected, and
     * clicks the add button. It will add a new appointment to the database if the form fields are valid.
     */
    public void addButtonClicked()
    {
        Appointment validAppointment = validAppointment();

        if (validAppointment != null) {
            appointmentDAO.create(validAppointment);
            populateTableView();
        }
    }

    /**
     * This method is called when the user has selected an appointment, had the form fill out automatically, and
     * clicks the modify button. It will update the selected appointment in the database if the form fields are valid.
     */
    public void modifyButtonClicked()
    {
        Appointment validAppointment = validAppointment();

        if (validAppointment != null) {
            appointmentDAO.update(validAppointment);
            populateTableView();
        }
    }

    /**
     * This method is called when the user selects an appointment and then clicks the delete button. It will delete
     * the selected appointment from the database after a confirmation window is confirmed.
     */
    public void deleteButtonClicked()
    {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

        if(selectedAppointment != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Are you sure you want to delete the \n" +
                    selectedAppointment.getTitle() + " appointment?");
            confirmationAlert.setTitle("Are you sure?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.get() == ButtonType.OK) {
                if(appointmentDAO.delete(selectedAppointment.getId())) {
                    appointmentTableView.setItems(appointmentDAO.getAllByUser());

                    Alert deleteConfirmedAlert = new Alert((Alert.AlertType.INFORMATION));
                    deleteConfirmedAlert.setContentText("The following appointment has been cancelled:" +
                            "\nAppointment ID: " + selectedAppointment.getId() +
                            "\nType: " + selectedAppointment.getType());
                    deleteConfirmedAlert.setTitle("Delete Appointment");
                    deleteConfirmedAlert.show();
                }
            }
        }
        else {
            Alert notSelectedAlert = new Alert(Alert.AlertType.WARNING);
            notSelectedAlert.setContentText("You must select an appointment to delete");
            notSelectedAlert.setTitle("No Appointment Selected");
            notSelectedAlert.show();
        }
    }

    /**
     * This method is called when the clear selection button is clicked. It will deselect an appointment from the
     * tableView and then reset all the form fields to their default value.
     */

    public void clearButtonClicked() {
        addModifyButton.setText("Add");
        deleteButton.setDisable(true);

        appointmentTableView.getSelectionModel().clearSelection();

        appointmentIdTextField.clear();
        titleTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        typeTextField.clear();
        contactComboBox.getSelectionModel().select(null);
        startTimeComboBox.getSelectionModel().select(null);
        endTimeComboBox.getSelectionModel().select(null);
        customerIdComboBox.getSelectionModel().select(null);
    }

    /**
     * This method will check that all the form fields are valid, ensure that the appointment
     * falls within business hours and does not conflict with other appointments. If so, it will return the valid
     * appointment. If not, it will return null.
     * @return the valid appointment if the form is valid, otherwise null
     */

    private Appointment validAppointment() {

        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Contact contact = contactComboBox.getSelectionModel().getSelectedItem();
        String type = typeTextField.getText();
        LocalDateTime startTime = LocalDateTime.of(startDate, startTimeComboBox.getSelectionModel().getSelectedItem());
        LocalDateTime endTime = LocalDateTime.of(endDate, endTimeComboBox.getSelectionModel().getSelectedItem());
        Customer customer = customerIdComboBox.getSelectionModel().getSelectedItem();

        StringBuilder stringBuilder = new StringBuilder();

        // test for empty fields
        if(title.compareTo("") == 0)
            stringBuilder.append("Title\n");
        if(description.compareTo("") == 0)
            stringBuilder.append("Description\n");
        if(location.compareTo("") == 0)
            stringBuilder.append("Location\n");
        if(startDate == null)
            stringBuilder.append("Start Date\n");
        if(endDate == null)
            stringBuilder.append("End Date\n");
        if(contact == null)
            stringBuilder.append("Contact\n");
        if(type.compareTo("") == 0)
            stringBuilder.append("Type\n");
        if(startTime == null)
            stringBuilder.append("Start Time\n");
        if(endTime == null)
            stringBuilder.append("End Time\n");
        if(customer == null)
            stringBuilder.append("Customer ID\n");


        if (stringBuilder.length() > 0) {// empty fields found
            Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
            emptyFieldAlert.setContentText("The following fields must have values:\n" + stringBuilder);
            emptyFieldAlert.setTitle("Empty Text Fields");
            emptyFieldAlert.show();
            return null;
        }

        if (!startTime.isBefore(endTime)) {
            Alert invalidTime = new Alert(Alert.AlertType.ERROR);
            invalidTime.setContentText("The start time must be before the end time!");
            invalidTime.setTitle("Invalid Appointment Time");
            invalidTime.show();
            return null;
        }

        if (!BusinessTimeUtil.isWithinBusinessHours(startTime, endTime)) {
            Alert outsideBusinessHoursAlert = new Alert(Alert.AlertType.WARNING);
            outsideBusinessHoursAlert.setContentText("This appointment is out of business hours.\n" + BusinessTimeUtil.getBusinessHours());
            outsideBusinessHoursAlert.setTitle("Outside of Business Hours");
            outsideBusinessHoursAlert.show();
            return null;
        }

        Appointment newAppointment = new Appointment(title, description, location, type, startTime, endTime,
                customer.getId(), UserDAO.getLoggedInUser().getId(), contact.getId(), contact.getName());

        try {
            newAppointment.setId(Integer.parseInt(appointmentIdTextField.getText()));
        } catch (NumberFormatException e) {
            // do nothing
        }

        if(isSchedulingConflict(newAppointment)) {
            Alert scheduleConflictAlert = new Alert(Alert.AlertType.WARNING);
            scheduleConflictAlert.setContentText("This appointment conflicts with another appointment.\n" +
                    "Please adjust the time of this appointment.");
            scheduleConflictAlert.setTitle("Scheduling Conflict");
            scheduleConflictAlert.show();
            return null;
        }

        return newAppointment;
    }

    /**
     * This method checks whether a new appointment will conflict schedules with any existing appointments.
     * @param newAppointment the appointment to test against other scheduling conflicts.
     * @return a boolean whether the schedule is in conflict or not.
     */
    private boolean isSchedulingConflict(Appointment newAppointment) {
        boolean NO_CONFLICT = false;
        boolean CONFLICT = true;
        LocalDateTime s2 = newAppointment.getStartTime();
        LocalDateTime e2 = newAppointment.getEndTime();

        ObservableList<Appointment> appointments = appointmentDAO.getAllByCustomer(newAppointment.getCustomerId());

        for (Appointment appointment : appointments) { // if (start is after or equal to start) and (end is before or equal to end)

            if(appointment.getId() != newAppointment.getId()) {
                LocalDateTime s1 = appointment.getStartTime();
                LocalDateTime e1 = appointment.getEndTime();

                if (!(((s2.isBefore(s1)) && !(e2.isAfter(s1))) || (!(s2.isBefore(e1)) && (e2.isAfter(e1)))))
                    return CONFLICT;
            }
        }

        return NO_CONFLICT;
    }

    /**
     * This method is called when the exit button is clicked by the user. It will return the user to the Main Form.
     * @param event the ActionEvent from the click.
     * @throws IOException
     */
    public void exitButtonClicked(ActionEvent event) throws IOException {

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("../fxml/MainForm.fxml"));
        Parent mainParent = mainLoader.load();
        Scene mainScene = new Scene(mainParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(mainScene);
        window.setTitle("Main");
        window.show();
    }

    /**
     * This method will populate the Appointments TableView corresponding with whether the weekly, monthly,
     * or all view radio button is selected.
     */
    public void populateTableView() {
        ObservableList<Appointment> allAppointments = appointmentDAO.getAllByUser();

        if (weekOrMonth.getSelectedToggle().equals(weeklyRadioButton)) {
            ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();

            for (Appointment appointment : allAppointments) {
                if (appointment.getStartTime().isAfter(LocalDateTime.now()) &&
                        appointment.getStartTime().isBefore(LocalDateTime.now().plusWeeks(1))) {
                    weeklyAppointments.add(appointment);
                }
            }

            appointmentTableView.setItems(weeklyAppointments);
        }
        else if (weekOrMonth.getSelectedToggle().equals(monthlyRadioButton)){
            ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();

            for (Appointment appointment : allAppointments) {
                if (appointment.getStartTime().isAfter(LocalDateTime.now()) &&
                        appointment.getStartTime().isBefore(LocalDateTime.now().plusMonths(1))) {
                    monthlyAppointments.add(appointment);
                }
            }

            appointmentTableView.setItems(monthlyAppointments);
        }
        else { // all selected
            appointmentTableView.setItems(appointmentDAO.getAll());
        }
    }
}
