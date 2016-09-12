package Bejeweled_pkg;

public class Highscore_classic {
	private String nome;
	private int score;
	private String nivel;
	public Highscore_classic(String nome, int score,  String nivel) {
		super();
		this.nome = nome;
		this.score = score;
		this.nivel = nivel;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

}
