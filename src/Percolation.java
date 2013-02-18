
public class Percolation {
	// WeightedQuickUnionUF data structure.
	private WeightedQuickUnionUF weightedQuickUnionUF;
	// A boolean array to mark if element is open or not. 
	private boolean[] isOpenList;
	
	// This value will be assigned to N in Percolation constructor.
	private int givenN;
	private int virtualBottomId = 0;
	
	private void checkBoundaries(int row, int column) {
		if (row <= 0 || row > givenN) 
			throw new IndexOutOfBoundsException("row index i out of bounds");
		if (column <= 0 || column > givenN) 
			throw new IndexOutOfBoundsException("column index j out of bounds");
	}
	
	private int xyToIndex(int row, int column) {
		return ((row-1) * givenN) + column;
	}
	private void connectSiteToNeighbor(int siteIndex, int neighborRow, 
			int neighborColumn) {
		int neighborIndex = xyToIndex(neighborRow, neighborColumn);
		StdOut.println("Connect " + siteIndex + " to " + neighborIndex);
		weightedQuickUnionUF.union(siteIndex, neighborIndex);
	}
	
	private void connectElementToOpenNeighbors(int row, int column) {
		int siteIndex = xyToIndex(row, column);
		
		if (row == 1) {
			StdOut.println("A first row element came, connecting to 0. Row: " + row + " column: " + column + " siteIndex: " + siteIndex);
			weightedQuickUnionUF.union(0, siteIndex);
		}
		
		if (row == givenN) {
			StdOut.println("A last row element came, connecting to virtualBottom. Row: " + row + " column: " + column + " siteIndex: " + siteIndex);
			weightedQuickUnionUF.union(virtualBottomId, siteIndex);
			//return;
		}
		
		
		StdOut.println("Me; coordinates: " + row + "-" + column + ", index: " + siteIndex);
		// Let's find neighbors of given element. 
		// Check border conditions, if neighbor is open also, connect them.
		if (row != 1) { // Above neighbor
			if (isOpen(row-1, column)) {
				StdOut.println("Above neighbour: " + xyToIndex(row-1, column));
				connectSiteToNeighbor(siteIndex, row-1, column);
			}
		} 
		if (row != givenN) { // Below neighbor 
			if (isOpen(row+1, column)) {
				StdOut.println("Below neighbour: " + xyToIndex(row+1, column));
				connectSiteToNeighbor(siteIndex, row+1, column);
			}
		} else {
			
		}
		
		if (column != 1) { // Left neighbor
			if (isOpen(row, column-1)) {
				StdOut.println("Left neighbour: " + xyToIndex(row, column-1));
				connectSiteToNeighbor(siteIndex, row, column-1);
			}
		} 
		if (column != givenN) { // Right neighbor
			if (isOpen(row, column+1)) {
				StdOut.println("Right neighbour: " + xyToIndex(row, column+1));
				connectSiteToNeighbor(siteIndex, row, column+1);
			}
		} else {
			
		}
	}
	
	public Percolation(int N) {
		StdOut.println("Constructor is called.");

		givenN = N;
		StdOut.println("GivenN: " + givenN);
		// We want N rows, N columns. 
		// Plus, 1 for virtual-top, 1 for virtual-bottom.
		int numberOfArrayElementsInQuickUnionFind = N * N + 2;
		StdOut.println("Number of elements in UF: " 
				+ numberOfArrayElementsInQuickUnionFind);
		
		isOpenList = new boolean[numberOfArrayElementsInQuickUnionFind];
	
		virtualBottomId = numberOfArrayElementsInQuickUnionFind - 1;
		StdOut.println("VirtualBottomId: " + virtualBottomId);
		
		isOpenList[0] = true;
		isOpenList[virtualBottomId] = true;
		
		// Initialize weightedQuickUnionUF object.
		weightedQuickUnionUF = 
				new WeightedQuickUnionUF(numberOfArrayElementsInQuickUnionFind);
		
//		printOpenList();
	}
	
	public void open(int i, int j) {
		checkBoundaries(i, j);
		int index = xyToIndex(i, j);
		isOpenList[index] = true;
		connectElementToOpenNeighbors(i, j);
//		printOpenList();
//		printRootsOfWeightedQuickUnionFindDataStructure();
	}
	
	public boolean isOpen(int i, int j) {
		checkBoundaries(i,j);
		//StdOut.println("Row: " + i + ", column: " + j + "; converted to " 
		//		+ xyToIndex(i, j));
		//StdOut.println("x:" + i + " y:" + j + " isOpen: " + isOpenList[xyToIndex(i, j)]);
		return isOpenList[xyToIndex(i, j)];
	}
	
	public boolean isFull(int i, int j) {
		checkBoundaries(i,j);
		return weightedQuickUnionUF.connected(0, xyToIndex(i, j));
			
	}
	
	public boolean percolates() {
		//StdOut.println("Trying to find if 0 and " + virtualBottomId + " is connected.");
		StdOut.println("root of 0:" + weightedQuickUnionUF.find(0));
		StdOut.println("root of virtualBottom:" + weightedQuickUnionUF.find(virtualBottomId));
		return weightedQuickUnionUF.connected(0, virtualBottomId);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Percolation p = new Percolation(10);
		StdOut.println("Done.");

	}
	private void printOpenList() {
		StdOut.println("=========");
		int virtualTop = isOpenList[0] ? 1 : 0;
		StdOut.println(virtualTop);
		for(int i=0; i<givenN; i++) {
			for(int j=1; j<givenN+1; j++) {
				int integerRepresentative = 0;
				if (isOpenList[i*givenN+j]) {
					integerRepresentative = 1;
				}
				StdOut.print(integerRepresentative);	
			}
			StdOut.println("");
		}
		int virtualBottom = isOpenList[givenN*givenN+1] ? 1 : 0;
		StdOut.println(virtualBottom);
		StdOut.println("=========");
	}
	private void printRootsOfWeightedQuickUnionFindDataStructure() {
		for(int i=0; i<virtualBottomId+1; i++) {
			if (i< 10)
				StdOut.print(i + "  ");
			else 
				StdOut.print(i + " ");
			
		}
		StdOut.println();
		for(int i=0; i<virtualBottomId+1; i++) {
			int root = weightedQuickUnionUF.find(i);
			if (root < 10) {
				StdOut.print(root + "  ");
			} else {
				StdOut.print(root + " ");
			}
				
		}
		StdOut.println();
	}
 
}
