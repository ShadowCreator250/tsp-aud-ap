package tsp.view;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.LineChart.SortingPolicy;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;

import tsp.TspMainApp;
import tsp.model.Point;
import tsp.solver.AntColonyOptimizationSolver;
import tsp.util.CSVFormatException;
import tsp.util.CSVUtil;

public class MainViewController {

  private static final String FILE_CHOOSER_LABEL = "FileChooser";

  private TspMainApp main;

  final NumberAxis xaxis = new NumberAxis();

  final NumberAxis yaxis = new NumberAxis();

  @FXML
  private LineChart<Number, Number> chart = new LineChart<Number, Number>(xaxis, yaxis);
  @FXML
  private ChoiceBox<String> loadActionSelect;

  private List<Point> points;

  @FXML
  private void initialize() {
    loadActionSelect.getItems().add(FILE_CHOOSER_LABEL);
    loadActionSelect.getItems().addAll(getCSVFilesUnderBinMain());
    loadActionSelect.getSelectionModel().select(0);

    chart.getData()
         .add(new Series<>());
    chart.setAxisSortingPolicy(SortingPolicy.NONE);
  }

  public void setMain(TspMainApp tspMainApp) {
    this.main = tspMainApp;
  }

  @FXML
  private void handleLoadButton() {
    String selectedItem = loadActionSelect.getSelectionModel().getSelectedItem();
    if(selectedItem != null) {
      if(selectedItem.equals(FILE_CHOOSER_LABEL)) {
        showFileChooser();
      } else {
        File file = new File(MainViewController.class.getClassLoader().getResource(selectedItem).getPath()).getAbsoluteFile();
        readPointsFromFileWithErrorHandling(file);
      }
    }
  }

  public String[] getCSVFilesUnderBinMain() {
    File absolutePathToMainClassInBin = new File(
        TspMainApp.class.getResource(TspMainApp.class.getSimpleName() + ".class").getPath()).getAbsoluteFile();
    String absolutePathToPackageOfMain = absolutePathToMainClassInBin.getParent();
    File binMainDir = Path.of(absolutePathToPackageOfMain, "./../").normalize().toFile();
    if(binMainDir.isDirectory()) {
      FilenameFilter csvFileFilter = (dir, name) -> name.toLowerCase().endsWith(".csv");
      return binMainDir.list(csvFileFilter);
    }
    return new String[0];
  }

  private void showFileChooser() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser.setInitialDirectory(new File("").getAbsoluteFile());
    File file = fileChooser.showOpenDialog(main.getPrimaryStage());
    readPointsFromFileWithErrorHandling(file);
  }

  private void readPointsFromFileWithErrorHandling(File file) {
    if(file != null) {
      try {
        points = (CSVUtil.readPointsFromFile(file.getAbsolutePath()));
        addPointsToChart(points);
      } catch(IOException e) {
        main.showAlert(AlertType.ERROR, main.getPrimaryStage(), "Error reading file", "An Error occured while reading a file.",
            e.getClass().getSimpleName() + ": " + e.getMessage());
      } catch(CSVFormatException e) {
        main.showAlert(AlertType.ERROR, main.getPrimaryStage(), "CSV Format Error", "There is an error with the CSV format.",
            e.getClass().getSimpleName() + ": " + e.getMessage());
      }
    }
  }

  private void addPointsToChart(List<Point> points) {
    chart.getData().get(0).getData().clear();
    points.forEach((Point c) -> chart.getData()
                                     .get(0).getData()
                                     .add(new Data<>(c.getX(), c.getY())));
  }

  @FXML
  private void handleSolveButton() {
    AntColonyOptimizationSolver solver = new AntColonyOptimizationSolver(points);

    while(solver.timesBestTourDstStaysSame < solver.TIMES_BEST_TOUR_DISTANCE_MUST_STAY_SAME_UNTIL_TERMINATION) {
      addPointsToChart(solver.solvePartial());
    }
  }

}
