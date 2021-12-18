package scheduleApp.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduleApp.ModelClasses.Contact;
import scheduleApp.ModelClasses.Country;
import scheduleApp.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The ContactDAO class is an implementation of the DAO Interface. It handles the CRUD operations on the database
 * for the contacts table.
 * @author Jason Jackson
 */

public class ContactDAO implements DAO<Contact>{

    private String sqlStatement;

    /**
     * This method returns all contacts stored in the contacts table of the database.
     * @return the contacts list
     */
    @Override
    public ObservableList<Contact> getAll() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        try {
            sqlStatement = "SELECT * FROM contacts";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");
                contactList.add(new Contact(contactId, contactName, contactEmail));
            }

            return contactList;

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
    public void create(Contact object) {
        // method not needed
    }

    /**
     * This method reads a single contact from the database, based off the contact ID.
     * @param id the id of the contact used to query the database.
     * @return the contact with the given contact ID
     */
    @Override
    public Contact read(int id) {
        try {
            sqlStatement = "SELECT * FROM contacts WHERE Contact_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {//match found
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");

                return new Contact(contactId, contactName, contactEmail);
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
    public void update(Contact object) {
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
