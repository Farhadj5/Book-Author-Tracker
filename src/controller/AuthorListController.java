package controller;

import model.AuthorTableGateway;
import java.sql.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Author;
import model.ConnectionFactory;
import view.ViewManager;

public class AuthorListController implements Initializable {
	private static Logger logger = LogManager.getLogger();
	private AuthorTableGateway gateway;
	
    @FXML private ListView<Author> authorList;
    @FXML private Button deleteButton;

    private ObservableList<Author> authors;
    private Author selectedAuthor;
    
    private boolean delete = false;
    
    
    
    public AuthorListController() {
    	this.gateway = new AuthorTableGateway(ConnectionFactory.createConnection());
    	ObservableList<Author> authors= null;
		try {
			authors = this.gateway.getAuthors();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	this.authors = authors;
    	
    	
    	
    }
   
    
	@FXML
	void onAuthorListClicked(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			selectedAuthor = authorList.getSelectionModel().getSelectedItem();
			if (event.getClickCount() == 2) {
				try {
					if (selectedAuthor != null) {
						// display detail of clicked author
						FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/AuthorDetailView.fxml"));
						loader.setController(new AuthorDetailController(selectedAuthor, gateway, false));
						GridPane rootPane = loader.load();
						Scene scene = new Scene(rootPane, 400, 200);
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.setTitle("Detail View for " + selectedAuthor.getAuthorFirstName());
						stage.show();

						logger.info(selectedAuthor.getAuthorFirstName() + " clicked");
					} 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
    
    @FXML void deletePressed(ActionEvent event){
    	gateway.deleteAuthor(selectedAuthor);
		ViewManager.getInstance().changeView("/view/AuthorListView.fxml");
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.authorList.setItems(authors);
		
	}

}
