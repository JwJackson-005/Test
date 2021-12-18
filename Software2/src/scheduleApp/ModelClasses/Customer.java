package scheduleApp.ModelClasses;

/**
 * A class to model a customer in the appointments app. The Customer has informaiton such as the database ID,
 * a name, address, postalCode, phone number, and country division.
 * @author Jason Jackson
 */
public class Customer {

    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private FirstLevelDivision division;
    private String divisionName;

    /**
     * Instantiates a new Customer.
     *
     * @param name        the name
     * @param address     the address
     * @param postalCode  the postal code
     * @param phoneNumber the phone number
     * @param division    the division
     */
    public Customer(String name, String address, String postalCode, String phoneNumber, FirstLevelDivision division) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.division = division;
        this.divisionName = division.getName();
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets postal code.
     *
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets postal code.
     *
     * @param postalCode the postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets division.
     *
     * @return the division
     */
    public FirstLevelDivision getDivision() {
        return division;
    }

    /**
     * Sets division.
     *
     * @param division the division
     */
    public void setDivision(FirstLevelDivision division) {
        this.division = division;
    }

    /**
     * Gets division name.
     *
     * @return the division name
     */
    public String getDivisionName() { return divisionName; }

}
