package mainForm;

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

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This is the controller class for the MainForm xml. This class will handle any functions while using the mainForm
 * and is the centerpiece of the inventory application.
 * @author Jason Jackson
 */

public class MainController implements Initializable {

    private int partAutoGenID, productAutoGenID;
    //FXML objects for the main form

    @FXML private TextField partSearch;
    @FXML private TextField productSearch;
    @FXML private TableView<Part>  partsView;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partCostColumn;

    @FXML private TableView<Product>  productsView;
    @FXML private TableColumn<Product, Integer>  productIdColumn;
    @FXML private TableColumn<Product, String>  productNameColumn;
    @FXML private TableColumn<Product, Integer>  productStockColumn;
    @FXML private TableColumn<Product, Double>  productCostColumn;

    /**
     * This method initializes appropriate values when the mainForm is presented to the user.
     *
     * FUTURE ENHANCEMENTS: There are a number of potential future enhancements to this project. In particular, if I were
     * to try and improve this product, I would look at the ability to automatically compile order lists for the inventory.
     * To do so, we could check how many products are needed in inventory, and compare their associated parts to the stock
     * of parts in inventory. If there is too few parts for the stock of Products, an order list would automatically be
     * created to balance out the number of parts.
     *
     * @param url supplied by the JavaFX library.
     * @param rb supplied by the JavaFX library.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        partAutoGenID = 1;
        productAutoGenID = 1;

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partSearch.textProperty().addListener((observable, oldValue, newValue) ->
                partsView.setItems(filterPartList(Inventory.getAllParts(), newValue))
        );

        productSearch.textProperty().addListener((observable, oldValue, newValue) ->
                productsView.setItems(filterProductList(Inventory.getAllProducts(), newValue))
        );
    }

    /**
     * This method exits the program when the user clicks the Exit button.
     */
    public void exitButtonPressed()
    {
        System.exit(1);
    }

    /**
     * This method transfers the user from the main form to the add part form.
     * @param event
     * @throws IOException
     */
    public void addPartButtonPressed(ActionEvent event) throws IOException
    {
        FXMLLoader partLoader = new FXMLLoader(getClass().getResource("addPartForm.fxml"));
        Parent addPartParent = partLoader.load();
        Scene addPartScene = new Scene(addPartParent);

        AddPartController partController = partLoader.getController();

        partController.addInitData(partAutoGenID);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(addPartScene);
        window.show();
    }

    /**
     * This method transfers the user from the main form to the add part form
     * with the purpose of modifying an existing part.
     * @param event
     * @throws IOException
     */
    public void modifyPartButtonPressed(ActionEvent event) throws IOException
    {
        ObservableList<Part> selectedRows = partsView.getSelectionModel().getSelectedItems();

        if(selectedRows.size() > 0) {
            FXMLLoader partLoader = new FXMLLoader(getClass().getResource("addPartForm.fxml"));
            Parent addPartParent = partLoader.load();
            Scene addPartScene = new Scene(addPartParent);

            AddPartController partController = partLoader.getController();

            Part oldPart = selectedRows.get(0);

            partController.modifyInitData(Inventory.getAllParts().indexOf(oldPart), this.partAutoGenID, oldPart.getId());
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(addPartScene);
            window.show();
        }
    }

    /**
     * This method is called when the delete button is pressed. It will call the deletePart function on any selected
     * part in the partsView TableView.
     */
    public void partDeleteButtonPressed()
    {
        ObservableList<Part> selectedRows = partsView.getSelectionModel().getSelectedItems();

        if(selectedRows.size() > 0)
            deletePart(selectedRows.get(0));
    }

    /**
     * This method transfers the user from the main form to the add product form.
     * @param event
     * @throws IOException
     */
    public void addProductButtonPressed(ActionEvent event) throws IOException
    {
        FXMLLoader productLoader = new FXMLLoader(getClass().getResource("addProductForm.fxml"));
        Parent productParent = productLoader.load();
        Scene productScene = new Scene(productParent);

        ProductController productController = productLoader.getController();

        productController.addInitData(productAutoGenID);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(productScene);
        window.show();
    }

    /**
     * This method transfers the user from the main form to the add product form
     * with the purpose of modifying an existing product.
     * @param event
     * @throws IOException
     */
    public void modifyProductButtonPressed(ActionEvent event) throws IOException
    {
        ObservableList<Product> selectedRows = productsView.getSelectionModel().getSelectedItems();

        if(selectedRows.size() > 0) {
            FXMLLoader productLoader = new FXMLLoader(getClass().getResource("addProductForm.fxml"));
            Parent productParent = productLoader.load();
            Scene productScene = new Scene(productParent);

            ProductController productController = productLoader.getController();

            Product oldProduct = selectedRows.get(0);

            productController.modifyInitData(Inventory.getAllProducts().indexOf(oldProduct), this.productAutoGenID, oldProduct.getId());
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(productScene);
            window.show();
        }
    }


    /**
     * This method is called when the delete button is pressed. It will call the deleteProduct function on any selected
     * part in the productsView TableView.
     */
    public void productDeleteButtonPressed()
    {
        ObservableList<Product> selectedRows = productsView.getSelectionModel().getSelectedItems();

        if(selectedRows.size() > 0)
            deleteProduct(selectedRows.get(0));
    }

