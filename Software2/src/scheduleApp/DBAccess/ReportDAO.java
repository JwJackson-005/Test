package scheduleApp.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduleApp.ModelClasses.Report;
import scheduleApp.utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

/**
 * The ReportDAO class is an implementation of the DAO Interface. It handles the CRUD operations on the database
 * for the reports table.
 * @author Jason Jackson
 */
public class ReportDAO implements DAO<Report> {

    /**
     * This method returns all reports stored in the reports table of the database.
     * @return the reports list
     */
    @Override
    public ObservableList<Report> getAll() {

        try {
            ObservableList<Report> reportList = FXCollections.observableArrayList();
            String sqlStatement = "select MONTH(Start) AS Month, Type, COUNT(Appointment_ID) as Count\n" +
                    "FROM appointments\n" +
                    "GROUP BY Month, Type;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Report matchedReport = getNextReport(rs);
                reportList.add(matchedReport);
            }

            return reportList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to help with code re-use. This method will take a ResultSet from a previously executed Query and
     * extract an report from that resultSet.
     * @param rs the resultSet from the query
     * @return a single report from the ResultSet
     */
    private Report getNextReport(ResultSet rs) {
        try {
            int monthId = rs.getInt("Month");
            String type = rs.getString("Type");
            int count = rs.getInt("Count");


            Report matchedReport = new Report(Month.of(monthId), type, count);

            return matchedReport;
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
    public void create(Report object) {   }

    /**
     * This method is not currently implemented in the program because it is not used. But
     * it is a requirement for the DAO interface.
     * @return  not used
     */
    @Override
    public Report read(int id) {
        return null;
    }

    /**
     * This method is not currently implemented in the program because it is not used. But
     * it is a requirement for the DAO interface.
     * @param object not used
     */
    @Override
    public void update(Report object) {

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
