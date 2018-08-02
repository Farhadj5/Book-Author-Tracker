package book;

import javafx.beans.property.SimpleStringProperty;

public class Publisher {
	private int id;
	private SimpleStringProperty publisherName;
	
	public Publisher() {
		publisherName = new SimpleStringProperty();
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setPName(String name) {
		this.publisherName.set(name);
	}
	
	public int getId() {
		return id;
	}
	
	public String getPubName() {
		return publisherName.get();
	}
	public SimpleStringProperty getName() {
		return publisherName;
	}

	@Override
	public String toString() {
		return getPubName();
	}
}
