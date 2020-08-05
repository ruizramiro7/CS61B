import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {
    @Test
    public void test() {
        Graph allDisjoint = Graph.loadFromText("inputs/graphTestAllDisjoint.in");
        Graph multiEdge = Graph.loadFromText("inputs/graphTestMultiEdge.in");
        Graph normal = Graph.loadFromText("inputs/graphTestNormal.in");
        Graph someDisjoint = Graph.loadFromText("inputs/graphTestSomeDisjoint.in");

        System.out.println("ALL DISJOINT");
        Graph t1p = allDisjoint.prims(0);
        Graph t1k = allDisjoint.kruskals();
        System.out.println(t1k);

        System.out.println("MULTIEDGE");
        Graph t2p = multiEdge.prims(0);
        Graph t2k = multiEdge.kruskals();
        System.out.println(t2k);

        System.out.println("NORMAL");
        Graph t3p = normal.prims(0);
        Graph t3k = normal.kruskals();
        System.out.println(t3k);

        System.out.println("SOME DISJOINT");
        Graph t4p = someDisjoint.prims(0);
        Graph t4k = someDisjoint.kruskals();
        System.out.println(t4p);

        Graph tp = new Graph();
        tp.addEdge(0, 1, 2);
        tp.addEdge(0, 2, 1);
        tp.addEdge(2, 1, 5);
        tp.addEdge(2, 4, 1);
        tp.addEdge(2, 5, 15);
        tp.addEdge(2, 5, 15);
        tp.addEdge(1, 4, 3);
        tp.addEdge(1, 3, 11);
        tp.addEdge(4, 6, 3);
        tp.addEdge(4, 3, 2);
        tp.addEdge(4, 5, 15);
        tp.addEdge(6, 5, 1);
        tp.addEdge(6, 3, 1);
        System.out.println("FROM DEMO PRIMS");
        System.out.println(tp.prims(0));

        Graph tk = new Graph();
        tk.addEdge(0, 2, 4);
        tk.addEdge(0, 4, 4);
        tk.addEdge(0, 3, 9);
        tk.addEdge(3, 5, 1);
        tk.addEdge(5, 7, 2);
        tk.addEdge(7, 6, 6);
        tk.addEdge(7, 4, 8);
        tk.addEdge(6, 4, 4);
        tk.addEdge(4, 2, 8);
        tk.addVertex(1);
        System.out.println("FROM DEMO K");
        System.out.println(tk.kruskals());

        Graph tree = new Graph();
        tree.addEdge(0, 1, 1);
        tree.addEdge(0, 2, 2);
        tree.addEdge(2, 3, 3);
        System.out.println(tree.kruskals());

    }
}
