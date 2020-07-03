/**
 * Implements a deque structure with constant-time insertion at front and back
 * using the generic java array as the core data structure.
 * @param <T> Object Type
 * @author Ramiro Ruiz
 * @author Brandon Bizzarro
 * @source https://cs61bl.org/su20/projects/deques/
 */
public class ArrayDeque<T> implements Deque<T>{
    private static final int INIT_CAPACITY = 8;
    private static final int RESIZE_FACTOR = 2;
    private static final double USAGE_LIMIT = (double)1 / (double)4;
    private T[] items;
    private int size;
    private int first;
    private int last;

    public ArrayDeque() {
        items = (T[]) new Object[INIT_CAPACITY];
        first = 0;
        last = 0;
    }

    /**
     * Adds item to front of array.
     * @param item Object of type T to add to the deque.
     */
    public void addFirst(T item) {
        if (size == 0) {
            addEmpty(item);
            size++;
        } else {
            first = minusOne(first);
            if (items.length == size) {
                upresize(items.length * RESIZE_FACTOR);
            }
            items[first] = item;
            size++;
        }
    }

    /**
     * Adds item to end of the list.
     * @param item Object of Type T to add to the deque.
     */
    public void addLast(T item) {
        if (size == 0) {
            addEmpty(item);
            size++;
        } else {
            last = plusOne(last);
            if ((items.length == size)) {
                upresize(items.length * RESIZE_FACTOR);
            }
            size++;
            items[last] = item;
        }
    }

    /**
     * Add an empty item in the set.
     * @param item Object of type T to add.
     */
    private void addEmpty(T item) {
        first = 0;
        last = 0;
        items[0] = item;
    }

    /**
     * Helper function for when adding an item.
     * @param index Numerical marker increasing using modular arithmetic
     *              with capacity.
     */
    private int plusOne(int index) {
        if (index + 1 == items.length) {
            index = 0;
        } else {
            index++;
        }
        return index;
    }

    /**
     * Returns size of array.
     * @return an integer representing the number of items in the deque.
     */
    public int size() {
        return size;
    }

    /**
     * Prints out array deque.
     */
    public void printDeque() {
        // Stops just before last item to prevent extra " "
        for (int x = 0; x < size - 1; x++) {
            System.out.print(get(x) + " ");
        }
        System.out.println(get(size - 1));
    }

    /**
     * Removes first item for the array.
     * @return the value contained in the item that was removed.
     */
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T result = items[first];
            items[first] = null;
            first = plusOne(first);
            size--;
            if (items.length * USAGE_LIMIT == size && size > INIT_CAPACITY) {
                downresize(items.length / RESIZE_FACTOR);
            }
            return result;
        }

    }

    /**
     * Removes last item from the array.
     * @return the value contained in the item that was removed.
     */
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T result = items[last];
            items[last] = null;
            last = minusOne(last);
            size--;
            if (items.length * USAGE_LIMIT == size && size > INIT_CAPACITY) {
                downresize(items.length / RESIZE_FACTOR);
            }
            return result;
        }

    }

    /**
     * Helper function for when removing an item.
     * @param index Numerical address decremented using modular arithmetic
     *              with capacity.
     */
    private int minusOne(int index) {
        if (index - 1 == -1) {
            index = items.length - 1;
        } else {
            index = index - 1;
        }
        return index;
    }

    /**
     * Return item in index passed in.
     * @param index Integer representing the numerical location of item in deque.
     * @return Returns the value of type T at the indexed location in the deque.
     */
    public T get(int index) {
        T it = items[(index + first) % items.length];
        return it;
    }

    /**
     * Increases the capacity of the array.
     * @param newsize Represents the new capacity of the array.
     */
    private void upresize(int newsize) {
        T[] newArray = (T[]) new Object[newsize];
        if (first <= last) {
            System.arraycopy(items, first, newArray, 0, size - first);
            System.arraycopy(items, 0, newArray, size - first, last + 1);

        } else {
            System.arraycopy(items, first, newArray, 0, size - first);
            System.arraycopy(items, 0, newArray, size - first, last + 1);
        }
        first = 0;
        last = newArray.length / 2;
        items = newArray;
    }

    /**
     * Decreases the capacity of the array.
     * @param newsize Represents the new capacity of the array.
     */
    private void downresize(int newsize) {
        T[] newArray = (T[]) new Object[newsize];
        if (first <= last) {
            System.arraycopy(items, first, newArray, 0, last - size);
        } else {
            System.arraycopy(items, first, newArray, 0, size - first);
            System.arraycopy(items, 0, newArray, size - first, last + 1);
        }
        first = 0;
        last = newArray.length / 2;
        items = newArray;
    }
}
