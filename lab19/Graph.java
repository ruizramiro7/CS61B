import java.util.*;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;
    private int startVertex;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
//        if(adjLists[v1].get(v2) != null){
//            adjLists[v1].get(v2).weight = weight;
//        }
//        else {
        adjLists[v1].add(new Edge(v1,v2,weight));
        // }
        // TODO: YOUR CODE HERE
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
//        if (adjLists[v1].get(v2) != null){
//            adjLists[v1].get(v2).weight = weight;
//        }
//        if (adjLists[v2].get(v1) != null){
//            adjLists[v2].get(v1).weight = weight;
//        }
        adjLists[v1].add(new Edge(v1, v2, weight));
        adjLists[v2].add(new Edge(v2, v1, weight));
        // TODO: YOUR CODE HERE
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        for (Edge e : adjLists[from]){
            if (e.to == to){
                return true;
            }
        }
        // TODO: YOUR CODE HERE
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        LinkedList neighbors = new LinkedList<>();
        Iterator<Edge> iterable = adjLists[v].iterator();
        while (iterable.hasNext()){
            Edge curr = iterable.next();
            neighbors.add(curr.to);
        }
        // TODO: YOUR CODE HERE
        return neighbors;
    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        int count = 0;
        for (int i = 0; i < vertexCount;i++){
            if (isAdjacent(i,v)){
                count++;
            }
        }
        // TODO: YOUR CODE HERE
        return count;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /**
     *  A class that iterates through the vertices of this graph,
     *  starting with a given vertex. Does not necessarily iterate
     *  through all vertices in the graph: if the iteration starts
     *  at a vertex v, and there is no path from v to a vertex w,
     *  then the iteration will not include w.
     */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        public DFSIterator(Integer start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                int i = fringe.pop();
                while (visited.contains(i)) {
                    if (fringe.isEmpty()) {
                        return false;
                    }
                    i = fringe.pop();
                }
                fringe.push(i);
                return true;
            }
            return false;
        }

        public Integer next() {
            int curr = fringe.pop();
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            lst.sort((Integer i1, Integer i2) -> -(i1 - i2));
            for (Integer e : lst) {
                fringe.push(e);
            }
            visited.add(curr);
            return curr;
        }

        //ignore this method
        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        ArrayList<Integer> path = new ArrayList<>();
        Iterator<Integer> iter = new DFSIterator(start);
        while (iter.hasNext()){
            path.add(iter.next());
        }
        if (start == stop){
            return true;
        }
        return path.contains(stop);
        // TODO: YOUR CODE HERE
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        ArrayList<Integer> path = new ArrayList<>();
        if (!pathExists(start,stop)){
            return path;
        }
        if (start == stop){
            path.add(start);
            return path;
        }

        ArrayList<Integer> checked = new ArrayList<>();
        Iterator<Integer> iter = new DFSIterator(start);
        while (iter.hasNext()){
            int curr = iter.next();
            if (curr == stop){
                break;
            }
            checked.add(curr);
        }
        int end = stop;
        path.add(end);
        for(int i = checked.size() -1; i>= 0; i--){
            if (checked.get(i) == start && isAdjacent(start,end)){
                path.add(checked.get(i));
                break;
            }else if (isAdjacent(checked.get(i), end)){
                path.add(checked.get(i));
                end = checked.get(i);
            }
        }
        Collections.reverse(path);

        // TODO: YOUR CODE HERE
        return path;
    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private int[] currentInDegree = new int[adjLists.length];


        // TODO: Instance variables here!

        TopologicalIterator() {
            fringe = new Stack<>();
            for (int i = 0; i < adjLists.length; i++){
                currentInDegree[i] = inDegree(i);
                if (currentInDegree[i] == 0){
                    fringe.push(i);
                    currentInDegree[i]--;
                }
            }
            // TODO: YOUR CODE HERE
        }

        public boolean hasNext() {
            return !fringe.isEmpty();
            // TODO: YOUR CODE HERE
        }

        public Integer next() {
            // TODO: YOUR CODE HERE
            int vertex = fringe.pop();
            for (Edge e : adjLists[vertex]){
                currentInDegree[e.to]--;
            }
            for (int i =0; i < adjLists.length; i++){
                if (currentInDegree[i] == 0 && !fringe.contains(i)){
                    fringe.push(i);
                    currentInDegree[i]--;
                }
            }
            return vertex;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
    public List<Integer> shortestPath(int start, int stop){
        LinkedList<Integer> fringe = new LinkedList<>();
        HashSet<Integer> checked = new HashSet<>();
        HashMap<Integer, Integer> distance = new HashMap<>();
        HashMap<Integer, Integer> prev = new HashMap<>();

        for (int i = 0; i < vertexCount; i++) {
            distance.put(i, Integer.MAX_VALUE);
        }
        distance.put(start, 0);
        fringe.add(start);

        while (!fringe.isEmpty()) {
            int v = fringe.poll();
            if (! checked.contains(v)) {
                checked.add(v);
                for (Object o : neighbors(v)) {
                    int neighbor = (int) o;
                    fringe.add(neighbor);
                    int weight = getEdge(v, neighbor).getWeight();
                    if (distance.get(neighbor) > distance.get(v) + weight) {
                        distance.put(neighbor, distance.get(v) + weight);
                        prev.put(neighbor, v);
                    }
                }
            }
        }
        ArrayList<Integer> shortest = new ArrayList<>();
        shortest.add(stop);
        int end = prev.get(stop);
        while (end != start) {
            shortest.add(end);
            end = prev.get(end);
        }
        shortest.add(start);
        Collections.reverse(shortest);
        return shortest;
    }

    public Edge getEdge(int u, int v){
        for (int i = 0; i < adjLists[u].size(); i++){
            if (adjLists[u].get(i).to == v){
                return adjLists[u].get(i);
            }
        }
        return null;
    }


    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
        public int getWeight(){
            return weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
        Graph g1 = new Graph(5);
        g1.generateG1();
        g1.printDFS(0);
        g1.printDFS(2);
        g1.printDFS(3);
        g1.printDFS(4);

        g1.printPath(0, 3);
        g1.printPath(0, 4);
        g1.printPath(1, 3);
        g1.printPath(1, 4);
        g1.printPath(4, 0);

        Graph g2 = new Graph(5);
        g2.generateG2();
        g2.printTopologicalSort();
    }
}