package bearmaps.utils.pq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

/* A MinHeap class of Comparable elements backed by an ArrayList. */
public class MinHeap<E extends Comparable<E>> {

    public static void main(String... args) {
        MinHeap<String> m = new MinHeap();
        m.insert("test");
    }

    private class ArrayMap {
        private HashMap<Integer, E> items;
        private HashMap<E, Integer> itemSet;
        //private HashSet<E> itemSet;
        public ArrayMap() {
            items = new HashMap<>();
            itemSet = new HashMap<>();
        }
        public void add(E item) {
            items.put(items.size(), item);
            itemSet.put(item, items.size());
        }
        public int size() {
            return items.size();
        }
        public E get(int index) {
            return items.get(index);
        }
        public int getIndex(E item) {
            return itemSet.get(item);
        }
        public void set(int index, E item) {
            if (!items.get(index).equals(item)) {
                itemSet.remove(items.get(index));
                itemSet.put(item, index);
            }
            items.put(index, item);
        }
        public void remove(int index) {
            itemSet.remove(items.get(index));
            items.remove(index);
        }
        public boolean contains(E item) {
            return itemSet.containsKey(item);
        }
    }

    /* An ArrayList that stores the elements in this MinHeap. */
    //private ArrayList<E> contents;
    private ArrayMap contents;
    private int size;
    // TODO: YOUR CODE HERE (no code should be needed here if not
    // implementing the more optimized version)

    /* Initializes an empty MinHeap. */
    public MinHeap() {
        contents = new ArrayMap();
        contents.add(null);
    }

    private int getIndex(E element) {
        return contents.getIndex(element);
    }

    /* Returns the element at index INDEX, and null if it is out of bounds. */
    private E getElement(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /* Sets the element at index INDEX to ELEMENT. If the ArrayList is not big
       enough, add elements until it is the right size. */
    private void setElement(int index, E element) {
        //while (index >= contents.size()) {
        //    contents.add(null);
        //}
        contents.set(index, element);
    }

    /* Swaps the elements at the two indices. */
    private void swap(int index1, int index2) {
        E element1 = getElement(index1);
        E element2 = getElement(index2);
        setElement(index2, element1);
        setElement(index1, element2);
    }

    /* Prints out the underlying heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /* Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getElement(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getElement(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getElement(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getElement(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /* Returns the index of the left child of the element at index INDEX. */
    private int getLeftOf(int index) {
        return 2 * index;
    }

    /* Returns the index of the right child of the element at index INDEX. */
    private int getRightOf(int index) {
        return 2 * index + 1;
    }

    /* Returns the index of the parent of the element at index INDEX. */
    private int getParentOf(int index) {
        return index / 2;
    }

    /* Returns the index of the smaller element. At least one index has a
       non-null element. If the elements are equal, return either index. */
    private int min(int index1, int index2) {
        E element1 = getElement(index1);
        E element2 = getElement(index2);
        if (element1 == null || element2 == null) {
            return element1 == null ? index2 : index1;
        }
        else if (element1.compareTo(element2) <= 0) {
            return index1;
        }
        return index2;
    }

    /* Returns but does not remove the smallest element in the MinHeap. */
    public E findMin() {
        return getElement(1);
    }

    /* Bubbles up the element currently at index INDEX. */
    private void bubbleUp(int index) {
        int parentIndex = getParentOf(index);
        if (parentIndex == 0 || min(index, parentIndex) == parentIndex) {
            return;
        }
        swap(index, parentIndex);
        bubbleUp(parentIndex);
    }

    /* Bubbles down the element currently at index INDEX. */
    private void bubbleDown(int index) {
        if (getElement(getLeftOf(index)) == null) {
            return;
        }
        int minChild = min(getLeftOf(index), getRightOf(index));
        if (min(minChild, index) == index) {
            return;
        }
        swap(index, minChild);
        bubbleDown(minChild);
    }

    /* Returns the number of elements in the MinHeap. */
    public int size() {
        return contents.size() - 1;
    }

    /* Inserts ELEMENT into the MinHeap. If ELEMENT is already in the MinHeap,
       throw an IllegalArgumentException.*/
    public void insert(E element) {
        if (contains(element)) {
            throw new IllegalArgumentException();
        }
        contents.add(element);
        bubbleUp(size());
    }

    /* Returns and removes the smallest element in the MinHeap. */
    public E removeMin() {
        E smallest = getElement(1);
        swap(1, size());
        contents.remove(size());
        bubbleDown(1);
        return smallest;
    }

    /* Replaces and updates the position of ELEMENT inside the MinHeap, which
       may have been mutated since the initial insert. If a copy of ELEMENT does
       not exist in the MinHeap, throw a NoSuchElementException. Item equality
       should be checked using .equals(), not ==. */
    public void update(E element) {
        if (!contains(element)) {
            throw new NoSuchElementException();
        }
        int index = getIndex(element);
        setElement(index, element);
        bubbleUp(index);
        bubbleDown(index);
    }

    /* Returns true if ELEMENT is contained in the MinHeap. Item equality should
       be checked using .equals(), not ==. */
    public boolean contains(E element) {
        return contents.contains(element);
    }
}

