public class ArrayDeque<T> implements Deque<T> {

    private int size;
    private static final int INIT_CAPACITY = 8;
    private static final double USAGE_LIMIT = 0.25;
    private static final int RESIZE_FACTOR = 2;
    private int capacity = INIT_CAPACITY;
    private T[] array;
    private int front;

    public ArrayDeque() {
        array = (T[]) new Object[INIT_CAPACITY];
        front = 0;
    }

    private int back() {
        return Math.floorMod(front + size + 1, capacity);
    }

    private void expand() {
        int index = first();
        int newCapacity = capacity * RESIZE_FACTOR;
        T[] newArray = (T[]) new Object[newCapacity];
        for (int s = 0; s < size; ++s) {
            newArray[s] = array[index];
            index = Math.floorMod(index + 1, capacity);
        }
        front = capacity - 1;
        capacity = newCapacity;
        array = newArray;
    }

    private void shrink() {
        int index = first();
        int newCapacity = capacity / RESIZE_FACTOR;
        T[] newArray = (T[]) new Object[newCapacity];
        for (int s = 0; s < size; ++s) {
            newArray[s] = array[index];
            index = Math.floorMod(index + 1, capacity);
        }
        front = capacity - 1;
        capacity = newCapacity;
        array = newArray;
    }

    /**
     * Adds an item the beginning of the deque in constant time.
     * @param item Object of type T to add to the deque.
     */
    public void addFirst(T item) {
        array[front] = item;
        front = Math.floorMod(front - 1, capacity);
        size += 1;
        if (size == capacity) {
            expand();
        }
    }

    /**
     * Adds an item the end of the deque in constant time.
     * @param item Object of Type T to add to the deque.
     */
    public void addLast(T item) {
        array[back()] = item;
        size += 1;
        if (size == capacity) {
            expand();
        }
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
        int index = first();
        for (int s = 0; s < size - 1; ++s) {
            System.out.print(array[index] + " ");
            index = Math.floorMod(index + 1, capacity);
        }
        System.out.println(array[index]);
    }

    private double usage() {
        return (double)size / (double)capacity;
    }

    private int first() {
        return Math.floorMod(front + 1, capacity);
    }

    private int last() {
        return Math.floorMod(back() - 1, capacity);
    }

    private boolean isBelowUsage() {
        return capacity > INIT_CAPACITY && usage() < USAGE_LIMIT;
    }

    /**
     * Removes the first item in the deque. If no such item exists, returns null.
     * @return the value contained in the item that was removed.
     */
    public T removeFirst() {
        T item = array[first()];
        array[first()] = null;
        front = Math.floorMod(front + 1, capacity);
        size -= 1;
        if (isBelowUsage()) {
            shrink();
        }
        return item;
    }

    /**
     * Removes the last item in the deque. If no such item exists, returns null.
     * @return the value contained in the item that was removed.
     */
    public T removeLast() {
        T item = array[last()];
        array[last()] = null;
        size -= 1;
        if (isBelowUsage()) {
            shrink();
        }
        return item;
    }

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item and so on. If no such item exists, returns null.
     * Does not alter the deque.
     * @param index integer representing the numerical location of item in deque.
     * @return the value of type T at the indexed location in the deque.
     */
    public T get(int index) {
        return array[Math.floorMod(first() + index, capacity)];
    }
}
