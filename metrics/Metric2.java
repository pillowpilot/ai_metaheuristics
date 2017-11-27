package metrics;

import util.ParetoFront;
import util.Pair;

public class Metric2{
    public static double evaluate(ParetoFront front, double sigma){
	double value = 0;

	for(Pair a: front){
	    value += calculateWCardinality(a, sigma, front);
	}
	value /= front.size()-1;
	
	return value;
    }

    private static int calculateWCardinality(Pair a, double sigma, ParetoFront front){
	int value = 0;
	for(Pair b: front){
	    if( distance(a, b) < sigma )
		value++;
	}
	return value;
    }
    private static double distance(Pair a, Pair b){
	return Math.sqrt(Math.pow(a.getFirst()-b.getFirst(), 2)+
			 Math.pow(a.getSecond()-b.getSecond(), 2));
    }
}
