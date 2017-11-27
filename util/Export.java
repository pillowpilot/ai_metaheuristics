package util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Export{
    public static void asCSV(List<Pair> front, String outputFilepath) throws IOException{
	List<String> lines = new ArrayList<>();
	lines.add("flow1,flow2");
	for(Pair solution: front){
	    final double first = solution.getFirst();
	    final double second = solution.getSecond();
	    lines.add(first+","+second);
	}
	Files.write(Paths.get(outputFilepath), lines);
    }
    public static void asCSV(ParetoFront front, String outputFilepath) throws IOException{
	asCSV(front.getAll(), outputFilepath);
    }
}
