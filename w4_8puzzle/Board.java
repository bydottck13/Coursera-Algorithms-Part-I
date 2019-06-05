import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private int n;
    private int[][] board;
    
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("blocks are null");
        }
        board = new int[blocks.length][];
        for (int i = 0; i < blocks.length; i++)
            board[i] = blocks[i].clone();
        n = board.length;
    }       
    
    // board dimension n
    public int dimension() {
        return n;
    }
    
    // number of blocks out of place
    public int hamming() {
        int hammingDist = 0;
        for (int i = 0; i < n*n-1; i++) {
            int row = i/n;
            int col = i%n;
            if (board[row][col] != (i+1)) {
                hammingDist++;
            }
        }
        return hammingDist;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattanDist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) {
                    manhattanDist = manhattanDist+Math.abs((board[i][j]-1)/n-i)+Math.abs((board[i][j]-1)%n-j);
                }
            }
        }
        return manhattanDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        boolean same = true;
        for (int i = 0; i < n*n-1; i++) {
            int row = i/n;
            int col = i%n;
            if (board[row][col] != (i+1)) {
                same = false;
                break;
            }
        }
        if (board[n-1][n-1] != 0) {
            return false;
        }
        return same;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        /* int blank = 0;
        int length = n*n-1;
        int ex1 = StdRandom.uniform(length);
        int ex2 = StdRandom.uniform(length);
        for (int i = 0; i < length; i++) {
            int row = i/n;
            int col = i%n;
            if (board[row][col] == 0) {
                blank = i;
                break;
            }
        }
        
        while (ex1 == blank) {
            ex1 = StdRandom.uniform(length);
        }
        while ((ex1 == blank) || (ex2 == ex1)) {
            ex2 = StdRandom.uniform(length);
        }
        int rowEx1 = ex1/n;
        int colEx1 = ex1%n;
        int rowEx2 = ex2/n;
        int colEx2 = ex2%n;
        // StdOut.println("Exchange ("+rowEx1+", "+colEx1+") with ("+rowEx2+", "+colEx2+")");

        int[][] twinBlocks = new int[board.length][];
        for (int i = 0; i < board.length; i++)
            twinBlocks[i] = board[i].clone();
        int tmp = twinBlocks[rowEx1][colEx1];
        twinBlocks[rowEx1][colEx1] = twinBlocks[rowEx2][colEx2];
        twinBlocks[rowEx2][colEx2] = tmp;
        Board twinBoard = new Board(twinBlocks); */
        int[] exRows = new int[2];
        int[] exCols = new int[2];
        int count = 0;
        for (int i = 0; i < n*n; i++) {
            int row = i/n;
            int col = i%n;
            if (board[row][col] != 0) {
                exRows[count] = row;
                exCols[count] = col;
                count++;
                if (count == 2) {
                    break;
                }
            }
        }
        int[][] twinBlocks = new int[board.length][];
        for (int i = 0; i < board.length; i++)
            twinBlocks[i] = board[i].clone();
        int tmp = twinBlocks[exRows[0]][exCols[0]];
        twinBlocks[exRows[0]][exCols[0]] = twinBlocks[exRows[1]][exCols[1]];
        twinBlocks[exRows[1]][exCols[1]] = tmp;
        Board twinBoard = new Board(twinBlocks);
        return twinBoard;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        /* for (int i = 0; i < this.board.length; i++) {
            if (!Arrays.equals(this.board[i], that.board[i])) {
                return false;
            }
        } */
        if (!Arrays.deepEquals(this.board, that.board)) {
            return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborBoards = new ArrayList<>();
        int row = 0, col = 0;
        for (int i = 0; i < n*n; i++) {
            row = i/n;
            col = i%n;
            if (board[row][col] == 0) {
                break;
            }
        }
        int[] plausibleRows, plausibleCols;
        int plausibleN = 0;

        if (row == 0) {
            plausibleRows = new int[1];
            plausibleRows[0] = 1;
            plausibleN++;
        } else if (row == n-1) {
            plausibleRows = new int[1];
            plausibleRows[0] = n-2;
            plausibleN++;
        } else {
            plausibleRows = new int[2];
            plausibleRows[0] = row-1;
            plausibleRows[1] = row+1;
            plausibleN = plausibleN+2;
        }
        if (col == 0) {
            plausibleCols = new int[1];
            plausibleCols[0] = 1;
            plausibleN++;
        } else if (col == n-1) {
            plausibleCols = new int[1];
            plausibleCols[0] = n-2;
            plausibleN++;
        } else {
            plausibleCols = new int[2];
            plausibleCols[0] = col-1;
            plausibleCols[1] = col+1;
            plausibleN = plausibleN+2;
        }
        for (int i = 0; i < plausibleRows.length; i++) {
            int[][] neighborsBoard = new int[board.length][];
            for (int j = 0; j < board.length; j++)
                neighborsBoard[j] = board[j].clone();
            int tmp = neighborsBoard[plausibleRows[i]][col];
            neighborsBoard[plausibleRows[i]][col] = neighborsBoard[row][col];
            neighborsBoard[row][col] = tmp;
            Board initial = new Board(neighborsBoard);
            neighborBoards.add(initial);
        }
        for (int i = 0; i < plausibleCols.length; i++) {
            int[][] neighborsBoard = new int[board.length][];
            for (int j = 0; j < board.length; j++)
                neighborsBoard[j] = board[j].clone();
            int tmp = neighborsBoard[row][plausibleCols[i]];
            neighborsBoard[row][plausibleCols[i]] = neighborsBoard[row][col];
            neighborsBoard[row][col] = tmp;
            Board initial = new Board(neighborsBoard);
            neighborBoards.add(initial);
        }
        return neighborBoards;
    }


    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder str1 = new StringBuilder();
        str1.append(n);
        str1.append("\n");
        for (int i = 0; i < n; i++) {
            StringBuilder str2 = new StringBuilder();
            for (int j = 0; j < n; j++) {
                str2.append(String.format("%2d ", board[i][j]));
            }
            str2.append("\n");
            str1.append(str2);
        }
        return str1.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        Board initial2 = new Board(blocks);
        String str = initial.toString();
        StdOut.println(str);
        StdOut.println("hamming: "+initial.hamming());
        StdOut.println("manhattan: "+initial.manhattan());
        StdOut.println(initial.isGoal());
        StdOut.println("Equals "+initial.equals(initial2));
        StdOut.println("twin");
        StdOut.println(initial.twin());

        for (Board b: initial.neighbors()) {
            StdOut.println(b);
            StdOut.println("hamming: "+b.hamming());
            StdOut.println("manhattan: "+b.manhattan());
        }
    }
}