package simulador;

// Classe de onde vão ser derivados todos os eventos.
// Contém apenas os atributos e métodos comuns a todos os eventos.
// Por isso é uma classe abstracta. Não haverá instâncias desta classe num simulador.

public abstract class Evento {
	protected double instante;  // Instante de ocorrência do evento
	protected Simulador s;      // Simulador onde ocorre o evento
        protected int flag;

	//Construtor
    Evento (double i, Simulador s, int flag){
	instante = i;
	this.s = s;
        this.flag = flag;
    }

	// Método que determina se o evento corrente ocorre primeiro, ou não, do que o evento e1
	// Se sim, devolve true; se não, devolve false
	// Usado para ordenar por ordem crescente de instantes de ocorrência a lista de eventos do simulador
    public boolean menor (Evento e1){
        return (instante < e1.instante);
    }

	// Método que executa um evento; a ser definido em cada tipo de evento
    abstract void executa (Servico s, int seed);

    // Método que devolve o instante de ocorrência do evento
    public double getInstante() {
        return instante;
    }
    
    public int getFlag(){
        return flag;
    }
}