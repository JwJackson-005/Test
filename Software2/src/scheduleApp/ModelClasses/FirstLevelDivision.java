package scheduleApp.ModelClasses;

/**
 * A class to model a first level division. A division is tied to a country, has a name, and a databae id number.
 * @author Jason Jackson
 */
public class FirstLevelDivision {

    private int id;
    private String name;
    private Country country;

    /**
     * Instantiates a new First level division.
     *
     * @param divisionId the division id
     * @param name       the name
     * @param country    the country
     */
    public FirstLevelDivision(int divisionId, String name, Country country) {
        this.id = divisionId;
        this.name = name;
        this.country = country;
    }

    @Override
    public String toString() {
        return this.name;
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
     * Sets division id.
     *
     * @param divisionId the division id
     */
    public void setDivisionId(int divisionId) {
        this.id = divisionId;
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
     * Gets country.
     *
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets country.
     *
     * @param country the country
     */
    public void setCountry(Country country) {
        this.country = country;
    }
}
