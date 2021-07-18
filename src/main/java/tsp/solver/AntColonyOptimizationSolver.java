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

public class AntColonyOptimizationSolver extends TspSolver {

  private static final double DOUBLE_TOLERANCE = 0.001;

  public static void main(String[] args) {
    String filePath = BruteForceSolver.class.getClassLoader().getResource("points-set1.csv").getPath().substring(1);
    List<Point> points = new ArrayList<>();
    try {
      points = CSVUtil.readPointsFromFile(filePath);
    } catch(IOException | CSVFormatException e) {
      e.printStackTrace();
    }
    AntColonyOptimizationSolver solver = new AntColonyOptimizationSolver(points);
    solver.solve();
    System.out.println("Final Solition:");
    solver.printSolution();
  }

  /**
   * Controls the extent to which ants will prefer nearby points.
   * Too high and algorithm will essentially be a greedy search,
   * but too low and the search will likely stagnate.
   */
  private static final double DISTANCE_POWER = 4.0; // 4.0

  /**
   * Controls how likely each ant will be to follow the same paths
   * as the ants be fore it. Too high and algorithm will keep searching
   * the same path, too low and it will search too many paths.
   */
  private static final double PHEROMONE_POWER = 1.0; // 1.0

  /**
   * Intensity of pheromone trail
   */
  private static final double PHEROMONE_INTENSITY = 10; // 10

  /**
   * Initial pheromone strength along all paths
   * (otherwise initial probabilities will all be zero)
   */
  private static final double INITIAL_PHEROMONE_INTENSITY = 1.0; // 1.0

  /**
   * Proportion of pheromone to evaporate each step
   */
  private static final double EVAPORATION_RATE = 0.3; // 0.3

  /**
   * Number of ants in each group
   */
  private static final int ANT_GROUP_SIZE = 20; // 20

  private static final Random RANDOM = new Random();

  public static final int TIMES_BEST_TOUR_DISTANCE_MUST_STAY_SAME_UNTIL_TERMINATION = 20;

  private double[][] pheromoneTrails;

  private ExecutorService executor;

  private boolean bestTourDstChanges;
  public int timesBestTourDstStaysSame;

  public AntColonyOptimizationSolver(List<Point> points) {
    super(points);
    pheromoneTrails = new double[points.size()][points.size()];
    setTrailsToInitialPheremoneIntensity(pheromoneTrails);
    this.executor = Executors.newWorkStealingPool(ANT_GROUP_SIZE);
  }

  private static void setTrailsToInitialPheremoneIntensity(double[][] pheromoneTrails) {
    for(int i = 0; i < pheromoneTrails.length; i++) {
      for(int j = 0; j < pheromoneTrails.length; j++) {
        pheromoneTrails[i][j] = INITIAL_PHEROMONE_INTENSITY;
      }
    }
  }

  @Override
  public void solve() {
    while(timesBestTourDstStaysSame < TIMES_BEST_TOUR_DISTANCE_MUST_STAY_SAME_UNTIL_TERMINATION) {
      bestTourDstChanges = false;
      double[][] desirabilityMatrix = createDesirabilityMatrix();

      List<Ant> ants = IntStream.rangeClosed(1, ANT_GROUP_SIZE)
                                .mapToObj(i -> new Ant(getIndices(), RANDOM.nextInt(getPointsCount()), desirabilityMatrix))
                                .collect(Collectors.toList());
      try {
        executor.invokeAll(ants).stream().map(future -> {
          try {
            return future.get();
          } catch(Exception e) {
            throw new IllegalStateException(e);
          }
        }).forEach((int[] path) -> {
          printPath(path);
          double roundTripDistance = calcRoundTripDistanceAndAddPheromoneToPaths(path);
          if(roundTripDistance < getBestTourDst()) {
            bestTourDstChanges = true;
            setBestTourDst(roundTripDistance);
            setBestTourIndices(path);
          }
        });
      } catch(InterruptedException e) {
        e.printStackTrace();
      }

      if(bestTourDstChanges) {
        timesBestTourDstStaysSame = 0;
      } else {
        timesBestTourDstStaysSame++;
      }
      System.out.println(timesBestTourDstStaysSame);

      evaporateSomePheromone();
      System.out.println("Partial Solution:");
      printSolution();
      System.out.println("\n ---- \n");
    }

  }

  public List<Point> solvePartial() {
    bestTourDstChanges = false;
    double[][] desirabilityMatrix = createDesirabilityMatrix();

    List<Ant> ants = IntStream.rangeClosed(1, ANT_GROUP_SIZE)
                              .mapToObj(i -> new Ant(getIndices(), RANDOM.nextInt(getPointsCount()), desirabilityMatrix))
                              .collect(Collectors.toList());
    try {
      executor.invokeAll(ants).stream().map(future -> {
        try {
          return future.get();
        } catch(Exception e) {
          throw new IllegalStateException(e);
        }
      }).forEach((int[] path) -> {
        printPath(path);
        double roundTripDistance = calcRoundTripDistanceAndAddPheromoneToPaths(path);
        if(roundTripDistance < getBestTourDst()) {
          bestTourDstChanges = true;
          setBestTourDst(roundTripDistance);
          setBestTourIndices(path);
        }
      });
    } catch(InterruptedException e) {
      e.printStackTrace();
    }

    if(bestTourDstChanges) {
      timesBestTourDstStaysSame = 0;
    } else {
      timesBestTourDstStaysSame++;
    }
    System.out.println(timesBestTourDstStaysSame);

    evaporateSomePheromone();
    return Arrays.stream(getBestTourIndices()).mapToObj(i -> getPoints().get(i)).collect(Collectors.toList());
  }

  private double calcRoundTripDistanceAndAddPheromoneToPaths(int[] path) {
    double roundTripDistance = 0.0;
    for(int i = 0; i < path.length; i++) {
      int nextIndex = (i + 1) % path.length;
      roundTripDistance += lookUpDistance(path[i], path[nextIndex]);
      pheromoneTrails[path[i]][path[nextIndex]] += PHEROMONE_INTENSITY;
    }
    return roundTripDistance;
  }

  private void evaporateSomePheromone() {
    for(int i = 0; i < pheromoneTrails.length; i++) {
      for(int j = 0; j < pheromoneTrails.length; j++) {
        pheromoneTrails[i][j] *= (1.0 - EVAPORATION_RATE);
      }
    }
  }

  private double[][] createDesirabilityMatrix() {
    double[][] result = new double[getPointsCount()][getPointsCount()];

    for(int i = 0; i < result.length; i++) { // fromPointIndex
      for(int j = 0; j < result.length; j++) { // toPointIndex
        if(i == j) {
          continue;
        }
        double dst = getAdjacencyMatrix()[i][j];
        if(dst <= DOUBLE_TOLERANCE && dst >= -DOUBLE_TOLERANCE) {
          throw new IllegalStateException("There are two points (at indexes " + i + " & " + j + ") that are too near together.");
        }
        double pheromoneStrength = pheromoneTrails[i][j];
        double desirability = Math.pow(1 / dst, DISTANCE_POWER) * Math.pow(pheromoneStrength, PHEROMONE_POWER);
        result[i][j] = desirability;
      }
    }
    return result;
  }

}
