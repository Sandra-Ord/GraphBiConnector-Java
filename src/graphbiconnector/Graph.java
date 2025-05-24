package graphbiconnector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Graph class represents an undirected graph.
 * Graph has a pointer to the first chosen vertex in the graph.
 * Method biConnect directs the solving of the problem.
 */
public class Graph {

    private final String id;
    Vertex first;
    private int info = 0;

    private int dfn = 0;

    public Graph (String s, Vertex v) {
        id = s;
        first = v;
    }

    public Graph (String s) {
        this (s, null);
    }

    /**
     * Creates a string with each line having a different vertex and following the vertex are all the arcs exiting it.
     * @return String format of a graph.
     */
    @Override
    public String toString() {
        String nl = System.getProperty ("line.separator");
        StringBuffer sb = new StringBuffer (nl);
        sb.append (id);
        sb.append (nl);
        Vertex v = first;
        while (v != null) {
            sb.append (v);
            sb.append (" -->");
            Arc a = v.first;
            while (a != null) {
                sb.append (" ");
                sb.append (a);
                sb.append (" (");
                sb.append (v);
                sb.append ("->");
                sb.append (a.target.toString());
                sb.append (")");
                a = a.next;
            }
            sb.append (nl);
            v = v.next;
        }
        return sb.toString();
    }

    public Vertex createVertex (String vid) {
        Vertex res = new Vertex (vid);
        res.next = first;
        first = res;
        return res;
    }

    public Arc createArc (String aid, Vertex from, Vertex to) {
        Arc res = new Arc (aid);
        res.next = from.first;
        from.first = res;
        res.target = to;
        return res;
    }

    /**
     * Create a connected undirected random tree with n vertices.
     * Each new vertex is connected to some random existing vertex.
     * @param n number of vertices added to this graph
     */
    public void createRandomTree (int n) {
        if (n <= 0)
            return;
        Vertex[] varray = new Vertex [n];
        for (int i = 0; i < n; i++) {
            varray [i] = createVertex ("v" + (n-i));
            if (i > 0) {
                int vnr = (int)(Math.random()*i);
                createArc ("a" + varray [vnr].toString() + "_"
                        + varray [i].toString(), varray [vnr], varray [i]);
                createArc ("a" + varray [i].toString() + "_"
                        + varray [vnr].toString(), varray [i], varray [vnr]);
            }
        }
    }

