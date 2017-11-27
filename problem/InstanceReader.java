package problem;

import java.io.IOException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;

public class InstanceReader{
    public static QAPInstance read(String filepath){
	QAPInstance qapInstance = null;
	try{
	    Scanner input = new Scanner(new FileReader(filepath));
	    final int size = input.nextInt();	    

	    int[][] flow1 = new int[size][size];
	    readMatrix(input, size, flow1);

	    int[][] flow2 = new int[size][size];
	    readMatrix(input, size, flow2);

	    int[][] distances = new int[size][size];
	    readMatrix(input, size, distances);

	    qapInstance = new QAPInstance(size, distances, flow1, flow2);
	    
	    input.close();
	}catch(IOException | NoSuchElementException  ex){
	    ex.printStackTrace();
	}
	return qapInstance;
    }
    private static void readMatrix(Scanner input, int size, int[][] matrix)
	throws NoSuchElementException, InputMismatchException{
	for(int i = 0; i < size; i++){
	    for(int j = 0; j < size; j++){
		matrix[i][j] = input.nextInt();
	    }
	}
    }
    public static void main(String[] args){
	//String filepath = "./instances/qapUni.75.0.1.qap.txt";
	//String filepath = "./instances/neosguide_qap_9.txt";
	String filepath = args[0];
	
	QAPInstance qap = InstanceReader.read(filepath);
	System.out.println("Problem size: " + qap.getProblemSize());

	System.out.println("Distances:");
	for(int location1 = 0; location1 < qap.getProblemSize(); location1++){
	    for(int location2 = 0; location2 < qap.getProblemSize(); location2++){
		System.out.print(String.format("%4d", qap.getDistance(location1, location2)));
	    }
	    System.out.println();
	}
	System.out.println();

	System.out.println("Flow 1:");
	for(int facility1 = 0; facility1 < qap.getProblemSize(); facility1++){
	    for(int facility2 = 0; facility2 < qap.getProblemSize(); facility2++){
		System.out.print(String.format("%4d", qap.getFlow1(facility1, facility2)));
	    }
	    System.out.println();
	}
	System.out.println();

	System.out.println("Flow 2:");
	for(int facility1 = 0; facility1 < qap.getProblemSize(); facility1++){
	    for(int facility2 = 0; facility2 < qap.getProblemSize(); facility2++){
		System.out.print(String.format("%4d", qap.getFlow2(facility1, facility2)));
	    }
	    System.out.println();
	}
	System.out.println();
    }
}
