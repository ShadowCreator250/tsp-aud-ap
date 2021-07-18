package tsp.solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tsp.model.Point;
import tsp.util.CSVFormatException;
import tsp.util.CSVUtil;

public class BruteForceSolver extends TspSolver {

  public static void main(String[] args) {
    String filePath = BruteForceSolver.class.getClassLoader().getResource("points-set-12-from-video.csv").getPath().substring(1);
    List<Point> points = new ArrayList<>();
    try {
      points = CSVUtil.readPointsFromFile(filePath);
    } catch(IOException | CSVFormatException e) {
      e.printStackTrace();
    }

    TspSolver solver = new BruteForceSolver(points);
    solver.solve();
    solver.printSolution();
  }

  public BruteForceSolver(List<Point> points) {
    super(points);
  }

  @Override
  public void solve() {
    // Call with length -1 to keep one element fixed in place. This avoids wasting time 
    // evaluating tours that are identical except for beginning at a different point
    generateSolutions(getIndices(), getPointsCount() - 1);
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

  private void evaluateSolution() {
    // Ignore solutions which are just the reverse of another solution
    if(getIndices()[0] < getIndices()[getPointsCount() - 2]) {

      // Calculate length of the path (including returning to start point)
      double tourDst = calcPathDistance(getIndices());

      // Save the path indices if this is the best solution found so far
      if(tourDst < getBestTourDst()) {
        setBestTourDst(tourDst);
        System.arraycopy(getIndices(), 0, getBestTourIndices(), 0, getPointsCount());
      }
    }
  }

}
