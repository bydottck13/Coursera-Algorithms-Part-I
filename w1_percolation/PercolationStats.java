import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
// import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private double mn;
    private double stdDev;
    private double cfnLo;
    private double cfnHi;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("n " + n + " is not greater than 0");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("trials " + trials + " is not greater than 0");
        }
        double[] thresholds = new double[trials];
        double allGrids = n*n;
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                perc.open(row, col);
            }
            int numberOfOpenSites = perc.numberOfOpenSites();
            // StdOut.println("trials["+i+"]: numberOfOpenSites is "+numberOfOpenSites);
            thresholds[i] = numberOfOpenSites/allGrids;
        }
        mn = StdStats.mean(thresholds);
        stdDev = StdStats.stddev(thresholds);
        double factor = 1.96*stdDev/Math.sqrt(trials);
        cfnLo = mn-factor;
        cfnHi = mn+factor;
    }

    // sample mean of percolation threshold
    public double mean() {
        return mn;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return cfnLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return cfnHi;
    }

    // test client (described below)
    public static void main(String[] args) {
        if (args.length == 2) {
            // Stopwatch sw = new Stopwatch();
            int n = Integer.parseInt(args[0]);
            int trials = Integer.parseInt(args[1]);

            // double t1 = sw.elapsedTime();
            PercolationStats percStat = new PercolationStats(n, trials);
            // double t_all = sw.elapsedTime()-t1;
            StdOut.println("mean                    = "+percStat.mean());
            StdOut.println("stddev                  = "+percStat.stddev());
            StdOut.println("95% confidence interval = ["+percStat.confidenceLo()+", "+percStat.confidenceHi()+"]");
            // StdOut.println("time                    = "+t_all);
        }
    }
}