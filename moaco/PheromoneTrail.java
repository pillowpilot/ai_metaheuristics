package moaco;

import java.util.List;
import util.SquareMatrix;

public class PheromoneTrail{
    private final int problemSize;
    private final double evaporationFactor, rho;
    private double[] maxObjectiveValue;
    private SquareMatrix pheromone;
    
    public PheromoneTrail(int problemSize, double evaporationFactor, double maxPossibleObjective){
	this.problemSize = problemSize;
	this.evaporationFactor = evaporationFactor;
	rho = 1.0-evaporationFactor;
	maxObjectiveValue = new double[2];
	maxObjectiveValue[0] = maxObjectiveValue[1] = maxPossibleObjective;
	
	pheromone = new SquareMatrix(problemSize);
	initializePheromones();
    }
    public void evaporate(){
	for(int from = 0; from < pheromone.size(); from++){
	    for(int to = 0; to < pheromone.size(); to++){
		double value = pheromone.get(from, to);
		pheromone.set(from, to, value*rho);
	    }
	}
    }
    public void update(Ant solution, int numberOfAnts){
	List<Integer> visitedLocations = solution.getAssigments();
	for(int i = 0; i < visitedLocations.size()-1; i++){
	    int from = visitedLocations.get(i);
	    int to = visitedLocations.get(i+1);
	    double nextTau = calculateNextTau(pheromone.get(from, to),
					      getDeltaTau(solution), 
					      numberOfAnts);
	    pheromone.set(from, to, nextTau);
	    pheromone.set(to, from, nextTau);
	}
	double deltaTau = getDeltaTau(solution);
    }
    public double getDeltaTau(Ant solution){
	double objective1 = solution.getObjective(0)/maxObjectiveValue[0];
	double objective2 = solution.getObjective(1)/maxObjectiveValue[1];

	return 1/(objective1+objective2);
    }
    public double getInitialTau(){
	return 1e4; // See [Stuezle00] 4.3 Pheromone Trail Initialization
    }
    public double calculateMinTau(double deltaTau, int numberOfAnts){
	return deltaTau/(2*numberOfAnts*rho); // See [Pinto05] 7 Multiobjective Max Min Ant System
    }
    public double calculateMaxTau(double deltaTau){
	return deltaTau/rho; // See [Pinto05] 7 Multiobjective Max Min Ant System
    }
    public boolean checkConvergence(double percentage){
	final double epsilon = 1e-6;

	int convergenceEdges = 0;
	for(int from = 0; from < problemSize; from++){
	    double minValue = Double.POSITIVE_INFINITY;
	    double maxValue = Double.NEGATIVE_INFINITY;
	    for(int to = 0; to < problemSize; to++){
		if( from != to ){
		    minValue = Math.min(minValue, pheromone.get(from, to));
		    maxValue = Math.max(maxValue, pheromone.get(from, to));
		}
	    }
	    int mins = 0, maxs = 0;
	    for(int to = 0; to < problemSize; to++){
		if( from != to ){
		    double value = pheromone.get(from, to);
		    if( Math.abs(value-minValue) < epsilon ) mins++;
		    if( Math.abs(value-maxValue) < epsilon ) maxs++;
		}
	    }
	    if( maxs + mins == problemSize - 1 )
		convergenceEdges++;
	}

	return problemSize*percentage <= convergenceEdges;
    }
    public void setMaxObjectiveFunctionValue(int objective, double value) throws IndexOutOfBoundsException{
	if( 0 <= objective && objective <= 1 )
	    maxObjectiveValue[objective] = value;
	else throw new IndexOutOfBoundsException();
    }
    public SquareMatrix getMatrix(){
	return pheromone;
    }
    public String toString(){
	return pheromone.toString();
    }
    private void initializePheromones(){
	final double initialTau = getInitialTau();
	for(int i = 0; i < pheromone.size(); i++){
	    for(int j = 0; j < pheromone.size(); j++){
		pheromone.set(i, j, initialTau);
	    }
	}
    }
    private double calculateNextTau(double tau, double deltaTau, int numberOfAnts){
	double nextTau = tau + deltaTau; // See [Pinto05] 7 Multiobjective Max Min Ant System
	nextTau = Math.max(nextTau, calculateMinTau(deltaTau, numberOfAnts));
	nextTau = Math.min(nextTau, calculateMaxTau(deltaTau));
	return nextTau;
    }
}
