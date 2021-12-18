package scheduleApp.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduleApp.ModelClasses.Country;
import scheduleApp.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The CountryDAO class is an implementation of the DAO Interface. It handles the CRUD operations on the database
 * for the countries table.
 * @author Jason Jackson
 */
public class CountryDAO implements DAO<Country> {

    private String sqlStatement;

    /**
     * This method returns all countries stored in the countries table of the database.
     * @return the countries list
     */
    @Override
    public ObservableList<Country> getAll() {

        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try {
            sqlStatement = "SELECT * FROM countries";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");

                countryList.add(new Country(countryId, countryName));
            }

            return countryList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method is not currently implemented in the program because it is not used. But
     * it is a requirement for the DAO interface.
     * @param object not used
     */
    @Override
    public void create(Country object) {
        //not needed
    }

    /**
     * This method reads a single country from the database, based off the country ID.
     * @param id the id of the country used to query the database.
     * @return the Country with the given country ID
     */
    @Override
    public Country read(int id) {
        try {
            sqlStatement = "SELECT * FROM countries WHERE Country_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {//match found
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");

                return new Country(countryId, countryName);
            }
            else
                return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method is not currently implemented in the program because it is not used. But
     * it is a requirement for the DAO interface.
     * @param object not used
     */
    @Override
    public void update(Country object) {
        // not needed
    }

    /**
     * This method is not currently implemented in the program because it is not used. But
     * it is a requirement for the DAO interface.
     * @return not used
     */
    @Override
    public boolean delete(int key) {

        return false;
    }
}
