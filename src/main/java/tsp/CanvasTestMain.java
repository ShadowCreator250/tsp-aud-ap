package tsp;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CanvasTestMain extends Application {

  public CanvasTestMain() {
    // TODO Auto-generated constructor stub
  }

  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;

    test2();

    primaryStage.show();
  }

  private void test2() {
    primaryStage.setTitle("Test2");

    canvasTest();

    primaryStage.show();
  }

  private void canvasTest() {
    primaryStage.setTitle("Drawing Operations Test");
    Group root = new Group();
    Canvas canvas = new Canvas(300, 250);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    drawShapes(gc);
    root.getChildren().add(canvas);
    primaryStage.setScene(new Scene(root));
  }

  private void drawShapes(GraphicsContext gc) {
    gc.setFill(Color.GREEN);
    gc.setStroke(Color.BLUE);
    gc.setLineWidth(5);
    gc.strokeLine(40, 10, 10, 40);
    gc.fillOval(10, 60, 30, 30);
    gc.strokeOval(60, 60, 30, 30);
    gc.fillRoundRect(110, 60, 30, 30, 10, 10);
    gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
    gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
    gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
    gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
    gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
    gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
    gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
    gc.fillPolygon(new double[] { 10, 40, 10, 40 },
        new double[] { 210, 210, 240, 240 }, 4);
    gc.strokePolygon(new double[] { 60, 90, 60, 90 },
        new double[] { 210, 210, 240, 240 }, 4);
    gc.strokePolyline(new double[] { 110, 140, 110, 140 },
        new double[] { 210, 210, 240, 240 }, 4);
  }

  public void showAlert(AlertType alertType, Window owner, String title, String header, String context) {
    Alert alert = new Alert(alertType);
    alert.initOwner(owner);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(context);
    alert.showAndWait();
  }

  public static void main(String[] args) {
    launch(args);
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }
}
