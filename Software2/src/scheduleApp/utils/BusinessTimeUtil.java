package scheduleApp.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * This class holds information and methods used to convert time zones. This supports the local time, UTC time, and
 * EDT time zones.
 */
public class BusinessTimeUtil {
    private static final ZonedDateTime open = ZonedDateTime.of(LocalDate.of(2020, 1, 1),
            LocalTime.of(8, 0), ZoneId.of("US/Eastern")); //8 am EST
    private static final ZonedDateTime close = ZonedDateTime.of(LocalDate.of(2020, 1, 1),
            LocalTime.of(22, 0), ZoneId.of("US/Eastern")); //10 pm EST
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Gets business hours.
     *
     * @return the business hours
     */
    public static String getBusinessHours() {
        return "Business hours are between " + open.toLocalTime().format(timeFormat) + " and " +
                close.toLocalTime().format(timeFormat) + " EST(UTC-05:00).";
    }

    /**
     * This method converts local system default time to Easter Daylight Savings Time
     *
     * @param time the time
     * @return the zoned date time
     */
    public static ZonedDateTime localToEST (LocalDateTime time) {
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime localZDT = time.atZone(localZone);

        ZonedDateTime estZDT = localZDT.withZoneSameInstant(ZoneId.of("US/Eastern"));

        return estZDT;
    }

    /**
     * This method checks whether a start and end time both fall within business hours.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return the boolean
     */
    public static boolean isWithinBusinessHours(LocalDateTime startTime, LocalDateTime endTime) {

        ZonedDateTime estStart = localToEST(startTime);
        ZonedDateTime estEnd = localToEST(endTime);

        return !estStart.toLocalTime().isBefore(open.toLocalTime()) && !estEnd.toLocalTime().isAfter(close.toLocalTime());
    }

    /**
     * This method converts local time to UTC time.
     *
     * @param time the time
     * @return the zoned date time
     */
    public static ZonedDateTime localToUTC (LocalDateTime time) {
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime localZDT = time.atZone(localZone);

        ZonedDateTime utcZDT = localZDT.withZoneSameInstant(ZoneId.of("UTC"));

        return utcZDT;
    }
}
