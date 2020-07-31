import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {
    @Test
    public void test() {
        Graph g = new Graph(8);
        g.addEdge(0, 2);
        g.addEdge(0, 3);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 5);
        g.addEdge(3, 5);
        g.addEdge(4, 6);
        g.addEdge(5, 7);
        g.addEdge(6, 7);
        System.out.println(g.topologicalSort());
    }
}
