#include <stdio.h>
#include <stdlib.h>
#include <GL/glut.h>
#include <math.h>
#include "Rgbimage.h"



RgbImage imag;
GLuint  texture[6];

//==================================LUZ
GLfloat lightpos[] = { 0.0, 20.0, 0.0, 1 };
GLfloat dir[] = { 0, -1, 0 };
GLint focoLuz = 100;
GLfloat nevoeiroCor[] = { 0.5, 0.5, 0.5, 1.0 };
GLint nevoeiro = 0;

//=======CAIXA======
GLfloat larguraCaixa = 7.0;
GLfloat	xCaixa = 0;
GLfloat yCaixa = larguraCaixa - 5; //y do fundo está em -5
GLfloat zCaixa = -20;
GLfloat limiteCaixa = 20;
GLfloat	inc = 0.5;
GLint fundoCaixa = 1;

//========BOLA========
GLfloat	rBola = 1.5;
GLfloat zBola = 20;
GLfloat yBola = 0;
GLfloat xBola = 0;
GLfloat xinc = 0;
GLint	onBox = 0;
GLfloat movebola = 0;
GLfloat vel_bola_voadora = -1.0;

//=============LANCAMENTO=======
GLfloat velocidade;
GLfloat decr_vel = 0.05;
GLfloat click;

//=======FUNDO DO FUNDO, AMBIENTE======
GLfloat larguraFundo = 120;
GLfloat comprimentoFundo = 200;
GLfloat alturaFundo = 100;

//=============OBSERVADOR=======
GLfloat raioObs = 50;
GLfloat angObs = 20;
GLfloat obs[3] = { raioObs*cos(angObs), 20, raioObs*sin(angObs)};

//========POWER
GLfloat pPower = 0.0; //percentagem
GLfloat incPower = 0.02;//
GLint pressButton = 0;

//======MALHA
GLint malha = 0;
GLfloat nMalha = 10;



void criaDefineTexturas()
{
	//PANO DE FUNDO
	glGenTextures(1, &texture[0]);
	glBindTexture(GL_TEXTURE_2D, texture[0]);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
	imag.LoadBmpFile("Imagens/relva.bmp");
	glTexImage2D(GL_TEXTURE_2D, 0, 3,
		imag.GetNumCols(),
		imag.GetNumRows(), 0, GL_RGB, GL_UNSIGNED_BYTE,
		imag.ImageData());

	//ESFERA
	glGenTextures(1, &texture[1]);
	glBindTexture(GL_TEXTURE_2D, texture[1]);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
	imag.LoadBmpFile("Imagens/TennisBallColorMap.bmp");
	glTexImage2D(GL_TEXTURE_2D, 0, 3,
		imag.GetNumCols(),
		imag.GetNumRows(), 0, GL_RGB, GL_UNSIGNED_BYTE,
		imag.ImageData());

	//MADEIRA
	glGenTextures(1, &texture[3]);
	glBindTexture(GL_TEXTURE_2D, texture[3]);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
	imag.LoadBmpFile("Imagens/madeira.bmp");
	glTexImage2D(GL_TEXTURE_2D, 0, 3,
		imag.GetNumCols(),
		imag.GetNumRows(), 0, GL_RGB, GL_UNSIGNED_BYTE,
		imag.ImageData());

	//PAREDE
	glGenTextures(1, &texture[4]);
	glBindTexture(GL_TEXTURE_2D, texture[4]);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
	imag.LoadBmpFile("Imagens/tennisWall.bmp");
	glTexImage2D(GL_TEXTURE_2D, 0, 3,
		imag.GetNumCols(),
		imag.GetNumRows(), 0, GL_RGB, GL_UNSIGNED_BYTE,
		imag.ImageData());

	//AGUA
	glGenTextures(1, &texture[5]);
	glBindTexture(GL_TEXTURE_2D, texture[5]);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
	imag.LoadBmpFile("Imagens/water.bmp");
	glTexImage2D(GL_TEXTURE_2D, 0, 3,
		imag.GetNumCols(),
		imag.GetNumRows(), 0, GL_RGB, GL_UNSIGNED_BYTE,
		imag.ImageData());
}

