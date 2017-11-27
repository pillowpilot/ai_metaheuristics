package moea;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.ArrayList;

import problem.QAP;
import util.Pair;
import util.ParetoFront;
import util.ConsoleReporter;

public class MOEAExperimenter{
    private final int maxGenerations, runs;
    private final QAP problem;
    private ParetoFront finalFront;
    private ConsoleReporter reporter;

    public MOEAExperimenter(int maxGenerations, QAP problem, int runs){
	this.maxGenerations = maxGenerations;
	this.problem = problem;
	this.runs = runs;
    }
    public MOEAExperimenter(int maxGenerations, QAP problem){
	this(maxGenerations, problem, 5);
    }
    public void setReporter(ConsoleReporter reporter){
	this.reporter = reporter;
    }
    public ParetoFront getParetoFront(){
	return finalFront;
    }
    public void run(){
	List<Callable<ParetoFront>> experiments = new ArrayList<>();	
	for(int i = 0; i < runs; i++){
	    String name = "Experiment MOEA #" + i;
	    MOEAExperiment experiment = new MOEAExperiment(name, maxGenerations, problem);
	    
	    experiments.add(experiment);

	    if( reporter != null )
		experiment.addObserver(reporter);	   	    
	}

	ExecutorService executor = Executors.newCachedThreadPool();
	try{
	    //System.out.println();
	    List<Future<ParetoFront>> futures = executor.invokeAll(experiments);

	    finalFront = new ParetoFront();
	    for(int i = 0; i < futures.size(); i++){
		ParetoFront front = futures.get(i).get();
		finalFront.merge( front );	    
	    }
	    
	}catch(InterruptedException | ExecutionException ex){
	    ex.printStackTrace();
	}
	executor.shutdown();
    }
}
