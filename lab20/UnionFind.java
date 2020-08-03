import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class UnionFind {
    int [] array;

    /* TODO: Add instance variables here. */

    /* Creates a UnionFind data structure holding N vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int N) {
        array = new int[N];
        Arrays.fill(array,-1);

        // TODO: YOUR CODE HERE
    }

    public void validate(int v){
        if (v >= array.length|| v <0){
            throw new IllegalArgumentException("Invalid input");
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        return -1 * array[find(v)];
        // TODO: YOUR CODE HERE
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        validate(v);
        return array[v];
        // TODO: YOUR CODE HERE
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        validate(v1);
        validate(v2);
        return find(v1) == find(v2);
        // TODO: YOUR CODE HERE
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid vertices are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        validate(v);
        if (array[v] < 0){
            return v;
        }else{
            ArrayList<Integer> path = new ArrayList<>();
            int root = v;
            while (array[root] >= 0){
                path.add(root);
                root = array[root];
            }
            for (int i : path){
                array[i] = root;
            }
            return root;
        }
        // TODO: YOUR CODE HERE
    }

    /* Connects two elements V1 and V2 together. V1 and V2 can be any element,
       and a union-by-size heuristic is used. If the sizes of the sets are
       equal, tie break by connecting V1's root to V2's root. Union-ing a vertex
       with itself or vertices that are already connected should not change the
       structure. */
    public void union(int v1, int v2) {
        int root1 = find(v1);
        int root2 = find(v2);
        if (root1 != root2){
            if (root1 < root2){
                array[root1] += array[root2];
                array[root2] = root1;
             }else {
                array[root2] += array[root1];
                array[root1] = root2;
            }
        }
        // TODO: YOUR CODE HERE
    }
}