    /**
     * This method adds a part to the Inventory system
     * and then calls the refresh method to update the partsView TableView
     * @param autoGenID the current autoGenID for the mainController.
     * @param newPart the new Part class to be added to the Inventory.
     */
    public void addPart(int autoGenID, Part newPart)
    {
        Inventory.addPart(newPart);
        this.partAutoGenID = autoGenID;
        this.partAutoGenID++;
        refresh();
    }

    /**
     * This method modifies an existing part in the Inventory system
     * and then calls the refresh method to update the partsView TableView
     * @param index the index of the part to be modified in the Inventory parts list.
     * @param newPart the new value that the Part will be changed to.
     * @param autoGenID the current autoGenID for the mainController.
     */
    public void modifyPart(int index, Part newPart, int autoGenID)
    {
        this.partAutoGenID = autoGenID;
        Inventory.updatePart(index, newPart);
        refresh();
    }

    /**
     * This method prompts the user to confirm the deletion, deletes an existing part in the Inventory system,
     * and then calls the refresh method to update the partsView TableView.
     * @param selectedPart the Part that will be deleted.
     */
    public void deletePart(Part selectedPart)
    {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Are you sure you wish to delete this part?");

        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
            refresh();
        } // else do nothing
    }

    /**
     * This method adds a product to the Inventory system
     * and then calls the refresh method to update the productsView TableView
     * @param autoGenID the current autoGenID for the mainController.
     * @param newProduct the new Product class to be added to the Inventory.
     */
    public void addProduct(int autoGenID, Product newProduct)
    {
        Inventory.addProduct(newProduct);
        this.productAutoGenID = autoGenID;
        this.productAutoGenID++;
        refresh();
    }

    /**
     * This method modifies an existing Product in the Inventory system
     * and then calls the refresh method to update the productsView TableView

     * RUNTIME/LOGICAL ERROR: One logical error that I was running into was my autoGenID resetting any time
     * I modified a part or product. If I were to add a part or product afterwards, it was giving me part/product IDs
     * that were already used. What I realized is that I was attempting to send just the autoGenID to the Part/Product
     * Controllers, and the returned value was the ID of the modified part. To get around this, I had to change the
     * methods that passed around autoID to also include a parameter with the modified part's ID. That way the autoGenID
     * wasn't getting updated when control was passed back to the MainController.

     * @param index the index of the product to be modified in the Inventory parts list.
     * @param newProduct the new value that the Product will be changed to.
     * @param autoGenID the current autoGenID for the mainController.
     */
    public void modifyProduct(int index, Product newProduct, int autoGenID)
    {
        this.productAutoGenID = autoGenID;
        Inventory.updateProduct(index, newProduct);
        refresh();
    }

    /**
     * This method prompts the user to confirm the deletion, deletes an existing product in the Inventory system,
     * and then calls the refresh method to update the productsView TableView.
     * @param selectedProduct the Product that will be deleted.
     */
    public void deleteProduct(Product selectedProduct)
    {
        if (selectedProduct.getAssociatedParts().size() > 0) // product has parts associated with it
        {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Cannot delete a product with associated parts. \nRemove associated parts before deletion");
            a.show();
        }
        else
        {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("Are you sure you wish to delete this product?");

            Optional<ButtonType> result = a.showAndWait();
            if (result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
                refresh();
            } // else do nothing
        }
    }

    /**
     * This method is used by the part controller to pass along the autoGenID value and call the refresh method.
     * @param autoGenID the current autoGenID value.
     */
    public void partRefreshTableView(int autoGenID)
    {
        this.partAutoGenID = autoGenID;
        refresh();
    }

    /**
     * This method is used by the product controller to pass along the autoGenID value and call the refresh method.
     * @param autoGenID the current autoGenID value.
     */
    public void productRefreshTableView(int autoGenID)
    {
        this.productAutoGenID = autoGenID;
        refresh();
    }

    /**
     * This method updates and repopulates the partsView and productsView TableViews
     */
    private void refresh()
    {
        partsView.setItems(Inventory.getAllParts());
        productsView.setItems(Inventory.getAllProducts());

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

    /**
     * This method determines whether the supplied Product's id or name contains the String in the searchBox.
     * @param product the Product to check against the searchText string.
     * @param searchText the String to check against the Product's id and name.
     * @return boolean whether a match was found or not.
     */
    private boolean searchFindsProduct(Product product, String searchText)
    {
        try {
            return (product.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    product.getId() == Integer.parseInt(searchText));
        }
        catch (NumberFormatException n)
        {
            return false;
        }
    }

    /**
     * This method calls the searchFindsProduct method for each Product in a Product list
     * and then returns a list of Products containing the stringText.
     * @param list the Product list to filter based off the searchText String.
     * @param searchText the String supplied by the searchBox TextField
     * @return a Product list of Products that contained the searchText String.
     */
    private ObservableList<Product> filterProductList(List<Product> list, String searchText){
        List<Product> filteredList = new ArrayList<>();
        for (Product product : list){
            if(searchFindsProduct(product, searchText)) filteredList.add(product);
        }
        return FXCollections.observableList(filteredList);
    }
}
