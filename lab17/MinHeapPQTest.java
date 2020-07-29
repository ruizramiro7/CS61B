import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapPQTest {

    @Test
    public void test1() {
        MinHeapPQ<String> h = new MinHeapPQ<>();
        h.insert("A", 0);
        h.insert("B", 1);
        h.insert("C", 2);
        assertEquals("A", h.peek());
        h.changePriority("A", 3);
        assertEquals("B", h.peek());

    }
}
