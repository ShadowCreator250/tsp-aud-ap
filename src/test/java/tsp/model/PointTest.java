package tsp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class PointTest {

  @Test
  void pointCoordsAreSetCorrectlyTest() {
    Point p = new Point(21, 212);

    assertEquals(21, p.getX());
    assertEquals(212, p.getY());
  }

  @Test
  void twoPointsWithSameCoordsAreEqualTest() {
    Point p1 = new Point(21, 212);
    Point p2 = new Point(21, 212);

    assertEquals(p1, p2);
  }

  @Test
  void twoPointsWithDifferentCoordsAreDifferent() {
    Point p1 = new Point(2, 12);
    Point p2 = new Point(1, 21);

    assertNotEquals(p1, p2);
  }

  @Test
  void twoPointsWithDifferentYCoordsAreDifferent() {
    Point p1 = new Point(21, 21);
    Point p2 = new Point(21, 212);

    assertNotEquals(p1, p2);
  }

  @Test
  void twoPointsWithDifferentXCoordsAreDifferent() {
    Point p1 = new Point(20, 212);
    Point p2 = new Point(21, 212);

    assertNotEquals(p1, p2);
  }

}
