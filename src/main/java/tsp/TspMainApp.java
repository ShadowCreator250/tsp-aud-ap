package tsp;

import javafx.application.Application;
import javafx.stage.Stage;

public class TspMainApp extends Application {

  private Stage primaryStage;

  public TspMainApp() {
    // TODO Auto-generated constructor stub
  }

	@Override
	public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("TSP App");

    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
