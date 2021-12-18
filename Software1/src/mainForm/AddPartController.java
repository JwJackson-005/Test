package mainForm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The AddPartController is a controller class which interfaces with the view of the
 * addPartForm. The class controls and validates user input and passes control gracefully back
 * to the mainForm when complete. The AddPartController is used to both add parts to the inventory
 * as well as modify parts already existing in the inventory. The form will change accordingly based off
 * whether it was called by adding or modifying a part.
 * @author Jason Jackson
 */

public class AddPartController implements Initializable {

    private int autoGenID, modifyIndex, modifyPartID;

    // These are FXML objects for the add Part Form
    @FXML private Label partOrProductLabel;
    @FXML private Label addOrModifyLabel;

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField invField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TextField machineOrCompanyField;

    @FXML private RadioButton inHouseRadio;
    @FXML private RadioButton outsourcedRadio;
    private ToggleGroup inHouseToggle;

    /**
     * This method initializes appropriate values when the Part form is presented to the user.
     * @param url supplied by the JavaFX library.
     * @param rb supplied by the JavaFX library.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        modifyPartID = 0;
        inHouseToggle = new ToggleGroup();
        this.inHouseRadio.setToggleGroup(inHouseToggle);
        this.outsourcedRadio.setToggleGroup(inHouseToggle);
        inHouseToggle.selectToggle(inHouseRadio);

    }

    /**
     * This method is called whenever a radio button is changed and then updates
     * the appropriate toggleGroup value.
     */
    public void inHouseChanged()
    {
        if (this.inHouseToggle.getSelectedToggle().equals(this.inHouseRadio))
            this.partOrProductLabel.setText("Machine ID");

        else if (this.inHouseToggle.getSelectedToggle().equals(this.outsourcedRadio))
            this.partOrProductLabel.setText("Company Name");
    }

    /**
     * This method will switch the form from the addPart form back to the main form.
     * @param event
     * @throws IOException
     */
    public void cancelButtonClicked(ActionEvent event) throws IOException
    {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("mainForm.fxml"));
        Parent mainFormParent = mainLoader.load();
        Scene mainFormScene = new Scene(mainFormParent);

