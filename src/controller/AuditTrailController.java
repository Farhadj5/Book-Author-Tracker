package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import book.Book;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.AuditTrailEntry;
import model.ConnectionFactory;
import view.ViewManager;

public class AuditTrailController implements Initializable{

	@FXML private ListView<AuditTrailEntry> auditList;
    @FXML private Button backButton;
    private ObservableList<AuditTrailEntry> audits;
	private Book book;
	
    public AuditTrailController(Book book){
    	this.book = book;
    	audits= null;
    }
    public void setAudits(ObservableList<AuditTrailEntry> audits){
    	this.audits = audits;
    }
	public void backPressed(){
		ViewManager.getInstance().changeView("/view/BookListView.fxml");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		audits = book.getAuditTrail();
		this.auditList.setItems(audits);
		
	}

}
