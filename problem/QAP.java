package problem;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.moeaframework.problem.AbstractProblem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.PRNG;

/*
  Evaluation is over a permutation p over {1, 2, ..., n}.
  p(i) is the location assigned to facility i.
 */
public class QAP extends AbstractProblem{
    private QAPInstance instance;

    public QAP(QAPInstance instance){
	super(1, 2);
	this.instance = instance;
    }
    public QAPInstance getQAPInstance(){
	return instance;
    }
    @Override
    public Solution newSolution(){
	Solution solution = new Solution(numberOfVariables, numberOfObjectives);
	solution.setVariable(0, EncodingUtils.newPermutation(instance.getProblemSize()));
	return solution;
    }
    @Override
    public void evaluate(Solution solution){
	final int[] permutation = EncodingUtils.getPermutation(solution.getVariable(0));

	double[] objectives = evaluate(permutation);

	solution.setObjectives(objectives);
    }
    public double[] evaluate(int[] permutation){
	double[] objectives = new double[2];
	objectives[0] = objectives[1] = 0;

	final int n = instance.getProblemSize();
	for(int facility1 = 0; facility1 < n; facility1++){
	    for(int facility2 = 0; facility2 < n; facility2++){
		int flow1 = instance.getFlow1(facility1, facility2);
		int flow2 = instance.getFlow2(facility1, facility2);

		int location1 = permutation[facility1];
		int location2 = permutation[facility2];
		int distance = instance.getDistance(location1, location2);

		double deltaCost1 = flow1*distance;
		double deltaCost2 = flow2*distance;

		objectives[0] += deltaCost1;
		objectives[1] += deltaCost2;
	    }
	}

        return objectives;
    }
}
