package simulador;

public class Simulador {

    // Relógio de simulação - variável que contém o valor do tempo em cada instante
    private double instante;
    
    /*Valores recebidos em GUI*/
    public int seed_chegada, seed_prep, seed_fixa, seed_teste, maq_prep, maq_fixa,
            maq_teste,tempo_pecas, seed_exed, exed_on;
    private double media_prep, media_fixa, media_teste, desvio_prep,
            desvio_fixa, desvio_teste, media_dist_normal;
    public Servico preparacao, fixacao, teste, exed;
    
    // Lista de eventos - onde ficam registados todos os eventos que vão ocorrer na simulação
    // Cada simulador só tem uma
    private ListaEventos lista;
    
    /*RANDOM's*/
    public RandomGenerator prep_rand, fixa_rand, teste_rand, exed_rand;
    
    public static GUI frame;

    // Construtor
    public Simulador() {
	// Inicialização de parâmetros do simulador
        seed_chegada = frame.getDist_normal_tb();
        seed_prep = frame.getPrep_seed_tb();
        seed_fixa = frame.getFixa_seed_tb();
        seed_teste = frame.getTeste_seed_tb();
        media_prep = frame.getPrep_media_tb();
        media_fixa = frame.getFixa_media_tb();
        media_teste = frame.getTeste_media_tb();
        desvio_prep = frame.getPrep_desvio_tb();
        desvio_fixa = frame.getFixa_desvio_tb();
        desvio_teste = frame.getTeste_desvio_tb();
        maq_prep = frame.getPrep_maq_tb();
        maq_fixa = frame.getFixa_maq_tb();
        maq_teste = frame.getTeste_maq_tb();
        tempo_pecas = frame.getTempoPecas();
        media_dist_normal = frame.getMedia_Dist_normal_tb();
        seed_exed = frame.getSeed_exed();
        exed_on = 0; /*se =1 entao executa como sendo exed*/
        
	// Inicialização do relógio de simulação
	instante = 0;
        
	// Criação do serviço
	preparacao = new Servico (this, media_prep, desvio_prep, seed_prep,0);
        fixacao = new Servico (this, media_fixa, desvio_fixa, seed_fixa,1);
        teste = new Servico (this, media_teste, desvio_teste, seed_teste,2);
        exed = new Servico (this, 2.25, 0.5, seed_exed,1);
        
	// Criação da lista de eventos
	lista = new ListaEventos(this);

        prep_rand = new RandomGenerator();
        fixa_rand = new RandomGenerator();
        teste_rand = new RandomGenerator();
        exed_rand = new RandomGenerator();
        
	// Agendamento da primeira chegada
        // Se não for feito, o simulador não tem eventos para simular
	insereEvento (new Chegada(instante, this, 0));
    }

        // programa principal
        public static void main(String[] args) {
            frame = new GUI();
            frame.setVisible(true);
        }

    // Método que insere o evento e1 na lista de eventos
	void insereEvento (Evento e1){
            lista.insereEvento (e1);
	}

    // Método que apresenta os resultados de simulação finais
	private void relat (){
            if (exed_on==0){
                frame.SetResultados("--Preparação--");
                preparacao.relat();
                frame.SetResultados("--Fixação--");
                fixacao.relat();
                frame.SetResultados("--Teste--");
                teste.relat();
            }
            else{
                frame.SetResultados("--Preparação--");
                preparacao.relat();
                frame.SetResultados("--Exe_d.ii--");
                exed.relat();
            }
	}

    // Método executivo do simulador
	public void executa (){
            Evento e1;
            if (exed_on==0){
                // Enquanto não atender todos os clientes
                if (frame.getPecas_rb()==1){
                    while (teste.getAtendidos() < tempo_pecas){
                        //lista.print();                        // Mostra lista de eventos - desnecessário; é apenas informativo
                        e1 = (Evento)(lista.removeFirst());     // Retira primeiro evento (é o mais iminente) da lista de eventos
                        instante = e1.getInstante();            // Actualiza relógio de simulação

                        if (e1.getFlag() == 0)
                            e1.executa(preparacao,seed_chegada);
                        else if (e1.getFlag() == 1)
                            e1.executa(fixacao, seed_chegada);
                        else if (e1.getFlag() == 2)
                            e1.executa(teste, seed_chegada);
                        
                        // Actualiza valores estatísticos
                        preparacao.act_stats();
                        fixacao.act_stats();
                        teste.act_stats();
                    }
                }
                else if (frame.getTempo_rb()==1){
                    while (instante < tempo_pecas){
                        //lista.print();                        // Mostra lista de eventos - desnecessário; é apenas informativo
                        e1 = (Evento)(lista.removeFirst());     // Retira primeiro evento (é o mais iminente) da lista de eventos
                        instante = e1.getInstante();            // Actualiza relógio de simulação

                        if (e1.getFlag() == 0)
                            e1.executa(preparacao,seed_chegada);
                        else if (e1.getFlag() == 1)
                            e1.executa(fixacao, seed_chegada);
                        else if (e1.getFlag() == 2)
                            e1.executa(teste, seed_chegada);
                        
                        // Actualiza valores estatísticos
                        preparacao.act_stats();
                        fixacao.act_stats();
                        teste.act_stats();
                    }
                }
                relat();  // Apresenta resultados de simulação finais
            }
            else{ /*EXE_D*/
                if (frame.getPecas_rb()==1){
                    // Enquanto não atender todos os clientes
                    while (exed.getAtendidos() < tempo_pecas){
                        //lista.print();                        // Mostra lista de eventos - desnecessário; é apenas informativo
                        e1 = (Evento)(lista.removeFirst());     // Retira primeiro evento (é o mais iminente) da lista de eventos
                        instante = e1.getInstante();            // Actualiza relógio de simulação

                        if (e1.getFlag() == 0)
                            e1.executa(preparacao,seed_chegada);
                        else if (e1.getFlag() == 1)
                            e1.executa(exed, seed_chegada);
                        
                        // Actualiza valores estatísticos
                        preparacao.act_stats();
                        exed.act_stats();
                    }
                }
                else if (frame.getTempo_rb()==1){
                    while (instante < tempo_pecas){
                        //lista.print();                        // Mostra lista de eventos - desnecessário; é apenas informativo
                        e1 = (Evento)(lista.removeFirst());     // Retira primeiro evento (é o mais iminente) da lista de eventos
                        instante = e1.getInstante();            // Actualiza relógio de simulação

                        if (e1.getFlag() == 0)
                            e1.executa(preparacao,seed_chegada);
                        else if (e1.getFlag() == 1)
                            e1.executa(exed, seed_chegada);
                        
                        // Actualiza valores estatísticos
                        preparacao.act_stats();
                        exed.act_stats();
                    }
                }
                relat();  // Apresenta resultados de simulação finais
            }
	}

    // Método que devolve o instante de simulação corrente
    public double getInstante() {
        return instante;
    }

    // Método que devolve a média dos intervalos de chegada
    public double getMedia_cheg() {
        return media_dist_normal;
    }
}
