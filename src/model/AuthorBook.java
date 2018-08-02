package model;

import book.Book;

public class AuthorBook {

	private Author author;
	private Book book;
	private int royalty;
	private boolean newRecord;
	
	public AuthorBook() {
		newRecord = true;
	}
	
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public void setRoyalty(double val) {
		this.royalty = (int) val;
	}
	
	public void setNewRecordFalse() {
		newRecord = false;
	}
	
	public Author getAuthor() {
		return this.author;
	}
	
	public Book getBook() {
		return this.book;
	}
	
	public int getRoyalty() {
		return this.royalty;
	}
	
	public String toString(){
		return this.getAuthor() + "   Royalty: " + this.getRoyalty() + "%";
	}
}
