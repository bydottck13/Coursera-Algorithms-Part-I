import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class FastCollinearPoints {
    private LineSegment[] fastLines;
    private int nLines = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points are null");
        }
        Point[] fastPoints = points.clone();
        int n = fastPoints.length;
        for (int i = 0; i < n; i++) {
            if (fastPoints[i] == null)
                throw new IllegalArgumentException("a point or some points are null");
        }
        Arrays.sort(fastPoints);
        for (int i = 0; i < n-1; i++) {
            if (fastPoints[i].compareTo(fastPoints[i+1]) == 0)
                throw new IllegalArgumentException("it contains a repeated point " + fastPoints[i]);
        }

        // slow version
        /* int test = 0;
        for (int i = 0; i < n-1; i++) {
            // preserve and start with the natural order
            Point[] slopePoints = fastPoints.clone(); 
            int slopeN = slopePoints.length;
            Arrays.sort(slopePoints, slopePoints[i].slopeOrder());

            double[] slopes = new double[slopeN-1];
            for (int j = 0; j < slopeN-1; j++) {
                slopes[j] = slopePoints[j].slopeTo(slopePoints[j+1]);
                // StdOut.println("slopes "+slopes[j]);
            }

            int count = 0;
            for (int j = 0; j < slopeN-2; j++) {
                if (slopes[j] == slopes[j+1]) {
                    count++;
                } else {
                    if (count >= 1) {
                        if (Double.compare(slopePoints[0].slopeTo(slopePoints[j+1]), slopes[j]) == 0) {
                            boolean smallPoint = true;
                            for (int k = count; k >= 0; k--) {
                                if (slopePoints[0].compareTo(slopePoints[j-k]) > 0) {
                                    smallPoint = false;
                                    break;
                                }
                            }
                            if (smallPoint) {
                                StdOut.println(slopePoints[0]+" -> "+slopePoints[j+1]);
                                test++;
                            }
                        }
                    }
                    count = 0;
                }
            }
            if (count >= 1) {
                if (Double.compare(slopePoints[0].slopeTo(slopePoints[slopeN-1]), slopes[slopeN-2]) == 0) {
                    boolean smallPoint = true;
                    for (int k = count; k >= 0; k--) {
                        if (slopePoints[0].compareTo(slopePoints[slopeN-2-k]) > 0) {
                            smallPoint = false;
                            break;
                        }
                    }
                    if (smallPoint) {
                        StdOut.println(slopePoints[0]+" -> "+slopePoints[slopeN-1]);
                        test++;
                    }
                }
            }
        }
        StdOut.println("Test lines "+test);
        StdOut.println(); */

        /* Set<HashMap<Point, Point>> newVariable = new HashSet<HashMap<Point, Point>>();
        for (int i = 0; i < n; i++) {
            Point[] slopePoints = fastPoints.clone();

            HashMap<Point, Double> map = new HashMap<>();
            for (int j = 0; j < n; j++) {
                map.put(slopePoints[j], slopePoints[i].slopeTo(slopePoints[j]));
            }
            LinkedHashMap<Point, Double> sortedMap = new LinkedHashMap<>();
            map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
            StdOut.println(sortedMap);
            List<Point> pointList = new ArrayList<Point>(sortedMap.keySet());
            List<Double> slopeList = new ArrayList<Double>(sortedMap.values());
            int count = 0;
            
            for (int j = 1; j < pointList.size()-1; j++) {
                //StdOut.println("point "+pointList.get(j)+", slope "+slopeList.get(j));
                if (Double.compare(slopeList.get(j), slopeList.get(j+1)) == 0) {
                    count++;
                } else {
                    if (count >= 2) {
                        Point[] candidatePoints = new Point[3];
                        candidatePoints[0] = pointList.get(0);
                        candidatePoints[1] = pointList.get(j-count+1);
                        candidatePoints[2] = pointList.get(j+1);
                        Arrays.sort(candidatePoints);
                        StdOut.println("*first point "+pointList.get(0));
                        StdOut.println("*middle point "+pointList.get(j-count+1));
                        StdOut.println("*last point "+pointList.get(j+1));
                        StdOut.println(pointList);
                        HashMap<Point, Point> line = new HashMap<Point, Point>();
                        line.put(candidatePoints[0], candidatePoints[2]);
                        newVariable.add(line);
                    }
                    count = 0;
                }
            }
            if (count >= 2) {
                Point[] candidatePoints = new Point[3];
                candidatePoints[0] = pointList.get(0);
                candidatePoints[1] = pointList.get(pointList.size()-count);
                candidatePoints[2] = pointList.get(pointList.size()-1);
                Arrays.sort(candidatePoints);
                StdOut.println("first point "+pointList.get(0));
                StdOut.println("middle point "+pointList.get(pointList.size()-count));
                StdOut.println("last point "+pointList.get(pointList.size()));
                HashMap<Point, Point> line = new HashMap<Point, Point>();
                line.put(candidatePoints[0], candidatePoints[2]);
                newVariable.add(line);
            }
        }
        StdOut.println(newVariable); */

        Point[] candidatePoint1s = new Point[1];
        Point[] candidatePoint2s = new Point[1];
        double[] candidateSlopes = new double[1];
        for (int i = 0; i < n-3; i++) {
            // preserve and start with the natural order
            Point[] slopePoints = Arrays.copyOfRange(fastPoints, i, n); 
            int slopeN = slopePoints.length;
            Arrays.sort(slopePoints, slopePoints[0].slopeOrder());

            /* List<Point> listPoints = Arrays.asList(slopePoints);
            Collections.sort(listPoints, listPoints.get(0).slopeOrder());
            slopePoints = listPoints.toArray(new Point[0]); */
            
            // count collinear points
            // Point p1 = slopePoints[0];
            // StdOut.println("start point is " + p1);
            int count = 0;
            int j = 0;
            while (j < slopeN-3) {
                double slope1 = slopePoints[0].slopeTo(slopePoints[j+1]);
                double slope2 = slopePoints[0].slopeTo(slopePoints[j+3]);
                if (Double.compare(slope1, slope2) == 0) {
                    // seek the last same slope
                    for (int k = j+2; k < slopeN-2; k++) {
                        double slope3 = slopePoints[0].slopeTo(slopePoints[k+2]);
                        if (Double.compare(slope1, slope3) == 0) {
                            count++;
                        } else {
                            break;
                        }
                    }
                    count = count + 2;
                    j = j + count;
                    boolean duplicateLine = true;
                    for (int k = 0; k < nLines; k++) {
                        if (Double.compare(slope1, candidateSlopes[k]) == 0) {
                            if (Double.compare(slopePoints[j+1].slopeTo(candidatePoint2s[k]), candidateSlopes[k]) == 0) {
                                duplicateLine = false;
                                break;
                            }
                        }
                    }
                    if (duplicateLine) {
                        candidatePoint1s[nLines] = slopePoints[j+1];
                        candidatePoint2s[nLines] = slopePoints[0];
                        candidateSlopes[nLines] = slope1;
                        nLines++;
                        // resize the array
                        if (nLines == candidatePoint1s.length) {
                            int size = candidatePoint1s.length*2;
                            Point[] copyPoint1 = new Point[size];
                            Point[] copyPoint2 = new Point[size];
                            double[] copySlopes = new double[size];

                            for (int k = 0; k < nLines; k++) {
                                copyPoint1[k] = candidatePoint1s[k];
                                copyPoint2[k] = candidatePoint2s[k];
                                copySlopes[k] = candidateSlopes[k];
                            }
                            candidatePoint1s = copyPoint1;
                            candidatePoint2s = copyPoint2;
                            candidateSlopes = copySlopes;
                        }
                    }
                    count = 0;
                }
                j++;
            }
            if (count >= 2) {
                boolean duplicateLine = true;
                double slope1 = slopePoints[0].slopeTo(slopePoints[slopeN-1]);
                for (int k = 0; k < nLines; k++) {
                    if (Double.compare(slope1, candidateSlopes[k]) == 0) {
                        if (Double.compare(slopePoints[slopeN-1].slopeTo(candidatePoint2s[k]), candidateSlopes[k]) == 0) {
                            duplicateLine = false;
                            break;
                        }
                    }
                }
                if (duplicateLine) {
                    candidatePoint1s[nLines] = slopePoints[slopeN-1];
                    candidatePoint2s[nLines] = slopePoints[0];
                    candidateSlopes[nLines] = slope1;
                    nLines++;
                    // resize the array
                    if (nLines == candidatePoint1s.length) {
                        int size = candidatePoint1s.length*2;
                        Point[] copyPoint1 = new Point[size];
                        Point[] copyPoint2 = new Point[size];
                        double[] copySlopes = new double[size];

                        for (int k = 0; k < nLines; k++) {
                            copyPoint1[k] = candidatePoint1s[k];
                            copyPoint2[k] = candidatePoint2s[k];
                            copySlopes[k] = candidateSlopes[k];
                        }
                        candidatePoint1s = copyPoint1;
                        candidatePoint2s = copyPoint2;
                        candidateSlopes = copySlopes;
                    }
                }
            }
        }

        // put lines into array
        fastLines = new LineSegment[nLines];
        for (int i = 0; i < nLines; i++) {
            fastLines[i] = new LineSegment(candidatePoint2s[i], candidatePoint1s[i]);
            // StdOut.println("Final line is " + fastLines[i]);
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return nLines;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lines = fastLines.clone();
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
        Stopwatch sw = new Stopwatch();
        double t1 = sw.elapsedTime();
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        double tAll = sw.elapsedTime()-t1;
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        StdOut.println("Total lines: " + collinear.numberOfSegments());
        StdOut.println("Total time: "+tAll);
    }
}