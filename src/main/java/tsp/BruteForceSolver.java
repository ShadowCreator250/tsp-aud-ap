package tsp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import tsp.model.Point;
import tsp.util.CSVFormatException;
import tsp.util.CSVUtil;

public class BruteForceSolver {

  public static void main(String[] args) {
    String filePath = BruteForceSolver.class.getClassLoader().getResource("points-set-12-from-video.csv").getPath().substring(1);
    List<Point> points = new ArrayList<>();
    try {
      points = CSVUtil.readPointsFromFile(filePath);
    } catch(IOException | CSVFormatException e) {
      e.printStackTrace();
    }

    BruteForceSolver bruteForceSolver = new BruteForceSolver(points);
    bruteForceSolver.solve();
    bruteForceSolver.printSolution();
  }

  private List<Point> points;
  private double[][] adjacencyMatrix;
  private int[] indices;
  private double bestTourDst;
  private int[] bestTourIndices;

  public BruteForceSolver(List<Point> points) {
    this.points = points;
    this.adjacencyMatrix = createAdjacencyMatrix(points);
    this.indices = IntStream.rangeClosed(0, points.size() - 1).toArray();
    System.out.println(Arrays.deepToString(adjacencyMatrix));
    this.bestTourDst = Double.MAX_VALUE;
    this.bestTourIndices = new int[indices.length];
  }

  private double[][] createAdjacencyMatrix(List<Point> points) {
    double[][] result = new double[points.size()][points.size()];

    for(int i = 0; i < result.length; i++) { // rows
      for(int j = 0; j <= i; j++) { // cols
        double dx = (double) points.get(i).getX() - points.get(j).getX();
        double dy = (double) points.get(i).getY() - points.get(j).getY();
        double distance = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
        result[i][j] = distance;
        result[j][i] = distance;
      }
    }

    return result;
  }

  private double lookUpDistance(int point1Index, int point2Index) {
    return adjacencyMatrix[point1Index][point2Index];
  }

  public void solve() {
    // Call with length -1 to keep one element fixed in place. This avoids wasting time 
    // evaluating tours that are identical except for beginning at a different point
    generateSolutions(indices, indices.length - 1);
  }

  /** Heap's algorithm for generating all permutations */
  private void generateSolutions(int[] indices, int n) {
    if(n == 1) {
      evaluateSolution();
      return;
    }

    for(int i = 0; i < n; i++) {
      generateSolutions(indices, n - 1);
      int swapIndex = (n % 2 == 0) ? i : 0;
      swap(indices, swapIndex, n - 1);
    }
  }

  private static void swap(int[] array, int index1, int index2) {
    int temp = array[index1];
    array[index1] = array[index2];
    array[index2] = temp;
  }

  private void evaluateSolution() {
    // Ignore solutions which are just the reverse of another solution
    if(indices[0] < indices[indices.length - 2]) {

      // Calculate length of the path (including returning to start point)
      double tourDst = 0;
      for(int i = 0; i < indices.length; i++) {
        int nextIndex = (i + 1) % indices.length;
        tourDst += lookUpDistance(indices[i], indices[nextIndex]);
      }

      // Save the path indices if this is the best solution found so far
      if(tourDst < bestTourDst) {
        bestTourDst = tourDst;
        System.arraycopy(indices, 0, bestTourIndices, 0, indices.length);
      }
    }
  }

  private void printSolution() {
    Point[] bestTourPointsArray = Arrays.stream(bestTourIndices).mapToObj(i -> points.get(i)).toArray(Point[]::new);
    String bestTourString = Arrays.toString(bestTourPointsArray);
    System.out.println("The solution has a distance of " + bestTourDst + " and is " + bestTourString + ".");
  }

  private static void print2dDoubleArray(double[][] adjacencyMatrix) {
    Arrays.stream(adjacencyMatrix).forEach(arr -> System.out.println(Arrays.toString(arr)));
  }

}
