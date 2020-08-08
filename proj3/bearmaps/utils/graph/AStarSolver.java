package bearmaps.utils.graph;
import jdk.jshell.spi.ExecutionControl;

import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
    }
    public SolverOutcome outcome() {
        throw new UnsupportedOperationException();
    }
    public List<Vertex> solution() {
        throw new UnsupportedOperationException();
    }
    public double solutionWeight() {
        throw new UnsupportedOperationException();
    }
    public int numStatesExplored() {
        throw new UnsupportedOperationException();
    }
    public double explorationTime() {
        throw new UnsupportedOperationException();
    }
}

