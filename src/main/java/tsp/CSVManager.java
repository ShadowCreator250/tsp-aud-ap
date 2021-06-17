package tsp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import tsp.model.Point;

public class CSVManager {
  
  private static final Pattern numberRegex = Pattern.compile("\\d+");

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

  private static int convertToInt(String string) {
    if(numberRegex.matcher(string).matches()) {
      return Integer.parseInt(string);
    }
    throw new IllegalArgumentException("String did contain non-digit characters");
  }
}
