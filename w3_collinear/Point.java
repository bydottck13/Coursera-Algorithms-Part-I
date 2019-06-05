/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (that == null) {
            throw new NullPointerException("Point is null");
        }
        if ((that.y == this.y) && (that.x == this.x)) {
            return Double.NEGATIVE_INFINITY;
        } else if ((that.y != this.y) && (that.x == this.x)) {
            return Double.POSITIVE_INFINITY;
        } else if ((that.y == this.y) && (that.x != this.x)) {
            return 0.0;
        } else {
            double slope = (that.y-this.y) / (double) (that.x-this.x);
            return slope;
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (that == null) {
            throw new NullPointerException("Point is null");
        }
        if (this.y < that.y) {
            return -1;
        } else if ((this.y == that.y) && (this.x < that.x)) {
            return -1;
        } else if ((this.y == that.y) && (this.x == that.x)) {
            return 0;
        }
        return 1;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new SlopeOrder(this);
    }

    private class SlopeOrder implements Comparator<Point> {
        private final Point p;

        public SlopeOrder(Point p) {
            this.p = p;
        }

        public int compare(Point p1, Point p2) {
            double s1 = this.p.slopeTo(p1);
            double s2 = this.p.slopeTo(p2);
            return Double.compare(s1, s2);
        }
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        LineSegment[] lines = new LineSegment[10];
        int nLines = 0;
        Arrays.sort(points);
        for (int i = 0; i < points.length; i++) {
            Point[] slopePoints = points;
            Arrays.sort(slopePoints, slopePoints[i].slopeOrder());

            // compute the slope
            double[] slopes = new double[n-1];
            for (int j = 0; j < n-1; j++) {
                slopes[j] = slopePoints[0].slopeTo(slopePoints[j+1]);
                // StdOut.println("p1 = " + slopePoints[0] + ", p2 = " + slopePoints[j+1] + ", slope is " + slopes[j]);
            }

            // count collinear points
            Point p1 = slopePoints[0];
            // StdOut.println("start point is " + p1);
            int count = 0;
            for (int j = 0; j < slopes.length-1; j++) {
                if (Double.compare(slopes[j], slopes[j+1]) == 0) {
                    count++;
                } else {
                    if (count >= 2) {
                        // StdOut.println("last point is " + slopePoints[j]);
                        Point[] collinearPoints = new Point[count+2];
                        for (int k = 0; k <= count; k++) {
                            // StdOut.println("all point is " + slopePoints[j-k]);
                            collinearPoints[k] = slopePoints[j-k+1];
                        }
                        collinearPoints[count+1] = p1;
                        Arrays.sort(collinearPoints);
                        // StdOut.println("line is from " + collinearPoints[0] + " to " + collinearPoints[count+1]);
                        LineSegment curLine = new LineSegment(collinearPoints[0], collinearPoints[count+1]);
                        boolean duplicateLine = true;
                        for (int k = 0; k < nLines; k++) {
                            if (lines[k].toString().equals(curLine.toString())) {
                                duplicateLine = false;
                                // StdOut.println("Duplicate line");
                            }
                        }
                        if (duplicateLine) {
                            lines[nLines++] = curLine;
                        }
                    }
                    count = 0;
                }
            }
            if (count >= 2) {
                // StdOut.println("last point is " + slopePoints[n-2]);
                Point[] collinearPoints = new Point[count+2];
                for (int k = 0; k <= count; k++) {
                    // StdOut.println("all point is " + slopePoints[j-k]);
                    collinearPoints[k] = slopePoints[n-1-k];
                }
                collinearPoints[count+1] = p1;
                Arrays.sort(collinearPoints);
                // StdOut.println("line is from " + collinearPoints[0] + " to " + collinearPoints[count+1]);
                LineSegment curLine = new LineSegment(collinearPoints[0], collinearPoints[count+1]);
                boolean duplicateLine = true;
                for (int k = 0; k < nLines; k++) {
                    if (lines[k].toString().equals(curLine.toString())) {
                        duplicateLine = false;
                        // StdOut.println("Duplicate line");
                    }
                }
                if (duplicateLine) {
                    lines[nLines++] = curLine;
                }
            }
        }
        for (int i = 0; i < nLines; i++) {
            StdOut.println("Final line is " + lines[i]);
        }

    }
}
