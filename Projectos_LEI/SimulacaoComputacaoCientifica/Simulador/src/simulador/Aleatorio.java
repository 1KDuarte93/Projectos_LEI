package simulador;

public class Aleatorio {

	// Gera um número segundo uma distribuição exponencial negativa de média m
    static double exponencial (double m, int seed){
        return (-m*Math.log(RandomGenerator.rand64(seed)));
    }
}
