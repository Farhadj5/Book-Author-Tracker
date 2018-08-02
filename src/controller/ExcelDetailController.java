package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.ExcelReport;
import book.Book;
import book.BookTableGateway;
import book.Publisher;
import book.PublisherTableGateway;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.AuthorBook;
import model.ConnectionFactory;

public class ExcelDetailController implements Initializable{
	private static Logger logger = LogManager.getLogger();
	
	@FXML private Button generateButton;
	@FXML private ComboBox<Publisher> publisherBox = new ComboBox<Publisher>();
	@FXML private TextField pathBox;
	
	private int publisher;
	private BookTableGateway gateway;
	private PublisherTableGateway pgateway;
	private ExcelReport report = new ExcelReport();
	private ObservableList<Publisher> publishers;
	private ObservableList<Book> books;
	private ArrayList<ObservableList<AuthorBook>> aBookList = new ArrayList<ObservableList<AuthorBook>>();
	
	
	public ExcelDetailController() {
		this.gateway = new BookTableGateway(ConnectionFactory.createConnection());
		this.pgateway = new PublisherTableGateway(ConnectionFactory.createConnection());
	}
	
	@FXML void onComboBoxAction() {
    	publisher = publisherBox.getValue().getId();
    	logger.info(publisherBox.getValue().getName() + " was selected (ID: " + publisherBox.getValue().getId());
    }
	
	@FXML void onGeneratePressed() {
		if (pathBox.textProperty().get() == null) {
			System.out.println("please enter a path for the file to be saved to");
			return;
		}
		System.out.println(pathBox.textProperty().get());
		try {
			books = gateway.getBooksByPublisher(publisher);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < books.size(); i++) {
			try {
				aBookList.add(gateway.getAuthorsForBook(books.get(i).getId()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			report.Generate(pgateway.getPublisherbyID(publisher).getPubName(),aBookList,pathBox.textProperty().get());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			publishers = pgateway.getPublishers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	publisherBox.setItems(publishers);
	}

}
