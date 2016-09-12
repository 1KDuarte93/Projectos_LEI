package simulador;

import java.util.Vector;

// Classe que representa um serviço com uma fila de espera associada

public class Servico {
    private int estado, seed, flag; // Variável que regista o estado do serviço: 0 - livre; 1 - ocupado
    private int atendidos; // Número de clientes atendidos até ao momento
    private double temp_ult, soma_temp_esp, soma_temp_serv, media, desvio_padrao; // Variáveis para cálculos estatísticos
    private Vector<Cliente> fila; // Fila de espera do serviço
    private Simulador s; // Referência para o simulador a que pertence o serviço

	// Construtor
    Servico (Simulador s, double media, double desvio_padrao, int seed, int flag){
    	this.s = s;
	fila = new Vector <Cliente>(); // Cria fila de espera
	estado = 0; // Livre
	temp_ult = s.getInstante(); // Tempo que passou desde o último evento. Neste caso 0, porque a simulação ainda não começou.
	atendidos = 0;  // Inicialização de variáveis
	soma_temp_esp = 0;
	soma_temp_serv = 0;
        this.flag = flag;
        this.media = media;
        this.desvio_padrao = desvio_padrao;
    }

	// Método que insere cliente (c) no serviço
    public void insereServico (Cliente c){
        double x1;
        if (s.exed_on==0){
            /*para não utilizar numeros aleatorios gerados com outras seed,media,desvio*/
            if (flag==0){
                x1 = s.prep_rand.gera(seed, media, desvio_padrao);
                if (estado < s.maq_prep){
                    estado++;
                    s.insereEvento (new Saida(s.getInstante()+x1, s, flag));
                }
                else
                    fila.addElement(c);
            }
            else if (flag==1){
                x1 = s.fixa_rand.gera(seed, media, desvio_padrao);
                if (estado < s.maq_fixa){
                    estado++;
                    s.insereEvento (new Saida(s.getInstante()+x1, s, flag));
                }
                else
                    fila.addElement(c);
            }
            else{
                x1 = s.teste_rand.gera(seed, media, desvio_padrao);
                if (estado < s.maq_teste){
                    estado++;
                    s.insereEvento (new Saida(s.getInstante()+x1, s, flag));
                }
                else
                    fila.addElement(c);
            }
        }
        else{ /*EXE_D*/
            /*para não utilizar numeros aleatorios gerados com outras seed,media,desvio*/
            if (flag==0){
                x1 = s.prep_rand.gera(seed, media, desvio_padrao);
                if (estado < s.maq_prep){
                    estado++;
                    s.insereEvento (new Saida(s.getInstante()+x1, s, flag));
                }
                else
                    fila.addElement(c);
            }
            else if (flag==1){
                x1 = s.fixa_rand.gera(seed, media, desvio_padrao);
                if (estado < s.maq_fixa){
                    estado++;
                    s.insereEvento (new Saida(s.getInstante()+x1, s, flag));
                }
                else
                    fila.addElement(c);
            }
        }
    }

	// Método que remove cliente do serviço
    public void removeServico (){
        double x1;
	atendidos++; // Regista que acabou de atender + 1 cliente
        /*entao atende mais 1 cliente*/
        if (s.exed_on==0){
            if(flag == 0)
                s.fixacao.insereServico(new Cliente());
            else if (flag == 1)
                s.teste.insereServico(new Cliente());
            if (fila.isEmpty())
                estado --; // Se a fila está vazia, liberta o serviço
            else { // Se não,
                // vai buscar próximo cliente à fila de espera e
                // Cliente c = (Cliente)fila.firstElement();
                fila.removeElementAt(0);
                if (flag==0)
                    x1 = s.prep_rand.gera(seed, media, desvio_padrao);
                else if (flag==1)
                    x1 = s.fixa_rand.gera(seed, media, desvio_padrao);
                else
                    x1 = s.teste_rand.gera(seed, media, desvio_padrao);
                s.insereEvento (new Saida(s.getInstante()+x1,s, flag));
            }
        }
        else{
            if(flag == 0)
                s.exed.insereServico(new Cliente());
            if (fila.isEmpty())
                estado --; // Se a fila está vazia, liberta o serviço
            else { // Se não,
                // vai buscar próximo cliente à fila de espera e
                // Cliente c = (Cliente)fila.firstElement();
                fila.removeElementAt(0);
                if (flag==0)
                    x1 = s.prep_rand.gera(seed, media, desvio_padrao);
                else
                    x1 = s.exed_rand.gera(seed, media, desvio_padrao);
                s.insereEvento (new Saida(s.getInstante()+x1,s, flag));
            }
        }
    }

	// Método que calcula valores para estatísticas, em cada passo da simulação ou evento
    public void act_stats(){
        // Calcula tempo que passou desde o último evento
	double temp_desde_ult = s.getInstante() - temp_ult;
	// Actualiza variável para o próximo passo/evento
	temp_ult = s.getInstante();
	// Contabiliza tempo de espera na fila
	// para todos os clientes que estiveram na fila durante o intervalo
	soma_temp_esp += fila.size() * temp_desde_ult;
	// Contabiliza tempo de atendimento
        if (flag==0)
            soma_temp_serv += estado * (temp_desde_ult/s.maq_prep);
        else if(flag==1)
            soma_temp_serv += estado * (temp_desde_ult/s.maq_fixa);
        else
            soma_temp_serv += estado * (temp_desde_ult/s.maq_teste);
    }

	// Método que calcula valores finais estatísticos
    public void relat (){
        // Tempo médio de espera na fila
	double temp_med_fila = soma_temp_esp / (atendidos+fila.size());
	// Comprimento médio da fila de espera
	// s.getInstante() neste momento é o valor do tempo de simulação,
        // uma vez que a simulação começou em 0 e este método só é chamdo no fim da simulação
	double comp_med_fila = soma_temp_esp / s.getInstante();
	// Tempo médio de atendimento no serviço
	double utilizacao_serv = soma_temp_serv / s.getInstante();
	// Apresenta resultados
        s.frame.SetResultados("Tempo médio de espera "+temp_med_fila+"\n"
                + "Comp. médio da fila "+comp_med_fila+"\nUtilização do serviço"
                + " "+utilizacao_serv+"\nTempo de simulação "+s.getInstante()+
                "\nNúmero de clientes atendidos "+atendidos+
                "\nNúmero de clientes na fila "+fila.size());
    }

    // Método que devolve o número de clientes atendidos no serviço até ao momento
    public int getAtendidos() {
        return atendidos;
    }
}