void init() {
	criaDefineTexturas();
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_LIGHTING);
	glEnable(GL_NORMALIZE);
	
	glFogfv(GL_FOG_COLOR, nevoeiroCor);
	glFogi(GL_FOG_MODE, GL_LINEAR); //GL_EXP, GL_EXP2, 
	glFogf(GL_FOG_START, 80.0);
	glFogf(GL_FOG_END, 100.0);
	//glFogf(GL_FOG_DENSITY, 0.2);
}

void draw_power(){
	glEnable(GL_SMOOTH);
	glBegin(GL_TRIANGLES);
		glColor4f(0.0, 1.0, 0.0, 0.5);     //verde
		glVertex3f(-10, -4.9, 30);
		glColor4f(0.0f, 1.0, 0.0, 0.5);     //verde
		glVertex3f(10, -4.9, 30);
		glColor4f(pPower, 0.0, 0.0, 0.5);     //vermelho
		glVertex3f(0, -4.9, 30 - 30 * pPower);
	glEnd();
	glDisable(GL_SMOOTH);
}

void draw_esfera() {
	glColor4f(1.0, 1.0, 1.0, 1);
	GLUquadricObj*  esfera = gluNewQuadric();
	glEnable(GL_TEXTURE_2D);
	glPushMatrix();
		glTranslatef(xBola, yBola, zBola);
		glBindTexture(GL_TEXTURE_2D, texture[1]);
		gluQuadricDrawStyle(esfera, GLU_FILL);
		gluQuadricNormals(esfera, GLU_SMOOTH);
		gluQuadricTexture(esfera, GL_TRUE);
		gluSphere(esfera, rBola, 150, 150);
		gluDeleteQuadric(esfera);
	glPopMatrix();
	glDisable(GL_TEXTURE_2D);
}

