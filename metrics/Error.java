package metrics;

import util.ParetoFront;
import util.Pair;

public class Error{
    private ParetoFront trueFront;

    public Error(ParetoFront trueFront){
	this.trueFront = trueFront;
    }
    public double evaluate(ParetoFront front){
	double value = 0;

	for(Pair a: front){
	    if( trueFront.getAll().contains(a) ) value += 1;
	}

	value /= trueFront.size();

	return value;
    }
}
