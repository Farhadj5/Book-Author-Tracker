package book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AppException;
import model.AuditTrailEntry;
import model.Author;
import model.AuthorBook;
import model.ConnectionFactory;



public class BookTableGateway {
private Connection conn;
private PublisherTableGateway pgateway;
private static Logger logger = LogManager.getLogger();

	public BookTableGateway(Connection conn){
		this.conn = conn;
		this.pgateway = new PublisherTableGateway(ConnectionFactory.createConnection());
	}
	
	public ObservableList<Book> getBooks(int page) throws Exception{
		int i = 0;
		int firstBook = (page * 50) - 50;
		ObservableList<Book> books = FXCollections.observableArrayList();
		
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT book.date_added, book.id, book.isbn, book.publisher_id, book.summary, book.title, book.year_published FROM book");
			ResultSet rs = statement.executeQuery();
			while (i < firstBook) {
				rs.next();
				i++;
			}
			while(rs.next() && i < (firstBook + 50) && i >= firstBook){
				Book book = new Book(this,pgateway);
				book.setDate(rs.getDate("date_added").toLocalDate());
				book.setId(rs.getInt("id"));
				book.setISBN(rs.getString("isbn"));
				book.setPublisher(rs.getInt("publisher_id"));
				book.setSummary(rs.getString("summary"));
				book.setTitle(rs.getString("title"));
				book.setYearPublished(rs.getInt("year_published"));
				books.add(book);	
				i++;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				if(statement != null) {
					statement.close();
					System.out.println("closed");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return books;
	}
	
	public ObservableList<Book> searchBooks(String searchParameter, int page) throws Exception{
		ObservableList<Book> books = FXCollections.observableArrayList();
		int i = 0;
		int firstBook = (page * 50) - 50;
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT book.date_added, book.id, book.isbn, book.publisher_id, book.summary, book.title, book.year_published FROM book WHERE book.title LIKE '%" + searchParameter + "%'");
			ResultSet rs = statement.executeQuery();
			while (i < firstBook) {
				rs.next();
				i++;
			}
			while(rs.next() && i < (firstBook + 50) && i >= firstBook){
				Book book = new Book(this, pgateway);
				book.setDate(rs.getDate("date_added").toLocalDate());
				book.setId(rs.getInt("id"));
				book.setISBN(rs.getString("isbn"));
				book.setPublisher(rs.getInt("publisher_id"));
				book.setSummary(rs.getString("summary"));
				book.setTitle(rs.getString("title"));
				book.setYearPublished(rs.getInt("year_published"));
				books.add(book);	
				i++;
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
		
		return books;
	}
	
	public void updateBook (Book book) throws AppException{
		PreparedStatement st = null;
		try {
			book.validateVars();
			st = conn.prepareStatement("update wnc992.book set title = ?, summary = ?, year_published = ?, publisher_id = ?, isbn = ? where id = ?");
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getyearpublished());
			st.setInt(4, book.getPublisher().getId());
			st.setString(5, book.getisbn());
			st.setInt(6, book.getId());
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
	
	
	public void addBook(Book book) throws AppException{
		PreparedStatement st = null;
		try {
			book.validateVars();
			st = conn.prepareStatement("INSERT INTO wnc992.book (title, summary, year_published, publisher_id, isbn) VALUES (?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3,book.getyearpublished());
			st.setInt(4, book.getPublisher().getId());
			st.setString(5, book.getisbn());
			st.executeUpdate();	
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			book.setId(rs.getInt(1));
			
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
	public void deleteAuthor(int authorId, int bookId){
		PreparedStatement st = null;
		try {
			
			st = conn.prepareStatement("DELETE FROM wnc992.author_book WHERE author_id = ? AND book_id = ?");
			st.setInt(1, authorId);
			st.setInt(2, bookId);
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
	public void addAuthor(int authId, int bookId, int royalty) throws AppException{
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO wnc992.author_book (author_id, book_id, royalty) VALUES (?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, authId);
			st.setInt(2, bookId);
			st.setFloat(3, (float)royalty/100);
			st.executeUpdate();	
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
	
	public void deleteBook(Book book) throws AppException{
		PreparedStatement st = null;
		try {
			book.validateVars();
			st = conn.prepareStatement("DELETE FROM wnc992.book WHERE id = ?");
			st.setInt(1, book.getId());
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
			statement = conn.prepareStatement("SELECT id,date_added,entry_msg FROM book_audit_trail WHERE book_id = ?");
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
	
	public ObservableList<AuthorBook> getAuthorsForBook(int id) throws Exception{
		ObservableList<AuthorBook> authorBooks = FXCollections.observableArrayList();
		
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT author_id, book_id, royalty FROM author_book WHERE book_id = ?");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				AuthorBook authorBook = new AuthorBook();			
				authorBook.setAuthor(getAuthor(rs.getInt("author_id")));
				authorBook.setBook(getBook(rs.getInt("book_id")));
				authorBook.setRoyalty(rs.getFloat("royalty")*100);
				authorBook.setNewRecordFalse();
				authorBooks.add(authorBook);
				
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
		
		return authorBooks;
		
	}
	
	public Author getAuthor(int id) throws Exception{
		Author author = new Author();
		
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT author.id as auth_id, author.first_name, author.last_name, author.dob, author.gender, author.web_site, author.last_modified FROM author WHERE id = " + id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				author.setLDAuthorDoB(rs.getDate("dob").toLocalDate());
				author.setAuthorFirstName(rs.getString("first_name"));
				author.setAuthorLastName(rs.getString("last_name"));
				author.setAuthorGender(rs.getString("gender"));
				author.setId(rs.getInt("auth_id"));
				author.setAuthorWebSite(rs.getString("web_site"));
				author.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
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
		
		return author;
	}
	
	public Book getBook(int id) throws Exception{
		Book book = new Book(this, pgateway);
		
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT book.date_added, book.id, book.isbn, book.publisher_id, book.summary, book.title, book.year_published FROM book WHERE id = " + id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				book.setDate(rs.getDate("date_added").toLocalDate());
				book.setId(rs.getInt("id"));
				book.setISBN(rs.getString("isbn"));
				book.setPublisher(rs.getInt("publisher_id"));
				book.setSummary(rs.getString("summary"));
				book.setTitle(rs.getString("title"));
				book.setYearPublished(rs.getInt("year_published"));		
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
		
		return book;
	}
	public int getTotalBooks() throws Exception{
		int total = 0;
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT COUNT(*) FROM book");
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				total = rs.getInt("COUNT(*)");
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
		
		return total;
	}
	
	public ObservableList<Book> getBooksByPublisher(int id) throws Exception{
		ObservableList<Book> books = FXCollections.observableArrayList();
		
		PreparedStatement statement = null;
		try{
			statement = conn.prepareStatement("SELECT book.date_added, book.id, book.isbn, book.publisher_id, book.summary, book.title, book.year_published FROM book WHERE book.publisher_id = " + id);
			ResultSet rs = statement.executeQuery();

			while(rs.next()){
				Book book = new Book(this,pgateway);
				book.setDate(rs.getDate("date_added").toLocalDate());
				book.setId(rs.getInt("id"));
				book.setISBN(rs.getString("isbn"));
				book.setPublisher(rs.getInt("publisher_id"));
				book.setSummary(rs.getString("summary"));
				book.setTitle(rs.getString("title"));
				book.setYearPublished(rs.getInt("year_published"));
				books.add(book);	
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				if(statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return books;
		
	}

}
