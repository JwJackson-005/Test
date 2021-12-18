package scheduleApp.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduleApp.ModelClasses.Customer;
import scheduleApp.utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The CustomerDAO class is an implementation of the DAO Interface. It handles the CRUD operations on the database
 * for the customers table.
 * @author Jason Jackson
 */
public class CustomerDAO implements DAO<Customer>{

    private String sqlStatement;

    public ObservableList<Customer> getAll() {

        /**
         * This method returns all customers stored in the customers table of the database.
         * @return the customers list
         */
        ObservableList<Customer> customersList = FXCollections.observableArrayList();

        try {
            sqlStatement = "SELECT * FROM customers";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Customer matchedCustomer = getNextCustomer(rs);
                customersList.add(matchedCustomer);
            }

            return customersList;

        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * This method inserts a new customer into the database.
     * @param object the new customer to be added to the database.
     */
    @Override
    public void create(Customer object) {

        try {
            sqlStatement = "Insert into customers(Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID)"
                    + "Values(?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setString(1, object.getName());
            ps.setString(2, object.getAddress());
            ps.setString(3, object.getPostalCode());
            ps.setString(4, object.getPhoneNumber());
            if (UserDAO.getLoggedInUser() != null) {
                ps.setString(5, UserDAO.getLoggedInUser().getUsername());
                ps.setString(6, UserDAO.getLoggedInUser().getUsername());
            }
            else {
                ps.setString(5, null);
                ps.setString(6, null);
            }
            ps.setInt(7, object.getDivision().getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads a single customer from the database, based off the customer_ID.
     * @param id the id of the customer used to query the database.
     * @return the customer with the given customer_ID
     */
    @Override
    public Customer read(int id) {

        try {
            sqlStatement = "SELECT * FROM customers WHERE Customer_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) //match found
                return getNextCustomer(rs);
            else
                return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method updates the customer given in the database, using the given customer object with an existing ID.
     * @param object the updated object with an existing customer_ID.
     */
    @Override
    public void update(Customer object) {

        try {
            sqlStatement = "UPDATE customers SET " +
                    "Customer_Name = ?, " +
                    "Address = ?, " +
                    "Postal_Code = ?, " +
                    "Phone = ?, " +
                    "Division_Id = ? " +
                    "WHERE Customer_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setString(1, object.getName());
            ps.setString(2, object.getAddress());
            ps.setString(3, object.getPostalCode());
            ps.setString(4, object.getPhoneNumber());
            ps.setInt(5, object.getDivision().getId());
            ps.setInt(6, object.getId());

            System.out.println(ps.executeUpdate() + " Rows Affected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will delete a specific customer from the database using the given customer_ID.
     * @param key the customer_ID of the customer to be deleted.
     * @return whether the customer was found and deleted from the database.
     */
    @Override
    public boolean delete(int key) {

        try {
            sqlStatement = "DELETE FROM customers WHERE Customer_ID = ?";
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            appointmentDAO.deleteCustomerAppointments(key);

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, key);

            int numRows = ps.executeUpdate();
            return (numRows > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to help with code re-use. This method will take a ResultSet from a previously executed Query and
     * extract an customer from that resultSet.
     * @param rs the resultSet from the query
     * @return a single customer from the ResultSet
     */
    private Customer getNextCustomer (ResultSet rs) {
        try {
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerZip = rs.getString("Postal_Code");
            String customerPhone = rs.getString("Phone");
            int divisionId = rs.getInt("Division_ID");

            FirstLevelDivisionDAO divisionDAO = new FirstLevelDivisionDAO();
            Customer matchedCustomer = new Customer(customerName, customerAddress, customerZip, customerPhone, divisionDAO.read(divisionId));
            matchedCustomer.setId(customerId);

            return matchedCustomer;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
