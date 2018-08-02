package book;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.ConnectionFactory;
import view.ViewManager;

public class bookListController implements Initializable{
	private static Logger logger = LogManager.getLogger();
	private BookTableGateway gateway;
	
    @FXML private ListView<Book> bookList;
    @FXML private TextField searchBar;
    @FXML private Button deleteButton;
    @FXML private Button nextPage;
    @FXML private Button prevPage;
    @FXML private TextField bookNum;
    @FXML private TextField bookTotal;

    private ObservableList<Book> books;
    
    private Book selectedBook;
    private int page;
    private int totalBooks = 0;
    private int bookStart = 0;
    private int bookEnd = 0;
    private boolean sBook = false;
    private ObservableList<Book> booksSearch = null;
    
    
    public bookListController() {
    	page = 1;
    	this.gateway = new BookTableGateway(ConnectionFactory.createConnection());
    	ObservableList<Book> books= null;
		try {
			books = this.gateway.getBooks(page);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	this.books = books;	
    }
   
    
	@FXML
	void onBookListClicked(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			selectedBook = bookList.getSelectionModel().getSelectedItem();
			if (event.getClickCount() == 2) {
				try {

					if (selectedBook != null) {
						// display detail of clicked book
						FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/BookDetailView.fxml"));
						loader.setController(new BookDetailController(selectedBook, gateway, false));
						GridPane rootPane = loader.load();
						Scene scene = new Scene(rootPane, 400, 200);
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.setTitle("Detail View for " + selectedBook.getTitle());
						stage.show();

						logger.info(selectedBook.getTitle() + " clicked");
					} 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
    
    @FXML void onSearchClick(ActionEvent event) {
    	
    	ObservableList<Book> booksSearch = null;
    	if (searchBar.getText().isEmpty()) {
    		this.bookList.setItems(books);
    		sBook = false;
    		return;
    	}
    		
		try {
			page = 1;
			sBook = true;
			booksSearch = this.gateway.searchBooks(searchBar.getText(),page);
			updateText();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		this.bookList.setItems(booksSearch);
		//ViewManager.getInstance().changeView("/view/BookListView.fxml");
    }
    @FXML void deletePressed(ActionEvent event){
    	gateway.deleteBook(selectedBook);
		ViewManager.getInstance().changeView("/view/BookListView.fxml");
    }
    
    @FXML void prevPressed(ActionEvent event) {
    	if (page !=1 && sBook) {
    		System.out.println("previous clicked. Loading");
    		page = page - 1;
    		try {
    			booksSearch = this.gateway.searchBooks(searchBar.getText(),page);
    		} catch (Exception e) {
    			
    			e.printStackTrace();
    		}
    		this.bookList.setItems(booksSearch);
    		this.updateText();
    	}else if (page != 1) {
    		System.out.println("previous clicked. Loading");
    		page = page - 1;
    		try {
    			books = this.gateway.getBooks(page);
    		} catch (Exception e) {
    			
    			e.printStackTrace();
    		}
    		this.bookList.setItems(books);
    		this.updateText();
    	}else {
    		System.out.println("No previous available");
    	}
    }
    
    @FXML void nextPressed(ActionEvent event) {
    	if (!(books.size() < 50) && sBook) {
    		System.out.println("Next clicked. Loading");
    		page = page + 1;
    		try {
    			booksSearch = this.gateway.searchBooks(searchBar.getText(),page);
    		} catch (Exception e) {
    			
    			e.printStackTrace();
    		}
    		this.bookList.setItems(booksSearch);
    		this.updateText();
    	}else if (!(books.size() < 50)) {
    		System.out.println("Next clicked. Loading");
    		page = page + 1;
    		try {
    			System.out.println("page: " + page);
    			books = this.gateway.getBooks(page);
    		} catch (Exception e) {
    			
    			e.printStackTrace();
    		}
    		this.bookList.setItems(books);
    		this.updateText();
    	}else {
    		System.out.println("Next unavailable");
    	}
    }
    
    public void updateText(){
    	bookStart = (page * 50) - 50;
    	bookEnd = bookStart + 49;
    	try {
			totalBooks = gateway.getTotalBooks();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//bookNum.setText("Fetched records " + bookStart + " to " + bookEnd);
    	//bookTotal.setText("out of " + totalBooks);
    	bookNum.textProperty().set("Fetched records " + bookStart + " to " + bookEnd);
    	bookTotal.textProperty().set("out of " + totalBooks);
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.bookList.setItems(books);
		this.updateText();
		
	}
}
