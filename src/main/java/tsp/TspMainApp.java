package tsp;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(TspMainApp.class.getResource("view/MainView.fxml"));

    BorderPane rootElement;
    try {
      rootElement = loader.load();
      Scene scene = new Scene(rootElement);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch(IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
