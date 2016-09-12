package rmiserver;

import java.io.Serializable;

public class AgendaItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String descricao;
	private String username;
        private String key;
	
	public AgendaItem(String descricao, String username){
		setDescricao(descricao);
		setUsername(username);
                setKey("Por decidir.");
                
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
        
        
}
