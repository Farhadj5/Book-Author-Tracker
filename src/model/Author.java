package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class Author {
	private int id;
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleObjectProperty<LocalDate> LDdob;
	private SimpleStringProperty dateOfBirth;
	private SimpleStringProperty gender;
	private SimpleStringProperty webSite;
	private LocalDateTime lastModified = null;
	private AuthorTableGateway gateway;
	
	public Author() {
		setId(-1);
		firstName = new SimpleStringProperty();
		lastName = new SimpleStringProperty();
		dateOfBirth = new SimpleStringProperty();
		LDdob = new SimpleObjectProperty<LocalDate>();
		gender = new SimpleStringProperty();
		webSite = new SimpleStringProperty();
		this.gateway = new AuthorTableGateway(ConnectionFactory.createConnection());;
	}
	
	
	public boolean isValidAuthorName(String authorName) {
		
		if(authorName.length() < 1 || authorName.length() > 100)
			return false;
		return true;
	}
	
	public boolean isValidGender(String Gender) {
		
		String gender = Gender.toLowerCase();
		if(gender.equals("male") || gender.equals("female") || gender.equals("unknown"))
			return true;
		return false;
	}
	
	public boolean isValidWebsite(String website) {
		if (website == null) {
			website = " ";
			return true;
		}
		if(website.length() > 100)
			return false;
		return true;
	}
	
	public boolean isValidDob(String dob) {
		try {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(dob, formatter);
		LocalDate localdatecurrent = LocalDate.now();
		if (localDate.isBefore(localdatecurrent)) {
			setLDAuthorDoB(localDate);
			return true;
		}
		}catch(Exception e) {
			throw new IllegalArgumentException("Invalid Date of Birth (yyyy-mm-dd)");
		}
		
		return false;
	}
	public void validateVars() {
		if(!isValidAuthorName(getAuthorFirstName()) || !isValidAuthorName(getAuthorLastName()))
			throw new IllegalArgumentException("Author name must be between 1 and 50 characters!");
		
		if(!isValidGender(getGender()))
			throw new IllegalArgumentException("Gender must be Male, Female, or Unknown");
		if(!isValidWebsite(getWebSite()))
			throw new IllegalArgumentException("Author name must be less than 100 characters!");
		if(!isValidDob(getDoB())) {
			throw new IllegalArgumentException("Invalid Date of Birth (yyyy-mm-dd)");
		}
	}
	
	@Override
	public String toString() {
		return "Author: " + getAuthorFirstName() + " " + getAuthorLastName();
	}

	public String getAuthorFirstName() {
		return firstName.get();
	}
	
	public String getAuthorLastName() {
		return lastName.get();
	}
	
	public LocalDate getDateofBirth() {
		return LDdob.get();
	}
	
	public String getGender() {
		return gender.get();
	}
	
	public String getWebSite() {
		return webSite.get();
	}

	public String getDoB() {
		return dateOfBirth.get();
	}
	public LocalDateTime getLastModified() {
		return this.lastModified;
	}
	
	public void setLastModified(LocalDateTime val) {
		this.lastModified = val;
	}
	public void setAuthorFirstName(String authorFName) {
		this.firstName.set(authorFName);
	}
	
	public void setAuthorLastName(String authorLName) {
		this.lastName.set(authorLName);
	}
	
	public void setAuthorDoB(String DoB) {
		this.dateOfBirth.set(DoB);
	}
	
	public void setLDAuthorDoB(LocalDate DoB) {
		this.LDdob.set(DoB);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = DoB.format(formatter);
		setAuthorDoB(formattedString);
	}
	
	public void setAuthorGender(String gender) {
		this.gender.set(gender);
	}
	
	public void setAuthorWebSite(String website) {
		this.webSite.set(website);
	}
	
	public SimpleStringProperty authorFirstNameProperty() {
		return firstName;
	}
	
	public SimpleStringProperty authorLastNameProperty() {
		return lastName;
	}
	
	public SimpleStringProperty authorDoBProperty() {
		return dateOfBirth;
	}
	
	public SimpleObjectProperty<LocalDate> authorLDDoBProperty() {
		return LDdob;
	}
	
	public SimpleStringProperty authorGenderProperty() {
		return gender;
	}
	
	public SimpleStringProperty authorWebSiteProperty() {
		return webSite;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public ObservableList<AuditTrailEntry> getAuditTrail(){
		return gateway.getAuditTrail(this.getId());
	}
}
