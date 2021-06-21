package tsp.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

import tsp.model.Point;

public class CSVUtil {

  private static final Pattern numberRegex = Pattern.compile("\\d+");

  // declaring constructor as private prevents that it is ever called
  private CSVUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Reads a csv file and converts its entries to points.<br>
   * Currently only supports positive point coordinates.
   * 
   * @param filename name of the csv file
   * @return a list of points
   * @throws IOException When something goes wrong while reading the file.
   */
  public static List<Point> readPointsFromFile(String filename) throws IOException {
    List<Point> points = new ArrayList<>();
    Path path = Paths.get(filename);

    try (Scanner sc = new Scanner(path, StandardCharsets.UTF_8)) {
      while(sc.hasNextLine()) {
        String[] split = sc.nextLine().split(";");

        points.add(new Point(convertToInt(split[0].trim()), convertToInt(split[1].trim())));
      }
    }

    return points;
  }

  private static int convertToInt(String string) {
    if(numberRegex.matcher(string).matches()) {
      return Integer.parseInt(string);
    }
    throw new IllegalArgumentException("String did contain non-digit characters");
  }

  public static Series<Number, Number> ConvertToSeries(List<Point> readPointsFromFile) {
    Series<Number, Number> series = new Series<Number, Number>();

    for(Point p : readPointsFromFile) {
      series.getData()
            .add(new Data<Number, Number>(p.getX(), p.getY()));
    }

    return series;
  }
}
