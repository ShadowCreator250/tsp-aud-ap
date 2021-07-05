package tsp.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.FileChooser;
import tsp.TspMainApp;
import tsp.util.CSVUtil;

public class MainViewController {

  private TspMainApp main;

  @FXML
  private ScatterChart<Number, Number> chart;

  //private Series<Number, Number> data;

  @FXML
  private void initialize() {
    chart.getData()
         .add(new Series<Number, Number>());
  }

  @FXML
  private void handleLoadButton() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    File file = fileChooser.showOpenDialog(main.getPrimaryStage());
    CSVUtil.readPointsFromFile(file.getAbsolutePath())
           .forEach(c -> {
             chart.getData()
                  .get(0)
                  .getData()
                  .add(new Data<Number, Number>(c.getX(), c.getY()));
           });
  }

  public void setMain(TspMainApp tspMainApp) {
    this.main = tspMainApp;

  }

}
