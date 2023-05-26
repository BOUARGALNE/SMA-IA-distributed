package ma.enset.GA_SMA.Genetic;

import java.util.Arrays;

public class GAApplication {


    public static void main(String[] args) {
        GenticAlgorithm ga=new GenticAlgorithm();
        ga.initialize();
        ga.sortPopulation();
        ga.showPopulation();
        int cpt=0;
        while (GAUtils.MAX_ITERATIONS>cpt &&  ga.getBestFintness()<GAUtils.CHROMOSOME_SIZE){
            System.out.println("Iteration : "+cpt);
            ga.crossover();
            ga.mutation();
            ga.sortPopulation();
            System.out.println(Arrays.toString(ga.getPopulation()[0].getChromosome()) +"  "+ga.getPopulation()[0].getFitness());
            cpt++;
        }

    }
}
