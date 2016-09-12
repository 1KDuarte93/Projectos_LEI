/* Criado por:
Kevin Duarte nº 2011159671
Ruben Leal nº2011181710
*/
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <pthread.h>
#include <semaphore.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/mman.h>  
#include <limits.h>  
#include <sys/shm.h>

#define PIPE_NAME   "np_client_server"
#define N 4

/*CENAS DE RODAR IMG*/
#define	FILE_MODE	(S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH)

#ifndef	MAP_FILE	/* 44BSD defines this & requires it to mmap files */
#define	MAP_FILE	0	/* to compile under systems other than 44BSD */
#endif

#define SIZE 20

#define DEBUG //Tirar o comentário desta linha para informações sobre DEBUG

struct stat statbuf;

struct pixel
{
	char R;
	char G;
	char B;
};

char * src;
int size;

struct pixel *get_pixel(char *buf, int *pos)
{
	struct pixel pix;
	struct pixel *ppix = &pix;
	ppix->R = buf[*pos];
	ppix->G = buf[(*pos)+1];
	ppix->B = buf[(*pos)+2];
	(*pos) += 3;
	return ppix;
}
/* FIM DAS CENAS DE RODAR IMAGEM*/

/*FUNCOES DECLARADAS*/
void rotation90();
void rotation180();
void rotation270();
void * worker(void* b);
int get_stat(int fdin);
int maiorfd(int fd0, int fd1, int fd2, int fd3);
/*FUNCOES DECLARADAS PARA RODAR IMAGEM*/
void write_pixel(struct pixel *ppix, char *buf, int *pos);
void * do_rotation(void *rotation);//EM PRINCIPIO NAO É NECESSARIA
int get_stat(int fdin); 
/*FIM DAS FUNCOES DECLARADAS PARA RODAR IMAGEM*/

/*VARIAVEIS GLOBAIS*/
int zero2p[2];
int p2zero[2];
int um2p[2];
int p2um[2];
int dois2p[2];
int p2dois[2];
int father_pid;
pthread_t thread90[N]; //4 threads para rotation 90
pthread_t thread180[N]; //4 threads para rotation 180
pthread_t thread270[N]; //4 threads para rotation 270
//POSIÇOES E BUFERS ENTRE THREADS
int write_pos90=0, read_pos90=0;
int write_pos180=0, read_pos180=0;
int write_pos270=0, read_pos270=0;
char buffer90[2*N][50]; //2x PARA QUE O BUFFER GUARDE EM [0] O NOME DA IMG E [1] O ID DO PROCESSO CLIENTE 
char buffer180[2*N][50];
char buffer270[2*N][50];
//90
pthread_mutex_t mutex90 = PTHREAD_MUTEX_INITIALIZER;
sem_t empty90;
sem_t full90;
//180
pthread_mutex_t mutex180 = PTHREAD_MUTEX_INITIALIZER;
sem_t empty180;
sem_t full180;
//270
pthread_mutex_t mutex270 = PTHREAD_MUTEX_INITIALIZER;
sem_t empty270;
sem_t full270;

//PRIORIDADE
//90
pthread_mutex_t pmutex90 = PTHREAD_MUTEX_INITIALIZER;
//180
pthread_mutex_t pmutex180 = PTHREAD_MUTEX_INITIALIZER;
//270
pthread_mutex_t pmutex270 = PTHREAD_MUTEX_INITIALIZER;


/*ESTRUTURA*/
typedef struct{
	pid_t id;
	int rotation;
	char file[64];
}dados;



void sigint(int signum) {
	if (getpid() == father_pid) {
		while(wait(NULL)!=-1);
		printf("Ok, bye bye!\n"); 
	} else printf ("Another child is dead!\n");
	exit(0);
}

void init(){
	sem_init(&empty90,0,N);
	sem_init(&full90, 0, 0);
	sem_init(&empty180,0,N);
	sem_init(&full180, 0, 0);
	sem_init(&empty270,0,N);
	sem_init(&full270, 0, 0);
}

void criar_pipe(){
	if (pipe(p2zero) == -1 || pipe(zero2p) == -1){
		printf ("ERRO NO PIPE!\n");
		exit(0);
	}
	if (pipe(p2um) == -1 || pipe(um2p) == -1){
		printf ("ERRO NO PIPE!\n");
		exit(0);
	}
	if (pipe(p2dois) == -1 || pipe(dois2p) == -1){
		printf ("ERRO NO PIPE!\n");
		exit(0);
	}
	 if ((mkfifo(PIPE_NAME, O_CREAT|O_EXCL|0600)<0) && (errno!= EEXIST))
  	{
	    perror("Cannot create pie: ");
	    exit(0);
  	}

}

