package controller;


import java.net.URL;
import java.util.ResourceBundle;

import book.Book;
import book.BookTableGateway;
import book.Publisher;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Author;
import model.AuthorTableGateway;
import model.ConnectionFactory;
import view.ViewManager;

public class AddAuthorToBookController implements Initializable {
	@FXML private TextField royaltyField;
	@FXML private ComboBox<Author> authorCombo;
	private AuthorTableGateway gateway;
	private ObservableList<Author> authors;
	private Book book;
	public AddAuthorToBookController(Book book){
		gateway = new AuthorTableGateway(ConnectionFactory.createConnection());
		this.book = book;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			authors = gateway.getAuthors();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		authorCombo.setItems(authors);

	}
	
	@FXML void onAddButtonClicked(ActionEvent event){
		BookTableGateway bg = new BookTableGateway(ConnectionFactory.createConnection());
		bg.addAuthor(authorCombo.getValue().getId(), book.getId(), Integer.parseInt(royaltyField.textProperty().get()) );
		ViewManager.getInstance().changeView("BookListview.fxml");
	}

}
