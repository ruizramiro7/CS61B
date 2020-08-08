package bearmaps.utils.ps;

import bearmaps.utils.graph.streetmap.Node;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {

    @Test
    public void testCompare(){
        Point p1 = new Point(2,3);
        Point p2 = new Point(4,2);
        Point p3 = new Point(4,5);
        Point p4 = new Point(3,3);
        Point p5 = new Point(1,5);
        Point p6 = new Point(4,4);
        KDTree kd = new KDTree(List.of(p1,p2,p3,p4,p5,p6));
        Point target = new Point(0,7);
        Point closest = kd.nearest(target.getX(), target.getY());
        assertEquals(p5, closest);


    }

}