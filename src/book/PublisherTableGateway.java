package book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PublisherTableGateway {
private Connection conn;
	
	public PublisherTableGateway(Connection conn){
		this.conn = conn;
	}
	
	public ObservableList<Publisher> getPublishers() throws Exception{
		ObservableList<Publisher> publishers = FXCollections.observableArrayList();
		
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT publisher.id, publisher.publisher_name FROM publisher");
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				Publisher publisher = new Publisher();
				publisher.setId(rs.getInt("id"));
				publisher.setPName(rs.getString("publisher_name"));
				publishers.add(publisher);		
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				if(statement != null)
					statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return publishers;
	}
	
	public Publisher getPublisherbyID(int id) throws Exception{
		Publisher publisher = new Publisher();
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT publisher.id, publisher.publisher_name FROM publisher WHERE publisher.id = ?");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				publisher.setId(rs.getInt("id"));
				publisher.setPName(rs.getString("publisher_name"));
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				if(statement != null)
					statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return publisher;
	}

}
