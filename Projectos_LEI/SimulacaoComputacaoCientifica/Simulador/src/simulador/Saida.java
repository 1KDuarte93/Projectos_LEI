package simulador;

// Classe que representa a saída de um cliente. Deriva de Evento.

public class Saida extends Evento {

    //Construtor
    Saida (double i, Simulador s, int flag){
        super(i, s, flag);
    }

    // Método que executa as acções correspondentes à saída de um cliente
    @Override
    void executa (Servico serv, int seed){
        // Retira cliente do serviço
        serv.removeServico();
    }
    
    // Método que descreve o evento.
    // Para ser usado na listagem da lista de eventos.
    @Override
    public String toString(){
        return "Saída em " + instante;
    }
}