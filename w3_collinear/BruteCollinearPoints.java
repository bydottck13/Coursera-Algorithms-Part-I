import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] bruteLines;
    private int nLines = 0;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points are null");
        }
        Point[] brutePoints = points.clone();
        int n = brutePoints.length;
        for (int i = 0; i < n; i++) {
            if (brutePoints[i] == null)
                throw new IllegalArgumentException("a point or some points are null");
        }
        Arrays.sort(brutePoints);
        for (int i = 0; i < n-1; i++) {
            if (brutePoints[i].compareTo(brutePoints[i+1]) == 0)
                throw new IllegalArgumentException("it contains a repeated point " + brutePoints[i]);
        }
        
        LineSegment[] candidateLines = new LineSegment[1];
        for (int i = 0; i < n-3; i++) {
            for (int j = i+1; j < n-2; j++) {
                for (int k = j+1; k < n-1; k++) {
                    for (int m = k+1; m < n; m++) {
                        double s1 = brutePoints[i].slopeTo(brutePoints[j]);
                        double s2 = brutePoints[i].slopeTo(brutePoints[k]);
                        double s3 = brutePoints[i].slopeTo(brutePoints[m]);
                        if ((Double.compare(s1, s2) == 0) && (Double.compare(s1, s3) == 0)) {                            
                            candidateLines[nLines] = new LineSegment(brutePoints[i], brutePoints[m]);
                            nLines++;
                            if (nLines == candidateLines.length) {
                                int size = candidateLines.length*2;
                                LineSegment[] copyLines = new LineSegment[size];
                                for (int x = 0; x < nLines; x++) {
                                    copyLines[x] = candidateLines[x];
                                }
                                candidateLines = copyLines;
                            }
                        }
                    }
                }
            }
        }

        // put lines into array
        bruteLines = new LineSegment[nLines];
        for (int i = 0; i < nLines; i++) {
            bruteLines[i] = candidateLines[i];
        }

    } 

    // the number of line segments
    public int numberOfSegments() {
        return nLines;
    }
    
    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lines = bruteLines.clone();
        return lines;
    }

    // unit testing (optional)
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        StdOut.println("Total lines: " + collinear.numberOfSegments());
    }
}