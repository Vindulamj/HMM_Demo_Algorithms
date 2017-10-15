import java.util.Arrays;

public class Test {

    public void excuteViterbi(String[] observations,
                              String[] states,
                              double[] startProbability,
                              double[][] transitionProbability,
                              double[][] emissionProbability) {

        //start - Healthier(normal) and start - Fever(normal)
        Node[] T = new Node[states.length];
        for (int state = 0; state < states.length; state++) {
            int[] intArray = new int[1];
            intArray[0] = state;
            T[state] = new Node( intArray, startProbability[state] * emissionProbability[state][0]);
        }

        //Calculate best path
        for(int j = 1 ; j < observations.length ; j++) {

            Node[] tempT = new Node[T.length];

            for(int  k = 0 ; k < T.length; k++) {

                double soFarMaxProb = 0;
                int [] soFarBestPath = new int[0];
                for (int i = 0; i < T.length; i++) {
                    double previousStateProb = T[i].currentBestProb;
                    int[] previousPath = T[i].currentBestPath;

                    double newProb = previousStateProb * transitionProbability[previousPath[previousPath.length - 1]][k] * emissionProbability[k][j];
                    int [] newPath = copyAndAddElement(previousPath, k);

                    if (soFarMaxProb < newProb) {
                        soFarMaxProb = newProb;
                        soFarBestPath = newPath;
                    }
                }
                Node node = new Node(soFarBestPath,soFarMaxProb);
                tempT[k] = node;
            }
            for(int p = 0 ; p <T.length ; p++) {
                T[p] = tempT[p];
            }
        }

        //print the results
        double maxProb = 0;
        int [] bestPath = new int[0];
        for (Node node : T) {
            if (maxProb < node.currentBestProb) {
                maxProb = node.currentBestProb;
                bestPath = node.currentBestPath;
            }
        }

        for(int state : bestPath) {
            System.out.print(states[state] + " ");
        }

    }

    // Data holder for state node
    private static class Node {
        public int[] currentBestPath;
        public double currentBestProb;

        public Node(int[] path, double prob) {
            this.currentBestPath = Arrays.copyOf(path, path.length);
            this.currentBestProb = prob;
        }
    }
    
    // Copy the given array and add the given element at the end
    private static int[] copyAndAddElement(int[] previousArray, int valueToBeInserted) {
        int[] newArray = Arrays.copyOf(previousArray, previousArray.length + 1);
        newArray[previousArray.length] = valueToBeInserted;
        return newArray;
    }

    //main method to test the example
    public static void main(String[] args) throws Exception {
        String[] states = { "Healthy", "Fever" };
        String[] observations = {"normal", "cold", "dizzy" };
        double[] startProbability = { 0.6, 0.4 };
        double[][] transitionProbability = { { 0.7, 0.3 }, { 0.4, 0.6 } };
        double[][] emissionProbability = { { 0.5, 0.4, 0.1 }, { 0.1, 0.3, 0.6 }};
        Test viterbiTest = new Test();
        viterbiTest.excuteViterbi(observations, states, startProbability, transitionProbability, emissionProbability);

    }
}
