package rmiserver;

import java.io.Serializable;

public class AgendaItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String descricao;
	private String key;
	
	public AgendaItem(String descricao, int id){
		setDescricao(descricao);
		setId(id);
		setKey("TBD");
                
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
        
        
}
