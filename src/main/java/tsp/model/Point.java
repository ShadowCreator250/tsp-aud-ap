package tsp.model;

public class Point {

  private int x;
  private int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(!(obj instanceof Point)) {
      return false;
    }
    Point other = (Point) obj;
    if(x != other.x) {
      return false;
    }
    if(y != other.y) {
      return false;
    }
    return true;
  }

}
