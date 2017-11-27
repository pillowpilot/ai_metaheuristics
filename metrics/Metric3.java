package metrics;

import util.ParetoFront;
import util.Pair;

public class Metric3{
    public static double evaluate(ParetoFront front){
	double maxDistance1 = Double.NEGATIVE_INFINITY;
	double maxDistance2 = Double.NEGATIVE_INFINITY;	
	for(Pair a: front){
	    for(Pair b: front){
		maxDistance1 = Math.max(maxDistance1, distance(a.getFirst(), b.getFirst()));
		maxDistance2 = Math.max(maxDistance2, distance(a.getSecond(), b.getSecond()));
	    }
	}

	return Math.sqrt(maxDistance1+maxDistance2);
    }
    private static double distance(double a, double b){
	return Math.sqrt(Math.pow(a-b, 2)+
			 Math.pow(a-b, 2));
    }
}
