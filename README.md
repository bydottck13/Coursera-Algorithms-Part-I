# Coursera: Algorithms, Part I
* [Coursera Website](https://www.coursera.org/learn/algorithms-part1)

# Assignments
* [Programming Assignment 1: Percolation](https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php)
* [Programming Assignment 2: Deques and Randomized Queues](https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php)
* [Programming Assignment 3: Pattern Recognition](https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php)
* [Programming Assignment 4: 8 Puzzle](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php)
* [Programming Assignment 5: Kd-Trees](https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php)

# Compile
```
$ javac-algs4 -Werror PercolationVisualizer.java
```

# Instructions
* MP1:
```
$ zip percolation-submit.zip Percolation.java PercolationStats.java
$ java-algs4 PercolationVisualizer input1-no.txt
$ java-algs4 PercolationStats 200 100
```

* MP2:
```
$ zip queues-submit.zip Deque.java RandomizedQueue.java Permutation.java
$ java-algs4 Permutation 3 < distinct.txt
```

* MP3:
```
$ zip collinear-submit.zip Point.java BruteCollinearPoints.java FastCollinearPoints.java
$ java-algs4 Point input6.txt
$ java-algs4 BruteCollinearPoints input8.txt
$ java-algs4 FastCollinearPoints input100.txt
```

* MP4:
For this MP, it is highly recommanded to read [FAQ](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/faq.php).
```
$ zip 8puzzle-submit.zip Board.java Solver.java
$ java-algs4 Solver puzzle04.txt
$ java-algs4 -Xmx1600m Solver puzzle30.txt
```

* MP5:
KdTree can refer to [BST](https://algs4.cs.princeton.edu/32bst/BST.java.html) and [FAQ](https://coursera.cs.princeton.edu/algs4/assignments/kdtree/faq.php).
```
$ zip kdtree-submit.zip PointSET.java KdTree.java
$ java-algs4 -Xmx1600m PointSET input100K.txt
$ java-algs4 -Xmx1600m RangeSearchVisualizer input1M.txt
```

## Reference
* [8-puzzle](https://github.com/himanData/8-puzzle)
* [How to check if an instance of 8 puzzle is solvable?](https://www.geeksforgeeks.org/check-instance-8-puzzle-solvable/)