void draw_caixa(){
	glColor4f(1.0, 1.0, 1.0, 1);
	glEnable(GL_TEXTURE_2D);
	glPushMatrix();
		glBindTexture(GL_TEXTURE_2D, texture[3]);
		glTranslated(xCaixa, yCaixa, zCaixa);
		glBegin(GL_QUADS); //plano xoy
			glNormal3f(0, 0, 1);
			glTexCoord2f(0, 0);		glVertex3f(-larguraCaixa / 2, -larguraCaixa / 2, larguraCaixa / 2);
			glTexCoord2f(1, 0);		glVertex3f(larguraCaixa / 2, -larguraCaixa / 2, larguraCaixa / 2);
			glTexCoord2f(1, 1);		glVertex3f(larguraCaixa / 2, larguraCaixa / 12, larguraCaixa / 2);
			glTexCoord2f(0, 1);		glVertex3f(-larguraCaixa / 2, larguraCaixa / 12, larguraCaixa / 2);
		glEnd();
		glBegin(GL_QUADS); //yoz
			glNormal3f(1, 0, 0);
			glTexCoord2f(0, 0);		glVertex3f(larguraCaixa / 2, -larguraCaixa / 2, larguraCaixa / 2);
			glTexCoord2f(1, 0);		glVertex3f(larguraCaixa / 2, -larguraCaixa / 2, -larguraCaixa / 2);
			glTexCoord2f(1, 1);		glVertex3f(larguraCaixa / 2, larguraCaixa / 2, -larguraCaixa / 2);
			glTexCoord2f(0, 1);		glVertex3f(larguraCaixa / 2, larguraCaixa / 12, larguraCaixa / 2);
		glEnd();
		glBegin(GL_QUADS); //tras
			glNormal3f(0, 0, -1);
			glTexCoord2f(0, 0);		glVertex3f(larguraCaixa / 2, -larguraCaixa / 2, -larguraCaixa / 2);
			glTexCoord2f(1, 0);		glVertex3f(-larguraCaixa / 2, -larguraCaixa / 2, -larguraCaixa / 2);
			glTexCoord2f(1, 1);		glVertex3f(-larguraCaixa / 2, larguraCaixa / 2, -larguraCaixa / 2);
			glTexCoord2f(0, 1);		glVertex3f(larguraCaixa / 2, larguraCaixa / 2, -larguraCaixa / 2);
		glEnd();
		glBegin(GL_QUADS); //yoz
			glNormal3f(-1, 0, 0);
			glTexCoord2f(0, 0);		glVertex3f(-larguraCaixa / 2, larguraCaixa / 12, larguraCaixa / 2); 
			glTexCoord2f(1, 0);		glVertex3f(-larguraCaixa / 2, larguraCaixa / 2, -larguraCaixa / 2); 
			glTexCoord2f(1, 1);		glVertex3f(-larguraCaixa / 2, -larguraCaixa / 2, -larguraCaixa / 2);
			glTexCoord2f(0, 1);		glVertex3f(-larguraCaixa / 2, -larguraCaixa / 2, larguraCaixa / 2);
		glEnd();
		if (fundoCaixa){
			glBegin(GL_QUADS); //fundo
				glNormal3f(0, 1, 0);
				glTexCoord2f(0, 0);		glVertex3f(-larguraCaixa / 2, -larguraCaixa / 2, larguraCaixa / 2);
				glTexCoord2f(1, 0);		glVertex3f(larguraCaixa / 2, -larguraCaixa / 2, larguraCaixa / 2);
				glTexCoord2f(1, 1);		glVertex3f(larguraCaixa / 2, -larguraCaixa / 2, -larguraCaixa / 2);
				glTexCoord2f(0, 1);		glVertex3f(-larguraCaixa / 2, -larguraCaixa / 2, -larguraCaixa / 2);
			glEnd();
		}
	glPopMatrix();
	glDisable(GL_TEXTURE_2D);
}

void draw_fundo() {
	float i, j;
	i = 10;
	j = 10;

	glColor4f(1.0, 1.0, 1.0, 1);
	glEnable(GL_TEXTURE_2D);
	glPushMatrix();
		glBindTexture(GL_TEXTURE_2D, texture[0]);
		if (malha){
			for (i = -40; i < 40; i++){
				for (j = -20; j < 20; j++){
					glNormal3f(0, 1, 0);
					glBegin(GL_TRIANGLES);
					glTexCoord2f((i + 40) / 80, (j + 20) / 40);		glVertex3f((j*-1) - 1, -5, (i*-1) - 1); //z = i
					glTexCoord2f((i + 41) / 80, (j + 20) / 40);		glVertex3f((j*-1) - 1, -5, (i*-1));
					glTexCoord2f((i + 41) / 80, (j + 21) / 40);		glVertex3f((j*-1), -5, (i*-1));
					glEnd();
					glBegin(GL_TRIANGLES);
					glTexCoord2f((i + 40) / 80, (j + 20) / 40);		glVertex3f((j*-1) - 1, -5, (i*-1) - 1);
					glTexCoord2f((i + 41) / 80, (j + 21) / 40);		glVertex3f((j*-1), -5, (i*-1));
					glTexCoord2f((i + 40) / 80, (j + 21) / 40);		glVertex3f((j*-1), -5, (i*-1) - 1);
					glEnd();
				}
			}
		}
		else{
			glBegin(GL_QUADS);
				glNormal3f(0, 1, 0);
				glTexCoord2f(0, 0);		glVertex3f(-20, -5, -40);
				glTexCoord2f(1, 0);		glVertex3f(-20, -5, 40);
				glTexCoord2f(1, 1);		glVertex3f(20, -5, 40);
				glTexCoord2f(0, 1);		glVertex3f(20, -5, -40);
			glEnd();
		}
	glPopMatrix();
	glDisable(GL_TEXTURE_2D);
}

