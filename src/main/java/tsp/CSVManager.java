package tsp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import tsp.model.Point;

public class CSVManager {

  /**
   * reads a csv file and converts it to points
   * @param filename name of the csv file
   * @return a list of points 
   * 
   */
  public static List<Point> readFromFile(String filename)
  {

    List<Point> points = new ArrayList<Point>();
    Path path = Paths.get(filename);
    
    try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.US_ASCII))
    {
      String line = reader.readLine();
      
      while(line != null) {
        String[] split = line.split(";");

        points.add(new Point(ConvertToInt(split[0]), ConvertToInt(split[1])));
      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }

    return points;
  }

  private static int ConvertToInt(String string) {
    if(string.matches("\\d+")) {
      return Integer.parseInt(string);
    }
    throw new IllegalArgumentException("String did contain non-digit characters");
  }
}
