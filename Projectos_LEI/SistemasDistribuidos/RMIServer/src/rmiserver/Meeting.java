package rmiserver;

import java.io.Serializable;
import java.util.ArrayList;




public class Meeting implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String titulo;
	private String resultado;
	private String localizacao;
	private String data;
        private String horainicio;
	private String duracao;
	private UserLogin userlogin;
	private ArrayList<AgendaItem> items;
	private ArrayList <String> pessoal; //so pessoal que ja aceitou o convite
	private ArrayList <ActionItem> actionitems;

	public Meeting(String titulo, String resultado, String localizacao,
			String data, String duracao, String horainicio, UserLogin userlogin) {
		super();
		this.titulo = titulo;
		this.resultado = resultado;
		this.localizacao = localizacao;
		this.data = data;
		this.duracao = duracao;
		this.userlogin = userlogin;
		this.items = new ArrayList<AgendaItem>();
		this.pessoal = new ArrayList<String>();
		this.actionitems = new ArrayList<ActionItem>();
                this.horainicio = horainicio;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public String getDuracao() {
		return duracao;
	}

	public void setDuracao(String duracao) {
		this.duracao = duracao;
	}

	public UserLogin getUserlogin() {
		return userlogin;
	}

	public void setUserlogin(UserLogin userlogin) {
		this.userlogin = userlogin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<AgendaItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<AgendaItem> items) {
		this.items = items;
	}
	
	public ArrayList<String> getPessoal() {
		return pessoal;
	}

	public void setPessoal(ArrayList<String> pessoal) {
		this.pessoal = pessoal;
	}

	public void addPessoal(String userlogin){
		this.pessoal.add(userlogin);
	}
	
	public void addItem(AgendaItem item){
		this.items.add(item);
	}

	public ArrayList <ActionItem> getActionitems() {
		return actionitems;
	}

	public void setActionitems(ArrayList <ActionItem> actionitems) {
		this.actionitems = actionitems;
	}
	
	public void addActionitems(ActionItem actionitem){
		this.actionitems.add(actionitem);
	}

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }
        
        
        
        
}
