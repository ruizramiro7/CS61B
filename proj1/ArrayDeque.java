/**
 *
 * @param <T>
 * @source https://cs61bl.org/su20/projects/deques/
 * @author Ramiro Ruiz
 * @author Brandon Bizzarro
 */
public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int first;
    private int last;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        first = 0;
        last = 0;
    }

    public ArrayDeque(ArrayDeque other) {
        size = 0;
        items = (T[]) new Object[other.size()];
        first = 0;
        last = 0;
        for (int i = 0; i < other.size(); i++) {
            addLast((T) other.get(i));
        }
    }
    /** adds item to front of array */
    public void addFirst(T item){
        if (size == 0) {
            addEmpty(item);
            size++;
        } else {
            first = minusOne(first);
            if (items.length == size) {
                upresize(items.length * 2);
            }
            items[first] = item;
            size++;
        }
    }
    /** adds item to end of the list */
    public void addLast(T item){
        if (size == 0) {
            addEmpty(item);
            size++;
        } else {
            last = plusOne(last);
            if ((items.length == size)) {
                upresize(items.length * 2);
            }
            size++;
            items[last] = item;
        }
    }
    /** add an empty thing in the set */
    private void addEmpty(T item) {
        first = 0;
        last = 0;
        items[0] = item;
    }
    /** helper function for when adding an item */
    private int plusOne(int index) {
        if (index + 1 == items.length) {
            index = 0;
        } else {
            index++;
        }
        return index;
    }

    /** checks if array is empty */
    public boolean isEmpty(){
        return size == 0;
    }

    /** returns size of array */
    public int size(){
        return size;
    }
    /** Prints out array deque */
    public void printDeque(){
        for (int x = 0; x < size; x++) {
            System.out.print(get(x));
            System.out.print(" ");
        }
        System.out.println();
    }
    /** removes first item for the array*/
    public T removeFirst(){
        if (size == 0) {
            return null;
        } else {
            T result = items[first];
            items[first] = null;
            first = plusOne(first);
            size--;
            if (items.length / 4 == size && size > 8) {
                downresize(items.length / 2);
            }
            return result;
        }

    }
    /** removes last item from the array*/
    public T removeLast(){
        if (size == 0) {
            return null;
        } else {
            T result = items[last];
            items[last] = null;
            last = minusOne(last);
            size--;
            if (items.length / 4 == size && size > 8) {
                downresize(items.length / 2);
            }
            return result;
        }

    }
    /** helper function for when removing an item */
    private int minusOne(int input) {
        if (input - 1 == -1) {
            input = items.length - 1;
        } else {
            input = input - 1;
        }
        return input;
    }
    /** return item in index inputed */
    public T get(int index){
        T it = items[(index + first) % items.length];
        return it;
    }
    /** resizes when the size of the array to be bigger */
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
    /**  resizes the size of the array to be smaller.*/
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