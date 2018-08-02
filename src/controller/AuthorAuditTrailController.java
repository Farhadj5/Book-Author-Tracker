package controller;

import java.net.URL;
import java.util.ResourceBundle;

import book.Book;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import model.AuditTrailEntry;
import model.Author;
import view.ViewManager;

public class AuthorAuditTrailController implements Initializable {

	@FXML private ListView<AuditTrailEntry> auditList;
    @FXML private Button backButton;
    private ObservableList<AuditTrailEntry> audits;
	private Author author;
	
    public AuthorAuditTrailController(Author author){
    	this.author = author;
    	audits= null;
    }
    public void setAudits(ObservableList<AuditTrailEntry> audits){
    	this.audits = audits;
    }
	public void backPressed(){
		ViewManager.getInstance().changeView("/view/AuthorListView.fxml");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		audits = author.getAuditTrail();
		this.auditList.setItems(audits);
		
	}
}
