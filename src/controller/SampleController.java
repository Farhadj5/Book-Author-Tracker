package controller;

import java.io.IOException;

import book.Book;
import book.BookTableGateway;
import book.PublisherTableGateway;
import book.BookDetailController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Author;
import model.AuthorTableGateway;
import model.ConnectionFactory;
import view.ViewManager;

public class SampleController{
	@FXML private MenuBar menuBar;
	@FXML private MenuItem menuItemChoice1;
	@FXML private MenuItem menuItemChoice2;
	@FXML private MenuItem menuItemChoice3;
	@FXML private MenuItem menuItemChoice4;
	@FXML private MenuItem menuItemChoice5;
	@FXML private MenuItem menuItemExit;
	@FXML private BorderPane rootPane;
	
	private AuthorTableGateway gateway;
	private BookTableGateway bgateway;
	private PublisherTableGateway pgateway;
	
	@FXML private void handleMenuAction(ActionEvent event) throws IOException {
		
		if(event.getSource() == menuItemChoice1) {
	        ViewManager.getInstance().changeView("/view/AuthorListView.fxml");
		} else if (event.getSource() == menuItemChoice2) {
	    	this.gateway = new AuthorTableGateway(ConnectionFactory.createConnection());
			Author author = new Author();
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/AuthorDetailView.fxml"));
			loader.setController(new AuthorDetailController(author,gateway,true));
			GridPane rootPane = loader.load();
			Scene scene = new Scene(rootPane, 400, 200);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Detail View for " + author.getAuthorFirstName());
			stage.show();
			
		}else if (event.getSource() == menuItemChoice3) {
			ViewManager.getInstance().changeView("/view/BookListView.fxml");
		}else if (event.getSource() == menuItemChoice4) {
			this.bgateway = new BookTableGateway(ConnectionFactory.createConnection());
			this.pgateway = new PublisherTableGateway(ConnectionFactory.createConnection());
			Book book = new Book(bgateway,pgateway);
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/BookDetailView.fxml"));
			loader.setController(new BookDetailController(book,bgateway,true));
			GridPane rootPane = loader.load();
			Scene scene = new Scene(rootPane, 400, 200);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Detail View for " + book.getTitle());
			stage.show();
		}else if (event.getSource() == menuItemChoice5) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/ExcelDetailView.fxml"));
			loader.setController(new ExcelDetailController());
			GridPane rootPane = loader.load();
			Scene scene = new Scene(rootPane, 400, 200);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Detail View for Excel Spreadsheet");
			stage.show();
		}else if(event.getSource() == menuItemExit) {
			System.exit(0);
		}
	}
	
	
}
