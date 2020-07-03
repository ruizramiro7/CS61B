public class ArrayDeque<T> implements Deque<T> {

    private int size;

    /**
     * Adds an item the beginning of the deque in constant time.
     * @param item Object of type T to add to the deque.
     */
    public void addFirst(T item) {
    }

    /**
     * Adds an item the end of the deque in constant time.
     * @param item Object of Type T to add to the deque.
     */
    public void addLast(T item) {
    }

    /**
     * Returns the number of items in the deque.
     * @return an integer representing the number of items in the deque.
     */
    public int size() {
        return this.size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    public void printDeque() {
    }

    /**
     * Removes the first item in the deque. If no such item exists, returns null.
     * @return the value contained in the item that was removed.
     */
    public T removeFirst() {
        return null;
    }

    /**
     * Removes the last item in the deque. If no such item exists, returns null.
     * @return the value contained in the item that was removed.
     */
    public T removeLast() {
        return null;
    }

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item and so on. If no such item exists, returns null.
     * Does not alter the deque.
     * @param index integer representing the numerical location of item in deque.
     * @return the value of type T at the indexed location in the deque.
     */
    public T get(int index) {
        return null;
    }
}
