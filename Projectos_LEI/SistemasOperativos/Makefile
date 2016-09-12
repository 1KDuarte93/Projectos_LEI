PROGRAMS = server client

all: $(PROGRAMS)
	
clean:
	rm -f server
	rm -f client

server: server.c
	gcc -pthread -D_REENTRANT -Wall server.c -o server

client: client.c
	gcc client.c -o client 