import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class GraphTest {

    @Test
    public void shortestPathTest(){
        Graph g1 = new Graph(5);
        g1.addEdge(0,1,10);
        g1.addEdge(0,4,5);
        g1.addEdge(1,2,1);
        g1.addEdge(1,4,2);
        g1.addEdge(2,3,4);
        g1.addEdge(3,2,6);
        g1.addEdge(3,0,7);
        g1.addEdge(4,3,2);
        g1.addEdge(4,2,9);
        g1.addEdge(4,1,3);
        List<Integer> path1 = Arrays.asList(0,4,1,2);
        List<Integer> path2 = Arrays.asList(2,3,0,4,1);
        List<Integer> path3 = Arrays.asList(1,4,3,0);
        System.out.println(g1.shortestPath(2,1));
        System.out.println(g1.shortestPath(1,0));
        assertEquals(path1, g1.shortestPath(0,2));
        assertEquals(path2, g1.shortestPath(2,1));
        assertEquals(path3, g1.shortestPath(1,0));
    }
    @Test
    public void edgeTest(){
        Graph g1 = new Graph(5);
        g1.addEdge(0,1,10);
        g1.addEdge(0,4,5);
        g1.addEdge(1,2,1);
        g1.addEdge(1,4,2);
        g1.addEdge(2,3,4);
        g1.addEdge(3,2,6);
        g1.addEdge(3,0,7);
        g1.addEdge(4,3,2);
        g1.addEdge(4,2,9);
        g1.addEdge(4,1,3);
        System.out.println(g1.getEdge(1,4));
    }


}
