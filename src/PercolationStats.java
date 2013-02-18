public class PercolationStats {
	Percolation p;
	int[][] triedRowColumnPairs;
	double numberOfTries = 0;
	double[] monteCarloResults;
	int mT;
	
	public PercolationStats(int N, int T) {
		if (N<=0 || T<=0) {
			throw new IllegalArgumentException("N and T must be bigger than 0.");
		}
		mT = T;
		monteCarloResults = new double[T];
		
		
		// StdRandom.uniform calls can produce same row:column pairs. 
		// Let's try again if row:column pair is tried before. 
		// I assume at N*N*2 calls are enough for percolation.
		for (int i=0; i<T; i++) {
			
			
			// Initialize Monte Carlo variables for experiment:
			p = new Percolation(N);
			numberOfTries = 0.0;
			triedRowColumnPairs = new int[N][N];
			
			// Let's begin the experiment.
			for (int j=0; j<N*N*2; j++) {
				int row = StdRandom.uniform(N) + 1;
				int column = StdRandom.uniform(N) + 1;
				if (triedRowColumnPairs[row-1][column-1] == 1) {
					continue;
				} else {
					triedRowColumnPairs[row-1][column-1] = 1;
					numberOfTries++;
					p.open(row, column);
					if (p.percolates()) {
						StdOut.println("Percolates at: " + numberOfTries);
						monteCarloResults[i] = numberOfTries/(N*N);
						
						// If it percolates, stop this experiment and continue
						// to next experiment.
						break;
					} 
				}
			}
		}
	}
	public double mean() {
		return StdStats.mean(monteCarloResults);
	}
	public double stddev() {
		return StdStats.stddev(monteCarloResults);
	}
	
	// Used the formula given in assignment definition.
	public double confidenceLo() {
		return mean() - ((1.96 * stddev()) / Math.sqrt(mT));
	}
	
	// Used the formula given in assignment definition.
	public double confidenceHi() {
		return mean() + ((1.96 * stddev()) / Math.sqrt(mT));
	}
	public static void main(String[] args) {
		PercolationStats pstats = new PercolationStats(20, 11);
		StdOut.println("mean\t\t\t = " + pstats.mean());
		StdOut.println("stddev\t\t\t = " + pstats.stddev());
		StdOut.println("95% confidence interval\t = " + pstats.confidenceLo() + ", " + pstats.confidenceHi());
	}
}