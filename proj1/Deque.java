public interface Deque<T> {
    /**
     * Adds an item the beginning of the deque in constant time.
     * @param item Object of type T to add to the deque.
     */
    void addFirst(T item);

    /**
     * Adds an item the end of the deque in constant time.
     * @param item Object of Type T to add to the deque.
     */
    void addLast(T item);

    /**
     * Returns true if this deque is empty and false otherwise.
     * @return true if deque is empty and false otherwise.
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of items in the deque.
     * @return an integer representing the number of items in the deque.
     */
    int size();

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    void printDeque();

    /**
     * Removes the first item in the deque. If no such item exists, returns null.
     * @return the value contained in the item that was removed.
     */
    T removeFirst();

    /**
     * Removes the last item in the deque. If no such item exists, returns null.
     * @return the value contained in the item that was removed.
     */
    T removeLast();

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item and so on. If no such item exists, returns null.
     * Does not alter the deque.
     * @param index integer representing the numerical location of item in deque.
     * @return the value of type T at the indexed location in the deque.
     */
    T get(int index);
}
