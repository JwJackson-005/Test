/**
 * Supplied class Part.java
 */
package mainForm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is made to model a product of the business. It consists of fields that describe the product
 * as well as a field that contains parts associated with the product.
 * @author Jason Jackson
 */
public class Product {

    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructor for a Product.
     * @param id Product's ID.
     * @param name Product's name.
     * @param price Product's price.
     * @param stock Product's stock.
     * @param min Product's min.
     * @param max Product's max.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = FXCollections.observableArrayList();
    }

    /**
     * Getter function for id.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter function for ID.
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter function for name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter function for name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter function for price.
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter function for price.
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Getter function for Stock.
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Setter function for stock.
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Getter function for min.
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Setter function for min.
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Getter function for max.
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Setter function for max.
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * This function adds a Part to the Product's associatedParts list.
     * @param part the part to add
     */
    public void addAssociatedPart(Part part)
    {
        this.associatedParts.add(part);
    }

    /**
     * This function deletes an existing part from the Product's associatedParts list.
     * @param selectedAssociatedPart the Part to delete
     * @return whether delete was successful
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart)
    {
        return this.associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return the associated parts list
     */
    public ObservableList<Part> getAssociatedParts() {return this.associatedParts;}

    /**
     * Setter function for the associatedParts list
     * @param parts the new list to set associatedParts to.
     */
    public void setAssociatedParts(ObservableList<Part> parts) {this.associatedParts = parts;}


}