        MainController controller = mainLoader.getController();
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(mainFormScene);
        window.show();
        controller.partRefreshTableView(this.autoGenID);
    }

    /**
     * This method is called when the save button is clicked. It will validate the form,
     * create a new Part, and then return the user back to the main Form.
     * @param event
     * @throws IOException
     */
    public void saveButtonClicked(ActionEvent event) throws IOException
    {
        try {
            if (validPart()) {
                Part newPart;
                int id;
                if (this.addOrModifyLabel.getText().compareTo("Add Part") == 0) // adding part
                {
                    id = this.autoGenID;
                    if (this.inHouseToggle.getSelectedToggle().equals(this.inHouseRadio)) {
                        newPart = new InHouse(id, this.nameField.getText(), Double.parseDouble(this.priceField.getText()),
                                Integer.parseInt(this.invField.getText()), Integer.parseInt(this.minField.getText()),
                                Integer.parseInt(this.maxField.getText()), Integer.parseInt(this.machineOrCompanyField.getText()));
                    } else // outSourced Toggle Selected
                    {
                        newPart = new Outsourced(id, this.nameField.getText(), Double.parseDouble(this.priceField.getText()),
                                Integer.parseInt(this.invField.getText()), Integer.parseInt(this.minField.getText()),
                                Integer.parseInt(this.maxField.getText()), this.machineOrCompanyField.getText());
                    }
                } else // modifying part
                {
                    id = this.modifyPartID;
                    if (this.inHouseToggle.getSelectedToggle().equals(this.inHouseRadio)) {
                        newPart = new InHouse(id, this.nameField.getText(), Double.parseDouble(this.priceField.getText()),
                                Integer.parseInt(this.invField.getText()), Integer.parseInt(this.minField.getText()),
                                Integer.parseInt(this.maxField.getText()), Integer.parseInt(this.machineOrCompanyField.getText()));
                    } else // outSourced Toggle Selected
                    {
                        newPart = new Outsourced(id, this.nameField.getText(), Double.parseDouble(this.priceField.getText()),
                                Integer.parseInt(this.invField.getText()), Integer.parseInt(this.minField.getText()),
                                Integer.parseInt(this.maxField.getText()), this.machineOrCompanyField.getText());
                    }
                }


                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("mainForm.fxml"));
                Parent mainFormParent = mainLoader.load();
                Scene mainFormScene = new Scene(mainFormParent);

                MainController controller = mainLoader.getController();

                if (addOrModifyLabel.getText().compareTo("Add Part") == 0)
                    controller.addPart(autoGenID, newPart);
                else {
                    controller.modifyPart(modifyIndex, newPart, autoGenID);
                }


                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(mainFormScene);
                window.show();
            }
        } catch (NumberFormatException n) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Part entered.");
            a.show();
        }
    }

    /**
     * This method initializes the data for the controller when adding a Part.
     * @param autoGenID the Inventory's Part autogenID
     */
    public void addInitData(int autoGenID)
    {
        this.autoGenID = autoGenID;
        this.addOrModifyLabel.setText("Add Part");
    }

    /**
     * This method will check the form's min, stock, and max fields to determine
     * if they are valid values. Additionally, it will validate all fields for proper input.
     * It will only return true if min, stock, and max are logically consistent.
     * @return boolean to return the result.
     */
    private boolean validPart()
    {
        int min, max, inv, machine;
        double price;

        try {
            inv = Integer.parseInt(this.invField.getText());
        } catch (NumberFormatException n) {
            Alert a =  new Alert(Alert.AlertType.ERROR);
            a.setContentText("Inv field can only have integers as input");
            a.show();
            return false;
        }
        try {
            price = Double.parseDouble(this.priceField.getText());
        } catch (NumberFormatException n) {
            Alert a =  new Alert(Alert.AlertType.ERROR);
            a.setContentText("Price field can only have decimals as input");
            a.show();
            return false;
        }
        try {
            max = Integer.parseInt(this.maxField.getText());
        } catch (NumberFormatException n) {
            Alert a =  new Alert(Alert.AlertType.ERROR);
            a.setContentText("Max field can only have integers as input");
            a.show();
            return false;
        }
        try {
            min = Integer.parseInt(this.minField.getText());
        } catch (NumberFormatException n) {
            Alert a =  new Alert(Alert.AlertType.ERROR);
            a.setContentText("Min field can only have integers as input");
            a.show();
            return false;
        }
        if (this.inHouseToggle.getSelectedToggle().equals(this.inHouseRadio)) {
            try {
                machine = Integer.parseInt(this.machineOrCompanyField.getText());
            } catch (NumberFormatException n) {
                Alert a =  new Alert(Alert.AlertType.ERROR);
                a.setContentText("Machine ID field can only have integers as input");
                a.show();
                return false;
            }
        }
        // this only returns true when min <= inv <= max
        if( ((min >=0) && (inv >= min) && (inv <= max)))
            return true;
        else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Part. Must be Min <= Inv <=  Max");
            a.show();
            return false;
        }
    }

    /**
     * This method initializes the data for the controller when modifying a Part.
     * @param index the Inventory index value for the modified part.
     * @param autoGenID the Inventory Part autogenID.
     * @param partID the partID for the modified Part.
     */
    public void modifyInitData(int index, int autoGenID, int partID) {
        this.autoGenID = autoGenID;
        this.modifyPartID = partID;
        this.modifyIndex = index;
        this.addOrModifyLabel.setText("Modify Part");
        populateFields(partID);
    }

    /**
     * This method fills in the appropriate TextFields for the modify form.
     * @param partID the partID for the modified Part.
     */
    public void populateFields(int partID)
    {
        Part selectedPart = Inventory.lookupPart(partID);

        this.idField.setText(Integer.toString(selectedPart.getId()));
        this.nameField.setText(selectedPart.getName());
        this.invField.setText(Integer.toString(selectedPart.getStock()));
        this.minField.setText(Integer.toString(selectedPart.getMin()));
        this.maxField.setText(Integer.toString(selectedPart.getMax()));
        this.priceField.setText(Double.toString(selectedPart.getPrice()));

        if (selectedPart instanceof InHouse)
        {
            this.machineOrCompanyField.setText(Integer.toString(((InHouse) selectedPart).getMachineID()));
        }
        else if (selectedPart instanceof Outsourced)
        {
            this.inHouseToggle.selectToggle(this.outsourcedRadio);
            this.machineOrCompanyField.setText(((Outsourced) selectedPart).getCompanyName());

        }
        else
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Part Returned");
            a.show();
        }

    }
}