void draw_fundo_ambiente() {
	glColor4f(1.0, 1.0, 1.0, 1);
	glEnable(GL_TEXTURE_2D);
	glPushMatrix();
		glBindTexture(GL_TEXTURE_2D, texture[4]);
		glBegin(GL_QUADS); //xoy
			glNormal3f(0, 0, -1);
			glTexCoord2f(0, 0);		glVertex3f(larguraFundo / 2, -alturaFundo / 2, comprimentoFundo / 2); 
			glTexCoord2f(1, 0);		glVertex3f(-larguraFundo / 2, -alturaFundo / 2, comprimentoFundo / 2); 
			glTexCoord2f(1, 1);		glVertex3f(-larguraFundo / 2, alturaFundo / 2, comprimentoFundo / 2);
			glTexCoord2f(0, 1);		glVertex3f(larguraFundo / 2, alturaFundo / 2, comprimentoFundo / 2);
		glEnd();
		glBegin(GL_QUADS); //yoz
			glNormal3f(-1, 0, 0);
			glTexCoord2f(0, 0);		glVertex3f(larguraFundo / 2, -alturaFundo / 2, -comprimentoFundo / 2); 
			glTexCoord2f(1, 0);		glVertex3f(larguraFundo / 2, -alturaFundo / 2, comprimentoFundo / 2);
			glTexCoord2f(1, 1);		glVertex3f(larguraFundo / 2, alturaFundo / 2, comprimentoFundo / 2);
			glTexCoord2f(0, 1);		glVertex3f(larguraFundo / 2, alturaFundo / 2, -comprimentoFundo / 2);
		glEnd();
		glBegin(GL_QUADS); //xoy
			glNormal3f(0, 0, 1);
			glTexCoord2f(0, 0);		glVertex3f(-larguraFundo / 2, -alturaFundo / 2, -comprimentoFundo / 2); 
			glTexCoord2f(1, 0);		glVertex3f(larguraFundo / 2, -alturaFundo / 2, -comprimentoFundo / 2);
			glTexCoord2f(1, 1);		glVertex3f(larguraFundo / 2, alturaFundo / 2, -comprimentoFundo / 2);
			glTexCoord2f(0, 1);		glVertex3f(-larguraFundo / 2, alturaFundo / 2, -comprimentoFundo / 2);
		glEnd();
		glBegin(GL_QUADS); //yoz
			glNormal3f(1, 0, 0);
			glTexCoord2f(0, 0);		glVertex3f(-larguraFundo / 2, -alturaFundo / 2, comprimentoFundo / 2); 
			glTexCoord2f(1, 0);		glVertex3f(-larguraFundo / 2, -alturaFundo / 2, -comprimentoFundo / 2);
			glTexCoord2f(1, 1);		glVertex3f(-larguraFundo / 2, alturaFundo / 2, -comprimentoFundo / 2);
			glTexCoord2f(0, 1);		glVertex3f(-larguraFundo / 2, alturaFundo / 2, comprimentoFundo / 2);
		glEnd();
		//AGUA
		glBindTexture(GL_TEXTURE_2D, texture[5]);
		glBegin(GL_QUADS);
			glNormal3f(0, 1, 0);
			glTexCoord2f(0, 0);		glVertex3f(larguraFundo / 2, -alturaFundo / 2, comprimentoFundo / 2);
			glTexCoord2f(1, 0);		glVertex3f(larguraFundo / 2, -alturaFundo / 2, -comprimentoFundo / 2);
			glTexCoord2f(1, 1);		glVertex3f(-larguraFundo / 2, -alturaFundo / 2, -comprimentoFundo / 2);
			glTexCoord2f(0, 1);		glVertex3f(-larguraFundo / 2, -alturaFundo / 2, comprimentoFundo / 2);
		glEnd();
	glPopMatrix();
	glDisable(GL_TEXTURE_2D);
}

