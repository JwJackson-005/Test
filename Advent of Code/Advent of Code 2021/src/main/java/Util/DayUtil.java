package Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DayUtil {

    protected static final String DEFAULT_DELIMITER = "\n";

    public static String readFileToString(String filename) {
        try {
            return Files.readString(Path.of(filename));
        } catch (IOException ioe) {
            System.out.println("Invalid Filename!");
            return null;
        }
    }

    public static String[] splitString(String string) {
        return splitString(string, DEFAULT_DELIMITER);
    }

    public static String[] splitString(String string, String delimiter) {
        return string.split(delimiter);
    }
}
