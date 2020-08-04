import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {
    @Test
    public void test() {
        Graph allDisjoint = Graph.loadFromText("inputs/graphTestAllDisjoint.in");
        Graph multiEdge = Graph.loadFromText("inputs/graphTestMultiEdge.in");
        Graph normal = Graph.loadFromText("inputs/graphTestNormal.in");
        Graph someDisjoint = Graph.loadFromText("inputs/graphTestSomeDisjoint.in");

        Graph t1p = allDisjoint.prims(0);
        Graph t1k = allDisjoint.kruskals();

    }
}
