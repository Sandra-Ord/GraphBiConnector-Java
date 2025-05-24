import static org.junit.Assert.*;

import graphbiconnector.Graph;
import graphbiconnector.Vertex;
import org.junit.Test;
import java.util.*;

/**
 * Class for testing the algorithm for finding and eliminating articulation points.
 */
public class GraphBiConnectorTest {

    @Test (timeout=10000)
    public void biConnectComplexGraph() {
        LinkedHashMap<String, List<String>> graphStructure = new LinkedHashMap<>();
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
        Graph graph = new Graph("Complex example Graph");

        graph.createGraphFromStructure(graphStructure);

        Set<Vertex> articulationPoints = graph.biConnectGraph();
        String apString = articulationPoints.toString();
        assertEquals ("Complex graph should have 6 articulation points, found " + articulationPoints,
                6, articulationPoints.size());
        assertTrue("Vertex a should be an articulation point", apString.contains("a"));
        assertTrue("Vertex d should be an articulation point", apString.contains("d"));
        assertTrue("Vertex e should be an articulation point", apString.contains("e"));
        assertTrue("Vertex g should be an articulation point", apString.contains("g"));
        assertTrue("Vertex i should be an articulation point", apString.contains("i"));
        assertTrue("Vertex m should be an articulation point", apString.contains("m"));

        articulationPoints = graph.biConnectGraph();
        assertEquals ("After edges have been added, no articulation points should be found, found " + articulationPoints,
                new HashSet<>(), articulationPoints);
    }

    @Test (timeout=10000)
    public void biConnectVerySmallGraph() {
        LinkedHashMap<String, List<String>> graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList());
        Graph graph = new Graph("Small Graph with one vertex");

        graph.createGraphFromStructure(graphStructure);

        Set<Vertex> articulationPoints = graph.biConnectGraph();
        assertEquals("No articulation points in a graph with 1 vertex.", 0, articulationPoints.size());


        graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b"));
        graphStructure.put("b", Arrays.asList("a"));
        graph = new Graph("Small Graph with two vertices");

        graph.createGraphFromStructure(graphStructure);

