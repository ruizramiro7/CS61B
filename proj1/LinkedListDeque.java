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
        public LinkedListNode<T> prev;
        public LinkedListNode<T> next;

        public LinkedListNode(T item, LinkedListNode<T> prev, LinkedListNode<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private LinkedListNode<T> front;
    private int size;

    public LinkedListDeque() {
        // Front is always a sentinel
        front = new LinkedListNode<T>(null, null, null);
        front.next = front;
        front.prev = front;
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
        front.next = new LinkedListNode<T>(item, front, front.next);
        size += 1;
    }

    /**
     * Adds an item the end of the deque in constant time.
     * @param item Object of Type T to add to the deque.
     */
    public void addLast(T item) {
        front.prev.next = new LinkedListNode<T>(item, front.prev, front);
        front.prev = front.prev.next;
        size += 1;
    }

    /**
     * Returns true if this deque is empty and false otherwise.
     * @return true if deque is empty and false otherwise.
     */
    public boolean isEmpty() {
        return front.next == front;
    }

    /**
     * Returns the number of items in the list.
     * @return
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
     * @return
     */
    public T removeFirst() {
        if (front.next == front) {
            return null;
        }
        else {
            T value = front.next.item;
            front.next = front.next.next;
            front.next.prev = front;
            return value;
        }
    }

    /**
     * Removes the last item in the deque. If no such item exists, returns null.
     * @return
     */
    public T removeLast() {
        if (front.next == front) {
            return null;
        }
        else {
            T value = front.prev.item;
            front.prev = front.prev.prev;
            front.prev.next = front;
            return value;
        }
    }

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item and so on. If no such item exists, returns null.
     * Does not alter the deque.
     * @param index
     * @return
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

    private LinkedListNode<T> getRecHelper(int index, LinkedListNode<T> n) {
        // front.item = null anyways so it still returns null when no such item exits.
        if (index <= 0 || n == front) {
            return n;
        }
        return getRecHelper(index - 1, n.next);
    }

    /**
     * Gets the item at the given index, recursively.
     * @param index
     * @return
     */
    public T getRecursive(int index) {
        return getRecHelper(index, front.next).item;
    }

}
