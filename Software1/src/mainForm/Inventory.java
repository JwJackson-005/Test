package mainForm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Inventory class is a static class which holds the program's list of parts and products.
 * @author Jason Jackson
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * This method adds a part to the Inventory's allParts list.
     * @param newPart the part to be added to the Inventory.
     */
    public static void addPart (Part newPart)
    {
        allParts.add(newPart);
    }

    /**
     * This method checks the Inventory's parts list to see if a match is found based off the partID field.
     * @param partId the ID to be checked against the Inventory's part list.
     * @return the Part if it is found, otherwise null.
     */
    public static Part lookupPart (int partId)
    {
        for (Part part : allParts) {
            if (part.getId() == partId)
                return part;
        }
        return null;
    }

    /**
     * This method checks the Inventory to see if a part by the name of the partName parameter exists, then returns
     * any parts matching the partName as a list.
     * @param partName the String to be checked against the parts list.
     * @return a list containing any Parts that match the partName.
     */
    public static ObservableList<Part> lookupPart (String partName)
    {
        ObservableList<Part> tempList = null;

        for (Part part : allParts) {
            if (part.getName().compareTo(partName) == 0) {
                tempList.add(part);
            }
        }
        return tempList;
    }

    /**
     * This method updates the Inventory's parts list to the new selectedPart value at the part index given.
     * @param index the index of the updated part.
     * @param selectedPart the new value of the Part.
     */
    public static void updatePart (int index, Part selectedPart)
    {
        allParts.set(index, selectedPart);
    }

    /**
     * This method deletes the part given as a parameter from the Inventory's parts list.
     * @param selectedPart the Part to be deleted from the inventory.
     * @return a boolean whether the deletion was successful or not.
     */
    public static boolean deletePart (Part selectedPart)
    {
        return allParts.remove(selectedPart);
    }

    /**
     * Getter method to return allParts.
     * @return the allParts list.
     */
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    /**
     * This method adds a product to the Inventory's product list.
     * @param newProduct the product to be added to the Inventory's product list.
     */
    public static void addProduct (Product newProduct)
    {
        allProducts.add(newProduct);
    }

    /**
     * This method checks the Inventory's product list to see if a match is found based off the productID field.
     * @param productId the ID to be checked against the Inventory's product list.
     * @return the product if it is found, otherwise null.
     */
    public static Product lookupProduct (int productId)
    {
        for (Product product : allProducts) {
            if (product.getId() == productId)
                return product;
        }
        return null;
    }

    /**
     * This method checks the Inventory to see if a product by the name of the productName parameter exists, then returns
     * any products matching the productName as a list.
     * @param productName the String to be checked against the products list.
     * @return a list containing any Products that match the productName.
     */
    public static ObservableList<Product> lookupProduct (String productName)
    {
        ObservableList<Product> tempList = null;

        for (Product product : allProducts) {
            if (product.getName().compareTo(productName) == 0) {
                tempList.add(product);
            }
        }
        return tempList;
    }

    /**
     * This method updates the Inventory's product list to the new selectedProduct value at the product index given.
     * @param index the index of the updated product.
     * @param selectedProduct the new value of the Product.
     */
    public static void updateProduct (int index, Product selectedProduct)
    {
        allProducts.set(index, selectedProduct);
    }

    /**
     * This method deletes the product given as a parameter from the Inventory's products list.
     * @param selectedProduct the product to be deleted from the inventory.
     * @return a boolean whether the deletion was successful or not.
     */
    public static boolean deleteProduct (Product selectedProduct)
    {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Getter method to return allProducts.
     * @return the allProducts list.
     */
    public static ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }
}
