import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p is null");
        }
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p is null");
        }
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rect is null");
        }
        ArrayList<Point2D> point2D = new ArrayList<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) {
                point2D.add(p);
            }
        }
        return point2D;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p is null");
        }
        Point2D np = null;
        double distance = Double.POSITIVE_INFINITY;
        for (Point2D p1 : pointSet) {
            double distanceComp = p1.distanceSquaredTo(p);
            int cmp = Double.compare(distance, distanceComp);
            if (cmp >= 0) {
                np = p1;
                distance = distanceComp;
            }
        }
        return np;
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        Stopwatch sp0 = new Stopwatch();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        double time0 = sp0.elapsedTime();

        StdOut.println("size "+brute.size());
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-0.1, 1.1);
        StdDraw.setYscale(-0.1, 1.1);
        StdDraw.setPenColor(StdDraw.RED);
        // StdDraw.setPenRadius(0.02);
        brute.draw();
        StdDraw.show();

        Point2D p = new Point2D(0.38, 0.72);
        Stopwatch sp1 = new Stopwatch();
        Point2D pNear = brute.nearest(p);
        double time1 = sp1.elapsedTime();
        StdDraw.setPenColor(StdDraw.BLUE);
        // StdDraw.setPenRadius(0.02);
        pNear.draw();
        StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.02);
        p.draw();
        StdDraw.show();
        StdOut.println("p is "+p+", and nearest neighbor is "+pNear);

        double xmin = 0.463;
        double xmax = 0.860;
        double ymin = 0.426;
        double ymax = 0.788;
        RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
        // StdOut.println("Range search with rectangle "+rect);
        // StdDraw.setPenRadius(0.01);
        rect.draw();
        StdDraw.show();

        Stopwatch sp2 = new Stopwatch();
        Iterable<Point2D> point2D = brute.range(rect);
        double time2 = sp2.elapsedTime();
        int i = 0;
        for (Point2D pIn : point2D) {
            pIn.draw();
            i++;
        }
        StdOut.println("All "+i+" points are inside "+rect);
        StdOut.println("Elapsed Time of insertion "+time0+"s");
        StdOut.println("Elapsed Time of nearest neighbor "+time1+"s");
        StdOut.println("Elapsed Time of range search "+time2+"s");
        StdDraw.show();
    }
}