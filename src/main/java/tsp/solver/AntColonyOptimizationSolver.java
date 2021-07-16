package tsp.solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tsp.model.Point;
import tsp.util.CSVFormatException;
import tsp.util.CSVUtil;

public class AntColonyOptimizationSolver extends TspSolver {

  public static void main(String[] args) {
    String filePath = BruteForceSolver.class.getClassLoader().getResource("points-set-12-from-video.csv").getPath().substring(1);
    List<Point> points = new ArrayList<>();
    try {
      points = CSVUtil.readPointsFromFile(filePath);
    } catch(IOException | CSVFormatException e) {
      e.printStackTrace();
    }

    TspSolver solver = new AntColonyOptimizationSolver(points);
    solver.solve();
    solver.printSolution();

  }

  public AntColonyOptimizationSolver(List<Point> points) {
    super(points);
  }

  @Override
  public void solve() {
    // TODO Auto-generated method stub

  }

  @Override
  public void printSolution() {
    // TODO Auto-generated method stub

  }

}
