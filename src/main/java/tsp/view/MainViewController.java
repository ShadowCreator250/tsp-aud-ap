package tsp.view;

import java.io.File;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.FileChooser;
import tsp.TspMainApp;

public class MainViewController {

  private TspMainApp main;

  @FXML
  private ScatterChart<Number, Number> chart;

  private ObservableList<Series<Number, Number>> data;

  @FXML
  private void initialize() {

  }
  
  @FXML
  private void handleLoadButton() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    File file = fileChooser.showOpenDialog(main.getPrimaryStage());

  }

  public void setMain(TspMainApp tspMainApp) {
    this.main = tspMainApp;

  }

}
