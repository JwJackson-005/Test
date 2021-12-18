package scheduleApp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is used to connect and close the database, as well as get the connection of the database whenever the
 * program needs without having to create a new connection.
 */
public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String serverName = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ07214";

    //private static final String jdbcUrl = protocol + vendorName + serverName + dbName;
    private static final String jdbcUrl = protocol + vendorName + serverName + dbName + "?connectionTimeZone=SERVER";


    private static final String MYSQLDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    private static final String username = "U07214";
    private static final String password = "53688934468";

    /**
     * Start connection connection.
     *
     * @return the connection
     */
    public static Connection startConnection() {

        try {
            Class.forName(MYSQLDriver);
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connection successful");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * Close connection.
     */
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public static Connection getConnection() {
        return conn;
    }
}
