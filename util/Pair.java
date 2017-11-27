package util;

public class Pair{
    private final double first, second;
    public Pair(double first, double second){
	this.first = first;
	this.second = second;
    }
    public double getFirst(){ return first; }
    public double getSecond(){ return second; }
    public boolean isDominatedBy(Pair other){
	boolean firstCondition = (other.first<=first && other.second<=second);
	boolean secondCondition = (other.first<first || other.second<second);
	return firstCondition && secondCondition;
    }
    public boolean equals(Pair other){
	return first==other.first && second==other.second;
    }
    public String toString(){
	return "("+first+", "+second+")";
    }
}
