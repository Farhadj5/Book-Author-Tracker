package book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import model.AuditTrailEntry;
import model.AuthorBook;
import model.ConnectionFactory;

public class Book {
	private int id;
	private SimpleStringProperty title;
	private SimpleStringProperty summary;
	private SimpleObjectProperty<Publisher> publisher;
	private SimpleStringProperty isbn;
	private SimpleIntegerProperty yearPublished;
	private SimpleObjectProperty<LocalDate> dateAdded;
	private SimpleStringProperty yearPublishedStr;
	private SimpleStringProperty dateAddedStr;
	private PublisherTableGateway gateway;
	private BookTableGateway bookGateway;
	private static Logger logger = LogManager.getLogger();
	public Book(BookTableGateway bgateway, PublisherTableGateway pgateway) {
		title = new SimpleStringProperty();
		summary = new SimpleStringProperty();
		publisher = new SimpleObjectProperty<Publisher>();
		isbn = new SimpleStringProperty();
		yearPublished = new SimpleIntegerProperty();
		dateAdded = new SimpleObjectProperty<LocalDate>();
		yearPublishedStr = new SimpleStringProperty();
		dateAddedStr = new SimpleStringProperty();
		this.gateway = pgateway;
		this.bookGateway = bgateway;
		
		try {
			publisher.set(gateway.getPublisherbyID(1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void validateVars() {
		if(!isValidTitle(getTitle()))
			throw new IllegalArgumentException("Title must be between 1 and 255 characters!");
		
		if(!isValidSummary(getSummary()))
			throw new IllegalArgumentException("Summary must be less than 65536 characters!");
		if(!isValidYearPublished())
			throw new IllegalArgumentException("Date must not be after the current year (2018)");
		if(!isValidisbn(getisbn())) {
			throw new IllegalArgumentException("isbn cannot be more than 13 characters");
		}
	}
	
	public Boolean isValidTitle(String title) {
		if (title.length() < 1 || title.length() > 255) {
			return false;
		}
		return true;
	}
	
	public Boolean isValidSummary(String summary) {
		if (summary.length() < 65536) {
			return true;
		}
		return false;
	}
	
	public Boolean isValidisbn(String isbn) {
		if (isbn.length() > 13) {
			return false;
		}
		return true;
	}
	public Boolean isValidYearPublished() {
		setYearPublished(Integer.parseInt(yearPublishedStr.get()));
		if (this.yearPublished.get() > 2018) {
			return false;
		}
		return true;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title.set(title);
	}
	
	public void setSummary(String summary) {
		this.summary.set(summary);
	}
	
	public void setPublisher(int pID) {
		
		try {
			this.publisher.set(this.gateway.getPublisherbyID(pID));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setISBN(String isbn) {
		this.isbn.set(isbn);
	}
	
	public void setYearPublished(int yrPub) {
		this.yearPublished.set(yrPub);
		this.yearPublishedStr.set(Integer.toString(yrPub));
	}
	
	public void setDate(LocalDate dateAdded) {
		this.dateAdded.set(dateAdded);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = dateAdded.format(formatter);
		dateAddedStr.set(formattedString);
	}
	
	public ObservableList<AuthorBook> getAuthors(){
		try {
			
			return bookGateway.getAuthorsForBook(this.id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public String getTitle() {
		return this.title.get();
	}
	
	public String getSummary() {
		return this.summary.get();
	}
	
	public int getyearpublished() {
		return this.yearPublished.get();
	}
	
	public String getisbn() {
		return this.isbn.get();
	}
	
	public Publisher getPublisher() {
		return publisher.get();
	}
	
	public int getId() {
		return this.id;
	}
	
	
	public SimpleStringProperty titleProperty() {
		return title;
	}
	
	public SimpleStringProperty summaryProperty() {
		return summary;
	}
	
	public SimpleStringProperty isbnProperty() {
		return isbn;
	}
	
	public SimpleStringProperty yearPublishedProperty() {
		return yearPublishedStr;
	}
	
	public SimpleStringProperty dateAddedProperty(){	
		return dateAddedStr;
	}
	
	@Override
	public String toString() {
		return "Book: " + getTitle();
	}
	public ObservableList<AuditTrailEntry> getAuditTrail(){
		return bookGateway.getAuditTrail(this.getId());
	}
}
