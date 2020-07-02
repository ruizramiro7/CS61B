/**
 *
 * @param <T>
 * @source https://cs61bl.org/su20/projects/deques/
 * @author Ramiro Ruiz
 * @author Brandon Bizzarro
 */
public class LinkedListDeque<T> {

    private static class LinkedListNode<T> {
        public T item;
        public LinkedListNode<T> next;

        public LinkedListNode(T item, LinkedListNode<T> next) {
            this.item = item;
            this.next = next;
        }
    }

    private LinkedListNode<T> front;
    private LinkedListNode<T> back;
    private int size;

    public LinkedListDeque() {
        // Front is always a sentinel
        front = new LinkedListNode<T>(null, null);
        //back = new LinkedListNode<T>(null, null);
        back = front;
        front.next = back;
        size = 0;
    }

    private LinkedListDeque<T> of(T... items) {
        LinkedListDeque<T> deque = new LinkedListDeque<T>();
        for (T item: items) {
            deque.addLast(item);
        }
        return deque;
    }

    /**
     * Adds an item the beginning of the deque in constant time.
     * @param item Object of type T to add to the deque.
     */
    public void addFirst(T item) {
        front.next = new LinkedListNode(item, front.next);
    }

    /**
     * Adds an item the end of the deque in constant time.
     * @param item Object of Type T to add to the deque.
     */
    public void addLast(T item) {
        back = new LinkedListNode(item, front);
    }

    /**
     * Returns true if this deque is empty and false otherwise.
     * @return true if deque is empty and false otherwise.
     */
    public boolean isEmpty() {
        return true;
    }

    /**
     * Returns the number of items in the list.
     * @return
     */
    public int size() {
        return 0;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    public void printDeque() {

    }

    /**
     * Removes the first item in the deque. If no such item exists, returns null.
     * @return
     */
    public T removeFirst() {
        return null;
    }

    /**
     * Removes the last item in the deque. If no such item exists, returns null.
     * @return
     */
    public T removeLast() {
        return null;
    }

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item and so on. If no such item exists, returns null.
     * Does not alter the deque.
     * @param index
     * @return
     */
    public T get(int index) {
        return null;
    }

    /**
     * Gets the item at the given index, recursively.
     * @param index
     * @return
     */
    public T getRecursive(int index) {
        return null;
    }

}