        articulationPoints = graph.biConnectGraph();
        assertEquals("No articulation points in a graph with 2 vertices.", 0, articulationPoints.size());
    }

    @Test (timeout=10000)
    public void biConnectFullGraph() {
        LinkedHashMap<String, List<String>> graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b", "c", "d"));
        graphStructure.put("b", Arrays.asList("a", "c", "d"));
        graphStructure.put("c", Arrays.asList("a", "b", "d"));
        graphStructure.put("d", Arrays.asList("a", "b", "c"));
        Graph graph = new Graph("Full Graph");

        graph.createGraphFromStructure(graphStructure);

        Set<Vertex> articulationPoints = graph.biConnectGraph();
        assertEquals ("Full graph shouldn't have any articulation points, found " + articulationPoints,
                new HashSet<>(), articulationPoints);
    }

    @Test (timeout=10000)
    public void biConnectCycleGraph() {
        LinkedHashMap<String, List<String>> graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b", "e"));
        graphStructure.put("b", Arrays.asList("a", "c"));
        graphStructure.put("c", Arrays.asList("b", "d"));
        graphStructure.put("d", Arrays.asList("c", "e"));
        graphStructure.put("e", Arrays.asList("d", "a"));
        Graph graph = new Graph("Simple Cycle Graph");

        graph.createGraphFromStructure(graphStructure);

        Set<Vertex> articulationPoints = graph.biConnectGraph();
        assertEquals ("Cycle graph shouldn't have any articulation points, found " + articulationPoints,
                new HashSet<>(), articulationPoints);
    }

    @Test (timeout=10000)
    public void biConnectChainGraph() {
        LinkedHashMap<String, List<String>> graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b"));
        graphStructure.put("b", Arrays.asList("a", "c"));
        graphStructure.put("c", Arrays.asList("b", "d"));
        graphStructure.put("d", Arrays.asList("c", "e"));
        graphStructure.put("e", Arrays.asList("d"));
        Graph graph = new Graph("Simple Chain Graph");

        graph.createGraphFromStructure(graphStructure);

        Set<Vertex> articulationPoints = graph.biConnectGraph();
        String apString = articulationPoints.toString();

        assertEquals("Articulation points should contain all vertices except the root and the leaf in a chain graph.", 3, articulationPoints.size());
        assertTrue("Vertex b should be an articulation point", apString.contains("b"));
        assertTrue("Vertex c should be an articulation point", apString.contains("c"));
        assertTrue("Vertex d should be an articulation point", apString.contains("d"));

        articulationPoints = graph.biConnectGraph();
        assertEquals ("After edges have been added, no articulation points should be found, found " + articulationPoints,
                new HashSet<>(), articulationPoints);
    }

    @Test (timeout=10000)
    public void biConnectRootAp() {
        LinkedHashMap<String, List<String>> graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b", "c", "d"));
        graphStructure.put("b", Arrays.asList("a"));
        graphStructure.put("c", Arrays.asList("a"));
        graphStructure.put("d", Arrays.asList("a"));
        Graph graph = new Graph("Root AP");

        graph.createGraphFromStructure(graphStructure);

        Set<Vertex> articulationPoints = graph.biConnectGraph();
        String apString = articulationPoints.toString();

        assertEquals("Only the root should be an articulation point.", 1, articulationPoints.size());
        assertTrue("Vertex a should be an articulation point", apString.contains("a"));

        articulationPoints = graph.biConnectGraph();
        assertEquals ("After edges have been added, no articulation points should be found, found " + articulationPoints,
                new HashSet<>(), articulationPoints);
    }

    @Test (timeout=10000)
    public void biConnectComplexGraphWithCycles() {
        LinkedHashMap<String, List<String>> graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b", "c"));
        graphStructure.put("b", Arrays.asList("a", "d"));
        graphStructure.put("c", Arrays.asList("d", "a", "g", "h"));
        graphStructure.put("d", Arrays.asList("b", "e", "f", "c"));
        graphStructure.put("e", Arrays.asList("f", "d"));
        graphStructure.put("f", Arrays.asList("d", "e"));
        graphStructure.put("g", Arrays.asList("c", "h"));
        graphStructure.put("h", Arrays.asList("c", "g"));
        Graph graph = new Graph("Complex Graph With Cycles");

        graph.createGraphFromStructure(graphStructure);

        Set<Vertex> articulationPoints = graph.biConnectGraph();
        String apString = articulationPoints.toString();
        assertEquals("Graph should have 2 articulation points.", 2, articulationPoints.size());
        assertTrue("Vertex c should be an articulation point", apString.contains("c"));
        assertTrue("Vertex d should be an articulation point", apString.contains("d"));

        articulationPoints = graph.biConnectGraph();
        assertEquals ("After edges have been added, no articulation points should be found, found " + articulationPoints,
                new HashSet<>(), articulationPoints);
    }


    @Test (timeout=10000)
    public void biConnectComplexGraphWithoutCycles() {
        LinkedHashMap<String, List<String>> graphStructure = new LinkedHashMap<>();
        graphStructure.put("a", Arrays.asList("b"));
        graphStructure.put("b", Arrays.asList("a", "c", "d", "f"));
        graphStructure.put("c", Arrays.asList("b"));
        graphStructure.put("d", Arrays.asList("b", "e"));
        graphStructure.put("e", Arrays.asList("d"));
        graphStructure.put("f", Arrays.asList("b", "g", "h"));
        graphStructure.put("g", Arrays.asList("f"));
        graphStructure.put("h", Arrays.asList("f"));
        Graph graph = new Graph("Complex Graph Without Cycles");

        graph.createGraphFromStructure(graphStructure);

        Set<Vertex> articulationPoints = graph.biConnectGraph();
        String apString = articulationPoints.toString();
        assertEquals("Graph should have 3 articulation points.", 3, articulationPoints.size());
        assertTrue("Vertex b should be an articulation point", apString.contains("b"));
        assertTrue("Vertex d should be an articulation point", apString.contains("d"));
        assertTrue("Vertex f should be an articulation point", apString.contains("f"));

        articulationPoints = graph.biConnectGraph();
        assertEquals ("After edges have been added, no articulation points should be found, found " + articulationPoints,
                new HashSet<>(), articulationPoints);
    }
}
