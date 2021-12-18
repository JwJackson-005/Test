package scheduleApp.DBAccess;

import javafx.collections.ObservableList;
import scheduleApp.ModelClasses.User;
import scheduleApp.utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The UserDAO class is an implementation of the DAO Interface. It handles the CRUD operations on the database
 * for the users table.
 * @author Jason Jackson
 */
public class UserDAO implements DAO<User>{

   private static User loggedInUser;
   private static String sqlStatement;

    /**
     * This method verifies a single user's log in attempt. It will check the database if the
     * username and password match. If a match is found, it will return true, else it will return false.
     * @param username the username
     * @param password the password
     * @return whether the login is valid
     */
   public static boolean verifyUser(String username, String password) {

       try {
           sqlStatement = "Select * FROM users WHERE User_Name = ? AND password = ?";
           PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
           ps.setString(1, username);
           ps.setString(2, password);

           ResultSet rs = ps.executeQuery();

           while(rs.next()) { // has a match

            User newUser = new User(rs.getInt("User_ID"), rs.getString("User_Name"), rs.getString("Password"));
            loggedInUser =  newUser;
            return true;

           }
           return false;
       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
   }

    /**
     * Getter method for the loggedInUser
     * @return the logged in User
     */
   public static User getLoggedInUser() {
       return loggedInUser;
   }

    /**
     * This method is not currently implemented in the program because it is not used. But
     * it is a requirement for the DAO interface.
     * @return not used
     */
    @Override
    public ObservableList<User> getAll() {
        return null; //not needed
    }

    /**
     * This method is not currently implemented in the program because it is not used. But
     * it is a requirement for the DAO interface.
     * @param object not used
     */
    @Override
    public void create(User object) {
        //not needed
    }

    /**
     * This method reads a single user from the database, based off the user ID.
     * @param id the id of the user used to query the database.
     * @return the user with the given user ID
     */
    @Override
    public User read(int id) {
        try {
            sqlStatement = "SELECT * FROM users WHERE User_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {//match found
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");

                return new User(userId, userName, password);
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
    public void update(User object) {
        //not needed
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
