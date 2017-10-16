import java.util.ArrayList;

public class ForwardBackwardAlgorithm {

    public void executeForwardBackwardsAlgo(String[] observations,
                                           String[] states,
                                           double[] startProbability,
                                           double[][] transitionProbability,
                                           double[][] emissionProbability) {

        //forward-algorithm
        ArrayList<double []> forwardList = new ArrayList<>();
        double [] forwardPrevious = new double[states.length];

        for(int i = 0 ; i < observations.length ; i++) {
            double [] f_curr = new double[states.length];
            for (int k = 0 ; k < states.length ; k++) {
                double prevForwardSum = 0;
                if (i == 0) {
                    prevForwardSum = startProbability[k];
                } else {
                    for (int y = 0 ; y <states.length ; y++) {
                        prevForwardSum += forwardPrevious[y] * transitionProbability[y][k];
                    }
                }
                f_curr[k] = emissionProbability[k][i] * prevForwardSum;

            }
            forwardList.add(f_curr);
            forwardPrevious = f_curr;
        }

        double forwardProbability = 0;
        for (int q = 0 ; q < states.length ; q++) {
            forwardProbability += forwardPrevious[q] * transitionProbability[q][transitionProbability.length];
        }

        //backward-algorithm
        ArrayList<double []> backwardList = new ArrayList<>();
        double [] backwardPrevious = new double[states.length];


        for(int i = 0 ; i < observations.length ; i++) {
            double [] backwardCurrent = new double[states.length];
            for (int k = 0 ; k < states.length ; k++) {

                if (i == 0) {
                    backwardCurrent[k] = transitionProbability[k][transitionProbability.length];
                } else {
                    for (int y = 0 ; y < states.length ; y++) {
                        backwardCurrent[k] +=  transitionProbability[k][y] *
                                emissionProbability[y][observations.length-i] * backwardPrevious[y];
                    }
                }
            }
            backwardList.add(backwardCurrent);
            backwardPrevious = backwardCurrent;
        }

        double backwardProbability = 0;
        for (int q = 0 ; q < states.length ; q++) {
            backwardProbability += startProbability[q] * emissionProbability[q][0] * backwardPrevious[q];
        }

        //merge the two parts
        ArrayList<Double> posteriorProbabilities = new ArrayList<>();
        for (int l = 0; l < observations.length ;l++) {
            for (int s = 0 ; s < states.length ; s++) {
                posteriorProbabilities.add((forwardList.get(l)[s] *
                        backwardList.get(observations.length - l-1)[s])/ forwardProbability);
            }
        }

        //print results
        for (int d = 0; d < observations.length * 2 ; d = d + states.length) {
            for(int b = 0 ; b < states.length ; b++) {
                System.out.print(d/states.length + " : " + posteriorProbabilities.get(d+b) + " ");
            }
            System.out.println();
        }

    }

    //main method to test the example
    public static void main(String[] args) throws Exception {
        String[] states = { "Healthier", "Fever" };
        String[] observations = {"normal", "cold", "dizzy"};
        double[] startProbability = { 0.6, 0.4 };
        double[][] transitionProbability = { { 0.69, 0.3, 0.01 }, { 0.4, 0.59, 0.01 } };
        double[][] emissionProbability = { { 0.5, 0.4, 0.1 }, { 0.1, 0.3, 0.6 }};
        ForwardBackwardAlgorithm forwardBackwardAlgorithmTest = new ForwardBackwardAlgorithm();
        forwardBackwardAlgorithmTest.executeForwardBackwardsAlgo(observations, states,
                startProbability, transitionProbability, emissionProbability);
    }
}

