package rmiserver;

import java.io.Serializable;


public class ActionItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String descricao;
	private String username;
	private boolean done;
	
	public ActionItem(String descricao, String username, boolean done){
		this.setUsername(username);
		this.setDescricao(descricao);
		this.setDone(done);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean getDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

}
