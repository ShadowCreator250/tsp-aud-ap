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

  /**
   * Controls the extent to which ants will prefer nearby points.
   * Too high and algorithm will essentially be a greedy search,
   * but too low and the search will likely stagnate.
   */
  private static final double DISTANCE_POWER = 4.0;

  /**
   * Controls how likely each ant will be to follow the same paths
   * as the ants be fore it. Too high and algorithm will keep searching
   * the same path, too low and it will search too many paths.
   */
  private static final double PHEROMONE_POWER = 1.0;

  /**
   * Intensity of Pheromone trail
   */
  private static final double PHEREMONE_INTENSITY = 10;

  /**
   * Initial Pheromone strength along all paths
   * (otherwise initial probabilities will all be zero)
   */
  private static final double INITIAL_PHEREMONE_INTENSITY = 1;

  /**
   * Proportion of Pheromone to evaporate each step
   */
  private static final double EVAPORATION_RATE = 0.3;

  /**
   * Number of ants in each group
   */
  private static final int ANT_GROUP_SIZE = 20;

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
