package problem;

public class QAPInstance{
    private int size;
    private int[][] distances;
    private int[][] flow1;
    private int[][] flow2;
    
    public QAPInstance(int size, int[][] distances, int[][] flow1, int[][] flow2) throws IllegalArgumentException{
	this.size = size;
	
	if( checkDistances(distances) )
	    this.distances = distances;
	else throw new IllegalArgumentException("Distance Matrix is faulty.");
	
	if( checkFlow(flow1) )
	    this.flow1 = flow1;
	else throw new IllegalArgumentException("Flow 1 Matrix is faulty.");
	
	if( checkFlow(flow2) )
	    this.flow2 = flow2;
	else throw new IllegalArgumentException("Flow 2 Matrix is faulty.");
    }
    public int getProblemSize(){
	return size;
    }    
    public int getDistance(int location1, int location2) throws IllegalArgumentException{
	return getEntry(distances, location1, location2);
    }
    public int getFlow1(int facility1, int facility2) throws IllegalArgumentException{
	return getEntry(flow1, facility1, facility2);
    }
    public int getFlow2(int facility1, int facility2) throws IllegalArgumentException{
	return getEntry(flow2, facility1, facility2);
    }
    private int getEntry(int[][] matrix, int i, int j) throws IllegalArgumentException{
	if( 0 <= i && i < size && 0 <= j && j < size )
	    return matrix[i][j];
	else throw new IllegalArgumentException(i+" and "+j+" must belong to [0, "+size+").");
    }
    public int getMaxDistance(){
	return getMaxEntry(distances);
    }
    public int getMaxFlow1(){
	return getMaxEntry(flow1);
    }
    public int getMaxFlow2(){
	return getMaxEntry(flow2);
    }
    private int getMaxEntry(int[][] matrix){
	double maxEntry = Double.NEGATIVE_INFINITY;
	for(int i = 0; i < matrix.length; i++){
	    for(int j = 0; j < matrix[i].length; j++){
		maxEntry = Math.max(maxEntry, matrix[i][j]);
	    }
	}
	return (int)(maxEntry);
    }
    /*
      Check if int[][] is a square matrix of size size
     */
    private static boolean checkMatrix(int size, int[][] matrix){
	if( matrix.length != size )
	    return false;
	
	for(int i = 0; i < matrix.length; i++)
	    if( matrix[i].length != size )
		return false;
	return true;
    }
    private boolean checkDistances(int[][] distances){
	// check if square matrix of right dimentions
	if( !checkMatrix(size, distances) )
	    return false;
	// check if distance to itself is null, d(i, i) = 0 for all i
	for(int i = 0; i < size; i++)
	    if( distances[i][i] != 0 )
		return false;
	// check if distance is reflexive, d(i, j) = d(j, i) for all i, j
	for(int i = 0; i < size; i++)
	    for(int j = i+1; j < size; j++)
		if( distances[i][j] != distances[j][i] )
		    return false;
	return true;
    }
    private boolean checkFlow(int[][] flow){
	return checkMatrix(this.size, flow);
    }    
}
