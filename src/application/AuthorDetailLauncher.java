package application;

import java.time.LocalDate;

import controller.AuthorDetailController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Author;

public class AuthorDetailLauncher extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/AuthorDetailView.fxml"));
		
		GridPane rootPane = loader.load();
		Scene scene = new Scene(rootPane, 400, 200);
		stage.setScene(scene);
		stage.setTitle("Author Detail");
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);

	}

}