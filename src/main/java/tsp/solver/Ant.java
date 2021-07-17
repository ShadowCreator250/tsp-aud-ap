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

  public Ant(int[] indices, int startIndex, DistributedRandomNumberGenerator[] randomTrailChoosers) {
    if(indices.length != randomTrailChoosers.length) {
      throw new IllegalArgumentException("The indexes and randomTrailChoosers arrays have different lengths which they shouldn't.");
    }
    if(startIndex < 0 || startIndex >= indices.length) {
      int clampToTop = (startIndex > indices.length) ? indices.length : startIndex;
      startIndex = (startIndex < 0) ? 0 : clampToTop;
    }
    this.indices = indices;
    this.currentPointIndex = startIndex;
    this.randomTrailChoosers = randomTrailChoosers;
    this.pointsVisited = new boolean[indices.length];
    this.path = new int[indices.length];
    for(int i = 0; i < path.length; i++) {
      path[i] = -1;
    }
    this.pointsVisitedCount = 0;
    this.step = -1;
    System.out.println("ant setup done");
    moveTo(currentPointIndex);
  }

  public void run() {
    while(pointsVisitedCount < indices.length) {
      int nextPointIndex = randomTrailChoosers[currentPointIndex].getDistributedRandomNumber();
      System.out.println(nextPointIndex);
      if(pointsVisited[nextPointIndex]) {
        continue;
      }
      moveTo(nextPointIndex);
    }
  }

  private void moveTo(int nextPointIndex) {
    step++;
    pointsVisited[nextPointIndex] = true;
    pointsVisitedCount++;
    path[step] = nextPointIndex;
    currentPointIndex = nextPointIndex;
  }

  public int[] getPath() {
    return path;
  }

  @Override
  public int[] call() throws Exception {
    run();
    return path;
  }

}
