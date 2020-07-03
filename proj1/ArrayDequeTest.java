import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void addFirstTest() {
       ArrayDeque<String> test = new ArrayDeque<String>();
       test.addFirst("item0");
       assertEquals("item0", test.get(0));
       test.addFirst("item-1");
       assertEquals("item-1", test.get(0));
    }

    @Test
    public void addLastTest() {
        ArrayDeque<String> test = new ArrayDeque<String>();
        test.addLast("item0");
        assertEquals("item0", test.get(0));
        test.addLast("item1");
        assertEquals("item1", test.get(1));
    }

    @Test
    public void printDequeTest() {
        ArrayDeque<String> test = new ArrayDeque<String>();
        test.addLast("item0");
        test.addLast("item1");
        test.addLast("item2");

        System.out.println("Expected:");
        System.out.println("item0 item1 item2");
        System.out.println("Actual:");
        test.printDeque();
    }

    @Test
    public void removeFirstTest() {
        ArrayDeque<String> test = new ArrayDeque<String>();
        test.addFirst("item0");
        test.addFirst("item-1");
        test.removeFirst();
        assertEquals("item0", test.get(0));
    }

    @Test
    public void removeLastTest() {
        ArrayDeque<String> test = new ArrayDeque<String>();
        test.addLast("item0");
        test.addLast("item1");
        test.removeLast();
        assertEquals("item0", test.get(0));
    }

    @Test
    public void resizeExpandTest() {
        ArrayDeque<String> test = new ArrayDeque<String>();
        for (int i = 0; i < 8; ++i) {
            test.addLast("test" + i);
        }
        // Should expand after size=8
        assertEquals(8, test.size());
        test.addLast("test8");
        test.addLast("test9");
        assertEquals("test9", test.get(9));

    }

    @Test
    public void resizeShrinkTest() {
        ArrayDeque<String> test = new ArrayDeque<String>();
        for (int i = 0; i < 9; ++i) {
            test.addLast("test" + i);
        }

        for (int i = 0; i < 7; ++i) {
            test.removeLast();
        }
        assertEquals(2, test.size());

    }
}
