package tsp.solver;

import java.util.concurrent.Callable;

import tsp.util.DistributedRandomNumberGenerator;

public class Ant implements Callable<int[]> {

  private static final int NO_POINT_FOUND_THRESHHOLD = 25;
  private int[] indices;
  private int currentPointIndex;
  private DistributedRandomNumberGenerator[] randomTrailChoosers;
  private boolean[] pointsVisited;
  private int pointsVisitedCount;
  private int[] path;
  private int step;
  private int noNextPointFoundCounter;

  public Ant(int[] indices, int startIndex, double[][] desirabilityMatrix) {
    if(indices.length != desirabilityMatrix.length) {
      throw new IllegalArgumentException("The indexes and randomTrailChoosers arrays have different lengths which they shouldn't.");
    }
    if(startIndex < 0 || startIndex >= indices.length) {
      int clampToTop = (startIndex > indices.length) ? indices.length : startIndex;
      startIndex = (startIndex < 0) ? 0 : clampToTop;
    }
    this.indices = indices;
    this.currentPointIndex = startIndex;
    this.randomTrailChoosers = generateRandomTrailChoosers(desirabilityMatrix);
    this.pointsVisited = new boolean[indices.length];
    this.path = new int[indices.length];
    for(int i = 0; i < path.length; i++) {
      path[i] = -1;
    }
    this.pointsVisitedCount = 0;
    this.step = 0;
    moveTo(currentPointIndex);
  }

  private DistributedRandomNumberGenerator[] generateRandomTrailChoosers(double[][] desirabilityMatrix) {
    DistributedRandomNumberGenerator[] result = new DistributedRandomNumberGenerator[desirabilityMatrix.length];

    for(int i = 0; i < desirabilityMatrix.length; i++) { // fromPoint
      DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
      for(int j = 0; j < desirabilityMatrix.length; j++) { // toPoint 
        drng.addNumber(j, desirabilityMatrix[i][j]); // with probabilities
      }
      result[i] = drng;
    }
    return result;
  }

  public void run() {
    while(pointsVisitedCount < indices.length) {

      // remove the visited point from the statistic
      for(DistributedRandomNumberGenerator drng : randomTrailChoosers) {
        drng.addNumber(currentPointIndex, 0.0);
      }

      int nextPointIndex = randomTrailChoosers[currentPointIndex].getDistributedRandomNumber();

      if(nextPointIndex == -1 || pointsVisited[nextPointIndex]) {
        if(nextPointIndex == -1) {
          syserr("The randomTrailChooser didn't choose a point. Trying again.");
        } else if(pointsVisited[nextPointIndex]) {
          syserr("The randomTrailChooser choose a point that was already visited. Trying again.");
        }
        noNextPointFoundCounter++;
        if(noNextPointFoundCounter > NO_POINT_FOUND_THRESHHOLD) {
          nextPointIndex = findFirstFreePoint(nextPointIndex);
        } else {
          continue;
        }
      }
      

      moveTo(nextPointIndex);
    }
  }

  private void syserr(String message) {
    System.err.println(this.toString() + " at " + step + ": " + message);
  }

  private int findFirstFreePoint(int nextPointIndex) {
    for(int i = 0; i < pointsVisited.length; i++) {
      if(!pointsVisited[i]) {
        nextPointIndex = i;
        break;
      }
      if(i == pointsVisited.length - 1) {
        syserr("There is no point left to visit.");
      }
    }
    return nextPointIndex;
  }

  private void moveTo(int nextPointIndex) {
    pointsVisited[nextPointIndex] = true;
    path[step] = nextPointIndex;
    currentPointIndex = nextPointIndex;

    step++;
    pointsVisitedCount++;
  }

  public int[] getPath() {
    return path;
  }

  @Override
  public int[] call() {
    run();
    return path;
  }

}