void resizeWindow(int w, int h) {
	glViewport(0, 0, w, h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(66.0, (float)w / (float)h, 1.0, 250.0);
}

void defineLuz()
{
	glEnable(GL_LIGHT0);
	glLightfv(GL_LIGHT0, GL_POSITION, lightpos);
	glLightf(GL_LIGHT0, GL_SPOT_CUTOFF, focoLuz);
	glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, dir);
}

void display() {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	gluLookAt(obs[0], obs[1], obs[2], 0, 0, 0, 0, 1, 0);

	defineLuz();
	//====FORCA DE LANCAMENTO================
	if (pressButton == 1){
		if (pPower < 1){
			pPower += 0.025;
		}
		draw_power();
	}

	draw_caixa();
	draw_esfera();
	draw_fundo();
	draw_fundo_ambiente();

	/*
	//1. Activa o uso do stencil buffer
	glEnable(GL_STENCIL_TEST);
	//2. Nao escreve no color buffer
	glColorMask(0, 0, 0, 0);
	//3. Torna inactivo o teste de profundidade
	glDisable(GL_DEPTH_TEST);
	//4. Coloca a 1 todos os pixels no stencil buffer que representam o chão
	glStencilFunc(GL_ALWAYS, 1, 1);
	glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
	//5. Desenhar o chão
	draw_fundo();
	//6. Activa a escrita de cor
	glColorMask(1, 1, 1, 1);
	//7. Activa o teste de profundidade
	glEnable(GL_DEPTH_TEST);

	//8. O stencil test passa apenas quando o pixel tem o valor 1 no stencil buffer
	glStencilFunc(GL_EQUAL, 1, 1);
	//9. Stencil buffer read-only
	glStencilFunc(GL_KEEP, GL_KEEP, GL_KEEP);

	//10. Desenha o objecto com a reflexão onde stencil buffer é 1
	glPushMatrix();
	//mudança de escala em torno do eixo dos yy
	glTranslatef(0.0, -10, 0.0);
	glScalef(1, -1, 1);
	draw_esfera();
	glPopMatrix();

	//11. Desactiva a utilização do stencil buffer
	glDisable(GL_STENCIL_TEST);

	//Blending
	glEnable(GL_BLEND);
	glColor4f(1.0, 1.0, 1.0, 0.7);
	draw_fundo();
	glDisable(GL_BLEND);
	//FIM REFLEXÃO*/
	
	glutSwapBuffers();
}

