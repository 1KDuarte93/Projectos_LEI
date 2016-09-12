package Bejeweled_pkg;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.JButton;


public class BGere_eventos implements ActionListener {
	private Bejeweled_game j;

	public BGere_eventos(Bejeweled_game j) {
		super();
		this.j = j;
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Object o=e.getSource();
		if (o==j.submeter_score){
			try {
				j.guardar_score();
				j.guardar_score_f.setVisible(false);
			} catch (IOException e1) {
				System.out.println("ERRO");
				e1.printStackTrace();
			}
		}
		
		if (o==j.scores){
			j.ler_scores_classic();
		}
		if (o==j.game_over){
			j.tempo_s = 121;
			j.guardar_score_f.setVisible(true);
		}
		
		
		if(o == j.classic_8){
			j.remove(j.panel_tab);
			j.n_colunas = 8;
			j.n_linhas = 8;
			j.matriz = new Peca[8][8];
			j.matriz_botoes = new JButton[8][8];
			j.setSize(new Dimension (900, 600));
			j.barra_tempo.setVisible(false);
			j.act_tab();
			do
				j.preenche_inicial();
			while(j.verificar_inicio() != true);
			j.lscore.setText("0");
			j.n_nivell.setText("1");
		}
		if(o == j.classic_10){
			j.remove(j.panel_tab);
			j.n_colunas = 10;
			j.n_linhas = 10;
			j.matriz = new Peca[10][10];
			j.matriz_botoes = new JButton[10][10];
			j.setSize(new Dimension (1050, 700));
			j.barra_tempo.setVisible(false);
			j.act_tab();
			do
				j.preenche_inicial();
			while(j.verificar_inicio() != true);
			j.lscore.setText("0");
			j.n_nivell.setText("1");
		}
		if(o == j.classic_6){
			j.remove(j.panel_tab);
			j.n_colunas = 6;
			j.n_linhas = 6;
			j.matriz = new Peca[6][6];
			j.matriz_botoes = new JButton[6][6];
			j.setSize(new Dimension (750, 500));
			j.barra_tempo.setVisible(false);
			j.act_tab();
			do
				j.preenche_inicial();
			while(j.verificar_inicio() != true);
			j.lscore.setText("0");
			j.n_nivell.setText("1");
		}
		if(o == j.tempo_8){
			j.remove(j.panel_tab);
			j.n_colunas = 8;
			j.n_linhas = 8;
			j.tempo_utilizado=0;
			j.matriz = new Peca[8][8];
			j.matriz_botoes = new JButton[8][8];
			j.setSize(new Dimension (900, 600));
			j.act_tab();
			do
				j.preenche_inicial();
			while(j.verificar_inicio() != true);
			j.lscore.setText("0");
			j.barra_tempo.setVisible(true);
			j.tempo_s = 0;
			j.n_nivell.setText("1");
		}
		if(o == j.tempo_10){
			j.remove(j.panel_tab);
			j.n_colunas = 10;
			j.n_linhas = 10;
			j.tempo_utilizado=0;
			j.matriz = new Peca[10][10];
			j.matriz_botoes = new JButton[10][10];
			j.setSize(new Dimension (1050, 700));
			j.act_tab();
			do
				j.preenche_inicial();
			while(j.verificar_inicio() != true);
			j.lscore.setText("0");
			j.barra_tempo.setVisible(true);
			j.tempo_s = 0;
			j.n_nivell.setText("1");
		}
		if(o == j.tempo_6){
			j.remove(j.panel_tab);
			j.n_colunas = 6;
			j.n_linhas = 6;
			j.tempo_utilizado=0;
			j.matriz = new Peca[6][6];
			j.matriz_botoes = new JButton[6][6];
			j.setSize(new Dimension (750, 500));
			j.act_tab();
			do
				j.preenche_inicial();
			while(j.verificar_inicio() != true);
			j.lscore.setText("0");
			j.barra_tempo.setVisible(true);
			j.tempo_s = 0;
			j.n_nivell.setText("1");
		}

		for(int x=0; x<j.n_linhas;x++){
			for(int y=0;y<j.n_colunas; y++){
				if (o == j.matriz_botoes[x][y]){
					j.muda_img(j.matriz[x][y]);
				}
			}
		}
	}

}
