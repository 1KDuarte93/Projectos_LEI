package Bejeweled_pkg;

import javax.swing.ImageIcon;

public class Peca{
	private ImageIcon img;
	private int x;
	private int y;
	private String cor;
	private int poder;
	public Peca(String cor, ImageIcon img, int x, int y, int poder) {
		super();
		this.img = img;
		this.cor = cor;
		this.x = x;
		this.y = y;
		this.poder = poder;
	}
	
	public ImageIcon getImg() {
		return img;
	}
	public void setImg(ImageIcon img) {
		this.img = img;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public int getPoder() {
		return poder;
	}

	public void setPoder(int poder) {
		this.poder = poder;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}


	
		
}