void update(int valor) {

	//CAIXA
	//limites com e sem bola
	if (xCaixa >= limiteCaixa && onBox == 1 && fundoCaixa == 1){
		//cair bola para lado direito
		glTranslated(larguraCaixa / 2, larguraCaixa / 2 - yCaixa, 0);
		if (xCaixa >= limiteCaixa){ //bola cai
			fundoCaixa = 0;
		}

	}
	else if (xCaixa <= limiteCaixa*(-1) && onBox == 1 && fundoCaixa == 1){
		//cair bola para lado esquerdo
		glTranslated(larguraCaixa / 2, larguraCaixa / 2 - yCaixa, 0);
		if (xCaixa <= limiteCaixa*(-1)){ //bola cai
			fundoCaixa = 0;
		}
	}
	else if (xCaixa > limiteCaixa && fundoCaixa == 1){
		inc = -0.5;
		xCaixa += inc;
	}
	else if (xCaixa < limiteCaixa*(-1) && fundoCaixa == 1){
		inc = 0.5;
		xCaixa += inc;
	}
	else if (fundoCaixa == 1){
		xCaixa += inc;
	}

	//BOLA
	if (movebola == 1 && onBox == 0 && fundoCaixa == 1){
		//parte da frente
		if ((zBola - rBola <= zCaixa + larguraCaixa / 2) &&
			(yBola - rBola <= yCaixa - (yCaixa - larguraCaixa / 12) && yBola + rBola >= yCaixa - larguraCaixa / 2) &&
			(xBola - rBola >= xCaixa - larguraCaixa / 2 && xBola + rBola <= xCaixa - larguraCaixa / 2)){
			vel_bola_voadora = vel_bola_voadora * (-1);
			zBola += vel_bola_voadora;
		}
		//se bola acertar dentro da caixa
		else if ((xBola - rBola >= xCaixa - larguraCaixa / 2) && (xBola + rBola <= xCaixa + larguraCaixa / 2) &&
			(yBola + rBola <= yCaixa + larguraCaixa / 2) &&
			(zBola + rBola <= zCaixa + larguraCaixa / 2) && (zBola - rBola >= zCaixa - larguraCaixa / 2)){
			//se entrou na caixa
			onBox = 1;
		}
		//se acertar em esquina esquerda
		else if ((xBola - rBola <= xCaixa - larguraCaixa / 2 && xBola + rBola >= xCaixa - larguraCaixa / 2) &&
			(zBola + rBola <= zCaixa + larguraCaixa / 2 && zBola - rBola >= zCaixa - larguraCaixa / 2) &&
			(yBola - rBola <= yCaixa + larguraCaixa / 2 - zBola - zCaixa && yBola - rBola <= yCaixa + larguraCaixa / 2)){
			if (xBola - xCaixa - larguraCaixa / 2 < 0){
				xinc = 0.7;
				onBox = 1;
			}
			else
				xinc = -0.7;
			zBola += vel_bola_voadora; //anda bola para a frente
			velocidade -= decr_vel; //velocidade emy decrementa
			yBola += velocidade; //y da bola decrementa
		} //esquina direita
		else if ((xBola - rBola <= xCaixa + larguraCaixa / 2 && xBola + rBola >= xCaixa + larguraCaixa / 2) &&
			(zBola + rBola <= zCaixa + larguraCaixa / 2 && zBola - rBola >= zCaixa - larguraCaixa / 2) &&
			(yBola - rBola <= yCaixa + larguraCaixa / 2 - zBola - zCaixa && yBola - rBola <= yCaixa + larguraCaixa / 2)){
			if (xBola - xCaixa + larguraCaixa / 2 < 0)
				xinc = 0.7;
			else{
				xinc = -0.7;
				onBox = 1;
			}
			zBola += vel_bola_voadora; //anda bola para a frente
			velocidade -= decr_vel; //velocidade emy decrementa
			yBola += velocidade; //y da bola decrementa
		}
		//se ainda nao tocou no chao
		else if (yBola > -5 + rBola){
			zBola += vel_bola_voadora; //anda bola para a frente
			velocidade -= decr_vel; //velocidade emy decrementa
			yBola += velocidade; //y da bola decrementa
		}
		else if (yBola <= -5 + rBola && zBola+rBola> -40){ //se tocou no chao
			if ((vel_bola_voadora <= -0.1 || vel_bola_voadora >= 0.1)){
				yBola = -5 + rBola;
				vel_bola_voadora += 0.2;
				zBola += vel_bola_voadora; //suaviza
			}
			else{
				movebola = 0;
				pPower = 0.0;
				xBola = 0;
				yBola = 0;
				zBola = 20;
				xinc = 0;
				vel_bola_voadora = -1.5;
				click = 0;
			}
		}
		else{
			velocidade -= decr_vel; //velocidade emy decrementa
			yBola += velocidade; //y da bola decrementa
		}

	}
	//dentro da caixa
	if (movebola == 1 && onBox == 1 && fundoCaixa == 1){
		if (zBola - rBola >= zCaixa - larguraCaixa / 2){//se tocou na parede dentro da caixa
			zBola = (zCaixa - larguraCaixa / 2) + rBola; //parou
		}
		if (yBola - rBola > yCaixa - larguraCaixa / 2){ //ainda nao atingui o fundo da caixa
			velocidade -= decr_vel; //velocidade emy decrementa
			yBola += velocidade; //y da bola decrementa
		}
		else if (yBola - rBola <= yCaixa - larguraCaixa / 2){
			yBola = (yCaixa - larguraCaixa / 2) + rBola;
		}
		if(xBola + rBola > xCaixa+larguraCaixa/2){
			xBola = (xCaixa+larguraCaixa/2) -rBola;
		}
		else if(xBola-rBola < xCaixa-larguraCaixa/2){
			xBola = (xCaixa-larguraCaixa/2) + rBola;
		}

		xBola += inc;
		limiteCaixa = 25;
		xinc = 0;
	}
	if (fundoCaixa == 0){
		velocidade -= decr_vel; //velocidade emy decrementa
		yBola += velocidade; //y da bola decrementa
	}

	if (yBola < -alturaFundo/2){
		fundoCaixa = 1;
		movebola = 0;
		pPower = 0.0;
		xBola = 0;
		yBola = 0;
		zBola = 20;
		xinc = 0;
		vel_bola_voadora = -1.5;
		onBox = 0;
		limiteCaixa = 20;
		click = 0;
	}

	glutPostRedisplay();
	glutTimerFunc(24, update, 0);
}

