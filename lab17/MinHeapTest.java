import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapTest {

    @Test
    public void test1() {
        MinHeap<Character> h = new MinHeap<>();
        h.insert('f');
        assertEquals((Character)'f', h.findMin());
        h.insert('h');
        assertEquals((Character)'f', h.findMin());
        h.insert('d');
        assertEquals((Character)'d', h.findMin());
        h.insert('b');
        assertEquals((Character)'b', h.findMin());
        h.insert('c');
        assertEquals((Character)'b', h.findMin());
        h.removeMin();
        assertEquals((Character)'c', h.findMin());
        h.removeMin();
        assertEquals((Character)'d', h.findMin());
        System.out.println(h);

    }
}
