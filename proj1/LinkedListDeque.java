/**
 * Represents a doubly-linked list implementation of a deque.
 * Designed to ensure constant time item insertion at the beginning
 * and end of the deque.
 * @param <T> Object type
 * @source https://cs61bl.org/su20/projects/deques/
 * @author Ramiro Ruiz
 * @author Brandon Bizzarro
 */
public class LinkedListDeque<T> implements Deque<T> {

    /**
     * Represents the nodes of the linked-list.
     * Contains an item of type T and references
     * to the previous and next node.
     * @param <T> Object Type of items in list.
     */
    private static class LinkedListNode<T> {
        public T item;
        public LinkedListNode<T> prev;
        public LinkedListNode<T> next;

        /**
         * Represents a node in the linked-list that contains the items accessible
         * through the API.
         * @param item The object of type T contained in the node
         * @param prev A reference to the previous node in the linked-list.
         * @param next A reference to the next node in the linked-list.
         */
        public LinkedListNode(T item, LinkedListNode<T> prev, LinkedListNode<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private final LinkedListNode<T> front;
    private int size;

    public LinkedListDeque() {
        // Front represents the sentinel which contains a null throwaway value.
        front = new LinkedListNode<>(null, null, null);
        // Set the front node to point forward and backward to itself.
        front.next = front;
        front.prev = front;
        size = 0;
    }

    /**
     * Adds an item the beginning of the deque in constant time.
     * @param item Object of type T to add to the deque.
     */
    public void addFirst(T item) {
        // Set first node to point backward to new first node containing ITEM.
        front.next.prev = new LinkedListNode<>(item, front, front.next);
        // Set front to point forward to new first node.
        front.next = front.next.prev;
        size += 1;
    }

    /**
     * Adds an item the end of the deque in constant time.
     * @param item Object of Type T to add to the deque.
     */
    public void addLast(T item) {
        // Set the last node to point forward to a new node containing ITEM.
        front.prev.next = new LinkedListNode<>(item, front.prev, front);
        // Set the front node to point backward to the new last node.
        front.prev = front.prev.next;
        size += 1;
    }

    /**
     * Returns the number of items in the deque.
     * @return Returns an integer representing the number of items in the deque.
     */
    public int size() {
        return this.size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    public void printDeque() {
        LinkedListNode<T> n = front.next;
        while (n.next != front) {
            System.out.print(n.item + " ");
            n = n.next;
        }
        System.out.println(n.item);
    }

    /**
     * Removes the first item in the deque. If no such item exists, returns null.
     * @return Returns the value contained in the item that was removed.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        else {
            // Save value contained in item to remove in a temporary variable.
            T value = front.next.item;
            // Set front to point forward to the second node in the list.
            front.next = front.next.next;
            // Set the new first node to point backward to front.
            front.next.prev = front;
            size -= 1;
            return value;
        }
    }

    /**
     * Removes the last item in the deque. If no such item exists, returns null.
     * @return Returns the value contained in the item that was removed.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        else {
            // Save value contained in item to remove in a temporary variable.
            T value = front.prev.item;
            // Set the front node to point backward to the second to last node.
            front.prev = front.prev.prev;
            // Set the new last node to point forward to front.
            front.prev.next = front;
            size -= 1;
            return value;
        }
    }

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item and so on. If no such item exists, returns null.
     * Does not alter the deque.
     * @param index Integer representing the numerical location of item in deque.
     * @return Returns the value of type T at the indexed location in the deque.
     */
    public T get(int index) {
        LinkedListNode<T> n = front.next;
        for (int i = 0; i != index; ++i) {
            if (n == front) {
                return null;
            }
            n = n.next;
        }
        return n.item;
    }

    /**
     * Implements a helper function to recursively obtain the item contained in
     * the node specified at the numerical address INDEX.
     * @param index An integer representing the numerical address of
     *              the node to get.
     * @param n The next node in the recursive descent.
     * @return Returns the item containing the node specified by the initial INDEX
     *         passed in using getRecursive().
     */
    private LinkedListNode<T> getRecHelper(int index, LinkedListNode<T> n) {
        // front.item = null anyways so it still returns null when no such item exits.
        if (index <= 0 || n == front) {
            return n;
        }
        return getRecHelper(index - 1, n.next);
    }

    /**
     * Gets the item at the given index, recursively.
     * @param index The numerical position of the item being accessed in the deque.
     * @return the value of type T contained at the numerical position INDEX.
     */
    public T getRecursive(int index) {
        return getRecHelper(index, front.next).item;
    }
}