void keyboard(unsigned char key, int x, int y){
	if (key == 27) exit(0);
	if (key == 'e') focoLuz += 5;
	if (key == 'r') focoLuz -= 5;
	if (key == 'w') lightpos[2] -= 2.0;
	if (key == 's') lightpos[2] += 2.0;
	if (key == 'a') lightpos[0] -= 2.0;
	if (key == 'd') lightpos[0] += 2.0;
	if (key == 'z') lightpos[1] -= 1.0;
	if (key == 'x') lightpos[1] += 1.0;

	if (key == 'm' && malha == 1) malha = 0;
	else if (key == 'm' && malha == 0) malha = 1;
	
	if (key == 'n' && nevoeiro == 0){
		nevoeiro = 1;
		glEnable(GL_FOG);
	}
	else if (key == 'n' && nevoeiro == 1){
		nevoeiro = 0;
		glDisable(GL_FOG);
	}
	glutPostRedisplay();
}

void teclasNotAscii(int key, int x, int y){

	if (key == GLUT_KEY_UP && raioObs>=1){
		raioObs -= 0.5;
	}
	if (key == GLUT_KEY_DOWN && raioObs <= 100){
		raioObs += 0.5;
	}

	if (key == GLUT_KEY_LEFT){
		angObs = (angObs + 0.1);
	}
	if (key == GLUT_KEY_RIGHT){
		angObs = (angObs - 0.1);
	}
	obs[0] = raioObs*cos(angObs);
	obs[2] = raioObs*sin(angObs);
	glutPostRedisplay();

}

void mouseClicks(int button, int state, int x, int y){
	if ((button == GLUT_LEFT_BUTTON) && (state == GLUT_DOWN) && click == 0)
	{
		pressButton = 1;
	}

	if ((button == GLUT_LEFT_BUTTON) && (state == GLUT_UP) && click == 0)
	{
		// entao dispara a bola!
		velocidade = pPower + 0.5;
		pressButton = 0;
		movebola = 1;
		click = 1;

	}
}

int main(int argc, char** argv) {
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH | GLUT_STENCIL);
	glutInitWindowSize(800, 600);
	glutCreateWindow("TennisBox 2014/2015");
	init();

	glutDisplayFunc(display);
	glutKeyboardFunc(keyboard);
	glutMouseFunc(mouseClicks);
	glutReshapeFunc(resizeWindow);
	glutTimerFunc(24, update, 0);
	glutSpecialFunc(teclasNotAscii);

	glutMainLoop();
	return 0;
}









