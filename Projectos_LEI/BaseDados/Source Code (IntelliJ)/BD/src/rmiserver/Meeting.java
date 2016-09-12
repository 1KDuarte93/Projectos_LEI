package rmiserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class Meeting implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String titulo;
	private String resultado;
	private String localizacao;
	private Date data;
	private String duracao;
	private ArrayList<AgendaItem> items;
	private ArrayList <String> pessoal; //so pessoal que ja aceitou o convite

	public Meeting(String titulo, String resultado, String localizacao,
				   Date data, String duracao) {
		super();
		this.titulo = titulo;
		this.resultado = resultado;
		this.localizacao = localizacao;
		this.data = data;
		this.duracao = duracao;
		this.items = new ArrayList<AgendaItem>();
		this.pessoal = new ArrayList<String>();
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
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
}
