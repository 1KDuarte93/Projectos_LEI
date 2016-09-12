package Bejeweled_pkg;

public class Highscore_temp extends Highscore_classic{
	private int tempo;

	public Highscore_temp(String nome, int score, String nivel, int tempo) {
		super(nome, score, nivel);
		this.tempo = tempo;
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	

}
