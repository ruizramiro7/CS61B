import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnionFindTest {
    @Test
    public void testInit(){
        UnionFind test = new UnionFind(4);
        assertEquals(4,test.array.length);
    }


    @Test
    public void testUnion(){
        UnionFind test = new UnionFind(4);
        test.union(0,1);
        System.out.println(test.parent(0));
        System.out.println(test.parent(1));
        test.union(2,3);
        assertEquals(2,test.parent(3));
        assertEquals(-2, test.parent(2));
        test.union(0,2);
        System.out.println(test.parent(0));
    }

    @Test
    public void testConnected(){
        UnionFind test = new UnionFind(4);
        test.union(0,1);
        test.union(2,3);
        System.out.println(test.connected(2,3));
        System.out.println(test.connected(2,0));
    }

    @Test
    public void testFind(){
        UnionFind test = new UnionFind(4);
        test.union(0,1);
        test.union(2,3);
        test.union(0,2);
        assertEquals(0,test.find(0));
        System.out.println(test.sizeOf(3));
        assertEquals(0,test.find(2));

    }

}
