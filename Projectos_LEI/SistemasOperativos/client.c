#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/mman.h>	
#include <string.h>	
#include <limits.h>	
#include  <pthread.h>

#define PIPE_NAME   "np_client_server"

#define	FILE_MODE	(S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH)

#ifndef	MAP_FILE	/* 44BSD defines this & requires it to mmap files */
#define	MAP_FILE	0	/* to compile under systems other than 44BSD */
#endif

#define SIZE 20

#define N 3

int fd;

struct stat statbuf;

char * src;
int size;

typedef struct{
	pid_t id;
	int rotation;
	char file[64];
}dados;



int get_stat(int fdin)
{
	struct stat pstatbuf;	
	if (fstat(fdin, &pstatbuf) < 0){	/* need size of input file */
		fprintf(stderr,"fstat error\n");
		exit(0);
	}
	return pstatbuf.st_size;
}


void handler(int signum){

	if (signum == SIGUSR1){
		munmap(src, size);
		close(fd);
		printf("Trabalho concluido com sucesso!!\n");
	}
	else if(signum == SIGUSR2){
		printf("Ocorreu um erro no servidor ao tentar executar a rotacao!!\n");
	}
	else if(signum == SIGINT){
		printf("O servidor foi terminado a meio do processo!!\n");
	}
}



int main(int argc, char *argv[]){
	printf("Aguarde...\n");
	dados dd;

	signal(SIGUSR1, handler);
	signal(SIGUSR2, handler);

  	sigset_t mask, unmask;

 	sigfillset(&mask);
  	sigprocmask(SIG_SETMASK, &mask, NULL);
  	sigemptyset(&unmask);
  	sigaddset(&unmask, SIGINT);
 	sigaddset(&unmask, SIGUSR1);
  	sigaddset(&unmask, SIGUSR2);
  	sigprocmask(SIG_UNBLOCK, &unmask, NULL);

	if (argc != 3){
		fprintf(stderr,"usage: newclient <image file> <rotation>\n");
		exit(1);
	}

	if ( (fd = open(argv[1], O_RDONLY)) < 0){
		fprintf(stderr,"can't open %s for reading\n", argv[1]);
		exit(1);
	}

	size = get_stat(fd);

	if ( (src = mmap(0, size, PROT_READ, MAP_FILE | MAP_SHARED, fd, 0)) == (caddr_t) -1){
		fprintf(stderr,"mmap error for input\n");
		exit(1);
	}

	dd.rotation = atoi(argv[2]);
	dd.id = getpid();
	strcpy(dd.file,argv[1]);

	int fd_named;
	if ((fd_named = open(PIPE_NAME, O_WRONLY)) < 0){
		printf("ERRO Cliente\n");
		exit(0);
	}
	write(fd_named, &dd, sizeof(dados));

	pause();
}