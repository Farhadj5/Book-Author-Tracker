package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Author;
import model.AuthorTableGateway;
import view.ViewManager;

public class AuthorDetailController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	
    @FXML private TextField FirstName;
    @FXML private TextField LastName;
    @FXML private TextField DoB;
    @FXML private TextField Gender;
    @FXML private TextField WebSite;
    @FXML private Button saveAuthor;
    @FXML private Button auditButton;
    private Author author;
    private boolean isNew;
    private boolean isModified;
    
    private AuthorTableGateway gateway;
    
    public AuthorDetailController() {
    	
    }
    
    public AuthorDetailController(Author author, AuthorTableGateway gateway, boolean isNew) {
    	this();
    	this.isNew = isNew;
    	this.gateway = gateway;
    	this.author = author;
    }
    
    @FXML void onSaveAuthorClicked(ActionEvent event) {
    	
    	if (isNew == true) {
    		gateway.addAuthor(author);
    	}else {
    		isModified = gateway.updateAuthor(author);
    	}
    	if (!isModified && !isNew) {
    		 logger.info("The author you are trying to update has since been modified. please go back to the author list and retrieve a fresh copy of the Author to make changes" );
    	}else {
    		logger.info("The author " + author.getAuthorFirstName() + " " + author.getAuthorLastName() + " has been saved");
    	}
    	isNew = false;
    	
    }
    @FXML void onAuditButtonClicked(ActionEvent event){
    	if(isNew == true){
    		Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Author Not Saved!");
            alert.setHeaderText("Error:");
            alert.setContentText("Please save author before checking audit trail");
     
            alert.showAndWait();
    	}else{
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuditTrailView.fxml"));
    		AuthorAuditTrailController controller = new AuthorAuditTrailController(author);
    		loader.setController(controller);
    		ViewManager.getInstance().setLoader(loader);
    		Stage stage = (Stage) auditButton.getScene().getWindow();
    		stage.close();
    	}
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FirstName.textProperty().bindBidirectional(author.authorFirstNameProperty());
		LastName.textProperty().bindBidirectional(author.authorLastNameProperty());
		DoB.textProperty().bindBidirectional(author.authorDoBProperty());
		Gender.textProperty().bindBidirectional(author.authorGenderProperty());
		WebSite.textProperty().bindBidirectional(author.authorWebSiteProperty());
	}

}

