import org.junit.Test;

import java.util.Comparator;
import java.util.PriorityQueue;

public class GraphTest {

    @Test
    public void testPrim(){
        Graph test = Graph.loadFromText("inputs/graphTestNormal.in");
        System.out.println(test.getAllVertices());
        Graph prim = test.prims(0);
        System.out.println(prim);

    }
    @Test
    public void testEdge(){
        Graph test = Graph.loadFromText("inputs/graphTestNormal.in");
        PriorityQueue<Edge> edge = new PriorityQueue<>(Comparator.comparing(Edge::getWeight));
        edge.addAll(test.getAllEdges());
        System.out.println(edge.peek());
        System.out.println(edge.peek().getSource());
        System.out.println(edge.poll());
        System.out.println(edge.poll());
    }
}
