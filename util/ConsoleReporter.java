package util;

import java.util.Observable;
import java.util.Observer;
import java.util.Map;
import java.util.HashMap;

// stop with .terminate()
public class ConsoleReporter implements Observer, Runnable{
    private long startTime;
    private long sleepInterval;
    private Map<Observable, String> experiments;

    public ConsoleReporter(long sleepInterval){
	experiments = new HashMap<>();
	this.sleepInterval = sleepInterval;
	startTime = System.currentTimeMillis();
    }
    public ConsoleReporter(){
	this(1000);
    }
    public void update(Observable subject, Object arg){	
	String state = (String) arg;
	experiments.put(subject, state);
    }
    public void run(){
	boolean keepRunning = true;
	while(keepRunning){
	    long currentTime = System.currentTimeMillis();
	    System.out.print("Reporting... ");
	    System.out.print((currentTime-startTime)/1000+"s elapsed.");
	    System.out.println();
	    for(String state: experiments.values()){
		System.out.println(state);
	    }
	    try{
		Thread.sleep(sleepInterval);
	    }catch(InterruptedException ex){
		keepRunning = false;
	    }
	}
    }
}
