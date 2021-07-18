package tsp.solver;

import java.util.concurrent.Callable;

import tsp.util.DistributedRandomNumberGenerator;

public class Ant implements Callable<int[]> {

  private int[] indices;
  private int currentPointIndex;
  private DistributedRandomNumberGenerator[] randomTrailChoosers;
  private boolean[] pointsVisited;
  private int pointsVisitedCount;
  private int[] path;
  private int step;

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
        continue;
      }

      moveTo(nextPointIndex);
    }
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
