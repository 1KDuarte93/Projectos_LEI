package Bejeweled_pkg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Bejeweled_game extends JFrame {
	private final static int dimH = 900;
	private final static int dimV = 600;
	int n_linhas = 8, n_colunas = 8, tempo_utilizado = 0;
	Peca[][] matriz = new Peca[n_linhas][n_colunas];
	JButton[][] matriz_botoes = new JButton[n_linhas][n_colunas];
	private ArrayList<ImageIcon> imagens = new ArrayList<ImageIcon>();
	private ArrayList<ImageIcon> pimagens = new ArrayList<ImageIcon>();
	private Container c = getContentPane();
	int poder, tipo; //para ajudar nos power-ups
	ImageIcon vazio= new ImageIcon("icons/0.png");
	ArrayList<Peca> muda_pecas = new ArrayList<Peca>();
	int tempo_s;
	JLabel lscore;
	JLabel cronometro;
	JPanel campo_score;
	JPanel panel_tab;
	JPanel modo_de_jogo;
	JPanel menu;
	JButton classic_8;
	JButton classic_10;
	JButton classic_6;
	JButton tempo_8;
	JButton tempo_10;
	JButton tempo_6;
	JButton game_over;
	JButton scores;
	JFrame frame_menu;
	JProgressBar barra_tempo;
	JTextArea classic_scores_l;
	JFrame mostrar_todos_scores;
	JTextField guardar_score_nome;
	JFrame guardar_score_f;
	JButton submeter_score;
	JLabel n_nivell;

	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Bejeweled_game bjw = new Bejeweled_game();
		bjw.mostrar_todos_scores.setVisible(false);
		bjw.setResizable(false);
		do
			bjw.preenche_inicial();
		while(bjw.verificar_inicio() != true);
		bjw.barra_tempo.setVisible(false);
		bjw.setVisible(true);
		bjw.guardar_score_f.setVisible(false);
		bjw.temporizador();
	}
	
	
	public Bejeweled_game(){
		//carrega imagens
		ImageIcon azul = new ImageIcon("icons/1.png");
		ImageIcon verde = new ImageIcon("icons/2.png");
		ImageIcon laranja = new ImageIcon("icons/3.png");
		ImageIcon rosa = new ImageIcon("icons/4.png");
		ImageIcon vermelho = new ImageIcon("icons/5.png");
		ImageIcon amarelo = new ImageIcon("icons/6.png");
		ImageIcon pazul = new ImageIcon("icons/1p.png");
		ImageIcon pverde = new ImageIcon("icons/2p.png");
		ImageIcon plaranja = new ImageIcon("icons/3p.png");
		ImageIcon prosa = new ImageIcon("icons/4p.png");
		ImageIcon pvermelho = new ImageIcon("icons/5p.png");
		ImageIcon pamarelo = new ImageIcon("icons/6p.png");
		
		setTitle("Bejeweled_beta_edition");
		JPanel panel_esq = new JPanel();
		getForeground();
		guardar_score_f = new JFrame();
		guardar_score_f.setTitle("Guardar Score");
		classic_scores_l = new JTextArea("");
		mostrar_todos_scores = new JFrame();
		mostrar_todos_scores.setTitle("Scores Guardados");
		guardar_score_nome = new JTextField("");
		submeter_score = new JButton("Submeter");
		JPanel submeter_scorepanel = new JPanel();
		submeter_scorepanel.add(submeter_score);
		JPanel guardar_p = new JPanel();
		guardar_p.setLayout(new GridLayout(2,1));
		guardar_p.add(guardar_score_nome);
		submeter_score.addActionListener(new BGere_eventos(this));
		guardar_p.add(submeter_scorepanel);
		guardar_score_f.add(guardar_p);
		mostrar_todos_scores.add(classic_scores_l);
		modo_de_jogo = new JPanel();
		modo_de_jogo.setLayout(new GridLayout(10, 1, 0, 0));
		JLabel modo_class = new JLabel("Classico:");
		modo_de_jogo.add(modo_class);
		classic_8 = new JButton("Classico 8x8");
		classic_8.addActionListener(new BGere_eventos(this));
		classic_8.setVisible(true);
		
		modo_de_jogo.add(classic_8);
		classic_10 = new JButton("Classico 10x10");
		classic_10.addActionListener(new BGere_eventos(this));
		classic_10.setVisible(true);
		modo_de_jogo.add(classic_10);
		classic_6 = new JButton("Classico 6x6");
		classic_6.addActionListener(new BGere_eventos(this));
		classic_6.setVisible(true);
		modo_de_jogo.add(classic_6);
		JLabel modo_temp = new JLabel("Temporizado:");
		modo_de_jogo.add(modo_temp);
		tempo_8 = new JButton("Temporizado 8x8");
		tempo_8.addActionListener(new BGere_eventos(this));
		tempo_8.setVisible(true);
		modo_de_jogo.add(tempo_8);
		tempo_10 = new JButton("Temporizado 10x10");
		tempo_10.addActionListener(new BGere_eventos(this));
		tempo_10.setVisible(true);
		modo_de_jogo.add(tempo_10);
		tempo_6 = new JButton("Temporizado 6x6");
		tempo_6.addActionListener(new BGere_eventos(this));
		tempo_6.setVisible(true);
		modo_de_jogo.add(tempo_6);
		
		
		JPanel scoreeover = new JPanel();
		scoreeover.setLayout(new GridLayout(4,1));
		JPanel nivelp = new JPanel();
		JLabel nivell = new JLabel("Nivel: ");
		n_nivell = new JLabel("1");
		nivelp.add(nivell);
		nivelp.add(n_nivell);
		
		setSize(dimH, dimV);
		panel_esq.add(modo_de_jogo);
		lscore = new JLabel("0");
		JLabel lnscore = new JLabel("Score: ");
		lnscore.setVisible(true);
		lscore.setVisible(true);
		campo_score = new JPanel();
		campo_score.add(lnscore);
		campo_score.add(lscore);
		campo_score.setVisible(true);
		game_over = new JButton("Game Over");
		game_over.addActionListener(new BGere_eventos(this));
		guardar_score_f.setBounds(new Rectangle(200,200));
		JPanel overp = new JPanel();
		overp.add(game_over);
		scores = new JButton("scores");
		scores.addActionListener(new BGere_eventos(this));
		JPanel scoresp = new JPanel();
		scoresp.add(scores);
		
		scoreeover.add(campo_score);
		scoreeover.add(nivelp);
		scoreeover.add(overp);
		scoreeover.add(scoresp);
		
		
		panel_esq.setLayout(new GridLayout(3, 1));
		panel_esq.add(scoreeover);
		
		barra_tempo = new JProgressBar(JProgressBar.VERTICAL);  
		barra_tempo.setBounds(new Rectangle(40, 40, 200, 40));  
		barra_tempo.setMinimum(0);   
		barra_tempo.setMaximum(120);  
		barra_tempo.setValue(0);
		barra_tempo.setBackground(Color.WHITE);
	    JPanel barra = new JPanel();
	    barra.add(barra_tempo);
	    panel_esq.add(barra);
		panel_esq.setVisible(true);
		act_tab();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.add(panel_esq, BorderLayout.WEST);
		c.add(panel_tab, BorderLayout.EAST);

		//Add as imagens ao array
		imagens.add(0,azul);
		imagens.add(1,verde);
		imagens.add(2,laranja);
		imagens.add(3,rosa);
		imagens.add(4,vermelho);
		imagens.add(5,amarelo);
		pimagens.add(0,pazul);
		pimagens.add(1,pverde);
		pimagens.add(2,plaranja);
		pimagens.add(3,prosa);
		pimagens.add(4,pvermelho);
		pimagens.add(5,pamarelo);

	}

	public void temporizador() throws InterruptedException{
		tempo_s = 0;
		while (true){
			Thread.sleep(1000);
			tempo_s++;
			tempo_utilizado++;
			barra_tempo.setValue(tempo_s);
			if (tempo_s >= 120 && barra_tempo.isShowing() == true){
				JOptionPane.showMessageDialog(null, "O Jogo acabou!");
				guardar_score_f.setVisible(true);
				break;
			}
		}
	}
	
	
	public void act_tab(){
		panel_tab = new JPanel();
		panel_tab.setVisible(true);
		panel_tab.setBounds(10, 11, dimV, dimV);
		panel_tab.setLayout(new GridLayout(n_linhas, n_colunas));
		panel_tab.setBackground(Color.BLUE);
		
		panel_tab.setVisible(true);
		
		int x, y;
		for (y=0; y<n_linhas; y++){
			for(x=0; x<n_colunas; x++){
				matriz_botoes[x][y] = new JButton ("");
				matriz_botoes[x][y].setOpaque(false);
				matriz_botoes[x][y].setContentAreaFilled(false);
				matriz_botoes[x][y].setBorderPainted(false);
				matriz_botoes[x][y].setVisible(true);
				matriz_botoes[x][y].addActionListener(new BGere_eventos(this));
				panel_tab.add(matriz_botoes[x][y]);
			}
		}
		panel_tab.repaint();
		c.add(panel_tab, BorderLayout.EAST);
	}
	
	
	
	void act_nivel(){
		int n;
		n = Integer.parseInt(lscore.getText());
		if (n<=50)
			n_nivell.setText(String.valueOf(1));
		else if ((n>50) && (n<=150))
			n_nivell.setText(String.valueOf(2));
		else if((n>150) && (n<=270))
			n_nivell.setText(String.valueOf(3));
		else if((n>270) && (n<=400))
			n_nivell.setText(String.valueOf(4));
		else if((n>400) && (n<=550))
			n_nivell.setText(String.valueOf(5));
		else if((n>550) && (n<=700))
			n_nivell.setText(String.valueOf(6));
		else if((n>700) && (n<=850))
			n_nivell.setText(String.valueOf(7));
		else if((n>850) && (n<=1050))
			n_nivell.setText(String.valueOf(8));
		else if((n>1050) && (n<=1250))
			n_nivell.setText(String.valueOf(9));
		else if((n>1250) && (n<=1700))
			n_nivell.setText(String.valueOf(10));
		else
			n_nivell.setText("Grand Master!");
	}
	
	public void guardar_score() throws IOException{
		String nome, ler_score, textofinal;
		int pontos_totais;
		nome = guardar_score_nome.getText();
		ler_score = lscore.getText();
		pontos_totais = Integer.parseInt(ler_score);
		textofinal = "";
		BufferedWriter escreve = new BufferedWriter(new FileWriter("Ficheiro.txt",true));
		if(barra_tempo.isVisible() == true){
			Highscore_temp x = new Highscore_temp(nome, pontos_totais, (n_nivell.getText()), tempo_utilizado);
			textofinal = "Tempo || Score:" + x.getScore() + " | Nome:" + x.getNome() + " | Nivel:" + x.getNivel() + " | Tempo:" + x.getTempo() + " (segundos).";
			escreve.newLine();
			escreve.write(textofinal);
			escreve.close();
		}
		else{
			Highscore_classic x = new Highscore_classic(nome, pontos_totais, (n_nivell.getText()));
			textofinal = "Classico || Score:" + x.getScore() + " | Nome:" + x.getNome() + " | Nivel:" + x.getNivel();
			escreve.newLine();
			escreve.write(textofinal);
			escreve.close();
		}
		lscore.setText("0");
		guardar_score_nome.setText("");
	}
	
	public void ler_scores_classic(){
		classic_scores_l.setText("");
		mostrar_todos_scores.setBounds(new Rectangle(600,600));
		
		 try {
		      FileReader ficheirotxt = new FileReader("Ficheiro.txt");
		      BufferedReader lerfich = new BufferedReader(ficheirotxt);
		      String linhas = "";
		      String linha = lerfich.readLine();
		      while (linha != null) {
		    	  linhas = linhas + "\n" + linha;
		    	  linha = lerfich.readLine();
		    	  
		      }
		      classic_scores_l.setText(linhas);
		      ficheirotxt.close();
		    } 
		 catch (IOException e) {
		        	System.out.println("Erro ao abrir");
		        }
		 
		 mostrar_todos_scores.setVisible(true);
		 
		 
	}
	
	public String cores_numeros(int n){
		String sfinal;
		switch(n){
		case 0:{
			sfinal = "azul";
			return sfinal;
		}
		case 1:{
			sfinal = "verde";
			return sfinal;
		}
		case 2:{
			sfinal = "laranja";
			return sfinal;
		}
		case 3:{
			sfinal = "rosa";
			return sfinal;
		}
		case 4:{ 
			sfinal = "vermelho";
			return sfinal;
		}
		case 5:{
			sfinal = "amarelo";
			return sfinal;
		}
		default:{
			sfinal = "Nao encontrado";
			return sfinal;
		}
		}
	}
	
	public void preenche_inicial(){
		int x, y, imagem;
		String cor;
		Random ram = new Random();
		for (y=0; y<n_linhas; y++){
			for(x=0; x<n_colunas; x++){
				imagem = ram.nextInt(6);
				cor = cores_numeros(imagem);
				matriz[x][y] = new Peca (cor, imagens.get(imagem), x, y, 0);
			}
		}
		for (y=0; y<n_linhas; y++){
			for(x=0; x<n_colunas; x++){
				imagem = ram.nextInt(6);
				matriz_botoes[x][y].setIcon(matriz[x][y].getImg());
			}
		}
	}

	public boolean verificar_jogada(int x0, int y0, int x1, int y1){
		if((x0==x1 && y0+1==y1) || (x0==x1 && y0-1==y1)) //esquerda e direita
			return true;
		else if((x0-1==x1 && y0==y1) || (x0+1==x1 && y0==y1)) //cima e baixo
			return true;
		else
			return false;

	}
	
	public boolean verificar_coluna(int x, int y){
		int enc_poderes;
		int fim_menor = y;
		int fim_maior = y;
		
		//verificar da peca pra cima
		while (fim_menor>=0){  //da peca ao [x][0]
			if(matriz[x][fim_menor].getCor() != matriz[x][y].getCor()){
				break;}
			else{
				fim_menor--;
			}
		}
		fim_menor++;
		
		//verificar da peca pra baixo
		while (fim_maior<n_colunas){  //da peca ao [x][7]
			if(matriz[x][fim_maior].getCor() != matriz[x][y].getCor())
				break;
			else{
				fim_maior++;
			}
		}
		fim_maior--;
		
		if ((fim_maior-fim_menor)==2){
			for (enc_poderes = fim_menor; enc_poderes<=fim_maior; enc_poderes++){
				if (pimagens.indexOf(matriz[x][enc_poderes].getImg()) != -1){
					fazer_poderes(matriz[x][enc_poderes]);
				}	
			}
			eliminar_coluna(fim_menor, fim_maior, x);
			somar_score(2);
			poder = 0;
			return true;
			}
		else if ((fim_maior-fim_menor)==3){
			for(int a=0; a<=5; a++){
				if(imagens.get(a) == matriz[x][y].getImg())
					tipo=a;
			}
			for (enc_poderes = fim_menor; enc_poderes<=fim_maior; enc_poderes++){
				if (pimagens.indexOf(matriz[x][enc_poderes].getImg()) != -1){
					fazer_poderes(matriz[x][enc_poderes]);
				}	
			}
			eliminar_coluna(fim_menor, fim_maior, x);
			somar_score(3);
			poder = 1;
			return true;
			}
		else if ((fim_maior-fim_menor)>=4){
			for(int a=0; a<=5; a++){
				if(imagens.get(a) == matriz[x][y].getImg())
					tipo=a;
			}
			for (enc_poderes = fim_menor; enc_poderes<=fim_maior; enc_poderes++){
				if (pimagens.indexOf(matriz[x][enc_poderes].getImg()) != -1){
					fazer_poderes(matriz[x][enc_poderes]);
				}	
			}
			eliminar_coluna(fim_menor, fim_maior, x);
			somar_score(4);
			poder = 2;
			return true;
			}
		else{
			return false;
		}
	}
	
	
	public boolean verificar_linha(int x, int y){
		int enc_poderes;
		int fim_menor = x;
		int fim_maior = x;
		
		//verificar da peca pra cima
		while (fim_menor>=0){  //da peca ao [x][0]
			if(matriz[fim_menor][y].getCor() != matriz[x][y].getCor()){
				break;}
			else{
				fim_menor--;}
		}
		fim_menor++;
		//verificar da peca pra baixo
		while (fim_maior<n_linhas){  //da peca ao [x][n_linhas-1]
			if(matriz[fim_maior][y].getCor() != matriz[x][y].getCor())
				break;
			else
				fim_maior++;
		}
		fim_maior--;
		
		
		if ((fim_maior-fim_menor)==2){
			for(int a=0; a<=5; a++){
				if(imagens.get(a) == matriz[x][y].getImg())
					tipo=a;
			}
			for (enc_poderes = fim_menor; enc_poderes<=fim_maior; enc_poderes++){
				if (pimagens.indexOf(matriz[enc_poderes][y].getImg()) != -1){
					fazer_poderes(matriz[enc_poderes][y]);
				}	
			}
			eliminar_linha(fim_menor, fim_maior, y);
			somar_score(2);
			poder = 0;
			actualizar_matriz();
			return true;
		}
		else if((fim_maior-fim_menor)==3){
			for(int a=0; a<=5; a++){
				if(imagens.get(a) == matriz[x][y].getImg())
					tipo=a;
			}
			for (enc_poderes = fim_menor; enc_poderes<=fim_maior; enc_poderes++){
				if (pimagens.indexOf(matriz[enc_poderes][y].getImg()) != -1){
					fazer_poderes(matriz[enc_poderes][y]);
				}	
			}
			eliminar_linha(fim_menor, fim_maior, y);
			somar_score(3);
			poder = 1;
			actualizar_matriz();
			return true;
		}
		else if((fim_maior-fim_menor)>=4){
			for(int a=0; a<=5; a++){
				if(imagens.get(a) == matriz[x][y].getImg())
					tipo=a;
			}
			for (enc_poderes = fim_menor; enc_poderes<=fim_maior; enc_poderes++){
				if (pimagens.indexOf(matriz[enc_poderes][y].getImg()) != -1){
					fazer_poderes(matriz[enc_poderes][y]);
				}
			}
			eliminar_linha(fim_menor, fim_maior, y);
			somar_score(4);
			poder = 2;
			actualizar_matriz();
			return true;
		}
		else{
			return false;
			}
		
	}
	
	public void fazer_poderes (Peca escolhido){ //1-destroi linha || 2-destroi_pecas_dessas
		int i, j;
		if (escolhido.getPoder() == 1){ 
			for(i=0; i<n_colunas; i++){
				matriz[i][escolhido.getY()].setImg(vazio);
			}
		}
		else if (escolhido.getPoder() == 2){
			for (i=0; i<n_linhas; i++){
				for (j=0; j<n_colunas; j++){
					if (matriz[i][j].getCor() == escolhido.getCor())
						matriz[i][j].setImg(vazio);
				}
			}
		}
		else{
			for(i=0; i<n_colunas; i++){
				matriz[i][escolhido.getY()].setImg(vazio);
			}
			for(j=0; j<n_colunas; j++){
				matriz[escolhido.getX()][j].setImg(vazio);
			}
		}
	}
	
	//RECEBE A PEÇA DEPOIS DE TROCADA
	public void eliminar_coluna(int menor, int j, int x){ //<maior> <menor> <cordenada x>
		int i;
		for (i = menor; i<=j; i++){
			matriz[x][i].setImg(vazio);
		}
	}
	//RECEBE A PEÇA DEPOIS DE TROCADA
	public void eliminar_linha(int menor, int j, int y){ //<maior> <menor> <cordenada x>
		int i;
		for (i = menor; i<=j; i++){
			matriz[i][y].setImg(vazio);
		}
	}
	
	
	public void cair_pecas(int x){
		
		int i, k=0;
		for (i=(n_linhas-1); i>=0; i--){
			if(matriz[x][i].getImg() == vazio){
				while((matriz[x][i].getImg() == vazio) && k>=0){
					for (k=i-1; k>=0; k--){
						if (matriz[x][k].getImg() != vazio){
							matriz[x][i].setImg(matriz[x][k].getImg());
							matriz[x][i].setCor(matriz[x][k].getCor());
							matriz[x][i].setX(matriz[x][k].getX());
							matriz[x][i].setY(matriz[x][k].getY());
							matriz[x][k].setImg(vazio);
							actualizar_matriz();
							break;
						}
					}
				}
			}
		}
	}
	
	
	public void preencher(){ // 1 - destruir linha || 2 - destruir todas as peças desse tipo 
		int x, y;
		String cor;
		int tipo_normal;
		Random ram = new Random();		
		for (y=0; y<n_linhas; y++){
			for(x=0; x<n_colunas; x++){ //percorre matriz
				if (matriz[x][y].getImg() == vazio){
					if (poder == 1){
						cor = cores_numeros(tipo);
						matriz[x][y].setImg(pimagens.get(tipo));
						matriz[x][y].setPoder(poder);
						matriz[x][y].setCor(cor);
						poder = 0;
					}
					else{
						tipo_normal = ram.nextInt(5);
						cor = cores_numeros(tipo_normal);
						matriz[x][y].setImg(imagens.get(tipo_normal));
						matriz[x][y].setCor(cor);
						matriz[x][y].setPoder(0);
						
					}
				}
			}
		}
		actualizar_matriz();
	}
	
	public void somar_score(int n){
		int actual;
		String ler;
		ler = lscore.getText();
		actual = Integer.parseInt(ler);
		actual += n;
		lscore.setText(String.valueOf(actual));
		act_nivel();
	}
	
	
	public void actualizar_matriz(){
		int i, j;
		for (i=0; i<n_linhas; i++){
			for (j=0; j<n_colunas; j++){
				matriz_botoes[i][j].setIcon(matriz[i][j].getImg());
				matriz[i][j].setX(i);
				matriz[i][j].setY(j);
				
			}
		}
	}
	
	public boolean verificar_inicio(){
		int i, j;
		for(i=0; i<n_linhas; i++){
			for(j=0; j<n_colunas; j++){
				if (i<n_linhas-2)
				if((matriz[i][j].getCor() == matriz[i+1][j].getCor()) && (matriz[i][j].getCor() == matriz[i+2][j].getCor())){
					return false;
				}
				if(j<n_colunas-2)
				if((matriz[i][j].getCor() == matriz[i][j+1].getCor()) && (matriz[i][j].getCor() == matriz[i][j+2].getCor())){
					return false;
				}
			}
			
		}
		return true;
	}
	
	public void muda_img(Peca x){
		Peca aux_peca1, aux_peca0;
		int x0, x1, y0, y1, xx0, xx1, yy0, yy1, i;
		muda_pecas.add(x);
		
		if (muda_pecas.size() == 2){
			x0 = muda_pecas.get(0).getX();
			y0 = muda_pecas.get(0).getY();
			x1 = muda_pecas.get(1).getX();
			y1 = muda_pecas.get(1).getY();
			if(verificar_jogada(x0, y0, x1, y1)){
				if (muda_pecas.get(0).getCor() == muda_pecas.get(1).getCor())
					JOptionPane.showMessageDialog(null, "Jogada incorrecta");
				else{
					aux_peca1 = matriz[x1][y1];
					aux_peca0 = matriz[x0][y0];
					matriz[x1][y1] = aux_peca0;
					matriz[x1][y1].setX(aux_peca1.getX());
					matriz[x1][y1].setY(aux_peca1.getY());
					matriz[x0][y0] = aux_peca1;
					matriz[x0][y0].setX(aux_peca0.getX());
					matriz[x0][y0].setY(aux_peca0.getY());
					
					xx0 = matriz[x0][y0].getX();
					yy0 = matriz[x0][y0].getY();
					xx1 = matriz[x1][y1].getX();
					yy1 = matriz[x1][y1].getY();
					if(!verificar_coluna(xx0, yy0))
					if(!verificar_linha(xx0, yy0))
					if(!verificar_coluna(xx1, yy1))
					if(!verificar_linha(xx1, yy1)){
						JOptionPane.showMessageDialog(null, "Jogada incorrecta");
						aux_peca1 = matriz[x1][y1];
						aux_peca0 = matriz[x0][y0];
						matriz[x1][y1] = aux_peca0;
						matriz[x1][y1].setX(aux_peca1.getX());
						matriz[x1][y1].setY(aux_peca1.getY());
						matriz[x0][y0] = aux_peca1;
						matriz[x0][y0].setX(aux_peca0.getX());
						matriz[x0][y0].setY(aux_peca0.getY());
						actualizar_matriz();
						
					}
						for (i=(n_linhas-1); i>=0; i--){
							cair_pecas(i);
						}
						preencher();
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Jogada incorrecta.");
			}
		muda_pecas.clear();
	
		}
	}
	
	

}


