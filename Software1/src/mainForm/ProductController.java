package mainForm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The ProductController is a controller class which interfaces with the view of the
 * addProductForm. The class controls and validates user input and passes control gracefully back
 * to the mainForm when complete. The ProductController is used to both add products to the inventory
 * as well as modify products already existing in the inventory. The form will change accordingly based off
 * whether it was called by adding or modifying a product.
 * @author Jason Jackson
 */
public class ProductController implements Initializable {

    private int autoGenID, modifyIndex, modifyProductID;

    // These are FXML objects for the add Part Form
    @FXML private Label addOrModifyLabel;

    @FXML private TextField partSearch;

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField invField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;

    @FXML private TableView<Part>  partsView;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partCostColumn;

    @FXML private TableView<Part>  associatedPartsView;
    @FXML private TableColumn<Part, Integer>  associatedPartsIdColumn;
    @FXML private TableColumn<Part, String>  associatedPartsNameColumn;
    @FXML private TableColumn<Part, Integer>  associatedPartsStockColumn;
    @FXML private TableColumn<Part, Double>  associatedPartsCostColumn;


    /**
     * This method initializes appropriate values when the Product form is presented to the user.
     * @param url supplied by the JavaFX library.
     * @param rb supplied by the JavaFX library.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        modifyProductID = 0;

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        this.partsView.setItems(Inventory.getAllParts());

        partSearch.textProperty().addListener((observable, oldValue, newValue) ->
                partsView.setItems(filterPartList(Inventory.getAllParts(), newValue))
        );

    }

    /**
     * This method will switch the form from the Product form back to the main form.
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
        controller.productRefreshTableView(this.autoGenID);
    }

    /**
     * This method is called when the save button is clicked. It will validate the form,
     * create a new Product, and then return the user back to the main Form.
     * @param event
     * @throws IOException
     */

    public void saveButtonClicked(ActionEvent event) throws IOException
    {
        try {
            if (validProduct()) {
                Product newProduct;

                if (addOrModifyLabel.getText().compareTo("Add Product") == 0) {
                    newProduct = new Product(this.autoGenID, this.nameField.getText(), Double.parseDouble(this.priceField.getText()),
                            Integer.parseInt(this.invField.getText()), Integer.parseInt(this.minField.getText()),
                            Integer.parseInt(this.maxField.getText()));
                } else // Modifying Product
                {
                    newProduct = Inventory.lookupProduct(modifyProductID);
                    newProduct.setName(this.nameField.getText());
                    newProduct.setPrice(Double.parseDouble(this.priceField.getText()));
                    newProduct.setMin(Integer.parseInt(this.minField.getText()));
                    newProduct.setMax(Integer.parseInt(this.maxField.getText()));
                    newProduct.setStock(Integer.parseInt(this.invField.getText()));
                }

                for (Part part : associatedPartsView.getItems()) {
                    if (!newProduct.getAssociatedParts().contains(part))
                        newProduct.addAssociatedPart(part);
                }

                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("mainForm.fxml"));
                Parent mainFormParent = mainLoader.load();
                Scene mainFormScene = new Scene(mainFormParent);

                MainController controller = mainLoader.getController();

                if (addOrModifyLabel.getText().compareTo("Add Product") == 0)
                    controller.addProduct(autoGenID, newProduct);
                else {
                    controller.modifyProduct(modifyIndex, newProduct, autoGenID);
                }


                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(mainFormScene);
                window.show();
            }
        } catch (NumberFormatException n)
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Product Entered.");
            a.show();
        }
    }

    /**
     * This method will add a part from the partsView tableView to the
     * associatedParts tableView.
     */
    public void addButtonClicked()
    {
        ObservableList<Part> selectedRows = partsView.getSelectionModel().getSelectedItems();

        if (selectedRows.size() == 0) //nothing selected
        {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Must have a part selected to add");
            a.show();
        }
        else
            associatedPartsView.getItems().add(selectedRows.get(0));

    }

    /**
     * This method will remove a part from the associatedParts tableView.
     */
    public void removeButtonClicked()
    {
        ObservableList<Part> selectedRows = associatedPartsView.getSelectionModel().getSelectedItems();

        if (selectedRows.size() == 0) //nothing selected
        {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Must have a part selected to remove");
            a.show();
        }
        else
        {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("Are you sure you wish to disassociate this part?");

            Optional<ButtonType> result = a.showAndWait();
            if (result.get() == ButtonType.OK) {
                associatedPartsView.getItems().remove((selectedRows.get(0)));
            } // else do nothing
        }
    }

    /**
     * This method initializes the data for the controller when adding a Product.
     * @param autoGenID the Inventory's Product autogenID
     */
    public void addInitData(int autoGenID)
    {
        this.autoGenID = autoGenID;
        this.addOrModifyLabel.setText("Add Product");
    }

    /**
     * This method will check the form's min, stock, and max fields to determine
     * if they are valid values. It will only return true min, stock, and max are logically consistent.
     * @return boolean to return the result.
     */
    private boolean validProduct()
    {
        int min, max, inv;
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

        // this only returns true when min <= inv <= max
        if( ((min >=0) && (inv >= min) && (inv <= max)))
            return true;
        else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Product. Must be Min <= Inv <=  Max");
            a.show();
            return false;
        }
    }

    /**
     * This method initializes the data for the controller when modifying a Product.
     * @param index the Inventory index value for the modified product.
     * @param autoGenID the Inventory Product autogenID.
     * @param productID the productID for the modified Product.
     */
    public void modifyInitData(int index, int autoGenID, int productID) {
        this.autoGenID = autoGenID;
        this.modifyProductID = productID;
        this.modifyIndex = index;
        this.addOrModifyLabel.setText("Modify Product");
        populateFields(productID);
    }

    /**
     * This method fills in the appropriate TextFields for the modify form.
     * @param productID the partID for the modified Part.
     */
    public void populateFields(int productID)
    {
        Product selectedProduct = Inventory.lookupProduct(productID);

        this.idField.setText(Integer.toString(selectedProduct.getId()));
        this.nameField.setText(selectedProduct.getName());
        this.invField.setText(Integer.toString(selectedProduct.getStock()));
        this.minField.setText(Integer.toString(selectedProduct.getMin()));
        this.maxField.setText(Integer.toString(selectedProduct.getMax()));
        this.priceField.setText(Double.toString(selectedProduct.getPrice()));

        this.associatedPartsView.setItems(selectedProduct.getAssociatedParts());
    }

    /**
     * This method determines whether the supplied Part's id or name contains the String in the searchBox.
     * @param part the Part to check against the searchText string.
     * @param searchText the String to check against the Part's id and name.
     * @return boolean whether a match was found or not.
     */
    private boolean searchFindsPart(Part part, String searchText)
    {
        try {
            return (part.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    part.getId() == Integer.parseInt(searchText));
        }
        catch (NumberFormatException n)
        {
            return false;
        }
    }

    /**
     * This method calls the searchFindsPart method for each Part in a Part list
     * and then returns a list of Parts containing the stringText.
     * @param list the Part list to filter based off the searchText String.
     * @param searchText the String supplied by the searchBox TextField
     * @return a Part list of Parts that contained the searchText String.
     */
    private ObservableList<Part> filterPartList(List<Part> list, String searchText){
        List<Part> filteredList = new ArrayList<>();
        for (Part part : list){
            if(searchFindsPart(part, searchText)) filteredList.add(part);
        }
        return FXCollections.observableList(filteredList);
    }
}