void criar_processos(){
	if (fork() == 0){
		rotation90();
		exit(0);
	}
	if (fork()==0){
		rotation180();
		exit(0);
	}
	if (fork()==0){
		rotation270();
		exit(0);
	}

}

void fechar_pipes(){
	close(p2zero[0]);
	close(zero2p[1]);
	close(p2um[0]);
	close(um2p[1]);
	close(p2dois[0]);
	close(dois2p[1]);
}

int main() {
  // Redirects SIGINT to sigint()
	init();
  	signal(SIGINT, sigint);
  	#ifdef DEBUG 
		printf("A criar 3 unnamed pipes e 1 named_pipe!\n");
	#endif  
  	criar_pipe();
  	#ifdef DEBUG 
		printf("Pipes criados!\n");
	#endif
  	father_pid = getpid();
  	#ifdef DEBUG 
		printf("A criar 3 processos!\n");
	#endif
  	criar_processos();
  	#ifdef DEBUG 
		printf("Processos criados!\n");
		printf("A fechar leitura/escrita pipes no processo pai!\n");
	#endif
  	fechar_pipes();
  	#ifdef DEBUG 
		printf("Pipes fechados para leitura/escrita!\n");
	#endif

  	int fd_named;
  	if ((fd_named=open(PIPE_NAME, O_RDWR)) < 0)
  	{
    	perror("Cannot open pipe for reading: ");
    	exit(0);
  	}

	int id_client;
	dados pedido;
	#ifdef DEBUG 
		printf("Processo pai pronto para receber pedidos!\n");
	#endif
		int maior;
	while (1) {
		fd_set read_set;
	    FD_ZERO(&read_set);
	    FD_SET(fd_named, &read_set); //adicionar named_pipe
	    FD_SET(zero2p[0], &read_set);
	    FD_SET(um2p[0], &read_set);
	    FD_SET(dois2p[0], &read_set);
	    maior = maiorfd(zero2p[0], um2p[0], dois2p[0], fd_named);
	    if ( select(maior, &read_set, NULL, NULL, NULL) != -1 ) {
	    	if (FD_ISSET(zero2p[0], &read_set)){
	    		read(zero2p[0], &id_client, sizeof(id_client));
	    		#ifdef DEBUG 
					printf("Recebido no server sinal de imagem concluida 90, id->%d\n", id_client);
				#endif
				kill (id_client, SIGUSR1);
	    	}
	    	if (FD_ISSET(um2p[0], &read_set)){
	    		read(um2p[0], &id_client, sizeof(id_client));
	    		#ifdef DEBUG 
					printf("Recebido no server sinal de imagem concluida 180, id->%d\n", id_client);
				#endif
				kill (id_client, SIGUSR1);
	    	}
	    	if (FD_ISSET(dois2p[0], &read_set)){
	    		read(dois2p[0], &id_client, sizeof(id_client));
	    		#ifdef DEBUG 
					printf("Recebido no server sinal de imagem concluida 270, id->%d\n", id_client);
				#endif
				kill (id_client, SIGUSR1);
	    	}
	    	if (FD_ISSET(fd_named, &read_set)){
	    		read(fd_named, &pedido, sizeof(dados));
  				if((pedido.rotation) == 90){
  					write(p2zero[1], &pedido, sizeof(dados));
  					#ifdef DEBUG 
						printf("Pedido de rotação 90 recebido! ->ID: %d, ->IMG: %s\n", pedido.id, pedido.file);
					#endif
  				}
  				else if((pedido.rotation) == 180){
  					write(p2um[1], &pedido, sizeof(dados));
  					#ifdef DEBUG 
						printf("Pedido de rotação 180 recebido! ->ID: %d, ->IMG: %s\n", pedido.id, pedido.file);
					#endif
  				}
  				else if((pedido.rotation) == 270){
  					write(p2dois[1], &pedido, sizeof(dados));
  					#ifdef DEBUG 
						printf("Pedido de rotação 270 recebido! ->ID: %d, ->IMG: %s\n", pedido.id, pedido.file);
					#endif
  				}
  				else{
  					#ifdef DEBUG
  						printf("Erro no numero de rotacao!\n");
  					#endif
  					kill (pedido.id, SIGUSR2);
  				}
  				
	    	}
		}
  }
  
  return 0;
}

