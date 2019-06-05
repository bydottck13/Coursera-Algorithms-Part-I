import edu.princeton.cs.algs4.WeightedQuickUnionUF;
// import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    private int nGrid;
    private int virTopSite;
    private int virBtmSite;

    private boolean[][] sites;
    private WeightedQuickUnionUF ufPerc, ufFull;
    private int count;

    // create n-by-n grid, with all sites blocked, 1/2 for open/full, 0 for blocked
    public Percolation(int n) {
        // StdOut.println("Create a grid with "+n+"x"+n);
        if (n <= 0) {
            throw new IllegalArgumentException("n " + n + " is not greater than 0");
        }
        count = 0;
        nGrid = n;
        sites = new boolean[n][n];
        virTopSite = n*n;
        virBtmSite = virTopSite+1;
        ufPerc = new WeightedQuickUnionUF(virBtmSite+1);
        ufFull = new WeightedQuickUnionUF(virBtmSite);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row-1, col-1);
        if (!sites[row-1][col-1]) {
            sites[row-1][col-1] = true;
            int p = nGrid*(row-1) + col-1;
            int q = 0;
            count++;
            // StdOut.println("open: p is "+p);
            if (row == 1) {
                ufPerc.union(p, virTopSite);
                ufFull.union(p, virTopSite);
            }
            if (row == nGrid) {
                ufPerc.union(p, virBtmSite);
            }

            if (row > 1) {
                if (sites[row-2][col-1]) {
                    q = nGrid*(row-2) + col-1;
                    ufPerc.union(p, q);
                    ufFull.union(p, q);
                }
            }
            if (row < nGrid) {
                if (sites[row][col-1]) {
                    q = nGrid*row + col-1;
                    ufPerc.union(p, q);
                    ufFull.union(p, q);
                }
            }
            if (col > 1) {
                if (sites[row-1][col-2]) {
                    q = nGrid*(row-1) + col-2;
                    ufPerc.union(p, q);
                    ufFull.union(p, q);
                }
            }
            if (col < nGrid) {
                if (sites[row-1][col]) {
                    q = nGrid*(row-1) + col;
                    ufPerc.union(p, q);
                    ufFull.union(p, q);
                }
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row-1, col-1);
        if (sites[row-1][col-1]) {
            return true;
        }
        return false;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row-1, col-1);
        int p = nGrid*(row-1) + col-1;
        return ufFull.connected(p, virTopSite);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufPerc.connected(virBtmSite, virTopSite);
    }

    // validate that n is outside its prescribed range
    private void validate(int row, int col) {
        if ((row < 0 || row >= nGrid) || (col < 0 || col >= nGrid)) {
            throw new IllegalArgumentException("index " + row + "/" + col + " is not between 0 and " + (nGrid));
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        // Percolation perc = new Percolation(20);
        // StdOut.println("site[][] "+sites[0][0]);
    }
}
