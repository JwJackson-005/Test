package scheduleApp.Controllers;

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
import scheduleApp.DBAccess.CountryDAO;
import scheduleApp.DBAccess.CustomerDAO;
import scheduleApp.DBAccess.FirstLevelDivisionDAO;
import scheduleApp.DBAccess.UserDAO;
import scheduleApp.ModelClasses.Country;
import scheduleApp.ModelClasses.Customer;
import scheduleApp.ModelClasses.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The CustomersController class is the Controller class used to handle the program logic while the user is viewing
 * the Customers Form. It interfaces heavily with the CustomersDAO class to pull data from the Model classes in
 * the database.
 * @author Jason Jackson
 */
public class CustomersController implements Initializable {

    @FXML private Label customersTitle;
    @FXML private Label customerIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private Label postalLabel;
    @FXML private Label countryLabel;
    @FXML private Label divisionLabel;

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerAddressColumn;
    @FXML private TableColumn<Customer, String> customerPostalColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;
    @FXML private TableColumn<Customer, String> customerDivisionColumn;

    @FXML private Label userLabel;
    @FXML private TextField customerIdTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField postalTextField;

    @FXML private ComboBox<Country> countryComboBox;
    @FXML private ComboBox<FirstLevelDivision> divisionComboBox;

    @FXML private Button addModifyButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    @FXML private Button exitButton;

    private CustomerDAO customerDAO;
    private ResourceBundle rb;

    /**
     * This method initializes all of the form fields, sets up the proper tableView column factories, and creates
     * a listener for the Customers TableView using a lambda expression.
     * LAMBDA JUSTIFICATION: The lambda is very helpful in this method because it quickly and cleanly sets up the
     * listener for the TableView to update the form fields and change the addModify button to the modify state. If
     * not for using this lambda, I would have had to pass multiple values to one or more methods.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        rb = ResourceBundle.getBundle("scheduleApp.lang", Locale.getDefault());

        userLabel.setText(rb.getString("user") + " " + UserDAO.getLoggedInUser().getUsername());
        customersTitle.setText(rb.getString("customers"));
        customerIdLabel.setText(rb.getString("customerId") + ":");
        nameLabel.setText(rb.getString("name") + ":");
        phoneLabel.setText(rb.getString("phone") + ":");
        addressLabel.setText(rb.getString("address") + ":");
        postalLabel.setText(rb.getString("postal") + ":");
        countryLabel.setText(rb.getString("country") + ":");
        divisionLabel.setText(rb.getString("division") + ":");

        addModifyButton.setText(rb.getString("add"));
        deleteButton.setText(rb.getString("delete"));
        clearButton.setText(rb.getString("clear"));
        exitButton.setText(rb.getString("exit"));

        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerNameColumn.setText(rb.getString("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerAddressColumn.setText(rb.getString("address"));
        customerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPostalColumn.setText(rb.getString("postal"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerPhoneColumn.setText(rb.getString("phone"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        customerDivisionColumn.setText(rb.getString("division"));

        customerDAO = new CustomerDAO();
        customerTableView.setItems(customerDAO.getAll());

        CountryDAO countryDAO = new CountryDAO();
        countryComboBox.setItems(countryDAO.getAll());

        countryComboBox.setPromptText(rb.getString("countryPrompt"));
        divisionComboBox.setPromptText(rb.getString("divisionPrompt"));
        divisionComboBox.setDisable(true);
        deleteButton.setDisable(true);

        customerTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                addModifyButton.setText(rb.getString("modify"));
                deleteButton.setDisable(false);
                customerIdTextField.setText(String.valueOf(newSelection.getId()));
                nameTextField.setText(newSelection.getName());
                phoneTextField.setText(newSelection.getPhoneNumber());
                addressTextField.setText(newSelection.getAddress());
                postalTextField.setText(newSelection.getPostalCode());

                for (int i = 0; i < countryComboBox.getItems().size(); i++) {
                    Country currentCountry = countryComboBox.getItems().get(i);
                    if (currentCountry.getId() == newSelection.getDivision().getCountry().getId()) {
                        countryComboBox.setValue(currentCountry);
                        break;
                    }
                }

                for (int i = 0; i < divisionComboBox.getItems().size(); i++) {
                    FirstLevelDivision currentDivision = divisionComboBox.getItems().get(i);
                    if (currentDivision.getId() == newSelection.getDivision().getId()) {
                        divisionComboBox.setValue(currentDivision);
                        break;
                    }
                }
            }
        });
    }

    /**
     * This method is called when the country combo box is changed. It will update the division ComboBox to reflect
     * the First Level Divisions appropriate to the selected country.
     */
    public void countryComboBoxChanged() {

        if(!countryComboBox.getSelectionModel().isEmpty()) {
            FirstLevelDivisionDAO divisionDAO = new FirstLevelDivisionDAO();
            divisionComboBox.setItems(divisionDAO.getAllByCountry(countryComboBox.getSelectionModel().getSelectedItem()));
            divisionComboBox.setDisable(false);
        }
    }

    /**
     * This method checks whether the addModify button is currently showing add or if it shows modify, then routes
     * the program to the appropriate functionality.
     */
    public void addOrModifyClicked() {
        if (addModifyButton.getText().compareTo(rb.getString("add")) == 0)
            addButtonClicked();
        else // Modify Clicked
            modifyButtonClicked();
    }

