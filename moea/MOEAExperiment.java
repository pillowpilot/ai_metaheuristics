package moea;

import java.io.IOException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Population;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.spi.AlgorithmFactory;

import problem.QAP;
import util.Pair;
import util.ParetoFront;

import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MOEAExperiment extends Observable implements Callable<ParetoFront>{
    private final String name;
    private final int maxGenerations;
    private final QAP problem;
    private int generation;
    private ParetoFront paretoFront;

    public MOEAExperiment(String name, int maxGenerations, QAP problem){
	this.name = name;
	this.maxGenerations = maxGenerations;
	this.problem = problem;
    }
    public ParetoFront call(){
	Algorithm algorithm = buildAlgorithm();

	generation = 0;
	while( generation < maxGenerations ){
	    algorithm.step();
	    paretoFront = buildParetoFront(algorithm.getResult());
	    generation++;
	    setChanged();
	    notifyObservers(getState());
	    //System.out.println("notify " + name);
	}
	algorithm.terminate();

	paretoFront = buildParetoFront(algorithm.getResult());
	return paretoFront;
    }
    private Algorithm buildAlgorithm(){
	String algorithmName = "NSGAII";

	Properties properties = new Properties();
	properties.setProperty("populationSize", "100"); // to change properties

	Algorithm algorithm = AlgorithmFactory.getInstance()
	    .getAlgorithm(algorithmName, properties, problem);

	return algorithm;
    }
    private ParetoFront buildParetoFront(NondominatedPopulation result){
	List<String> lines = new ArrayList<>();
	ParetoFront paretoFront = new ParetoFront();
	for(Solution solution: result){
	    paretoFront.add(new Pair(solution.getObjective(0), solution.getObjective(1)));
	    lines.add(Arrays.toString(EncodingUtils.getPermutation(solution.getVariable(0))) + " => " +
		      Arrays.toString(solution.getObjectives()));
	}
	String filepath = "results";
	try{	    
	    Files.write(Paths.get(filepath), lines);
	}catch(IOException ex){
	    ex.printStackTrace();
	}
	return paretoFront;
    }
    public ParetoFront getParetoFront(){
	return paretoFront;
    }
    public int getGeneration(){
	return generation;
    }
    public String getName(){
	return name;
    }
    public String getState(){
	StringBuilder out = new StringBuilder();
	out.append(name);
	out.append(":");

	if( generation == 0 ) out.append("Unstarted.");
	else if( generation == maxGenerations ) out.append("Finished.");
	else{
	    out.append(" Generation=");
	    out.append(generation);
	}

	if( generation != 0 ){
	    out.append(" OneOfPareto=");
	    out.append(paretoFront.getAll().get(0));
	    out.append(" ParetoSize=");
	    out.append(paretoFront.size());
	}

	return out.toString();
    }
}
