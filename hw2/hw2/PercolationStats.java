package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private Percolation cur;
    private double meanAdd;
    private double[] tempList;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        tempList = new double[T];
        for (int i = 0; i < T; i++) {
            cur = pf.make(N);
            while (!cur.percolates()) {
                cur.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            tempList[i] = ((double) cur.numberOfOpenSites() / (double) (N * N));
            meanAdd += ((double) cur.numberOfOpenSites() / (double) (N * N));
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return meanAdd / tempList.length;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double mu = mean();
        double temp = 0;
        for (int i = 0; i < tempList.length; i++) {
            temp = temp + (tempList[i] - mu) * (tempList[i] - mu);
        }
        double sum = temp / (tempList.length - 1);
        return Math.pow(sum, 0.5);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        double temp = mean();
        double temp1 = 1.96 * stddev() / (Math.pow(tempList.length, 0.5));
        return temp - temp1;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        double temp = mean();
        double temp1 = 1.96 * stddev() / (Math.pow(tempList.length, 0.5));
        return temp + temp1;
    }
}
