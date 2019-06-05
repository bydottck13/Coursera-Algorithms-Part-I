import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {  
    private Node first = null;
    private Node last = null;

    private int n = 0;

    private class Node
    {
        Item item;
        Node prev;
        Node next;
    }

    // construct an empty deque
    public Deque() { }

    // is the deque empty?                         
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque              
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null");
        }
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;
        first.next = oldfirst;
        if (isEmpty()) {
            last = first;
        } else {
            oldfirst.prev = first;
        }
        n++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null");
        }
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.prev = oldlast;
        last.next = null;
        // special cases for empty queue
        if (isEmpty()) {
            first = last;
        } else {
            oldlast.next = last;
        }
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        Node current = first;
        Item item = current.item;
        first = current.next;
        current.next = null;
        if (first != null) {
            first.prev = null;
        }
        n--;
        // special cases for empty queue
        if (isEmpty()) {
            last = null;
        }
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        Node current = last;
        Item item = current.item;
        last = current.prev;
        current.prev = null;
        if (last != null) {
            last.next = null;
        }
        n--;
        // special cases for empty queue
        if (isEmpty()) {
            first = null;
        }
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        // throw UnsupportedOperationException
        public void remove() {
            /* not supported */
            throw new UnsupportedOperationException("not supported");
        }

        // throw NoSuchElementException if no more items in iteration
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("No more items to return");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        int[] test = StdRandom.permutation(2048000);
        Deque<Integer> deque = new Deque<Integer>();

        for (Integer i : test) {
            deque.addFirst(i);
        }
        int j = 0;
        for (Integer i : deque) {
            StdOut.println(i);
            j++;
        }
        StdOut.println("j is " + j);

    }
}
