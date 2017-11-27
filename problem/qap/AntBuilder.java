package problem.qap;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import problem.QAPInstance;
import moaco.Ant;
import moaco.PheromoneTrail;
import util.SquareMatrix;

public class AntBuilder{
    private Random random;
    private QAPInstance problem;
    private SquareMatrix pheromone;

    private int neighborsCount;
    private int[] neighbors;
    private double[] probabilities;

    public AntBuilder(QAPInstance problem, SquareMatrix pheromone, long seed){
	this.problem = problem;
	this.pheromone = pheromone;
	random = new Random(seed);
	neighbors = new int[problem.getProblemSize()];
	probabilities = new double[problem.getProblemSize()];
    }
    public AntBuilder(QAPInstance problem, SquareMatrix pheromone){
	this(problem, pheromone, System.currentTimeMillis());
    }
    public Ant build(double alpha, double beta){
	Ant ant = new Ant();

	int firstLocation = random.nextInt(problem.getProblemSize());
	ant.addLocation(firstLocation);
	while( ant.getAssigments().size() != problem.getProblemSize()){
	    int currentLocation = ant.getLastAssigment();

	    buildNeighborSet(ant);
	    calculateProbabilities(currentLocation, alpha, beta);
	    int nextLocation = chooseNextLocation();
	    ant.addLocation(nextLocation);
	}

	return ant;
    }
    private void buildNeighborSet(Ant solution){
	final int currentLocation = solution.getLastAssigment();
	neighborsCount = 0;
	for(int location = 0; location < problem.getProblemSize(); location++){
	    if( problem.getDistance(currentLocation, location) > 0 &&
		!solution.visited(location) ){
		neighbors[neighborsCount++] = location;
	    }
	}
    }
    private void calculateProbabilities(int currentLocation, double alpha, double beta){
	for(int i = 0 ; i < neighborsCount; i++){
	    double pheromoneValue = pheromone.get(currentLocation, neighbors[i]);
	    double visibility = 1.0/(double)(problem.getDistance(currentLocation, neighbors[i]));
	    pheromoneValue = Math.pow(pheromoneValue, alpha);
	    visibility = Math.pow(visibility, beta);
	    probabilities[i] = pheromoneValue*visibility;
	}
	double sum = 0;
	for(int i = 0; i < neighborsCount; i++) sum += probabilities[i];

	for(int i = 0; i < neighborsCount; i++)
	    probabilities[i] /= sum;
    }
    private int chooseNextLocation(){
	double sum = 0;
	for(int i = 0; i < neighborsCount; i++) sum += probabilities[i];

	double outcome = random.nextDouble()*sum;

	for(int i = 1; i < neighborsCount; i++)
	    probabilities[i] += probabilities[i-1];

	int index = 0;
	while( probabilities[index] < outcome ) index++;

	return neighbors[index];
    }
}
