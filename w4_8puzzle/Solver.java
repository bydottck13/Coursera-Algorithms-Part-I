import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {
    private ArrayList<Board> solutionBoards = new ArrayList<>();
    private int moves = -1;
    private boolean solvable = false;

    private class Node {
        Node predecessor;
        Board board;
        int priority;
        int manhattan;
        int moves;
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Board is null");
        }
        MinPQ<Node> pq = new MinPQ<Node>(new BoardOrder());
        MinPQ<Node> pqTwin = new MinPQ<Node>(new BoardOrder());
        Board stepBoard = initial;
        Board twinBoard = initial.twin();
        int moveLocal = 0, moveTwinLocal = 0;
        Node parentNode = new Node();
        parentNode.board = stepBoard;
        parentNode.predecessor = null;
        parentNode.manhattan = stepBoard.manhattan();
        parentNode.priority = parentNode.manhattan;
        parentNode.moves = 0;
        // StdOut.println("initial "+initial);
        
        while ((!stepBoard.isGoal()) && (!twinBoard.isGoal())) {
            moveLocal++;
            moveTwinLocal++;
            // for normal initial board
            for (Board b: stepBoard.neighbors()) {
                if (parentNode.predecessor == null) {
                    Node node = new Node();
                    node.predecessor = parentNode;
                    node.board = b;
                    node.manhattan = b.manhattan();
                    node.priority = node.manhattan+moveLocal;
                    node.moves = moveLocal;
                    pq.insert(node);
                    // StdOut.println("manhattan "+b.manhattan()+", moves "+moveLocal);
                    // StdOut.println(b);
                } else if (!parentNode.predecessor.board.equals(b)) {
                    Node node = new Node();
                    node.predecessor = parentNode;
                    node.board = b;
                    node.manhattan = b.manhattan();
                    node.priority = node.manhattan+moveLocal;
                    node.moves = moveLocal;
                    pq.insert(node);
                }
            }
            Node nextNode = pq.delMin();
            stepBoard = nextNode.board;
            moveLocal = nextNode.moves;
            parentNode = nextNode;
            // StdOut.println("Next step "+stepBoard);

            // for twin board
            for (Board b: twinBoard.neighbors()) {
                Node node = new Node();
                node.predecessor = parentNode;
                node.board = b;
                node.manhattan = b.manhattan();
                node.priority = node.manhattan+moveTwinLocal;
                node.moves = moveTwinLocal;
                pqTwin.insert(node);
            }
            Node nextTwinNode = pqTwin.delMin();
            twinBoard = nextTwinNode.board;
            moveTwinLocal = nextTwinNode.moves;
        }
        if (stepBoard.isGoal()) {
            solvable = true;
            ArrayList<Board> solutionReversedBoards = new ArrayList<>();
            moves = parentNode.moves;
            while (parentNode != null) {
                // StdOut.println(parentNode.board);
                solutionReversedBoards.add(parentNode.board);
                // StdOut.println("moves = "+parentNode.moves);
                parentNode = parentNode.predecessor;
            }
            // StdOut.println("moves = "+moves+" vs. solutionReversedBoards size "+solutionReversedBoards.size());
            for (int i = moves; i >= 0; i--) {
                solutionBoards.add(solutionReversedBoards.get(i));
            }
        }
    }

    private class BoardOrder implements Comparator<Node> {
        
        public int compare(Node b1, Node b2) {
            int cmp = Integer.compare(b1.priority, b2.priority);
            if (cmp == 0) {
                return Integer.compare(b1.manhattan, b2.manhattan);
            }
            return cmp;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        // there is a more elegent way by counting inversions
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        return solutionBoards;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}