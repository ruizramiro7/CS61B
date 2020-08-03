import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class UnionFindTests {

    @Test
    public void tests() {
        UnionFind uf = new UnionFind(16);
        assertEquals(uf.sizeOf(0), 1);
        uf.union(0, 1);
        System.out.println(Arrays.toString(uf.items));
        uf.union(0, 1);
        System.out.println(Arrays.toString(uf.items));
        uf.union(3, 0);
        System.out.println(Arrays.toString(uf.items));
        //assertEquals(uf.sizeOf(0), 2);
    }
}
