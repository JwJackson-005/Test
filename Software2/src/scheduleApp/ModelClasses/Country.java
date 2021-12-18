package scheduleApp.ModelClasses;

/**
 * A class to model a country. It is a basic class that only has a database ID and the country's name
 * @author Jason Jackson
 */
public class Country {

    private int id;
    private String name;

    /**
     * Instantiates a new Country.
     *
     * @param countryId the country id
     * @param name      the name
     */
    public Country(int countryId, String name) {
        this.id = countryId;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
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
     * Sets country id.
     *
     * @param countryId the country id
     */
    public void setCountryId(int countryId) {
        this.id = countryId;
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
}
