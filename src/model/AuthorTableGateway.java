package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuthorTableGateway {
	private Connection conn;
	
	public AuthorTableGateway(Connection conn){
		this.conn = conn;
	}
	
	public ObservableList<Author> getAuthors() throws Exception{
		ObservableList<Author> authors = FXCollections.observableArrayList();
		
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT author.id as auth_id, author.first_name, author.last_name, author.dob, author.gender, author.web_site, author.last_modified FROM author");
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				Author author = new Author();
				author.setLDAuthorDoB(rs.getDate("dob").toLocalDate());
				author.setAuthorFirstName(rs.getString("first_name"));
				author.setAuthorLastName(rs.getString("last_name"));
				author.setAuthorGender(rs.getString("gender"));
				author.setId(rs.getInt("auth_id"));
				author.setAuthorWebSite(rs.getString("web_site"));
				author.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
				authors.add(author);
				
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
		
		return authors;
	}
	
	public boolean updateAuthor (Author author) throws AppException{
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT last_modified From author WHERE id = " + author.getId());
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				if (author.getLastModified().isBefore(rs.getTimestamp("last_modified").toLocalDateTime())) {
					System.out.println("author: " + author.getLastModified() + "\nrs: " + rs.getTimestamp("last_modified"));
					return false;
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			try {
				if(st != null)
					st.close();
				st = null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		try {
			author.validateVars();
			st = conn.prepareStatement("update wnc992.author set first_name = ?, last_name = ?, dob = ?, gender = ?, web_site = ? where id = ?");
			st.setString(1, author.getAuthorFirstName());
			st.setString(2, author.getAuthorLastName());
			st.setDate(3,Date.valueOf(author.getDateofBirth()));
			st.setString(4, author.getGender());
			st.setString(5, author.getWebSite());
			st.setInt(6, author.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
				st = null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}

		try {
			st = conn.prepareStatement("SELECT last_modified From author WHERE id = " + author.getId());
			ResultSet rs = st.executeQuery();
			rs.next();
				author.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
				//System.out.println("author final: " + author.getLastModified());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			try {
				if(st != null)
					st.close();
				st = null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		return true;
	}
	
	public void addAuthor(Author author) throws AppException{
		PreparedStatement st = null;
		try {
			author.validateVars();
			st = conn.prepareStatement("INSERT INTO wnc992.author (first_name, last_name, dob, gender, web_site) VALUES (?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			st.setString(1, author.getAuthorFirstName());
			st.setString(2, author.getAuthorLastName());
			st.setDate(3,Date.valueOf(author.getDateofBirth()));
			st.setString(4, author.getGender());
			st.setString(5, author.getWebSite());
			st.executeUpdate();	
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			author.setId(rs.getInt(1));
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}

	}
	
	public void deleteAuthor(Author author) throws AppException{
		PreparedStatement st = null;
		try {
			author.validateVars();
			st = conn.prepareStatement("DELETE FROM wnc992.author WHERE id = ?");
			st.setInt(1, author.getId());
			st.executeUpdate();	
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}

	}
	public ObservableList<AuditTrailEntry> getAuditTrail(int id){
		ObservableList<AuditTrailEntry> audits = FXCollections.observableArrayList();
		//GETAUDITS
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT id,date_added,entry_msg FROM author_audit_trail WHERE author_id = ?");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				AuditTrailEntry audit = new AuditTrailEntry();
				audit.setId(rs.getInt("id"));
				audit.setDateAdded(rs.getDate("date_added"));
				audit.setMessage(rs.getString("entry_msg"));
				audits.add(audit);		
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
		
		return audits;
	}
}
