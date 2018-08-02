package view;

import java.io.IOException;
import java.net.URL;

import controller.AuditTrailController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import model.AuditTrailEntry;

public class ViewManager {
	private static ViewManager singleton = null;
	private BorderPane rootNode;
	private ViewManager() {
	}

	public void setRootNode(BorderPane rootNode) {
		this.rootNode = rootNode;
	}
	public static ViewManager getInstance() {
		if(singleton == null) {
			singleton = new ViewManager();
		}

		return singleton;
	}
	
	public void changeView(String path){
		changeView(rootNode, path, this);
	}
	public static void changeView(BorderPane rootNode, String path, Object prevController){
		URL fxmlFile = prevController.getClass().getResource(path);
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		
		Parent contentView;
		try {
			contentView = loader.load();
			//get rid of reference to previous content view
			rootNode.setCenter(null);
			
			rootNode.setCenter(contentView);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setLoader(FXMLLoader loader){
		Parent contentView;
		try {
			contentView = loader.load();
			//get rid of reference to previous content view
			rootNode.setCenter(null);
			
			rootNode.setCenter(contentView);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
