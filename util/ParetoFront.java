package util;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ParetoFront implements Iterable<Pair>{
    private List<Pair> front;

    public ParetoFront(){
	front = new ArrayList<>();
    }
    public boolean add(Pair solution){ // returns true if front changes
	if( front.size() == 0 ){
	    front.add(solution);
	    return true;
	}

	boolean isDominatedByAny = false;
	boolean isDifferentToAll = true;
	for(Pair frontSolution: front){
	    if( solution.isDominatedBy(frontSolution) )
		isDominatedByAny = true;
	    if( frontSolution.equals(solution) )
		isDifferentToAll = false;
	}
	if( isDominatedByAny ) return false;
	else if( !isDifferentToAll ) return false; // solution already in front
	else{
	    List<Pair> toRemove = new ArrayList<>();
	    for(Pair frontSolution: front){
		if( frontSolution.isDominatedBy(solution) )
		    toRemove.add(frontSolution);
	    }
	    front.removeAll(toRemove);
	    front.add(solution);
	    return true;
	}	
    }
    public boolean addAll(List<Pair> solutions){
	boolean frontChanged = false;
	for(Pair solution: solutions)
	    if( add(solution) ) frontChanged = true;
	return frontChanged;
    }
    public boolean merge(ParetoFront other){
	return addAll(other.getAll());
    }
    public List<Pair> getAll(){
	return front;
    }
    public int size(){
	return front.size();
    }
    public Iterator<Pair> iterator(){
	return front.iterator();
    }
    public String toString(){
	StringBuilder out = new StringBuilder();
	out.append("{\n");
	if( front.size() != 0 ){
	    for(int i = 0; i < front.size()-1; i++){
		out.append(front.get(i));
		out.append(", \n");
	    }
	    out.append(front.get(front.size()-1));
	    out.append("\n");
	}
	out.append("}");	
	return out.toString();
    }
}

