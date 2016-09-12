package simulador;

// Classe que representa a chegada de um cliente. Deriva de Evento.

public class Chegada extends Evento {

    //Construtor
    Chegada (double i, Simulador s, int flag){
        super (i, s, flag);
    }

	// Método que executa as acções correspondentes à chegada de um cliente
    @Override
    void executa (Servico serv,int seed){
	// Coloca cliente no serviço - na fila ou a ser atendido, conforme o caso
	serv.insereServico (new Cliente());
        // Agenda nova chegada para daqui a Aleatorio.exponencial(s.media_cheg) instantes
	s.insereEvento (new Chegada(s.getInstante()+Aleatorio.exponencial(s.getMedia_cheg(), seed), s, 0));
    }

    // Método que descreve o evento.
    // Para ser usado na listagem da lista de eventos.
    @Override
    public String toString(){
         return "Chegada em " + instante;
    }
}