void rotation90() {
  	close(p2zero[1]); //fecha esrcita do p2zero
  	close(zero2p[0]); //fecha leitura do zero2p
  	int i;
  	int id_rotation[2];
  	//CRIAR AS THREADS
  	#ifdef DEBUG 
		printf("A criar threads no rotation90()\n");
	#endif
  	for(i=0; i<N; i++){
  		id_rotation[0] = i; //ID
  		id_rotation[1] = 90; //rotacao
  		pthread_create(&thread90[i], NULL, worker, (void *) id_rotation);
	}
	#ifdef DEBUG 
		printf("Threads criadas no rotation90()\n");
	#endif
  	dados pedido;
  	while (1) {
		fd_set read_set;
    	FD_ZERO(&read_set);
    	FD_SET(p2zero[0], &read_set);
    	if ( select(p2zero[0]+1, &read_set, NULL, NULL, NULL) != -1 ) {
			read(p2zero[0], &pedido, sizeof(dados));
			#ifdef DEBUG 
				printf("RECEBIDO EM ROT90: rotacao: %d, id: %d, imagem: %s\n", pedido.rotation, pedido.id, pedido.file);
			#endif
			
			sem_wait(&empty90);
		    pthread_mutex_lock(&mutex90);


		    strcpy(buffer90[write_pos90], pedido.file);//ESCREVE NO BUFFER O NOME DA IMG
		    write_pos90 = (write_pos90+1) % N; //ACTUALIZA A POSICAO DE ESCRITA		
		    char c[20];		//PASSA PRA STRING E METE NO ARRAY DE BUFFER
		    sprintf(c, "%d", pedido.id);
		    strcpy(buffer90[write_pos90], c);
		    write_pos90 = (write_pos90+1) % N;
		    sem_post(&full90);
		    pthread_mutex_unlock(&pmutex90);
		    pthread_mutex_unlock(&mutex90);
		} 
  	}
  	//DESFAZ AS THREADS
  	for (i=0; i<N; i++){
  		pthread_join(thread90[i], NULL);
  	}
}

void rotation180() {
  	close(p2um[1]);
  	close(um2p[0]);
  	//CRIAR THREADS
  	#ifdef DEBUG 
		printf("A criar threads no rotation180()\n");
	#endif
  	int id_rotation[2], i;
  	for(i=0; i<N; i++){
  		id_rotation[0] = i; //ID
  		id_rotation[1] = 180; //rotacao
  		pthread_create(&thread180[i], NULL, worker, (void *) id_rotation);
	}
	#ifdef DEBUG 
		printf("Threads criadas no rotation180()\n");
	#endif
  	dados pedido;
  	while (1) {
		fd_set read_set;
    	FD_ZERO(&read_set);
    	FD_SET(p2um[0], &read_set);
    	if ( select(p2um[0]+1, &read_set, NULL, NULL, NULL) != -1 ) {
			read(p2um[0], &pedido, sizeof(dados));
			#ifdef DEBUG 
				printf("RECEBIDO EM ROT180: rotacao: %d, id: %d, imagem: %s\n", pedido.rotation, pedido.id, pedido.file);
			#endif
			sem_wait(&empty180);
			pthread_mutex_lock(&mutex180);
			strcpy(buffer180[write_pos180], pedido.file);//ESCREVE NO BUFFER O NOME DA IMG
			write_pos180 = (write_pos180+1) % N; //ACTUALIZA A POSICAO DE ESCRITA
			char c[20];
			sprintf(c, "%d", pedido.id);
		    strcpy(buffer180[write_pos180], c);
		    write_pos180 = (write_pos180+1) % N;
			sem_post(&full180);
			pthread_mutex_unlock(&pmutex180);
			pthread_mutex_unlock(&mutex180);
		} 
  	}
  	//DESFAZ AS THREADS
  	for (i=0; i<N; i++){
  		pthread_join(thread180[i], NULL);
  	}
}

