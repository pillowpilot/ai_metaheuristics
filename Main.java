import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import problem.QAP;
import problem.InstanceReader;
import moea.MOEAExperimenter;
import moaco.MOACOExperimenter;
import metrics.*;
import util.ParetoFront;
import util.Pair;
import util.Export;
import util.ConsoleReporter;

public class Main{
    private static int maxGenerations;
    private static String outputFilepath;
    private static QAP problem;

    private static ParetoFront trueFront, moeaFront, moacoFront;
    
    public static void main(String[] args){
	//final String instanceFilepath = "./instances/qapUni.75.0.1.qap.txt";
	//final String instanceFilepath = "./instances/neosguide_qap_9.txt";
	maxGenerations = Integer.parseInt(args[0]);
	outputFilepath = args[2];
	final String instanceFilepath = args[1];	

	problem = new QAP(InstanceReader.read(instanceFilepath));
	trueFront = new ParetoFront();
	
	long startTime = System.currentTimeMillis();
	
	runMOEAExperiments(outputFilepath);
	runMOACOExperiments(outputFilepath);
	
	long endTime = System.currentTimeMillis();
	
	System.out.println("All experiments has finished successfully.");
	System.out.println("Total time :" + (endTime-startTime)/(1000) + "s");

	List<String> report = evaluateParetoFronts();
	for(String line: report)
	    System.out.println(line);

	try{
	    String trueFrontCSVFilepath = outputFilepath  + "_TRUE.csv";
	    Export.asCSV(trueFront, trueFrontCSVFilepath);
	    String reportFilepath = outputFilepath + "_REPORT.csv";
	    Files.write(Paths.get(reportFilepath), report);
	}catch(IOException ex){
	    ex.printStackTrace();
	}
	
    }
    private static void runMOEAExperiments(String csvFilepath){
	ConsoleReporter consoleReporter = new ConsoleReporter();
	Thread consoleReporterThread = new Thread(consoleReporter);
	consoleReporterThread.start();	

	MOEAExperimenter moeaExperimenter = new MOEAExperimenter(maxGenerations, problem);
	moeaExperimenter.setReporter(consoleReporter);
	moeaExperimenter.run();

	consoleReporterThread.interrupt();

	moeaFront = moeaExperimenter.getParetoFront();
	trueFront.merge(moeaFront);
	//System.out.println(moeaParetoFront);

	try{
	    csvFilepath += "_MOEA.csv";
	    Export.asCSV(moeaFront, csvFilepath);
	}catch(IOException ex){
	    ex.printStackTrace();
	}
    }
    private static void runMOACOExperiments(String csvFilepath){
	ConsoleReporter consoleReporter = new ConsoleReporter();
	Thread consoleReporterThread = new Thread(consoleReporter);
	consoleReporterThread.start();

	MOACOExperimenter moacoExperimenter = new MOACOExperimenter(maxGenerations, problem);
	moacoExperimenter.setReporter(consoleReporter);
	moacoExperimenter.run();

	consoleReporterThread.interrupt();
	
	moacoFront = moacoExperimenter.getParetoFront();
	trueFront.merge(moacoFront);
	
	try{
	    csvFilepath += "_MOACO.csv";
	    Export.asCSV(moacoFront, csvFilepath);
	}catch(IOException ex){
	    ex.printStackTrace();
	}
    }
    private static List<String> evaluateParetoFronts(){
	List<String> lines = new ArrayList<>();

	Metric1 metric1 = new Metric1(trueFront);
	double m1MOEA = metric1.evaluate(moeaFront);
	double m1MOACO = metric1.evaluate(moacoFront);

	lines.add("Metric1");
	lines.add("MOEA ="+m1MOEA);
	lines.add("MOACO="+m1MOACO);

	double sigma = calculateSigma(); // See [Paciello]
	double m2MOEA = Metric2.evaluate(moeaFront, sigma);
	double m2MOACO = Metric2.evaluate(moacoFront, sigma);

	lines.add("Metric2");
	lines.add("MOEA ="+m2MOEA);
	lines.add("MOACO="+m2MOACO);

	double m3MOEA = Metric3.evaluate(moeaFront);
	double m3MOACO = Metric3.evaluate(moacoFront);

	lines.add("Metric3");
	lines.add("MOEA ="+m3MOEA);
	lines.add("MOACO="+m3MOACO);

	metrics.Error error = new metrics.Error(trueFront);
	double errorMOEA = error.evaluate(moeaFront);
	double errorMOACO = error.evaluate(moacoFront);

	lines.add("Error");
	lines.add("MOEA ="+errorMOEA);
	lines.add("MOACO="+errorMOACO);

	return lines;
    }
    private static double calculateSigma(){
	Pair best1 = trueFront.getAll().get(0);
	Pair best2 = trueFront.getAll().get(0);
	for(Pair a: trueFront){
	    if( a.getFirst() < best1.getFirst() ) best1 = a;
	    if( a.getSecond() < best2.getSecond() ) best2 = a;	    
	}

	double distance = Math.sqrt(Math.pow(best1.getFirst()-best2.getFirst(), 2)+
				    Math.pow(best1.getSecond()-best2.getSecond(), 2));

	return distance*0.1;
    }
}
