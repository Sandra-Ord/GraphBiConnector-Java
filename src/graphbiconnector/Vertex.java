package graphbiconnector;

import java.util.*;

/**
 * Vertex represents a point in the graph.
 * Vertex has:
 *    a pointer to the next vertex,
 *    a pointer to the first Arc coming from the vertex.
 * Vertex also holds additional information relevant to the problem.
 * Vertex has:
 *    visited field - one of values [-1, 0, 1],
 *       visited holds information about if and how the vertex has been processed during Depth-First Search (DFS).
 *       -1 - vertex has not been visited,
 *        0 - vertex has been visited, but not completely processed (low hasn't been found yet),
 *        1 - vertex has been processed, all necessary values have been found.
 *    discovery - order number, in which the vertex was found during DFS.
 *    low - the smallest discovery nr reachable from the vertex through the current DFS traversal,
 *    dfsChildren - list of vertexes that were found to be the direct child of the vertex during DFS,
 *    backEdgesDfn - list of vertex's back edges' destination dfn values.
 */
public class Vertex {

    final String id;
    Vertex next;
    Arc first;
    int info = 0;

    boolean visited = false;
    Integer discovery = null;
    Integer low;

    List<Vertex> dfsChildren = new ArrayList<>();
    List<Integer> backEdgesDfn = new ArrayList<>();

    Vertex (String s, Vertex v, Arc e) {
        id = s;
        next = v;
        first = e;
    }

    Vertex (String s) {
        this (s, null, null);
    }

    /**
     * @return Vertex is represented by its name (id).
     */
    @Override
    public String toString() {
        return id;
    }

    /**
     * Adds the new arc as the last arc coming from the vertex by finding each arc's next until there is no more.
     * @param a - arc to add
     * @param target - the vertex that the arc targets
     */
    void addLastArc(Arc a, Vertex target) {
        a.target = target;
        if (first == null) {
            first = a;
            return;
        }
        Arc arc = first;
        while (arc.next != null) {
            arc = arc.next;
        }
        arc.next = a;
    }

    /**
     * Adds a vertex child to the dfsChildren list if it has been found to be the direct child in the DFS tree.
     * @param child - child to add to DFS children.
     */
    void addDfsChild(Vertex child) {
        dfsChildren.add(child);
    }

    /**
     * Iterates through all children and their low values to find the smallest one.
     * @return The smallest low value of all the children.
     */
    private Integer dfsChildLowValue() {
        return dfsChildren.stream()
                .map(child -> child.low)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    /**
     * Adds an integer which is the back edges DFN (discovery number) to the backEdgesDfn List.
     * Back edge is an edge which exists in the original graph but isn't in the DFS tree.
     *    Back edge's DFN is the already DFS visited neighbour's discovery number.
     * @param i - back edge's dfn.
     */
    void addBackEdgeDfn(Integer i) {
        backEdgesDfn.add(i);
    }

    /**
     * Finds the smallest DFN of all back edges.
     * @return smallest DFN of a back edge.
     */
    private Integer minBackEdgeDfn() {
        return backEdgesDfn.stream()
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    /**
     * Finds the low value of the vertex.
     * Low value is found with the formula:
     *      low() = min{vertex's discovery number, min(children's low values), min(back edges' DFNs)}
     *      Everything but the discovery number of the vertex could be null.
     * @return - low value of the vertex.
     */
    Integer low() {
        this.low = Arrays.stream(
                        new Integer[]{discovery, dfsChildLowValue(), minBackEdgeDfn()})
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);
        return this.low;
    }
}