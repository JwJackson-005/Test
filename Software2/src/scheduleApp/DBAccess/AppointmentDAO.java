package scheduleApp.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduleApp.ModelClasses.Appointment;
import scheduleApp.utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * The AppointmentDAO class is an implementation of the DAO Interface. It handles the CRUD operations on the database
 * for the appointments table.
 * @author Jason Jackson
 */
public class AppointmentDAO implements DAO<Appointment> {

    private String sqlStatement;

    /**
     * This method returns all appointments stored in the appointments table of the database.
     * @return the appointments list
     */
    @Override
    public ObservableList<Appointment> getAll() {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        sqlStatement = "select Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID," +
                "contacts.Contact_ID, Contact_Name from appointments " +
                "inner join contacts on appointments.Contact_ID = contacts.Contact_ID;";

        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment matchedAppointment = getNextAppointment(rs);
                appointmentsList.add(matchedAppointment);
            }

            return appointmentsList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method inserts a new appointment into the database.
     * @param object the new appointment to be added to the database.
     */
    @Override
    public void create(Appointment object) {

        try {
            sqlStatement = "Insert into appointments(Appointment_ID, Title, Description, Location, Type, Start, End," +
                    "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                    "Values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, object.getId());
            ps.setString(2, object.getTitle());
            ps.setString(3, object.getDescription());
            ps.setString(4, object.getLocation());
            ps.setString(5, object.getType());
            ps.setTimestamp(6, Timestamp.valueOf(object.getStartTime()));
            ps.setTimestamp(7, Timestamp.valueOf(object.getEndTime()));
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(12, object.getCustomerId());
            ps.setInt(13, object.getUserId());
            ps.setInt(14, object.getContactId());

            if (UserDAO.getLoggedInUser() != null) {
                ps.setString(9, UserDAO.getLoggedInUser().getUsername());
                ps.setString(11, UserDAO.getLoggedInUser().getUsername());
            }
            else {
                ps.setString(9, null);
                ps.setString(11, null);
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads a single appointment from the database, based off the appointment_ID.
     * @param id the id of the appointment used to query the database.
     * @return the appointment with the given appointment_ID
     */
    @Override
    public Appointment read(int id) {

        try {
            sqlStatement = "select Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID," +
                    "contacts.Contact_ID, Contact_Name from appointments " +
                    "inner join contacts on appointments.Contact_ID = contacts.Contact_ID;" +
                    "WHERE Appointment_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) //match found
                return getNextAppointment(rs);
            else
                return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method updates the appointment given in the database, using the given appointment object with an existing ID.
     * @param object the updated object with an existing appointment_ID.
     */
    @Override
    public void update(Appointment object) {

        try {
            sqlStatement = "UPDATE appointments SET " +
                    "Title = ?, " +
                    "Description = ?, " +
                    "Location = ?, " +
                    "Type = ?, " +
                    "Start = ?, " +
                    "End = ?, " +
                    "Customer_ID = ?, " +
                    "User_ID = ?, " +
                    "Contact_ID = ? " +
                    "WHERE Appointment_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setString(1, object.getTitle());
            ps.setString(2, object.getDescription());
            ps.setString(3, object.getLocation());
            ps.setString(4, object.getType());
            ps.setTimestamp(5, Timestamp.valueOf(object.getStartTime()));
            ps.setTimestamp(6, Timestamp.valueOf(object.getEndTime()));
            ps.setInt(7, object.getCustomerId());
            ps.setInt(8, UserDAO.getLoggedInUser().getId());
            ps.setInt(9, object.getContactId());
            ps.setInt(10, object.getId());

            System.out.println(ps.executeUpdate() + " Rows Affected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will delete a specific appointment from the database using the given appointment_ID.
     * @param key the appointment_ID of the appointment to be deleted.
     * @return whether the appointment was found and deleted from the database.
     */
    @Override
    public boolean delete(int key) {

        try {
            sqlStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";

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
     * This method is used to delete all appointments that have a given customerID. This is used when deleting a customer,
     * where the appointments a customer has must also be deleted ahead of time.
     * @param customerId the customer_ID to delete all appointments from.
     */
    public void deleteCustomerAppointments(int customerId) {
        try {
            sqlStatement = "DELETE FROM appointments WHERE Customer_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, customerId);
            System.out.print(ps.executeUpdate() + " Row(s) affected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to help with code re-use. This method will take a ResultSet from a previously executed Query and
     * extract an Appointment from that resultSet.
     * @param rs the resultSet from the query
     * @return a single appointment from the ResultSet
     */
    private Appointment getNextAppointment(ResultSet rs) {
        try {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime = rs.getTimestamp("End").toLocalDateTime();
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");

            Appointment matchedAppointment = new Appointment(title, description, location,
                    type, startTime, endTime, customerId, userId, contactId, contactName);

            matchedAppointment.setId(appointmentId);

            return matchedAppointment;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns all Appointments for the currently logged in user.
     * @return the appointments list
     */
    public ObservableList<Appointment> getAllByUser() {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        sqlStatement = "select Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID," +
                "contacts.Contact_ID, Contact_Name from appointments " +
                "inner join contacts on appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE User_ID = ?;";

        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, UserDAO.getLoggedInUser().getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment matchedAppointment = getNextAppointment(rs);
                appointmentsList.add(matchedAppointment);
            }

            return appointmentsList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method gets all appointments for a given contact.
     * @param contactId the contactID for the contact.
     * @return a list of all appointments with the contact ID.
     */
    public ObservableList<Appointment> getAllByContact(int contactId) {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID," +
                "contacts.Contact_ID, Contact_Name from appointments " +
                "inner join contacts on appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE contacts.Contact_ID = ? " +
                "ORDER BY Start;";

        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, contactId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment matchedAppointment = getNextAppointment(rs);
                appointmentsList.add(matchedAppointment);
            }

            return appointmentsList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method gets all appointments for a given customer.
     * @param customerId the customerId for the customer.
     * @return a list of all appointments with the customer ID.
     */
    public ObservableList<Appointment> getAllByCustomer(int customerId) {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID," +
                "contacts.Contact_ID, Contact_Name from appointments " +
                "inner join contacts on appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE appointments.Customer_ID = ? " +
                "ORDER BY Start;";

        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment matchedAppointment = getNextAppointment(rs);
                appointmentsList.add(matchedAppointment);
            }

            return appointmentsList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method gets all appointments that are between two dates, inclusively.
     * @param startDate the start date
     * @param endDate the end date
     * @return an appointments list between the two dates.
     */
    public ObservableList<Appointment> getAllByDates(LocalDate startDate, LocalDate endDate) {
        try {
            ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
            PreparedStatement ps;

            if (startDate == null && endDate == null) { //return all Appointments ordered by Date
                sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID," +
                        "contacts.Contact_ID, Contact_Name from appointments " +
                        "inner join contacts on appointments.Contact_ID = contacts.Contact_ID " +
                        "ORDER BY Start;";
                ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            }
            else { // two dates were provided
                sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID," +
                        "contacts.Contact_ID, Contact_Name from appointments " +
                        "inner join contacts on appointments.Contact_ID = contacts.Contact_ID " +
                        "WHERE Start BETWEEN ? AND ?" +
                        "ORDER BY Start;";
                ps = DBConnection.getConnection().prepareStatement(sqlStatement);
                ps.setDate(1, Date.valueOf(startDate));
                LocalDateTime adjustedDate = LocalDateTime.of(endDate, LocalTime.of(23,59,59));
                ps.setTimestamp(2, Timestamp.valueOf(adjustedDate));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment matchedAppointment = getNextAppointment(rs);
                appointmentsList.add(matchedAppointment);
            }

            return appointmentsList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



}
