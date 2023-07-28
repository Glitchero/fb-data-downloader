package model;

import com.google.gson.annotations.SerializedName;

public class Paging {

	@SerializedName("cursors") Cursors cursors;
	@SerializedName("next") String next; //Recently added
	@SerializedName("previous") String previous;
	
	public Paging(Cursors cursors, String next, String previous) {
		this.cursors = cursors;
		this.next = next;
		this.previous = previous;
	}

	public Cursors getCursors() {
		return cursors;
	}

	public void setCursors(Cursors cursors) {
		this.cursors = cursors;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}
	
	
	
}