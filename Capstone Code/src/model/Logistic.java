package model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/** This is the clas that houses all of the methods for the predictive algorithm. */
public class Logistic
{
    /** the learning rate */
    private double rate;

    /** the weight to learn */
    private double[][] weights;

    /** the number of iterations */
    private int ITERATIONS = 3000;

    /** Logistic class constructor*/
    public Logistic(int n)
    {
        this.rate = 0.0001;
        weights = new double[4][n];
    }

    private static double sigmoid(double z)
    { return 1.0 / (1.0 + Math.exp(-z)); }

    /** This method trains the algorithm based on a given set of data. */
    public void train(List<Instance> instances) {
        for (int n=0; n<ITERATIONS; n++) {
            double lik0 = 0.0;
            double lik1 = 0.0;
            double lik2 = 0.0;
            double lik3 = 0.0;

            for (int i=0; i<instances.size(); i++) {
                int[][] x = instances.get(i).x;
                double[] predicted = prediction(x);
                int[] label = instances.get(i).label;

                // Learn for each column
                for (int j=0; j<weights.length; j++)
                { weights[0][j] = weights[0][j] + rate * (label[i] - predicted[0]) * x[0][j]; }
                for (int j=0; j<weights.length; j++)
                { weights[1][j] = weights[1][j] + rate * (label[i] - predicted[1]) * x[1][j]; }
                for (int j=0; j<weights.length; j++)
                { weights[2][j] = weights[2][j] + rate * (label[i] - predicted[2]) * x[2][j]; }
                for (int j=0; j<weights.length; j++)
                { weights[3][j] = weights[3][j] + rate * (label[i] - predicted[3]) * x[3][j]; }

                // Not necessary for learning
                lik0 += label[i] * Math.log(predicted[0]) + (1-label[i]) * Math.log(1- predicted[0]);
                lik1 += label[1] * Math.log(predicted[1]) + (1-label[1]) * Math.log(1- predicted[1]);
                lik2 += label[2] * Math.log(predicted[2]) + (1-label[2]) * Math.log(1- predicted[2]);
                lik3 += label[3] * Math.log(predicted[3]) + (1-label[3]) * Math.log(1- predicted[3]);

            }
            //System.out.println("iteration: " + n + " " + Arrays.toString(weights) + " mle: " + lik0);
        }
    }

    /** This method calculates the predicted weights and such for each column. */
    public double[] prediction(int[][] x) {
        double logit0 = .0;
        double logit1 = .0;
        double logit2 = .0;
        double logit3 = .0;

        // Get the weights for each column
        for (int i=0; i<weights.length;i++)
        { logit0 += weights[0][i] * x[0][i]; }
        for (int i=0; i<weights.length;i++)
        { logit1 += weights[1][i] * x[1][i]; }
        for (int i=0; i<weights.length;i++)
        { logit2 += weights[2][i] * x[2][i]; }
        for (int i=0; i<weights.length;i++)
        { logit3 += weights[3][i] * x[3][i]; }

        double[] predict = new double[4];
        predict[0] = sigmoid(logit0);
        predict[1] = sigmoid(logit1);
        predict[2] = sigmoid(logit2);
        predict[3] = sigmoid(logit3);
        return predict;
    }

    public double getProbability(double[] x)
    {
        double pred = (x[0] + x[1] + x[2] + x[3]) / 4;
        return (pred * 100);
    }

    /** This is the inner class of instance, it is what the dataset will be. */
    public static class Instance
    {
        public int[] label;
        public int[][] x; // age, type of travel, class, flight distance

        public Instance(int[] label, int[][] x) {
            this.label = label;
            this.x = x;
        }
    }

    /** This method gets the data set so that the algorithm can be trained.*/
    public static List<Instance> readDataSet() throws FileNotFoundException
    {
        List<Instance> dataset = new ArrayList<Instance>();

        int i = 0;
        int[][] data = new int[4][Review.getAllReviews().size()];
        int[] label = new int[Review.getAllReviews().size()];
        // Get all the data and format it.
        for (Review review : Review.getAllReviews())
        {
            data[0][i] = review.getAge();
            data[3][i] = review.getDistance();

            if (review.getTravelType().equals("Business travel"))
            { data[1][i] = 1; }
            else { data[1][i] = 2; }

            if (review.getClas().equals("Business"))
            { data[2][i] = 1; }
            else if (review.getClas().equals("Eco"))
            { data[2][i] = 2; }
            else { data[2][i] = 3; }

            if (review.getSatis().equals("satisfied"))
            { label[i] = 1; }
            else { label[i] = 0;}
            i++;
        }

        Instance instance = new Instance(label, data);
        dataset.add(instance);

        return dataset;
    }


}
