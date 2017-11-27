package util;

import java.util.List;
import java.util.ArrayList;

public class SquareMatrix{
    private final int size;
    private List<List<Double>> values;
	
    public SquareMatrix(int size){
	this.size = size;
	values = new ArrayList<>();
	for(int i = 0; i < size; i++){
	    
	    List<Double> temp = new ArrayList<>();
	    for(int j = 0; j < size; j++) temp.add(0.0);
	    
	    values.add(temp);
	}
    }
    public Double get(int row, int column) throws IndexOutOfBoundsException{
	if( 0<=row && row<size && 0<=column && column<size )
	    return values.get(row).get(column);
	else{
	    String msg = "("+row+", "+column+") must belong to [0, "+size+")^2";
	    throw new IndexOutOfBoundsException(msg);
	}
    }
    public void set(int row, int column, double value) throws IndexOutOfBoundsException{
	if( 0<=row && row<size && 0<=column && column<size )
	    values.get(row).set(column, value);
	else{
	    String msg = "("+row+", "+column+") must belong to [0, "+size+")^2";
	    throw new IndexOutOfBoundsException(msg);
	}
    }
    public int getMax(){
	double value = Double.POSITIVE_INFINITY;
	for(int row = 0; row < size; row++){
	    for(int column = 0; column < size; column++){
		value = Math.max(value, get(row, column));
	    }
	}
	return (int)(value);
    }
    public int size(){ return size; }
    public String toString(){
	StringBuilder out = new StringBuilder();
	final String format = "%13.3f";
	
	for(int i = 0; i < values.size(); i++){
	    out.append(String.format(format, get(i, 0)));
	    for(int j = 1; j < values.size(); j++){
		out.append(", ");
		out.append(String.format(format, get(i, j)));
	    }
	    out.append("\n");
	}

	return out.toString();
    }
}
