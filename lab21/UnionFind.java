import java.util.Arrays;

public class UnionFind {

    public int[] items;

    /* Creates a UnionFind data structure holding N vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int N) {
        items = new int[N];
        Arrays.fill(items, -1);
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        if (parent(v) < 0) {
            return -1 * parent(v);
        }
        return sizeOf(parent(v));
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return items[v];
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid vertices are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v > items.length - 1) {
            throw new IllegalArgumentException();
        }
        else if (parent(v) < 0) {
            return v;
        }
        int curr = parent(v);
        while (parent(curr) > 0) {
            curr = parent(curr);
        }
        //items[v] = curr;
        return curr;
    }

    /* Connects two elements V1 and V2 together. V1 and V2 can be any element,
       and a union-by-size heuristic is used. If the sizes of the sets are
       equal, tie break by connecting V1's root to V2's root. Union-ing a vertex
       with itself or vertices that are already connected should not change the
       structure. */
    public void union(int v1, int v2) {
        int r1 = find(v1);
        int r2 = find(v2);
        if (r1 == r2) {
            return;
        }
        else if (sizeOf(r1) <= sizeOf(r2)) {
            items[r2] += items[r1];
            items[v1] = v2;
        }
        else {
            items[r1] += items[r2];
            items[v2] = v1;
        }
    }
}
