package scheduleApp.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduleApp.ModelClasses.Country;
import scheduleApp.ModelClasses.FirstLevelDivision;
import scheduleApp.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The FirstLevelDivisionDAO class is an implementation of the DAO Interface. It handles the CRUD operations on the database
 * for the first level divisions table.
 * @author Jason Jackson
 */
public class FirstLevelDivisionDAO implements DAO<FirstLevelDivision> {

    private String sqlStatement;

    /**
     * This method is not currently implemented in the program because it is not used. But
     * it is a requirement for the DAO interface.
     * @return not used
     */
    @Override
    public ObservableList<FirstLevelDivision> getAll() {
        return null; // not needed
    }

    /**
     * This method is not currently implemented in the program because it is not used. But
     * it is a requirement for the DAO interface.
     * @param object not used
     */
    @Override
    public void create(FirstLevelDivision object) {

        //not needed

    }

    /**
     * This method reads a single first level division from the database, based off the first level division ID.
     * @param id the id of the first level division used to query the database.
     * @return the first level division with the given first level division ID
     */
    @Override
    public FirstLevelDivision read(int id) {
        try {
            sqlStatement = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {//match found
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("COUNTRY_ID");

                CountryDAO countryDAO = new CountryDAO();
                Country divisionCountry = countryDAO.read(countryId);

                return new FirstLevelDivision(divisionId, divisionName, divisionCountry);
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
    public void update(FirstLevelDivision object) {

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

    /**
     * This method is used to get all the first level divisions for a given country.
     * @param country the country to query the database
     * @return a list of all first level divisions for the country
     */
    public ObservableList<FirstLevelDivision> getAllByCountry(Country country) {
        try {
            sqlStatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = ?";
            ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, country.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                divisionList.add(new FirstLevelDivision(divisionId, divisionName, country));
            }

            return divisionList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
