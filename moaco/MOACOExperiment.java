package moaco;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

import problem.QAP;
import problem.QAPInstance;
import util.ParetoFront;

public class MOACOExperiment extends Observable implements Callable<ParetoFront>{
    private final String name;
    private final int maxGenerations;
    private final long seed;
    private final QAPInstance problemData;
    private M3AS algorithm;
    private int generation;
    private ParetoFront paretoFront;

    public MOACOExperiment(String name, int maxGenerations, QAPInstance problemData, long seed){
	this.name = name;
	this.maxGenerations = maxGenerations;
	this.problemData = problemData;
	this.seed = seed;
    }
    public ParetoFront call(){
	algorithm = new M3AS(problemData.getProblemSize(), // See [Stuetzle00] 2.3.1 Tour constr
			     1, // See [Stuetzle00] 4.4 Experiments with Min-Max Ant System
			     2, // See [Stuetzle00] 4.4 Experiments with Min-Max Ant System
			     1-0.98, // See [Stuetzle00] 4.4 Experiments with Min-Max Ant System
			     new QAP(problemData),
			     calculateMaxPossibleObjectiveValue(),
			     seed);

	generation = 0;
	while( generation < maxGenerations ){
	    algorithm.step();
	    if( generation % 1000 == 1 )
		algorithm.restorePheromoneTrails();

	    paretoFront = algorithm.getResult();
	    generation++;
	    setChanged();
	    notifyObservers(getState());
	}

	return paretoFront;
    }

    public ParetoFront getParetoFront(){
	return paretoFront;
    }
    public String getName(){
	return name;
    }
    public String getState(){
	StringBuilder out = new StringBuilder();
	out.append(name);
	out.append(":");

	if( generation == 0 ) out.append("Unstarted.");
	else if( generation == maxGenerations ) out.append("Finished.");
	else{
	    out.append(" Generation=");
	    out.append(generation);
	}

	if( generation != 0 ){
	    out.append(" OneOfPareto=");
	    out.append(paretoFront.getAll().get(0));
	    out.append(" ParetoSize=");
	    out.append(paretoFront.size());
	}

	return out.toString();
    }
    private double calculateMaxPossibleObjectiveValue(){
	double maxFactor1 = problemData.getMaxDistance()*problemData.getMaxFlow1();
	double maxFactor2 = problemData.getMaxDistance()*problemData.getMaxFlow2();

	return Math.max(maxFactor1, maxFactor2)*Math.pow(problemData.getProblemSize(), 2);
    }
}
