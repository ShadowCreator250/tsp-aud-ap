package tsp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Helper class to provide a random Integers out of a set where each Integer has it's own probability.
 * <p>
 * Source: <a href="https://stackoverflow.com/questions/20327958/random-number-with-probabilities">
 * https://stackoverflow.com/questions/20327958/random-number-with-probabilities</a>
 * but I made some small edits.
 * <p>
 * It's not the most efficient algorithm but it should do for now.
 * <p>
 * Usage:
 * <pre>
 * <code>
 * DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
 * drng.addNumber(1, 0.3d); // Adds the numerical value 1 with a probability of 0.3 (30%)
 * // [...] Add more values
 * 
 * int random = drng.getDistributedRandomNumber(); // Generate a random number
 * </code>
 * </pre>
 * 
 * @author trylimits
 */
public class DistributedRandomNumberGenerator {

  private static final Random RANDOM = new Random();
  private Map<Integer, Double> distribution;
  private double distSum;

  public DistributedRandomNumberGenerator() {
    distribution = new HashMap<>();
  }

  /**
   * Add the integer <code>value</code> to the set of possible outcomes and stores its <code>probability</code> with it.
   * <p>
   * If the value is already in the set, its probability is updated to the passed value.
   * 
   * @param value
   * @param probability
   */
  public void addNumber(int value, double probability) {
    if(this.getDistribution().containsKey(value)) {
      distSum -= this.getDistribution().get(value);
    }
    this.getDistribution().put(value, probability);
    distSum += probability;
  }

  public int getDistributedRandomNumber() {
    double rand = RANDOM.nextDouble();
    double ratio = 1.0 / distSum;
    double tempDist = 0;
    for(Entry<Integer, Double> distr : getDistribution().entrySet()) {
      tempDist += distr.getValue();
      if(rand / ratio <= tempDist) {
        return distr.getKey();
      }
    }
    return -1;
  }

  public Map<Integer, Double> getDistribution() {
    return distribution;
  }

  public double getDistSum() {
    return distSum;
  }

  /**
   * testing its functionality
   * 
   * @param args
   */
  public static void main(String[] args) {
    DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
    drng.addNumber(1, 0.2d);
    drng.addNumber(2, 0.3d);
    drng.addNumber(3, 0.0d);

    int testCount = 1000000;

    HashMap<Integer, Double> test = new HashMap<>();

    for(int i = 0; i < testCount; i++) {
      int random = drng.getDistributedRandomNumber();
      test.put(random, (test.get(random) == null) ? (1d / testCount) : test.get(random) + 1d / testCount);
    }

    System.out.println(test.toString());
  }

}
