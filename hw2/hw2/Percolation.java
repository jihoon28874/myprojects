package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF waterSite;
    private WeightedQuickUnionUF underSite;
    private int N;
    private boolean[][] arrayOpen;
    private int numOpen;
    private int percWorld;
    private int waterWorld;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        this.N = N;
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        arrayOpen = new boolean[N][N];
        for (int i = 0; i < arrayOpen.length; i++) {
            for (int j = 0; j < arrayOpen.length; j++) {
                arrayOpen[i][j] = false;
            }
        }
        waterWorld = N * N;
        percWorld = N * N + 1;
        waterSite = new WeightedQuickUnionUF(N * N + 2);
        underSite = new WeightedQuickUnionUF(N * N + 1);
        for (int i = 0; i < N; i++) {
            waterSite.union(waterWorld, xyTo1D(0, i));
        }
        for (int i = 0; i < N; i++) {
            waterSite.union(percWorld, xyTo1D(N - 1, i));
        }
        for (int i = 0; i < N; i++) {
            underSite.union(waterWorld, xyTo1D(0, i));
        }
        numOpen = 0;
    }

    private int xyTo1D(int r, int c) {
        int gridNum;
        gridNum = r * N + c;
        return gridNum;
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException();
        }
        if (isOpen(row, col)) {
            return;
        }
        arrayOpen[row][col] = true;
        numOpen = numOpen + 1;
        if (arrayOpen[row][col]) {
            openHelper(row, col, row, col - 1);
            openHelper(row, col, row - 1, col);
            openHelper(row, col, row + 1, col);
            openHelper(row, col, row, col + 1);
        }
    }

    private void openHelper(int row, int col, int newRow, int newCol) {
        if (newRow < 0 || newRow >= N || newCol < 0 || newCol >= N) {
            return;
        }
        if (arrayOpen[newRow][newCol]) {
            waterSite.union(xyTo1D(row, col), xyTo1D(newRow, newCol));
            underSite.union(xyTo1D(row, col), xyTo1D(newRow, newCol));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException();
        }
        return arrayOpen[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            return false;
        } else {
            return underSite.connected(xyTo1D(row, col), waterWorld);
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        if (numberOfOpenSites() == 0) {
            return false;
        }
        return waterSite.connected(waterWorld, percWorld);
    }
}
