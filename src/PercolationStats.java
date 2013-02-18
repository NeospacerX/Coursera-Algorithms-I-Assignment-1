/*----------------------------------------------------------------
 *  Author:        Samet Atdag
 *  Written:       15/2/2013
 *  Last updated:  18/2/2013
 *
 *  Compilation:   javac PercolationStats.java
 *  Execution:     java PercolationStats
 *  
 *  Statistics tools for Percolation data structure.  
 *
 * % java PercolationStats 2 10000
 * mean                    = 0.666925
 * stddev                  = 0.11776536521033558
 * 95% confidence interval = 0.6646167988418774, 0.6692332011581226
 *
 *----------------------------------------------------------------*/
public class PercolationStats {
    
    // Percolation object to get statistics.
    private Percolation p;
    
    // To avoid same row,column pairs, we hold the already tried pairs in this
    // array.
    private int[][] triedRowColumnPairs;
    
    // Number of open() calls. (without retries)
    private double numberOfTries = 0;
    
    // Result of each experiment is hold in this array.
    private double[] monteCarloResults;
    
    // Parameter T which is given at creation.
    private int mT;
    
    /**
     *   Initializes PercolationStats, with NxN size Percolation Object and 
     *   T-times trying Monte Carlo simulation.
     *
     *   Throws a IllegalArgumentException if N or T is smaller then 1.
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw 
                new IllegalArgumentException("N, T must be bigger than 0.");
        }
        
        mT = T;
        monteCarloResults = new double[T];
        
        
        /* StdRandom.uniform calls can produce same row:column pairs. 
           Let's try again if row:column pair is tried before. 
           I assume at N*N*2 calls are enough for percolation. */
        for (int i = 0; i < T; i++) {
            
            // Initialize Monte Carlo variables for experiment:
            p = new Percolation(N);
            numberOfTries = 0.0;
            triedRowColumnPairs = new int[N][N];
            
            // Let's begin the experiment.
            for (int j = 0; j < N * N * 2; j++) {
                int row = StdRandom.uniform(N) + 1;
                int column = StdRandom.uniform(N) + 1;
                if (triedRowColumnPairs[row-1][column-1] == 1) {
                    continue;
                } else {
                    triedRowColumnPairs[row-1][column-1] = 1;
                    numberOfTries++;
                    p.open(row, column);
                    if (p.percolates()) {
                        monteCarloResults[i] = numberOfTries/(N*N);
                        
                        /* If it percolates, stop this 
                           experiment and continue to next 
                           experiment.*/
                        break;
                    } 
                }
            }
        }
        p = null;
        triedRowColumnPairs = null;
    }
    
    /**
     *   Calculates and returns arithmetic average of Monte Carlo simulation
     *   results.
     *
     */
    public double mean() {
        return StdStats.mean(monteCarloResults);
    }
    
    /**
     *   Calculates and returns standard deviation of Monte Carlo simulation
     *   results. 
     *
     */
    public double stddev() {
        return StdStats.stddev(monteCarloResults);
    }
    
    /**
     *   Calculates and returns lower bound of the 95% confidence interval 
     *   using the formula given in assignment definition
     *
     */
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(mT));
    }
    
    /**
     *   Calculates and returns upper bound of the 95% confidence interval 
     *   using the formula given in assignment definition
     *
     */
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(mT));
    }
    
    /**
     *   Test client for testing PercolationStats object.
     *
     *   Throws a IllegalArgumentException if first or second argument is 
     *   smaller then 1.
     */
    public static void main(String[] args) {
        PercolationStats pstats = 
            new PercolationStats(Integer.parseInt(args[0]), 
                Integer.parseInt(args[1]));
        StdOut.println("mean\t\t\t = " + pstats.mean());
        StdOut.println("stddev\t\t\t = " + pstats.stddev());
        StdOut.println("95% confidence interval\t = " + pstats.confidenceLo()
                + ", " + pstats.confidenceHi());
    }
}