    /**
     * This method is called when the user has filled out the form manually, does not have a customer selected, and
     * clicks the add button. It will add a new customer to the database if the form fields are valid.
     */
    private void addButtonClicked() {

        Customer validCustomer = validCustomer();

        if (validCustomer != null) {
            customerDAO.create(validCustomer);
            customerTableView.setItems(customerDAO.getAll());
        }
    }

    /**
     * This method is called when the user has selected a customer, had the form fill out automatically, and
     * clicks the modify button. It will update the selected customer in the database if the form fields are valid.
     */
    private void modifyButtonClicked()
    {
        Customer validCustomer = validCustomer();

        if (validCustomer != null) {
            customerDAO.update(validCustomer);
            customerTableView.setItems(customerDAO.getAll());
        }
    }

    /**
     * This method is called when the clear selection button is clicked. It will deselect a customer from the
     * tableView and then reset all the form fields to their default value.
     */
    public void clearButtonClicked() {
        addModifyButton.setText(rb.getString("add"));
        deleteButton.setDisable(true);

        customerTableView.getSelectionModel().clearSelection();

        customerIdTextField.clear();
        customerIdTextField.setPromptText(rb.getString("customerIdPrompt"));
        nameTextField.clear();
        phoneTextField.clear();
        addressTextField.clear();
        postalTextField.clear();
        divisionComboBox.getSelectionModel().clearSelection();
        countryComboBox.getSelectionModel().clearSelection();
        customerTableView.getSelectionModel().clearSelection();
    }

    /**
     * This method will check that all the form fields are valid and ensure that the customer
     * phone and postal code are valid forms with a very basic check. If so, it will return the valid
     * Customer. If not, it will return null.
     * @return the valid customer if the form is valid, otherwise null
     */
    private Customer validCustomer() {

        String name = nameTextField.getText();
        String phone = phoneTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalTextField.getText();
        FirstLevelDivision division = divisionComboBox.getSelectionModel().getSelectedItem();

        StringBuilder stringBuilder = new StringBuilder();

        // test for empty fields
        if(name.compareTo("") == 0)
            stringBuilder.append(rb.getString("name") + "\n");
        if(phone.compareTo("") == 0)
            stringBuilder.append(rb.getString("phone") + "\n");
        if(address.compareTo("") == 0)
            stringBuilder.append(rb.getString("address") + "\n");
        if(postalCode.compareTo("") == 0)
            stringBuilder.append(rb.getString("postal") + "\n");
        if(division == null)
            stringBuilder.append(rb.getString("division") + "\n");

        if (stringBuilder.length() > 0) {// empty fields found
            Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
            emptyFieldAlert.setContentText(rb.getString("emptyError") + ":\n" + stringBuilder);
            emptyFieldAlert.setTitle(rb.getString("emptyErrorTitle"));
            emptyFieldAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            emptyFieldAlert.show();
            return null;
        }

        // test for bad Phone
        if (!phone.matches("^[0-9-()]*$")) { // not a number, '-' or parens
            Alert badPhoneAlert = new Alert(Alert.AlertType.ERROR);
            badPhoneAlert.setContentText(rb.getString("phoneError"));
            badPhoneAlert.setTitle(rb.getString("phoneErrorTitle"));
            badPhoneAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            badPhoneAlert.show();
            return null;
        }

        // test for bad Postal Code
        if (!postalCode.matches("^[a-zA-Z0-9]*$")) { //not alphanumeric
            Alert badPostalAlert = new Alert(Alert.AlertType.ERROR);
            badPostalAlert.setContentText(rb.getString("postalError"));
            badPostalAlert.setTitle(rb.getString("postalErrorTitle"));
            badPostalAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            badPostalAlert.show();
            return null;
        }

        Customer newCustomer = new Customer(name, address, postalCode, phone, division);

        try {
            newCustomer.setId(Integer.parseInt(customerIdTextField.getText()));
        } catch (NumberFormatException e) {
            // do nothing
        }

        return newCustomer;
    }

    /**
     * This method is called when the user selects a customer and then clicks the delete button. It will delete
     * the selected customer from the database after a confirmation window is confirmed.
     */
    public void deleteButtonClicked()
    {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) { //a customer is selected

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText(rb.getString("confirmationAlert1") + "\n" +
                    selectedCustomer.getName() + " " + rb.getString("confirmationAlert2"));
            confirmationAlert.setTitle(rb.getString("confirmationAlertTitle"));
            confirmationAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.get() == ButtonType.OK) {

                if(customerDAO.delete(selectedCustomer.getId())) {
                    customerTableView.setItems(customerDAO.getAll());

                    Alert deleteConfirmedAlert = new Alert((Alert.AlertType.INFORMATION));
                    deleteConfirmedAlert.setContentText(selectedCustomer.getName() + " " + rb.getString("deleteAlert"));
                    deleteConfirmedAlert.setTitle(rb.getString("deleteTitle"));
                    deleteConfirmedAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    deleteConfirmedAlert.show();
                }
            }
        }
        else {
            Alert notSelectedAlert = new Alert(Alert.AlertType.WARNING);
            notSelectedAlert.setContentText(rb.getString("notSelectedAlert"));
            notSelectedAlert.setTitle(rb.getString("notSelectedTitle"));
            notSelectedAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            notSelectedAlert.show();
        }
    }

    /**
     * This method is called when the exit button is clicked by the user. It will return the user to the Main Form.
     * @param event the ActionEvent from the click.
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
