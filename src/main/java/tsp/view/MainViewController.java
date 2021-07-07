package tsp.view;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import tsp.TspMainApp;
import tsp.model.Point;
import tsp.util.CSVFormatException;
import tsp.util.CSVUtil;

public class MainViewController {

  private TspMainApp main;

  @FXML
  private ScatterChart<Number, Number> chart;

  //private Series<Number, Number> data;

  @FXML
  private void initialize() {
    chart.getData()
         .add(new Series<>());
  }

  public void setMain(TspMainApp tspMainApp) {
    this.main = tspMainApp;
  }

  @FXML
  private void handleLoadButton() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    File file = fileChooser.showOpenDialog(main.getPrimaryStage());
    try {
      CSVUtil.readPointsFromFile(file.getAbsolutePath())
             .forEach((Point c) -> chart.getData()
                                        .get(0)
                                        .getData()
                                        .add(new Data<>(c.getX(), c.getY())));
    } catch(IOException e) {
      main.showAlert(AlertType.ERROR, main.getPrimaryStage(), "Error reading file", "An Error occured while reading a file.",
          e.getClass().getSimpleName() + ": " + e.getMessage());
    } catch(CSVFormatException e) {
      main.showAlert(AlertType.ERROR, main.getPrimaryStage(), "CSV Format Error", "There is an error with the CSV format.",
          e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }

}
