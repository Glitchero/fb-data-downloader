package model;

import com.google.gson.annotations.SerializedName;

public class From {

	@SerializedName("name") String name;
	@SerializedName("email") String email;
	@SerializedName("id") String id;

	
	
	public From(String name, String email, String id) {
		super();
		this.name = name;
		this.email = email;
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
