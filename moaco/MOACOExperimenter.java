package moaco;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import problem.QAP;
import util.Pair;
import util.ParetoFront;
import util.ConsoleReporter;

public class MOACOExperimenter{
    private final int maxGenerations, runs;
    private final QAP problem;
    private final Random random;
    private ParetoFront finalFront;
    private ConsoleReporter reporter;

    public MOACOExperimenter(int maxGenerations, QAP problem, int runs, long seed){
	this.maxGenerations = maxGenerations;
	this.problem = problem;
	this.runs = runs;
	random = new Random(seed);
    }
    public MOACOExperimenter(int maxGenerations, QAP problem){
	this(maxGenerations, problem, 5, System.currentTimeMillis());
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
	    String name = "Experiment MOACO#" + i;
	    MOACOExperiment experiment = new MOACOExperiment(name,
							     maxGenerations,
							     problem.getQAPInstance(),
							     random.nextLong());

	    experiments.add(experiment);

	    if( reporter != null )
		experiment.addObserver(reporter);
	}

	ExecutorService executor = Executors.newCachedThreadPool();
	try{
	    List<Future<ParetoFront>> futures = executor.invokeAll(experiments);

	    finalFront = new ParetoFront();
	    for(int i = 0; i < experiments.size(); i++){
		ParetoFront front = futures.get(i).get();
		finalFront.merge( front );
	    }
	}catch(InterruptedException | ExecutionException ex){
	    ex.printStackTrace();
	}

	executor.shutdown();
    }
}
