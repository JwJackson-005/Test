/**
* Supplied class Part.java 
 */
package mainForm;

/**
 * This is an abstract class that represents a part for a business. Further classes Inhouse and Outsourced will
 * inherit from the attributes and methods of this class in future use.
 * @author Jason Jackson
 */
public abstract class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructor for a part. Will only be called by inherited classes.
     * @param id the Part's id.
     * @param name the Part's name.
     * @param price the Part's price.
     * @param stock the Part's stock.
     * @param min the Part's min.
     * @param max the Part's max.
     */
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Getter function for ID.
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
     * Getter function for stock.
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
    
}