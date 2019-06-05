import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;

public class KdTree {
    private int size;
    private Node root;
    private final RectHV rootRect = new RectHV(0.0, 0.0, 1.0, 1.0);

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    private class PointNearest {
        private Point2D p;

        public PointNearest(Point2D p) {
            this.p = p;
        }
    }

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p is null");
        }
        root = insert(root, p, true, rootRect.xmin(), rootRect.ymin(), rootRect.xmax(), rootRect.ymax());
    }

    private Node insert(Node x, Point2D p, boolean vertical, double xmin, double ymin, double xmax, double ymax) {
        if (x == null) {
            size++;
            // StdOut.println("Insert "+p+" with "+rectParent);
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }
        int cmp;
        if (vertical) {
            cmp = Double.compare(p.x(), x.p.x());
        } else {
            cmp = Double.compare(p.y(), x.p.y());
        }
        if (cmp < 0) {
            if (vertical) {
                x.lb = insert(x.lb, p, !vertical, xmin, ymin, x.p.x(), ymax);
            } else {
                x.lb = insert(x.lb, p, !vertical, xmin, ymin, xmax, x.p.y());
            }
        } else if (cmp > 0) {
            if (vertical) {
                x.rt = insert(x.rt, p, !vertical, x.p.x(), ymin, xmax, ymax);
            } else {
                x.rt = insert(x.rt, p, !vertical, xmin, x.p.y(), xmax, ymax);
            }
        } else {
            if (vertical) {
                if (Double.compare(x.p.y(), p.y()) == 0) {
                    x.p = p;
                } else {
                    x.rt = insert(x.rt, p, !vertical, x.p.x(), ymin, xmax, ymax);
                }
            } else {
                if (Double.compare(x.p.x(), p.x()) == 0) {
                    x.p = p;
                } else {
                    x.rt = insert(x.rt, p, !vertical, xmin, x.p.y(), xmax, ymax);
                }
            }
        }
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p is null");
        }
        return get(p) != null;
    }

    private Point2D get(Point2D p) {
        return get(root, p, true);
    }

    private Point2D get(Node x, Point2D p, boolean vertical) {
        if (p == null) {
            throw new IllegalArgumentException("p is null");
        }
        if (x == null) return null;
        int cmp;
        if (vertical) {
            cmp = Double.compare(p.x(), x.p.x());
        } else {
            cmp = Double.compare(p.y(), x.p.y());
        }
        if (cmp < 0) {
            return get(x.lb, p, !vertical);
        } else if (cmp > 0) {
            return get(x.rt, p, !vertical);
        } else {
            if (vertical) {
                if (Double.compare(x.p.y(), p.y()) == 0) {
                    return x.p;
                } else {
                    return get(x.rt, p, !vertical);
                }
            } else {
                if (Double.compare(x.p.x(), p.x()) == 0) {
                    return x.p;
                } else {
                    return get(x.rt, p, !vertical);
                }
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x != null) {
            draw(x.lb);
            // StdDraw.setPenColor(StdDraw.BLACK);
            // StdDraw.setPenRadius(0.02);
            x.p.draw();
            // StdDraw.setPenColor(StdDraw.RED);
            // StdDraw.setPenRadius(0.001);
            // x.rect.draw();
            draw(x.rt);
        }
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rect is null");
        }
        ArrayList<Point2D> point2D = new ArrayList<>();
        seekPoints(root, point2D, rect);
        return point2D;
    }

    private void seekPoints(Node x, ArrayList<Point2D> point2D, RectHV rectTarget) {
        if (x != null) {
            // StdOut.println("Seek point "+x.p);
            if (rectTarget.contains(x.p)) {
                // StdOut.println("Add "+x.p+" into array");
                point2D.add(x.p);
            }
            if (x.lb != null) {
                if (x.lb.rect.intersects(rectTarget)) {
                    // StdOut.println("Search lb "+x.lb.rect);
                    seekPoints(x.lb, point2D, rectTarget);
                }
            }
            if (x.rt != null) {
                if (x.rt.rect.intersects(rectTarget)) {
                    // StdOut.println("Search rt "+x.rt.rect);
                    seekPoints(x.rt, point2D, rectTarget);
                }
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p is null");
        }
        
        double distance = Double.POSITIVE_INFINITY;
        PointNearest npClass = new PointNearest(null);
        seekNearest(root, distance, p, npClass);
        // StdOut.println("npClass.p is "+npClass.p);
        // StdOut.println("npClass.distance is "+npClass.distance);
        Point2D np = npClass.p;
        // StdOut.println("np is "+np);
        return np;
    }

    private double seekNearest(Node x, double distCurrent, Point2D p, PointNearest npClass) {
        if (x != null) {
            // StdOut.println("seekNearest: go x.p = "+x.p);
            double distance = x.p.distanceSquaredTo(p);
            double distanceNear = distCurrent;
            int cmp = Double.compare(distanceNear, distance);
            // StdOut.println("["+x.p+"] distanceNear "+distanceNear+", distance "+distance);
            if (cmp >= 0) {
                // StdOut.println("Update: "+x.p+" with distance "+distance);
                distanceNear = distance;
                npClass.p = x.p;
            }
            if ((x.lb != null) && (x.rt != null)) {
                double distLB = x.lb.rect.distanceSquaredTo(p);
                double distRT = x.rt.rect.distanceSquaredTo(p);
                int cmpLBRT = Double.compare(distLB, distRT);
                // decide which one goes first
                if (cmpLBRT < 0) {
                    // StdOut.println("Go LB");
                    distanceNear = seekNearest(x.lb, distanceNear, p, npClass);
                    // still need to check the other substee
                    int cmpRT = Double.compare(distanceNear, distRT);
                    if (cmpRT >= 0) {
                        distanceNear = seekNearest(x.rt, distanceNear, p, npClass);
                    }
                } else {
                    // StdOut.println("Go RT");
                    distanceNear = seekNearest(x.rt, distanceNear, p, npClass);
                    // still need to check the other substee
                    int cmpLB = Double.compare(distanceNear, distLB);
                    if (cmpLB >= 0) {
                        distanceNear = seekNearest(x.lb, distanceNear, p, npClass);
                    }
                }
            } else {
                if (x.lb != null) {
                    // StdOut.println("Seek lb: p="+p+", dist="+x.lb.rect.distanceSquaredTo(p)+" with "+x.lb.rect+" vs. "+distanceNear);
                    int cmpLB = Double.compare(distanceNear, x.lb.rect.distanceSquaredTo(p));
                    if (cmpLB >= 0) {
                        distanceNear = seekNearest(x.lb, distanceNear, p, npClass);
                    }
                }
                if (x.rt != null) {
                    // StdOut.println("Seek rt: p="+p+", dist="+x.rt.rect.distanceSquaredTo(p)+" with "+x.rt.rect+" vs. "+distanceNear);
                    int cmpRT = Double.compare(distanceNear, x.rt.rect.distanceSquaredTo(p));
                    if (cmpRT >= 0) {
                        distanceNear = seekNearest(x.rt, distanceNear, p, npClass);
                    }
                }
            }
            
            return distanceNear;
        }
        return distCurrent;
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        Stopwatch sp0 = new Stopwatch();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        double time0 = sp0.elapsedTime();

        StdOut.println("size "+kdtree.size());
        StdOut.println("contain (0.372, 0.497) "+kdtree.contains(new Point2D(0.372, 0.497)));
        StdOut.println("contain (0.372, 0.496) "+kdtree.contains(new Point2D(0.372, 0.496)));
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-0.1, 1.1);
        StdDraw.setYscale(-0.1, 1.1);
        StdDraw.setPenColor(StdDraw.RED);
        // StdDraw.setPenRadius(0.001);
        kdtree.draw();
        StdDraw.show();

        Point2D p = new Point2D(0.38, 0.72);
        Stopwatch sp1 = new Stopwatch();
        Point2D pNear = kdtree.nearest(p);
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
        StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.01);
        rect.draw();
        StdDraw.show();

        // StdDraw.setPenRadius(0.02);
        Stopwatch sp2 = new Stopwatch();
        Iterable<Point2D> point2D = kdtree.range(rect);
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