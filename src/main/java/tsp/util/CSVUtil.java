package tsp.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import tsp.model.Point;

public class CSVUtil {

  private static final Pattern numberRegex = Pattern.compile("\\d+");
  private static final String DEFAULT_DELIMITER = ";";

  // declaring constructor as private prevents that it is ever called
  private CSVUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Reads a csv file and converts its entries to {@link Point}s.<br>
   * Currently only supports positive {@link Point} coordinates.<br>
   * <br>
   * Uses the default delimiter to split the 2 {@link Point} coordinates.
   * 
   * @param filename name of the csv file
   * @return a list of {@link Point}s
   * @throws IOException        when something goes wrong while reading the file.
   * @throws CSVFormatException when something with the csv in the file isn't right.
   * @see CSVUtil#readPointsFromFile(String, String)
   */
  public static List<Point> readPointsFromFile(String filename) throws IOException, CSVFormatException {
    return readPointsFromFile(filename, DEFAULT_DELIMITER);
  }

  /**
   * Reads a csv file and converts its entries to {@link Point}s.<br>
   * Currently only supports positive {@link Point} coordinates.<br>
   * <br>
   * Because this method uses {@link Scanner#nextLine()} to read the line, the new line symbol will be excluded
   * and thus a line like "20;\n" will not result in 2 arguments but "20; \n" will.
   * 
   * @param filename  the name of the csv file
   * @param delimiter the delimiter that splits the coordinates of the {@link Point}
   * @return a list of {@link Point}s
   * @throws IOException        when something goes wrong while reading the file.
   * @throws CSVFormatException when something with the csv in the file isn't right.
   * @see uses: {@link CSVUtil#readPointFromString(String, String)}
   */
  public static List<Point> readPointsFromFile(String filename, String delimiter) throws IOException, CSVFormatException {
    if(filename == null) {
      throw new IllegalArgumentException("The filename can not be null.");
    }

    List<Point> points = new ArrayList<>();
    Path path = Paths.get(filename);

    try (Scanner sc = new Scanner(path, StandardCharsets.UTF_8)) {
      while(sc.hasNextLine()) {
        points.add(readPointFromString(sc.nextLine(), delimiter));
      }
    }

    return points;
  }

  /**
   * Reads a String that optimally is in CSV syntax and converts it to a {@link Point}.<br>
   * Currently only supports positive {@link Point} coordinates.<br>
   * <br>
   * Uses the default delimiter to split the 2 {@link Point} coordinates.
   * 
   * @param csvString the String in CSV
   * @return a {@link Point}
   * @throws CSVFormatException when something with the csv isn't right.
   */
  public static Point readPointFromString(String csvString) throws CSVFormatException {
    return readPointFromString(csvString, DEFAULT_DELIMITER);
  }

  /**
   * Reads a String that optimally is in CSV syntax and converts it to a {@link Point}.
   * 
   * @param csvString the String in CSV format
   * @param delimiter the delimiter that splits the coordinates of the {@link Point}
   * @return a {@link Point}
   * @throws CSVFormatException when something with the csv isn't right.
   */
  public static Point readPointFromString(String csvString, String delimiter) throws CSVFormatException {
    if(csvString == null) {
      throw new IllegalArgumentException("The csvString can not be null.");
    }
    if(delimiter == null) {
      throw new IllegalArgumentException("The delimiter can not be null.");
    }
    if(!csvString.contains(delimiter)) {
      throw new CSVFormatException("Failed to convert: String \"" + csvString + "\" does not contain the delimiter \"" + delimiter + "\".");
    }
    String[] split = csvString.split(delimiter);

    String x = split[0];
    String y = split.length > 1 ? split[1] : "";

    return new Point(convertToInt(x), convertToInt(y));
  }

  private static int convertToInt(String string) throws CSVFormatException {
    String trimmedString = string.trim();

    if(numberRegex.matcher(trimmedString).matches()) {
      return Integer.parseInt(trimmedString);
    }
    throw new CSVFormatException("Failed to convert String to int: String \"" + string + "\" is empty or contains non-digit characters.");
  }
}
