package graphbiconnector;

import java.util.*;
import java.util.stream.Collectors;

/** Container class to different classes, to solve the given problem.
 * Problem:
 * Computer networks should avoid single points of failure,
 *    that is, network nodes that can disconnect the network if they fail.
 *    We say a connected graph G is biconnected
 *    if it contains no vertex whose removal would divide G into two or more connected components.
 * Give an O(n + m)-time algorithm for
 *    adding at most n edges to a connected graph G, to guarantee that G is biconnected.
 *    Graph G has
 *       n ≥ 3 vertices,
 *       m ≥ n − 1 edges.
 */
public class GraphBiConnector {

    /** Main method. */
    public static void main (String[] args) {
        GraphBiConnector a = new GraphBiConnector();
        a.run();
    }

    /** Actual main method to run examples and everything. */
    public void run() {
        LinkedHashMap<String, List<String>> graphStructure;

        // --------------------------------------------- Complex Graph ---------------------------------------------
        graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b", "e", "p"));
        graphStructure.put("b", Arrays.asList("a", "c"));
        graphStructure.put("c", Arrays.asList("b", "d"));
        graphStructure.put("d", Arrays.asList("c", "f", "h", "l", "q", "e"));
        graphStructure.put("e", Arrays.asList("q", "d", "m", "a"));
        graphStructure.put("f", Arrays.asList("d", "g"));
        graphStructure.put("g", Arrays.asList("f", "i", "k", "h"));
        graphStructure.put("h", Arrays.asList("g", "d"));
        graphStructure.put("i", Arrays.asList("g", "j"));
        graphStructure.put("j", Arrays.asList("i"));
        graphStructure.put("k", Arrays.asList("g"));
        graphStructure.put("l", Arrays.asList("d"));
        graphStructure.put("m", Arrays.asList("e", "n", "o"));
        graphStructure.put("n", Arrays.asList("m", "o"));
        graphStructure.put("o", Arrays.asList("m", "n"));
        graphStructure.put("p", Arrays.asList("a"));
        graphStructure.put("q", Arrays.asList("d", "e"));
        printGraphBeforeAndAfter("Complex example graph", graphStructure);

        // ---------------------------------------- Graph with one vertex ----------------------------------------
        graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList());
        printGraphBeforeAndAfter("Small Graph with one vertex", graphStructure);

        // --------------------------------------- Graph with two vertices ---------------------------------------
        graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b"));
        graphStructure.put("b", Arrays.asList("a"));
        printGraphBeforeAndAfter("Small Graph with two vertices", graphStructure);

        // ----------------------------------------- Graph is a full graph ---------------------------------------
        graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b", "c", "d"));
        graphStructure.put("b", Arrays.asList("a", "c", "d"));
        graphStructure.put("c", Arrays.asList("a", "b", "d"));
        graphStructure.put("d", Arrays.asList("a", "b", "c"));
        printGraphBeforeAndAfter("Full Graph", graphStructure);

        // ---------------------------------------- Graph is a simple cycle ---------------------------------------
        graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b", "e"));
        graphStructure.put("b", Arrays.asList("a", "c"));
        graphStructure.put("c", Arrays.asList("b", "d"));
        graphStructure.put("d", Arrays.asList("c", "e"));
        graphStructure.put("e", Arrays.asList("d", "a"));
        printGraphBeforeAndAfter("Simple Cycle Graph", graphStructure);

        // ---------------------------------------- Graph is a simple chain ---------------------------------------
        graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b"));
        graphStructure.put("b", Arrays.asList("a", "c"));
        graphStructure.put("c", Arrays.asList("b", "d"));
        graphStructure.put("d", Arrays.asList("c", "e"));
        graphStructure.put("e", Arrays.asList("d"));
        printGraphBeforeAndAfter("Simple Chain Graph", graphStructure);

        // ------------------------------- Graph when root is an articulation point -------------------------------
        graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b", "c", "d"));
        graphStructure.put("b", Arrays.asList("a"));
        graphStructure.put("c", Arrays.asList("a"));
        graphStructure.put("d", Arrays.asList("a"));
        printGraphBeforeAndAfter("Root AP", graphStructure);

        // -------------------------------------- Complex Graph With Cycles --------------------------------------
        graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b", "c"));
        graphStructure.put("b", Arrays.asList("a", "d"));
        graphStructure.put("c", Arrays.asList("d", "a", "g", "h"));
        graphStructure.put("d", Arrays.asList("b", "e", "f", "c"));
        graphStructure.put("e", Arrays.asList("f", "d"));
        graphStructure.put("f", Arrays.asList("d", "e"));
        graphStructure.put("g", Arrays.asList("c", "h"));
        graphStructure.put("h", Arrays.asList("c", "g"));
        printGraphBeforeAndAfter("Complex Graph With Cycles", graphStructure);

        // ------------------------------------- Complex Graph Without Cycles -------------------------------------
        graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b"));
        graphStructure.put("b", Arrays.asList("a", "c", "d", "f"));
        graphStructure.put("c", Arrays.asList("b"));
        graphStructure.put("d", Arrays.asList("b", "e"));
        graphStructure.put("e", Arrays.asList("d"));
        graphStructure.put("f", Arrays.asList("b", "g", "h"));
        graphStructure.put("g", Arrays.asList("f"));
        graphStructure.put("h", Arrays.asList("f"));
        printGraphBeforeAndAfter("Complex Graph Without Cycles", graphStructure);

        // ------------------------------------ Big Graphs with Time Measuring ------------------------------------
        bigGraphTimeMeasuring("Big graph example with the most edges (the least articulation points):", "Big graph 1", 2222, 10000);
        bigGraphTimeMeasuring("Big graph example with more edges (some articulation points):", "Big graph 1", 2222, 3333);
        bigGraphTimeMeasuring("Big graph example with the least edges (the most articulation points):", "Big graph 2", 2222, 2221);
    }

    /**
     * Measure the time it takes to find all the articulation points in a random graph with n vertices and m edges.
     * @param title Title to print out that specifies what will be printed next.
     * @param graphName Name for the random graph that will be created based on the parameters.
     * @param n Number of vertices that should exist in the graph.
     * @param m Number of edges that should exist in the graph.
     */
    private static void bigGraphTimeMeasuring(String title, String graphName, int n, int m) {
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println(title);
        Graph b = new Graph(graphName);
        b.createRandomSimpleGraph (n, m);
        long startTime = System.currentTimeMillis();
        b.biConnectGraph(true, false);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("For a graph with " + n + " vertices and " + m + " edges, finding and eliminating articulation points took: " + executionTime + " ms");
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    /**
     * Print out the original graph built from the structure and the graph once the articulation points have been eliminated.
     * @param graphName Name for the graph that will be created from the graphStructure.
     * @param graphStructure Map structure for creating a simple graph that specifies each vertex and where the arcs coming from it should go.
     */
    private static void printGraphBeforeAndAfter(String graphName, LinkedHashMap<String, List<String>> graphStructure) {
        System.out.println("===========================================================================================");
        Graph graph = new Graph(graphName);
        graph.createGraphFromStructure(graphStructure);
        System.out.println(graph);
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("Graph after biConnecting:");
        graph.biConnectGraph(true, true);
        System.out.println(graph);
        //System.out.println("Graph after biConnecting an already biConnected graph:");
        //graph.biConnectGraph(true, true);
        //System.out.println(graph);
        System.out.println("===========================================================================================");

    }
}