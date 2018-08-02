package book;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.AddAuthorToBookController;
import controller.AuditTrailController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Author;
import model.AuthorBook;
import model.AuthorTableGateway;
import model.ConnectionFactory;
import view.ViewManager;

public class BookDetailController implements Initializable {
private static Logger logger = LogManager.getLogger();
	
    @FXML private TextField title;
    @FXML private TextField summary;
    @FXML private TextField yearPublished;
    @FXML private TextField isbn;
    @FXML private Button saveBook;
    @FXML private Button auditButton;
    @FXML private ListView<AuthorBook> authorList;
    //TODO ADD NEW BUTTONS AND AUTHORS ROYALTIES
    private ObservableList<AuthorBook> authorBook;
    @FXML private ComboBox<Publisher> publisherbox = new ComboBox<Publisher>();

    private Book book;
    private boolean isNew;
    private boolean delete = false;
    
    private BookTableGateway gateway;
    private PublisherTableGateway pubgateway;
    private ObservableList<Publisher> publishers;
    
    public BookDetailController(Book book, BookTableGateway gateway, boolean isNew) {
    	this.isNew = isNew;
    	this.gateway = gateway;
    	this.book = book;
    	
    	pubgateway = new PublisherTableGateway(ConnectionFactory.createConnection());
    	try {
			authorBook = this.book.getAuthors();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    }
    
    @FXML void onComboBoxAction() {
    	book.setPublisher(publisherbox.getValue().getId());
    	logger.info(publisherbox.getValue().getName() + " was selected (ID: " + publisherbox.getValue().getId());
    }
    @FXML void onSaveBookClicked(ActionEvent event) {
    	
    	if (isNew == true) {
    		gateway.addBook(book);
    		isNew = false;
    	}else {
    		gateway.updateBook(book);
    	}
    	logger.info("The book " + book.getTitle() + " has been saved");
    	Stage stage = (Stage) saveBook.getScene().getWindow();
	    stage.close();
    	
    }
    @FXML void onAuditButtonClicked(ActionEvent event){
    	if(isNew == true){
    		Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Book Not Saved!");
            alert.setHeaderText("Error:");
            alert.setContentText("Please save book before checking audit trail");
     
            alert.showAndWait();
    	}else{
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuditTrailView.fxml"));
    		AuditTrailController controller = new AuditTrailController(book);
    		loader.setController(controller);
    		ViewManager.getInstance().setLoader(loader);
    		Stage stage = (Stage) auditButton.getScene().getWindow();
    		stage.close();
    	}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title.textProperty().bindBidirectional(book.titleProperty());
		summary.textProperty().bindBidirectional(book.summaryProperty());
		yearPublished.textProperty().bindBidirectional(book.yearPublishedProperty());
		isbn.textProperty().bindBidirectional(book.isbnProperty());
		//TODO
		logger.info(authorBook);
		authorList.setItems(authorBook);
		try {
			publishers = pubgateway.getPublishers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	publisherbox.setItems(publishers);
    	try {
			publisherbox.setValue(book.getPublisher());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML void onAddAuthorButtonClicked(ActionEvent event){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddAuthorView.fxml"));
		AddAuthorToBookController controller = new AddAuthorToBookController(book);
		loader.setController(controller);
		ViewManager.getInstance().setLoader(loader);
		Stage stage = (Stage) auditButton.getScene().getWindow();
		stage.close();
	}
	@FXML void onDeleteAuthorButtonClicked(ActionEvent event){
		delete = true;
	}
	@FXML void onAuthorListClicked(MouseEvent event){
		Author author = authorList.getSelectionModel().getSelectedItem().getAuthor();
		Book book = authorList.getSelectionModel().getSelectedItem().getBook();
		if (delete == true) {
			gateway.deleteAuthor(author.getId(), book.getId());
			Stage stage = (Stage) auditButton.getScene().getWindow();
			stage.close();
		}
	}
}
