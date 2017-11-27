package moaco;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import problem.QAP;
import problem.qap.AntBuilder;
import util.Pair;
import util.ParetoFront;

public class M3AS{
    private final int numberOfAnts;
    private final double alpha, beta, evaporationFactor, maxPossibleObjective;
    private QAP problem;
    private AntBuilder antBuilder;
    private PheromoneTrail pheromone;
    private AntParetoFront paretoFront;

    public M3AS(int numberOfAnts,
		double alpha,
		double beta,
		double evaporationFactor,
		QAP problem,
		double maxPossibleObjective,
		long seed){
	this.numberOfAnts = numberOfAnts;
	this.alpha = alpha;
	this.beta = beta;
	this.evaporationFactor = evaporationFactor;
	this.problem = problem;
	this.maxPossibleObjective = maxPossibleObjective;
	restorePheromoneTrails();
	antBuilder = new AntBuilder(problem.getQAPInstance(), pheromone.getMatrix(), seed);
	paretoFront = new AntParetoFront();
    }
    public ParetoFront getResult(){
	ParetoFront paretoFront = new ParetoFront();
	for(Ant ant: this.paretoFront) paretoFront.add(ant.getObjectivesPair());	
	return paretoFront;
    }
    public void step(){
	for(int ant = 0; ant < numberOfAnts; ant++){
	    Ant solution = antBuilder.build(alpha, beta);
	    double[] objectives = problem.evaluate(toArray(solution.getAssigments()));
	    solution.setObjectives(objectives);
	    paretoFront.add(solution);
	}
	pheromone.evaporate();
	
	for(Ant ant: paretoFront){
	    pheromone.update(ant, numberOfAnts); // See [Pinto05] 7. Multiobjective Min Max Ant System
	}
    }
    public boolean checkConvergence(double percentage){
	return pheromone.checkConvergence(percentage);
    }
    public void restorePheromoneTrails(){
	this.pheromone = new PheromoneTrail(problem.getQAPInstance().getProblemSize(),
					   evaporationFactor,
					   maxPossibleObjective);
    }
    public void printPheromones(){
	System.out.println(pheromone);
    }
    private int[] toArray(List<Integer> list){
	int[] array = new int[list.size()];
	for(int i = 0; i < list.size(); i++) array[i] = list.get(i);
	return array;
    }

    class AntParetoFront implements Iterable<Ant>{
	private List<Ant> front;	

	public AntParetoFront(){
	    front = new ArrayList<>();
	}
	public boolean add(Ant solution){
	    if( front.size() == 0 ){
		front.add(solution);
		return true;
	    }

	    boolean isDominatedByAny = false;
	    boolean isDifferentToAll = true;
	    for(Ant frontSolution: front){
		if( solution.isDominatedBy(frontSolution) )
		    isDominatedByAny = true;
		if( frontSolution.equals(solution) )
		    isDifferentToAll = false;
	    }
	    if( isDominatedByAny ) return false;
	    else if( !isDifferentToAll ) return false; // solution already in front
	    else{
		List<Ant> toRemove = new ArrayList<>();
		for(Ant frontSolution: front){
		    if( frontSolution.isDominatedBy(solution) )
			toRemove.add(frontSolution);
		}
		front.removeAll(toRemove);
		front.add(solution);
		return true;
	    }	
	}
	public List<Ant> getAll(){
	    return front;
	}
	public Iterator<Ant> iterator(){
	    return front.iterator();
	}
    }
}
