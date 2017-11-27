package moaco;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;

import util.Pair;

public class Ant{    
    private double[] objectives;
    private List<Integer> partialSolution;
    private HashSet<Integer> visitedLocations;

    public Ant(){
	partialSolution = new ArrayList<>();
	objectives = new double[2];
	visitedLocations = new HashSet<>(100);
    }
    public void addLocation(int location){
	partialSolution.add(location);
	visitedLocations.add(location);
    }
    public List<Integer> getAssigments(){
	return partialSolution;
    }
    public int getLastAssigment() throws NoSuchElementException{
	if( partialSolution.size() == 0 )
	    throw new NoSuchElementException("There is no assigments yet!");
	return partialSolution.get(partialSolution.size()-1);
    }
    public boolean visited(int location){
	return visitedLocations.contains(location);
    }
    public void setObjectives(double[] objectives){
	this.objectives = objectives;
    }
    public void setObjective(int index, double value) throws IndexOutOfBoundsException{
	if( 0<=index&&index<objectives.length )
	    objectives[index] = value;
	else throw new IndexOutOfBoundsException();
    }
    public double[] getObjectives(){
	return objectives;
    }
    public Pair getObjectivesPair(){
	return new Pair(objectives[0], objectives[1]);
    }
    public double getObjective(int index){
	if( 0<=index&&index<objectives.length )
	    return objectives[index];
	else throw new IndexOutOfBoundsException();
    }
    public boolean isDominatedBy(Ant other){
	return getObjectivesPair().isDominatedBy(other.getObjectivesPair());
    }
    public int getNumberOfObjectives(){
	return objectives.length;
    }
    public boolean equals(Ant other){
	if( partialSolution.size() != other.partialSolution.size() )
	    return false;
	for(int i = 0; i < partialSolution.size(); i++){
	    if( partialSolution.get(i) != other.partialSolution.get(i) )
		return false;
	}
	return true;
    }
    public String toString(){
	StringBuilder out = new StringBuilder();
	out.append(partialSolution);
	out.append(" => ");
	out.append(Arrays.toString(objectives));
	return out.toString();
    }
}
