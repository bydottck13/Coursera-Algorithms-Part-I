import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;

    private int n = 0;
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null");
        }
        if (n == items.length) {
            resize(2 * items.length);
        }
        items[n++] = item;
    }
    
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        int randN = StdRandom.uniform(n);
        Item item = items[randN];
        n--;
        // the order doesn't matter
        items[randN] = items[n];
        items[n] = null;
        if (n > 0 && n == items.length/4) {
            resize(items.length/2);
        }
        return item;
    }

    private void resize(int capacity)
    {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++)
            copy[i] = items[i];
        items = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        int randN = StdRandom.uniform(n);
        return items[randN];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int i = n;
        private final int[] order = StdRandom.permutation(n);

        public boolean hasNext() {
            return i > 0;
        }

        // throw UnsupportedOperationException
        public void remove() {
            /* not supported */
            throw new UnsupportedOperationException("not supported");
        }

        // throw NoSuchElementException if no more items in iteration
        public Item next() {
            if (i == 0) {
                throw new NoSuchElementException("No more items to return");
            }
            return items[order[--i]];
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        
        StdOut.println("enqueue");
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        StdOut.println("size is " + queue.size());
        for (Integer i : queue) {
            StdOut.println(i);
        }
        
        StdOut.println("dequeue");
        queue.dequeue();
        StdOut.println("size is " + queue.size());
        for (Integer i : queue) {
            StdOut.println(i);
        }
        
        StdOut.println("sample");
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
        StdOut.println(queue.sample());
    }
}