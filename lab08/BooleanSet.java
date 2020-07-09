/**
 * Represent a set of nonnegative ints from 0 to maxElement for some initially
 * specified maxElement.
 */
public class BooleanSet implements SimpleSet {

    private boolean[] contains;
    private int size;

    /** Initializes a set of ints from 0 to maxElement. */
    public BooleanSet(int maxElement) {
        contains = new boolean[maxElement + 1];
        size = 0;
    }

    /** Adds k to the set. */
    public void add(int k) {
        // TODO
        contains[k] = true;
        size += 1;
    }

    /** Removes k from the set. */
    public void remove(int k) {
        // TODO
        contains[k] = false;
        size -= 1;
    }

    /** Return true if k is in this set, false otherwise. */
    public boolean contains(int k) {
        return contains[k];
    }

    /** Return true if this set is empty, false otherwise. */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /** Returns the number of items in the set. */
    public int size() {
        //TODO
        return size;
    }

    /** Returns an array containing all of the elements in this collection. */
    public int[] toIntArray() {
        // TODO
        int[] intArray = new int[size];
        for (int i = 0, j = 0; i < contains.length; ++i) {
            if (contains(i)) {
                intArray[j] = i;
                j += 1;
            }
        }
        return intArray;
    }
}