package metrics;

import util.ParetoFront;
import util.Pair;

public class Metric1{
    private ParetoFront trueFront;

    public Metric1(ParetoFront trueFront){
	this.trueFront = trueFront;
    }

    public double evaluate(ParetoFront front){
	double value = 0;

	for(Pair a: front){
	    value += getMinimumDistance(a);
	}
	value /= front.size();

	return value;
    }
    private double getMinimumDistance(Pair a){
	double minDistance = Double.POSITIVE_INFINITY;
	for(Pair b: trueFront){
	    minDistance = Math.min(minDistance, distance(a, b));
	}
	return minDistance;
    }
    private double distance(Pair a, Pair b){
	return Math.sqrt(Math.pow(a.getFirst()-b.getFirst(), 2)+
			 Math.pow(a.getSecond()-b.getSecond(), 2));
    }
}
