package tsp.solver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import tsp.model.Point;

public abstract class TspSolver {

  private List<Point> points;
  private int pointsCount;
  private double[][] adjacencyMatrix;
  private int[] indices;
  private double bestTourDst;
  private int[] bestTourIndices;

  TspSolver(List<Point> points) {
    this.points = points;
    this.pointsCount = points.size();
    this.adjacencyMatrix = createAdjacencyMatrix(points);
    this.indices = IntStream.rangeClosed(0, points.size() - 1).toArray();
    this.bestTourDst = Double.MAX_VALUE;
    this.bestTourIndices = new int[getIndices().length];
  }

  public static double[][] createAdjacencyMatrix(List<Point> points) {
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

  public double lookUpDistance(int point1Index, int point2Index) {
    return adjacencyMatrix[point1Index][point2Index];
  }

  public abstract void solve();

  public abstract void printSolution();

  public static void swap(int[] array, int index1, int index2) {
    int temp = array[index1];
    array[index1] = array[index2];
    array[index2] = temp;
  }

  public static void print2dDoubleArray(double[][] adjacencyMatrix) {
    Arrays.stream(adjacencyMatrix).forEach(arr -> System.out.println(Arrays.toString(arr)));
  }

  public List<Point> getPoints() {
    return points;
  }

  public int getPointsCount() {
    return pointsCount;
  }

  public double[][] getAdjacencyMatrix() {
    return adjacencyMatrix;
  }

  public int[] getIndices() {
    return indices;
  }

  public double getBestTourDst() {
    return bestTourDst;
  }

  public void setBestTourDst(double bestTourDst) {
    this.bestTourDst = bestTourDst;
  }

  public int[] getBestTourIndices() {
    return bestTourIndices;
  }

  public void setBestTourIndices(int[] bestTourIndices) {
    this.bestTourIndices = bestTourIndices;
  }

}
