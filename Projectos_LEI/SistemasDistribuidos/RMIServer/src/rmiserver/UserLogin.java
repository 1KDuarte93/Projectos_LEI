package rmiserver;

import java.io.Serializable;
import java.util.ArrayList;

public class UserLogin implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private ArrayList <Convite> convites;
	
	
	public UserLogin(String username, String password){
		this.setUsername(username);
		this.setPassword(password);
		this.convites = new ArrayList<Convite>();
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
	public ArrayList <Convite> getConvites() {
		return convites;
	}
	public void setConvites(ArrayList <Convite> convites) {
		this.convites = convites;
	}
	public void addConvite(Convite convite){
		this.convites.add(convite);
	}
}
