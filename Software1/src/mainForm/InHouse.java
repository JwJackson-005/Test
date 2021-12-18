package mainForm;

/**
 * This class extends the Part abstract class. It is meant to model a Part of a Product that
 * is made in-house by the business. As a result, it extends the Part class by adding a machineID field.
 * @author Jason Jackson
 */
public class InHouse extends Part{

    private int machineID;

    /**
     * Constructor for an InHouse Part. Requires all the same paramaters as a Part, with the addition of a machineID.
     * @param id InHouse's ID.
     * @param name InHouse's name.
     * @param price InHouse's price.
     * @param stock InHouse's stock.
     * @param min InHouse's min.
     * @param max InHouse's max.
     * @param machineID InHouse's machineID.
     */
    public InHouse (int id, String name, double price, int stock, int min, int max, int machineID)
    {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Returns machineID
     * @return the InHouse's machine ID.
     */
    public int getMachineID() {
        return this.machineID;
    }

    /**
     * Sets the machine ID
     * @param machineID the machine ID to set.
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
