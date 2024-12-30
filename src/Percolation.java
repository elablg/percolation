import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // declaring our variables (the ones in the pdf):
    private int gridSize; // represents a size of the grid (n)
    private int gridSquared; // numbers of size in a grid (n * n)
    private boolean[][] grid; // represents the grid
    private WeightedQuickUnionUF wquFind; // using weighted quick union to keep track of all opened/connected sites
    private int virtualTop; // represents virtual top site, connect virtualTop to every open site in the first row of the grid
    private int virtualBottom; // represents virtual bottom site, connect virtualBottom to every open site at the last row of the grid


    public Percolation(int n, double p) {
        // initializing the instance variables that we created above
        gridSize = n;
        gridSquared = n * n;
        grid = new boolean[n][n];
        wquFind = new WeightedQuickUnionUF(gridSquared + 2); // +2 is the virtual top and virtual bottom
        virtualTop = gridSquared;
        virtualBottom = gridSquared + 1;

        openAllSites(p); // calling openAllSites with probability p
    }

    private void openSite(int row, int col) { // takes two parameters integer row and integer column.
        // to check if the sites are open, if so return it and do nothing.
        if (!isOpen(row, col)) {
            grid[row][col] = true;
        }
        // opens the site
        //grid[row][col] = true;

        // connecting virtualTop to the first row
        if (row == 0) { // checks if the current site is in the first row
            wquFind.union(virtualTop, row * gridSize + col);
        }
        //connect virtualBottom to the last row
        if (row == gridSize - 1) {
            wquFind.union(virtualBottom, row * gridSize + col);
        }
        //connecting our current open site to the adjacent open site/sites :
        connectAdj(row, col);

    }

    private void connectAdj(int row, int col) {
        // this method is for connecting two open sites;

        int[][] adjacents = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        // this 2D array is for defining whether it is above, below, left or right

        for (int[] adj : adjacents) {
            // interates each direction and giving new coordinates
            int adjRow = row + adj[0];
            int adjCol = col + adj[1];

            // if it's open and adjacent, connect our current site to the adjacent site
            if (isValid(adjRow, adjCol) && grid[adjRow][adjCol]) {
                wquFind.union(row * gridSize + col, adjRow * gridSize + adjCol); // we are getting the index here
            }
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < gridSize && col >= 0 && col < gridSize;
        // isValid method checks whether given site is within the bounds of our grid:
        // ensuring the given row and column are >= 0 and also smaller than the number of row/column in the grid
    }

    // this private method checks if a site of a grid is open or not
    private boolean isOpen(int row, int col) {
        return isValid(row, col) && grid[row][col];
        //return grid[row - 1][col - 1]; // this checks the state of the specified site
        // index in java start from 0, but our grid description started from 1. Therefore adjusted the index by doing - 1.
    }

    private void openAllSites(double p) {
        //in here we are opening all the sites with probability p that was in the pdf
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (StdRandom.bernoulli(p)) { // using bernoulli that is in StdRandom
                    openSite(i, j);
                }
            }
        }
    }

    // percolationCheck returns true if the system percolates
    private boolean percolationCheck() {
        return wquFind.connected(virtualTop, virtualBottom); // we are checking it with weighted quick union
    }

    private void displayGrid() {
        // here we are displaying the grid to see which are percolates
        // loop within a loop and we are iterating our rows and columns. we do this to cover every cell in the grid
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (grid[row][col]) {
                    // this checks whether the current site is open and if it's its setting the color to white
                    StdDraw.setPenColor(StdDraw.WHITE);
                    //the parameters of the squares
                    // the last parameter (halfLength) is the distance between each cell, when i put 0.5 they touch each other
                    // I tried to find the best values to the display everything while trying to not overlap with each other
                    StdDraw.filledSquare(col + 0.5, gridSize - row - 0.5, 0.48);
                }
                else {
                    // else the site is close -> set the cell to black
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledSquare(col + 0.5, gridSize - row - 0.5, 0.48);
                }
            }
        }
        StdDraw.show(); // display the grid
        StdDraw.pause(5000); // pause for 5 seconds
    }

    // testing percolation:
    public static void main(String[] args) {
        int gridSize = 10; // one side of the grid -> it's now a 6x6 grid
        int numbOfGrid = 10; // we will display 10 different grids

        // our StdDraw grid setup - our frame size is 600 by 600 and our x and y coordinate length is 10
        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(0, gridSize);
        StdDraw.setYscale(0, gridSize);

        // we are iterating the p till 10 to display 10(numbOfGrid) different grids.
        for (int i = 0; i < numbOfGrid; i++) {
            double p = StdRandom.uniformDouble(0.10, 0.99); // Random probability between 0.10 and 0.99
            Percolation percolation = new Percolation(gridSize, p);
            percolation.displayGrid(); // displaying our grid

            // printing the result and the probability to the console as the grid is displaying
            System.out.println("Percolation result is: " + percolation.percolationCheck() + " - the probability is: " + p);
        }
    }

}