package graphbiconnector;

/**
 * Arc represents one arrow in the graph.
 * Two-directional edges are represented by two Arc objects (for both directions).
 * Arc has:
 *    a pointer to the target vertex,
 *    a pointer to the next Arc coming from the same source vertex.
 */
public class Arc {

    final String id;
    Vertex target;
    Arc next;

    Arc (String s, Vertex v, Arc a) {
        id = s;
        target = v;
        next = a;
    }

    Arc (String s) {
        this (s, null, null);
    }

    /**
     * @return Arc is represented by its name (id).
     */
    @Override
    public String toString() {
        return id;
    }
}