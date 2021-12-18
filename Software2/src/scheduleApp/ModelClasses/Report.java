package scheduleApp.ModelClasses;

import java.time.Month;

/**
 * A class made to house information for the reports tab. Specifically it holds the information needed to populate
 * the first report after a database query: a month, a type, and a count.
 * @author Jason Jackson
 */
public class Report {

    private Month month;
    private String type;
    private int count;

    /**
     * Instantiates a new Report.
     *
     * @param month the month
     * @param type  the type
     * @param count the count
     */
    public Report(Month month, String type, int count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }

    /**
     * Gets month.
     *
     * @return the month
     */
    public Month getMonth() {
        return month;
    }

    /**
     * Sets month.
     *
     * @param month the month
     */
    public void setMonth(Month month) {
        this.month = month;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets count.
     *
     * @param count the count
     */
    public void setCount(int count) {
        this.count = count;
    }
}