void rotation270() {
  	close(p2dois[1]);
  	close(dois2p[0]);
  	int id_rotation[2], i;
  	#ifdef DEBUG 
		printf("A criar threads no rotation270()\n");
	#endif
  	for(i=0; i<N; i++){
  		id_rotation[0] = i; //ID
  		id_rotation[1] = 270; //rotacao
  		pthread_create(&thread270[i], NULL, worker, (void *) id_rotation);
	}
	#ifdef DEBUG 
		printf("Threads criadas no rotation270()\n");
	#endif
  	dados pedido;
  	while (1) {
		fd_set read_set;
    	FD_ZERO(&read_set);
    	FD_SET(p2dois[0], &read_set);
	    if ( select(p2dois[0]+1, &read_set, NULL, NULL, NULL) != -1 ) {
			read(p2dois[0], &pedido, sizeof(dados));
			#ifdef DEBUG 
				printf("RECEBIDO EM ROT270: rotacao: %d, id: %d, imagem: %s\n", pedido.rotation, pedido.id, pedido.file);;
			#endif
			sem_wait(&empty270);
			pthread_mutex_lock(&mutex270);
			strcpy(buffer270[write_pos270], pedido.file);//ESCREVE NO BUFFER O NOME DA IMG
			write_pos270 = (write_pos270+1) % N; //ACTUALIZA A POSICAO DE ESCRITA
			char c[20];
			sprintf(c, "%d", pedido.id);
		    strcpy(buffer270[write_pos270], c);
		    write_pos270 = (write_pos270+1) % N;
		    #ifdef DEBUG 
				printf("Escrito em buffer270: id: %d, imagem: %s\n", pedido.id, pedido.file);
			#endif
			sem_post(&full270);
			pthread_mutex_unlock(&pmutex270);
			pthread_mutex_unlock(&mutex270);
		};
  	}
  	//DESFAZ AS THREADS
  	for (i=0; i<N; i++){
  		pthread_join(thread270[i], NULL);
  	}
}

int maiorfd(int fd0, int fd1, int fd2, int fd3){
	int maior=0;
	if (fd0>maior)
		maior = fd0;
	if (fd1>maior)
		maior = fd1;
	if (fd2>maior)
		maior = fd2;
	if (fd3>maior)
		maior = fd3;
	return (maior+1);
}

