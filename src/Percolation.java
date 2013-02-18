/*----------------------------------------------------------------
 *  Author:        Samet Atdag
 *  Written:       15/2/2013
 *  Last updated:  18/2/2013
 *
 *  Compilation:   javac Percolation.java
 *  Execution:     java Percolation
 *  
 *  This file contains implementation of Percolation data structure. 
 *
 *
 *----------------------------------------------------------------*/

public class Percolation {

    // WeightedQuickUnionUF data structure.
    private WeightedQuickUnionUF weightedQuickUnionUF;
    
    // A boolean array to mark if element is open or not. 
    private boolean[] isOpenList;
    
    // This value will be assigned to N in Percolation constructor.
    private int givenN;
    
    // Index of virtualBottom element in weightedQuickUnionFind data structure.
    private int virtualBottomIndex = 0;
    
    /**
     *   Percolation data structure object constructor.
     * 
     *      @param int N for NxN grid.
     */
    public Percolation(int N) {

        givenN = N;
        
        /* We want N rows, N columns. 
           Plus, 1 for virtual-top, 1 for virtual-bottom.*/
        int numberOfArrayElements = N * N + 2;
        
        isOpenList = new boolean[numberOfArrayElements];
    
        virtualBottomIndex = numberOfArrayElements - 1;
        
        isOpenList[0] = true;
        isOpenList[virtualBottomIndex] = true;
        
        // Initialize weightedQuickUnionUF object.
        weightedQuickUnionUF = 
                new WeightedQuickUnionUF(numberOfArrayElements);
    }
    
    /**
     *   Opens the site on i-th row and j-th column.
     *
     *   @param int i: 1-based row index.
     *   @param int j: 1-based column index.
     *
     *   Throws a IndexOutOfBoundsException if i or j is smaller than 0 or 
     *   greater than grid size N.
     */
    public void open(int i, int j) {
        checkBoundaries(i, j);
        int index = xyToIndex(i, j);
        isOpenList[index] = true;
        connectElementToOpenNeighbors(i, j);
    }
    
    /**
     *   Returns if the site on i-th row and j-th column is open.
     *
     *   @param int i: 1-based row index.
     *   @param int j: 1-based column index.
     *
     *   Throws a IndexOutOfBoundsException if i or j is smaller than 0 or 
     *   greater than grid size N.
     */
    public boolean isOpen(int i, int j) {
        checkBoundaries(i, j);
        return isOpenList[xyToIndex(i, j)];
    }
    
    /**
     *   Returns if the site on i-th row and j-th column is connected to 
     *   virtualTop element. 
     *
     *   @param int i: 1-based row index.
     *   @param int j: 1-based column index.
     *
     *   Throws a IndexOutOfBoundsException if i or j is smaller than 0 or 
     *   greater than grid size N.
     */
    public boolean isFull(int i, int j) {
        checkBoundaries(i, j);
        return weightedQuickUnionUF.connected(0, xyToIndex(i, j));
            
    }
    
    /**
     *   Returns if Percolation object percolates by testing if virtualTop
     *   and virtualBottom is connected. 
     *
     */
    public boolean percolates() {
        return weightedQuickUnionUF.connected(0, virtualBottomIndex);
    }
    
    /**
     *   Test client for testing Percolation object.
     *
     */
    public static void main(String[] args) {
        Percolation p = new Percolation(10);
        StdOut.println("Done.");

    }
    
    /**
     *   Checks the row and column index by boundary conditions.  
     *
     *   @param int i: 1-based row index.
     *   @param int j: 1-based column index.
     *
     *   Throws a IndexOutOfBoundsException if i or j is smaller than 0 or 
     *   greater than grid size N.
     */
    private void checkBoundaries(int row, int column) {
        if (row <= 0 || row > givenN) 
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (column <= 0 || column > givenN) 
            throw new IndexOutOfBoundsException("column index j out of bounds");
    }
    
    /**
     *   Convert 2-D array to 1-D array representation..  
     *
     *   @param int i: 1-based row index.
     *   @param int j: 1-based column index.
     *
     */
    private int xyToIndex(int row, int column) {
        return ((row-1) * givenN) + column;
    }
    
    /**
     *   Connect siteIndex site to [neighborRow,neighborColumn] site.  
     *
     *   @param int siteIndex: Index of site in 1-D array.
     *   @param int neighborRow: 1-based row index.
     *   @param int neighborColumn: 1-based column index.
     *
     *   Throws a IndexOutOfBoundsException if i or j is smaller than 0 or 
     *   greater than grid size N.
     */
    private void connectSiteToNeighbor(int siteIndex, int neighborRow, 
            int neighborColumn) {
        int neighborIndex = xyToIndex(neighborRow, neighborColumn);
        weightedQuickUnionUF.union(siteIndex, neighborIndex);
    }
    
    /**
     *   Connect site on [row,column] to all open neighbors.  
     *
     *   @param int i: 1-based row index.
     *   @param int j: 1-based column index.
     *
     *   Throws a IndexOutOfBoundsException if i or j is smaller than 0 or 
     *   greater than grid size N.
     */
    private void connectElementToOpenNeighbors(int row, int column) {
        int siteIndex = xyToIndex(row, column);
        
        if (row == 1) {
            weightedQuickUnionUF.union(0, siteIndex);
        }
        
        if (row == givenN) {
            weightedQuickUnionUF.union(virtualBottomIndex, siteIndex);
        }
        
        
        /* Let's find neighbors of given element. 
         Check border conditions, if neighbor is open also, connect them.*/
        if (row != 1) { // Above neighbor
            if (isOpen(row-1, column)) {
                connectSiteToNeighbor(siteIndex, row-1, column);
            }
        } 
        if (row != givenN) { // Below neighbor 
            if (isOpen(row+1, column)) {
                connectSiteToNeighbor(siteIndex, row+1, column);
            }
        }
        
        if (column != 1) { // Left neighbor
            if (isOpen(row, column-1)) {
                connectSiteToNeighbor(siteIndex, row, column-1);
            }
        } 
        if (column != givenN) { // Right neighbor
            if (isOpen(row, column+1)) {
                connectSiteToNeighbor(siteIndex, row, column+1);
            }
        }
    }
}