    /**
     * Create an adjacency matrix of this graph.
     * Side effect: corrupts info fields in the graph
     * @return adjacency matrix
     */
    public int[][] createAdjMatrix() {
        info = 0;
        Vertex v = first;
        while (v != null) {
            v.info = info++;
            v = v.next;
        }
        int[][] res = new int [info][info];
        v = first;
        while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
                int j = a.target.info;
                res [i][j]++;
                a = a.next;
            }
            v = v.next;
        }
        return res;
    }

    /**
     * Create a connected simple (undirected, no loops, no multiple arcs) random graph with n vertices and m edges.
     * @param n number of vertices
     * @param m number of edges
     */
    public void createRandomSimpleGraph (int n, int m) {
        if (n <= 0)
            return;
        if (n > 2500)
            throw new IllegalArgumentException ("Too many vertices: " + n);
        if (m < n-1 || m > n*(n-1)/2)
            throw new IllegalArgumentException
                    ("Impossible number of edges: " + m);
        first = null;
        createRandomTree (n);       // n-1 edges created here
        Vertex[] vert = new Vertex [n];
        Vertex v = first;
        int c = 0;
        while (v != null) {
            vert[c++] = v;
            v = v.next;
        }
        int[][] connected = createAdjMatrix();
        int edgeCount = m - n + 1;  // remaining edges
        while (edgeCount > 0) {
            int i = (int)(Math.random()*n);  // random source
            int j = (int)(Math.random()*n);  // random target
            if (i==j)
                continue;  // no loops
            if (connected [i][j] != 0 || connected [j][i] != 0)
                continue;  // no multiple edges
            Vertex vi = vert [i];
            Vertex vj = vert [j];
            createArc ("a" + vi + "_" + vj, vi, vj);
            connected [i][j] = 1;
            createArc ("a" + vj + "_" + vi, vj, vi);
            connected [j][i] = 1;
            edgeCount--;  // a new edge happily created
        }
    }

    /**
     * Create a connected simple graph (undirected, no loops, no multiple arcs from structure).
     * @param structure - map of vertex names, where
     *                  key is the name of the source vertex and
     *                  the value is a list of the destination vertices from that source.
     */
    public void createGraphFromStructure(LinkedHashMap<String, List<String>> structure) {
        validateSimpleGraphStructure(structure);

        Map<String, Vertex> vertices = new LinkedHashMap<>();
        for (String s : structure.keySet()) {
            vertices.put(s, new Vertex(s));
        }

        Vertex lastVertex = null;
        for (Map.Entry<String, List<String>> entry : structure.entrySet()) {
            String sourceVertex = entry.getKey();
            List<String> values = entry.getValue();
            Vertex from = vertices.get(sourceVertex);

            if (lastVertex == null) first = from;
            else lastVertex.next = from;

            for (String destinationVertex : values) {
                Vertex to = vertices.get(destinationVertex);
                Arc arc = new Arc("a" + sourceVertex + "_" + destinationVertex);
                from.addLastArc(arc, to);
            }
            lastVertex = from;
        }
    }

    /**
     * Check the conditions of a map creating a simple graph.
     *      Graph has to be connected, undirected and have no self loops.
     * @param structure - map of vertex names, where
     *                  key is the name of the source vertex and
     *                  the value is a list of the destination vertices from that source.
     */
    private static void validateSimpleGraphStructure(LinkedHashMap<String, List<String>> structure) {
        int vertices = structure.size();
        int edges = structure.values().stream().mapToInt(List::size).sum() / 2;
        if (vertices == 0)
            throw new IllegalArgumentException("To build a graph from structure, you must have at least one vertex!");
        if (vertices > 2500)
            throw new IllegalArgumentException ("Too many vertices: " + vertices);
        if (edges < vertices - 1 || edges > vertices*(vertices-1)/2)
            throw new IllegalArgumentException
                    ("Impossible number of edges: " + edges);

        Set<String> keysSet = new HashSet<>(structure.keySet());

        for (Map.Entry<String, List<String>> entry : structure.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            // Check if all values are keys in the map
            if (!keysSet.containsAll(values)) {
                throw new RuntimeException("Graph can't be directional: " +
                        key + " has a value in " + values + " that is not specified in the map.");
            }
            // Graph has to be not directional - all the values of one key have to have the key as one of their values.
            for (String value : values) {
                if (!structure.get(value).contains(key)) {
                    throw new RuntimeException("Graph can't be directional: " +
                            key + " has " + value + " amongst its values " + values + " but " +
                            value + " doesn't have " + key + " among the values " + structure.get(value) + ".");
                }
            }
            // No self-loops allowed
            if (values.contains(key)) {
                throw new RuntimeException("Self loops are not allowed: " +
                        key + " has itself as one of the values " + values + ".");
            }
        }
    }

    /**
     * Method biConnect directs the solving of finding articulationpoints and eliminating them.
     * Articulation points are found and eliminated in the biConnectGraphFunction() method.
     * Articulation points are saved to the set articulationPoints with future debugging and testing in mind.
     */
    public Set<Vertex> biConnectGraph() {
        Set<Vertex> articulationPoints = new HashSet<>();
        Set<Arc> addedEdges = new HashSet<>();
        biConnectGraphFunction(first, null, articulationPoints, addedEdges);
        dfn = 0;
        return articulationPoints;
    }

    /**
     * An overload of biConnectGraph() that allows the user to also print out the articulation points and added edges.
     * @param print - Boolean value to determine whether the information should be printed or not.
     * @param detailed - if true will print out all the APs and added edges in addition to the AP count.
     */
    public Set<Vertex> biConnectGraph(boolean print, boolean detailed) {
        if (!print) {
            return biConnectGraph();
        }
        Set<Vertex> articulationPoints = new HashSet<>();
        Set<Arc> addedEdges = new HashSet<>();
        biConnectGraphFunction(first, null, articulationPoints, addedEdges);
        System.out.println("Articulation point count: " + "(" + articulationPoints.size() + ")");
        if (detailed) {
            System.out.println("Articulation points found: " +
                    articulationPoints.stream()
                            .map(each -> each.id)
                            .collect(Collectors.joining(", ")));
            System.out.println("Edges that were added: " + addedEdges.stream()
                    .map(each -> each.id)
                    .collect(Collectors.joining(", ")));
        }
        dfn = 0; // reset the state of the graph.
        return articulationPoints;
    }

    /**
     * Performs a Depth-First Search (DFS) and uses Trajan's algorithm to find articulation points in a graph.
     *      Updates discovery times for each vertex.
     *      Finds current vertex's children in the DFS tree.
     *      Finds back edge DFN values for the current vertex.
     * @param current The current vertex being visited in the DFS traversal.
     * @param parent  The parent vertex of the current vertex in the DFS tree.
     * @param articulationPoints Set of articulationPoints to update during the search.
     * @param addedEdges Set of edges that were added once an articulation point was found to eliminate it.
     */
    private void biConnectGraphFunction(Vertex current, Vertex parent, Set<Vertex> articulationPoints, Set<Arc> addedEdges) {
        current.visited = true;
        current.discovery = ++dfn;
        // DFS through the graph.
        Arc arc = current.first;
        while (arc != null) {
            Vertex child = arc.target;
            if (!child.visited) {
                // child vertex is a child to the current vertex in the dfs tree.
                current.addDfsChild(child);
                biConnectGraphFunction(child, current, articulationPoints, addedEdges);
            } else {
                if (child != parent && child.discovery < current.discovery){
                    // edge that connects the vertex to its ancestor in the dfs tree (creates a cycle in the graph).
                    // edge that exists in the original graph but not in the dfs tree.
                    current.addBackEdgeDfn(child.discovery);
                }
            }
            arc = arc.next;
        }
        current.low(); // low value of the vertex

        if (parent == null && current.dfsChildren.size() == 1) return;  // Root with 1 child -> not an AP
        if (current.dfsChildren.size() < 1) return; // current vertex is a leaf -> not an AP
        Vertex prevChild = current.dfsChildren.get(0);
        for (Vertex dfsChild : current.dfsChildren) {
            if (parent == null) {
                articulationPoints.add(current);
                // Root is an articulation point - connect the children between themselves.
                if (prevChild != dfsChild) {
                    addBiDirectionalEdge(prevChild, dfsChild, addedEdges);
                    prevChild = dfsChild;
                }
                continue;
            }
            if (current.discovery <= dfsChild.low) {
                // Current point is an articulation point - add an edge from the parent to all the children.
                articulationPoints.add(current);
                addBiDirectionalEdge(parent, dfsChild, addedEdges);
            }
        }
        // Will no longer be used, so will be reset.
        current.dfsChildren = new ArrayList<>();
        current.backEdgesDfn = new ArrayList<>();
    }

    /**
     * Create an undirected edge between source and destination.
     *      Create an arc from source to destination and from destination to source.
     * @param source - vertex that needs to connect to the destination via the edge.
     * @param destination - vertex that needs to connect to the source via the edge.
     */
    private void addBiDirectionalEdge(Vertex source, Vertex destination, Set<Arc> edges) {
        edges.add(addEdge(source, destination));
        edges.add(addEdge(destination, source));
    }

    /**
     * Creates an arc from source to destination.
     * @param source - vertex to start the arc from.
     * @param destination - vertex to end the arc at.
     * @return - arc created between the points.
     */
    private static Arc addEdge(Vertex source, Vertex destination) {
        Arc arc = new Arc("a" + source.id + "_" + destination.id);
        source.addLastArc(arc, destination);
        return arc;
    }
}