/*WORKER*/
void * worker(void* b){ //passar ponteiro para o buffer!
	int *array = (int *) b;
	int id_thread = array[0];//ID 
	int rotation = array[1]; //rotation
	int id_client;


	/*COMEÇAR A RODAR IMG*/
	int	fdout;
	char	*dst;
	char fich[SIZE];
	int fdin;
	int counter,index, pos, x, y;
	int xmax = 0;
	int ymax = 0;
	int colormax = 0;
	int sucesso = 1;
	if (rotation==90){
		while(1){
			/*THREADS*/
			sem_wait (&full90);
			pthread_mutex_lock(&pmutex90);
			pthread_mutex_lock(&mutex90);

			char *e = malloc(50*sizeof(char));
			strcpy(e, buffer90[read_pos90]); //LE O NOME DA IMG
			read_pos90 = (read_pos90+1) %N;
			#ifdef DEBUG 
				printf("Leu do buffer na thread ID[%d] de rotation %d a img: %s\n", id_thread, rotation, e);
			#endif
			id_client = atoi(buffer90[read_pos90]);
			read_pos90 = (read_pos90+1) %N;
			/*JA LEU, JA PODE DEIXAR O SEMAFORO*/
			pthread_mutex_unlock(&mutex90);
			sem_post(&empty90);
			/*MAIN*/
			if ( (fdin = open(e, O_RDONLY)) < 0){
				fprintf(stderr,"can't open for reading\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			size = get_stat(fdin);

		/* Zona # 1*/
			if ( (src = mmap(0, size, PROT_READ, MAP_FILE | MAP_SHARED, fdin, 0)) == (caddr_t) -1){
				fprintf(stderr,"mmap error for input\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			/* Construção do nome do ficheiro de saída */
			int tam = strlen(e);
			e[tam-4] = '\0';
			sprintf(fich, "%s_%d.ppm", e, rotation);
			//sprintf(fich, "yaa.ppm");

			/* Zona #2 */
			if ( (fdout = open(fich, O_RDWR | O_CREAT | O_TRUNC,FILE_MODE)) < 0){
				fprintf(stderr,"can't creat %s for writing\n", fich);
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}

			/* set size of output file */
			if (lseek(fdout, size - 1, SEEK_SET) == -1){
				fprintf(stderr,"lseek error\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			if (write(fdout, "", 1) != 1){
				fprintf(stderr,"write error\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			

			if ( (dst = mmap(0, size, PROT_READ | PROT_WRITE,MAP_SHARED,fdout, 0)) == (caddr_t) -1){
				fprintf(stderr,"mmap error for output\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			/* Fim da Zona #2 */ 

			sscanf(src,"P6\n%d %d\n%d\n",&xmax,&ymax,&colormax);
			
			struct pixel imagem [ymax][xmax];
			
			for (counter=0, index=0; counter<3;index++){
				if (src[index]=='\n')
					++counter;
			} 	
			pos=index-1;
			for (y=0;y<ymax;y++)
				for (x=0;x<xmax;x++)
					imagem[y][x] = *(get_pixel(src,&pos));
			pos=index;
			sprintf(dst,"P6\n%d %d\n%d\n",ymax,xmax,colormax);
			for (x=0; x<xmax; x++)
				for (y=ymax-1; y> -1;y--)
					write_pixel(&(imagem[y][x]),dst,&pos);
			/* Zona #3 */
			munmap(dst,size);
			munmap(src, size);
			close(fdout);
			close (fdin);
			/* Fim da Zona #3 */
			#ifdef DEBUG 
				printf("Imagem rodada90, envia por unnamed pipe o id do processo cliente -> %d\n", id_client);
			#endif
			if (sucesso){
				write(zero2p[1], &id_client, sizeof(id_client));
			}
			free(e);
		}
	}
	else if (rotation==180){
		while(1){
			/*THREADS*/
			sem_wait (&full180);
			pthread_mutex_lock(&pmutex180);
			pthread_mutex_lock(&mutex180);

			char *e = malloc(50*sizeof(char));
			strcpy(e, buffer180[read_pos180]); //LE O NOME DA IMG
			read_pos180 = (read_pos180+1) %N;
			#ifdef DEBUG 
				printf("Leu do buffer na thread ID[%d] de rotation %d a img: %s\n", id_thread, rotation, e);
			#endif
			id_client = atoi(buffer180[read_pos180]);
			read_pos180 = (read_pos180+1) %N;
			/*JA LEU, JA PODE DEIXAR O SEMAFORO*/
			pthread_mutex_unlock(&mutex180);
			sem_post(&empty180);
			/*MAIN*/
			if ( (fdin = open(e, O_RDONLY)) < 0){
				fprintf(stderr,"can't open for reading\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			size = get_stat(fdin);

		/* Zona # 1*/
			if ( (src = mmap(0, size, PROT_READ, MAP_FILE | MAP_SHARED, fdin, 0)) == (caddr_t) -1){
				fprintf(stderr,"mmap error for input\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			/* Construção do nome do ficheiro de saída */
			int tam = strlen(e);
			e[tam-4] = '\0';
			sprintf(fich, "%s_%d.ppm", e, rotation);
			//sprintf(fich, "yaa.ppm");

			/* Zona #2 */
			if ( (fdout = open(fich, O_RDWR | O_CREAT | O_TRUNC,FILE_MODE)) < 0){
				fprintf(stderr,"can't creat %s for writing\n", fich);
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}

			/* set size of output file */
			if (lseek(fdout, size - 1, SEEK_SET) == -1){
				fprintf(stderr,"lseek error\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			if (write(fdout, "", 1) != 1){
				fprintf(stderr,"write error\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			

			if ( (dst = mmap(0, size, PROT_READ | PROT_WRITE,MAP_SHARED,fdout, 0)) == (caddr_t) -1){
				fprintf(stderr,"mmap error for output\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			/* Fim da Zona #2 */ 

			sscanf(src,"P6\n%d %d\n%d\n",&xmax,&ymax,&colormax);
			
			struct pixel imagem [ymax][xmax];
			
			for (counter=0, index=0; counter<3;index++){
				if (src[index]=='\n')
					++counter;
			} 	
			pos=index-1;
			for (y=0;y<ymax;y++)
				for (x=0;x<xmax;x++)
					imagem[y][x] = *(get_pixel(src,&pos));
			pos=index;
			sprintf(dst,"P6\n%d %d\n%d\n",xmax,ymax,colormax);
				for (y=ymax-1;y>-1;y--)
					for (x = xmax-1; x>-1; x--)
						write_pixel(&(imagem[y][x]),dst,&pos);
			/* Zona #3 */
			munmap(dst,size);
			munmap(src, size);
			close(fdout);
			close (fdin);
			/* Fim da Zona #3 */
			#ifdef DEBUG 
				printf("Imagem rodada180, envia por unnamed pipe o id do processo cliente -> %d\n", id_client);
			#endif
			if (sucesso){
				write(um2p[1], &id_client, sizeof(id_client));
			}
			free(e);
		}
	}
	else if (rotation==270){
		/*THREADS*/
		while(1){
			sem_wait (&full270);
			pthread_mutex_lock(&pmutex270);
			pthread_mutex_lock(&mutex270);

			char *e = malloc(50*sizeof(char));
			strcpy(e, buffer270[read_pos270]); //LE O NOME DA IMG
			read_pos270 = (read_pos270+1) %N;
			#ifdef DEBUG 
				printf("Leu do buffer na thread ID[%d] de rotation %d a img: %s\n", id_thread, rotation, e);
			#endif
			id_client = atoi(buffer270[read_pos270]);
			read_pos270 = (read_pos270+1) %N;
			/*JA LEU, JA PODE DEIXAR O SEMAFORO*/
			pthread_mutex_unlock(&mutex270);
			sem_post(&empty270);
			/*MAIN*/
			if ( (fdin = open(e, O_RDONLY)) < 0){
				fprintf(stderr,"can't open for reading\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			size = get_stat(fdin);

			/* Zona # 1*/
			if ( (src = mmap(0, size, PROT_READ, MAP_FILE | MAP_SHARED, fdin, 0)) == (caddr_t) -1){
				fprintf(stderr,"mmap error for input\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			/* Construção do nome do ficheiro de saída */
			int tam = strlen(e);
			e[tam-4] = '\0';
			sprintf(fich, "%s_%d.ppm", e, rotation);
			//sprintf(fich, "yaa.ppm");

			/* Zona #2 */
			if ( (fdout = open(fich, O_RDWR | O_CREAT | O_TRUNC,FILE_MODE)) < 0){
				fprintf(stderr,"can't creat %s for writing\n", fich);
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}

			/* set size of output file */
			if (lseek(fdout, size - 1, SEEK_SET) == -1){
				fprintf(stderr,"lseek error\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			if (write(fdout, "", 1) != 1){
				fprintf(stderr,"write error\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			

			if ( (dst = mmap(0, size, PROT_READ | PROT_WRITE,MAP_SHARED,fdout, 0)) == (caddr_t) -1){
				fprintf(stderr,"mmap error for output\n");
				kill (id_client, SIGUSR2); //ENVIA SINAL DE ERRO
				sucesso = 0;
			}
			/* Fim da Zona #2 */ 

			sscanf(src,"P6\n%d %d\n%d\n",&xmax,&ymax,&colormax);
			
			struct pixel imagem [ymax][xmax];
			
			for (counter=0, index=0; counter<3;index++){
				if (src[index]=='\n')
					++counter;
			} 	
			pos=index-1;
			for (y=0;y<ymax;y++)
				for (x=0;x<xmax;x++)
					imagem[y][x] = *(get_pixel(src,&pos));
			pos=index;
			sprintf(dst,"P6\n%d %d\n%d\n",xmax,ymax,colormax);
				for (y=ymax-1;y>-1;y--)
					for (x = xmax-1; x>-1; x--)
						write_pixel(&(imagem[y][x]),dst,&pos);
			/* Zona #3 */
			munmap(dst,size);
			munmap(src, size);
			close(fdout);
			close (fdin);
			/* Fim da Zona #3 */
			#ifdef DEBUG 
				printf("Imagem rodada270, envia por unnamed pipe o id do processo cliente -> %d\n", id_client);
			#endif
			if (sucesso){
				write(dois2p[1], &id_client, sizeof(id_client));
			}
			free(e);
		}
	}
	else
		printf("ERROR!\n");
 	pthread_exit(NULL);
	/*FIM DA ROTACAO*/
}

/*FUNCOES DE RODAR IMAGEM*/
void write_pixel(struct pixel *ppix, char *buf, int *pos)
{
	buf[*pos] = ppix->G;
	buf[(*pos)+1] = ppix->B;
	buf[(*pos)+2] = ppix->R;
	(*pos) += 3;
}

int get_stat(int fdin)
{
	struct stat pstatbuf;	
	if (fstat(fdin, &pstatbuf) < 0)	/* need size of input file */
	{
		fprintf(stderr,"fstat error\n");
		exit(1);
	}
	return pstatbuf.st_size;
}
/*FIM DE FUNCOES DE RODAR IMAGEM*/

