package rmiserver;

import java.io.Serializable;
import java.util.ArrayList;

public class UserLogin implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String username;
	private String password;
	
	
	public UserLogin(int id, String username, String password){
		this.setId(id);
		this.setUsername(username);
		this.setPassword(password);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
