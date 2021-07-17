package tsp.solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tsp.model.Point;
import tsp.util.CSVFormatException;
import tsp.util.CSVUtil;
import tsp.util.DistributedRandomNumberGenerator;

public class AntColonyOptimizationSolver extends TspSolver {

  public static void main(String[] args) {
    String filePath = BruteForceSolver.class.getClassLoader().getResource("points-set3.csv").getPath().substring(1);
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
   * Intensity of pheromone trail
   */
  private static final double PHEROMONE_INTENSITY = 10;

  /**
   * Initial pheromone strength along all paths
   * (otherwise initial probabilities will all be zero)
   */
  private static final double INITIAL_PHEROMONE_INTENSITY = 1;

  /**
   * Proportion of pheromone to evaporate each step
   */
  private static final double EVAPORATION_RATE = 0.3;

  /**
   * Number of ants in each group
   */
  private static final int ANT_GROUP_SIZE = 20;

  private static final Random RANDOM = new Random();

  private double[][] pheremoneTrails;

  private ExecutorService executor;

  public AntColonyOptimizationSolver(List<Point> points) {
    super(points);
    pheremoneTrails = new double[points.size()][points.size()];
    setTrailsToInitialPheremoneIntensity(pheremoneTrails);
    this.executor = Executors.newWorkStealingPool(ANT_GROUP_SIZE);
  }

  private static void setTrailsToInitialPheremoneIntensity(double[][] pheremoneTrails) {
    for(int i = 0; i < pheremoneTrails.length; i++) {
      for(int j = 0; j < pheremoneTrails.length; j++) {
        pheremoneTrails[i][j] = INITIAL_PHEROMONE_INTENSITY;
      }
    }
  }

  @Override
  public void solve() {
    double[][] desirabilityMatrix = createDesirabilityMatrix();
    DistributedRandomNumberGenerator[] randomTrailChoosers = new DistributedRandomNumberGenerator[desirabilityMatrix.length];
    
    for(int i = 0; i < desirabilityMatrix.length; i++) {
      DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
      for(int j = 0; j < desirabilityMatrix.length; j++) {
        drng.addNumber(j, desirabilityMatrix[i][j]);
      }
      randomTrailChoosers[i] = drng;
    }
    
    List<Ant> ants = IntStream.rangeClosed(1, 1)
                              .mapToObj(i -> new Ant(getIndices(), RANDOM.nextInt(getPointsCount()), randomTrailChoosers))
                              .collect(Collectors.toList());
    System.out.println("ants start");
    try {
      executor.invokeAll(ants).stream().map(future -> {
        try {
          return future.get();
        } catch(Exception e) {
          throw new IllegalStateException(e);
        }
      }).forEach(path -> {
        System.out.println(Arrays.toString(path));

        for(int i = 0; i < path.length; i++) {
          pheremoneTrails[path[i]][path[(i + 1) % path.length]] += PHEROMONE_INTENSITY;
        }
        System.out.println("\n-----\n");
      });
    } catch(InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println();
    print2dDoubleArray(pheremoneTrails);


  }

  private double[][] createDesirabilityMatrix() {
    double[][] result = new double[getPointsCount()][getPointsCount()];

    for(int i = 0; i < result.length; i++) { // fromPointIndex
      for(int j = 0; j < result.length; j++) { // toPointIndex
        if(i == j) {
          continue;
        }
        double dst = getAdjacencyMatrix()[i][j];
        double pheromoneStrength = pheremoneTrails[i][j];
        double desirability = Math.pow(1 / dst, DISTANCE_POWER) * Math.pow(pheromoneStrength, PHEROMONE_POWER);
        result[i][j] = desirability;
      }
    }
    return result;
  }

  @Override
  public void printSolution() {
    // TODO Auto-generated method stub

  }

}
