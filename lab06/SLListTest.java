import org.junit.Test;
import static org.junit.Assert.*;

public class SLListTest {

    @Test
    public void testSLListAdd() {
        SLList test1 = SLList.of(1, 3, 5);
        SLList test2 = new SLList();

        test1.add(1, 2);
        test1.add(3, 4);
        assertEquals(5, test1.size());
        assertEquals(3, test1.get(2));
        assertEquals(4, test1.get(3));

        test2.add(1, 1);
        assertEquals(1, test2.get(0));
        assertEquals(1, test2.size());

        test2.add(10, 10);
        assertEquals(10, test2.get(1));
        test1.add(0, 0);
        assertEquals(SLList.of(0, 1, 2, 3, 4, 5), test1);
    }

    @Test
    public void testSLLisReverse() {
        SLList forward = SLList.of(1, 3, 5);
        SLList reverse = SLList.of(5, 3, 1);
        forward.reverse();
        assertEquals(reverse, forward);

        SLList case1For = SLList.of(1);
        SLList case1Rev = SLList.of(1);
        case1For.reverse();
        assertEquals(case1For, case1Rev);

        SLList case0For = new SLList();
        SLList case0Rev = new SLList();
        case0For.reverse();
        assertEquals(case0For, case0Rev);
    }
}
