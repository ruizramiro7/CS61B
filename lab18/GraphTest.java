import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {
    @Test
    public void test() {
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        assertEquals(g.pathExists(0, 2), true);
        assertEquals(g.pathExists(0, 3), false);
        System.out.println(g.path(0, 3));
    }
}
