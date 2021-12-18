package mainForm;

/**
 * This class extends the Part abstract class. It is meant to model a Part of a Product that
 * is outsourced by the business. As a result, it extends the Part class by adding a companyName field which
 * includes the name of the company being contracted.
 * @author Jason Jackson
 */
public class Outsourced extends Part{

    private String companyName;

    /**
     * Constructor for an Outsourced.
     * @param id Outsourced's id.
     * @param name Outsourced's name.
     * @param price Outsourced's price.
     * @param stock Outsourced's stock.
     * @param min Outsourced's min.
     * @param max Outsourced's max.
     * @param companyName Outsourced's companyName.
     */
    public Outsourced (int id, String name, double price, int stock, int min, int max, String companyName)
    {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Getter function for the companyName
     * @return the companyName String.
     */
    public String getCompanyName() {
        return this.companyName;
    }

    /**
     * Setter method for setting a new company name
     * @param companyName a String to update the companyName field.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
