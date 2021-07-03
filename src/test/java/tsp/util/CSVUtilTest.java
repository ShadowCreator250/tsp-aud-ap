package tsp.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import tsp.model.Point;

class CSVUtilTest {

  /*
   * getResource() always searches in the bin folder so it is important what we defined as output folders in the build path.
   * When using class.getResource() a relative path is resolved based from the package the class is in.
   * When using getResource of the ClassLoader a relative path is resolved based on the root folder (bin). (searches the folder tree)
   * When the path starts with a "/" it is assumed that it is a absolute path from the root to the resource.
   * substring(1) is necessary because the path is absolute and begins with "/C:/..."
   * but to load the resource it is necessary to strip the first "/".
   */
  private String buildPath(String csvFileName) {
    System.out.println(CSVUtilTest.class.getClassLoader().getResource(csvFileName).getPath());
    return CSVUtilTest.class.getClassLoader().getResource(csvFileName).getPath().substring(1);
  }

  // Passing the test name as an unused argument is not ideal but a better solution will only be available whenJUnit 5.8 comes out: https://github.com/junit-team/junit5/pull/2521
  @ParameterizedTest(name = "{index}: {1}")
  @CsvSource({
      "-120;-30, readNegativeIntsUnsuccessfullyTest",
      "20.5;20.75, readDoublesUnsuccessfullyTest",
      "hi;ha, readCharactersUnsuccessfullyTest",
      "20;, readPositiveIntsWithEmptyRightColumnUnsuccessfullyTest",
      ";20, readPositiveIntsWithEmptyLeftColumnUnsuccessfullyTest",
      "20, readPositiveIntsWithEmptyRightColumnAndWithoutSemicolonUnsuccessfullyTest"
  })
  void invalidCsvWithDefaultDelimiterTest(String csvString, String testName) {
    assertThrows(CSVFormatException.class, () -> CSVUtil.readPointFromString(csvString));
  }

  @ParameterizedTest(name = "{index}: {1}")
  @CsvSource({
      "100;100, readPositiveIntsSuccessfullyTest",
      "20;20;20, readPositiveIntsWithAdditionalColumnSuccessfullyTest",
      "'\n   235  \t;   976   \t   ', readPositiveIntsWithSpaceSuccessfullyTest"
  })
  void validCsvWithDefaultDelimiterTest(String csvString, String testName) {
    assertDoesNotThrow(() -> CSVUtil.readPointFromString(csvString));
  }

  @Test
  void readPositiveIntsFromFileSuccessfullyTest() {
    String path = buildPath("points-tst-set-positive-ints.csv");

    List<Point> points = assertDoesNotThrow(() -> CSVUtil.readPointsFromFile(path));
    assertEquals(new Point(0, 0), points.get(0));
    assertEquals(new Point(20, 20), points.get(1));
    assertEquals(new Point(100, 100), points.get(2));
  }

}
