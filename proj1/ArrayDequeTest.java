import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    public static void main(String[] args) {
        testResize(128);
    }

    @Test
    public void testInit() {
        ArrayDeque<Integer> num = new ArrayDeque<>();
        assertTrue(num.isEmpty());
    }

    @Test
    public void testAddFirst() {
        ArrayDeque<String> test = new ArrayDeque<>();
        test.addFirst("1");
        assertEquals(1, test.size());
        assertEquals("1", test.get(0));
        test.addFirst("2");
        assertEquals(2, test.size());
        assertEquals("2", test.get(0));
    }

    @Test
    public void testAddLast() {
        ArrayDeque<String> test = new ArrayDeque<>();
        test.addLast("item0");
        assertEquals(1, test.size());
        assertEquals("item0", test.get(0));
        test.addLast("item1");
        assertEquals(2, test.size());
        assertEquals("item1", test.get(1));
    }

    @Test
    public void removeFirstTest() {
        LinkedListDeque<String> test = new LinkedListDeque<>();
        test.addFirst("item0");
        assertEquals(1, test.size());
        assertEquals("item0", test.get(0));
        test.addFirst("item1");
        assertEquals(2, test.size());
        assertEquals("item1", test.get(0));
        test.removeFirst();
        assertEquals(1, test.size());
        assertEquals("item0", test.get(0));
    }

    @Test
    public void removeLastTest() {
        LinkedListDeque<String> test = new LinkedListDeque<>();
        test.addFirst("item0");
        assertEquals(1, test.size());
        assertEquals("item0", test.get(0));
        test.addFirst("item1");
        assertEquals(2, test.size());
        assertEquals("item1", test.get(0));
        test.removeFirst();
        assertEquals(1, test.size());
        assertEquals("item0", test.get(0));
    }

    @Test
    public static void testResize(int runs) {
        ArrayDeque<Integer> arr = new ArrayDeque();
        for (int x = 1; x <= runs; x++) {
            arr.addFirst(x);
            arr.printDeque();
        }
        for (int x = 1; x <= runs; x++) {
            arr.removeFirst();
            arr.printDeque();
        }
    }
}
