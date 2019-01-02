import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    HashMap<Long, Node> nodemap;
    LinkedList<Way> ways;
    HashMap<Long, ArrayList<Node>> adj;

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        nodemap = new HashMap<>();
        ways = new LinkedList<>();
        adj = new HashMap<>();
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    public static class Node {

        long id;
        double lon;
        double lat;
        String name;

        public Node(long identity, double longi, double lati) {
            id = identity;
            lon = longi;
            lat = lati;
            name = null;
        }
    }

    public static class Edge {

        Node one;
        Node two;

        public Edge(Node v, Node w) {

            one = v;
            two = w;

        }
    }

    public static class Way {

        long id;
        LinkedList<Node> waynodes;
        LinkedList<Edge> wayedges;
        boolean isvalid;

        public Way(long identity) {
            id = identity;
            waynodes = new LinkedList<>();
            wayedges = new LinkedList<>();
            isvalid = false;
        }
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        LinkedList<Node> gottago = new LinkedList<>();

        for (Node n : nodemap.values()) {
            if (!(adj.containsKey(n.id))) {
                gottago.add(n);
            }
        }
        for (Node n : gottago) {
            this.removeNode(n);
        }
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        ArrayList<Long> result = new ArrayList<>();
        for (Long id: nodemap.keySet()) {
            result.add(id);
        }
        return result;
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        ArrayList<Long> adjacent = new ArrayList();
        for (Node node: adj.get(v)) {
            adjacent.add(node.id);
        }
        return adjacent;
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        Node one = nodemap.get(v);
        Node two = nodemap.get(w);
        return Math.sqrt(Math.pow((one.lon - two.lon), 2) + Math.pow((one.lat - two.lat), 2));
    }

    double finddistance(long v, double longi, double lati) {
        Node node = nodemap.get(v);
        double lon = longi;
        double lat = lati;
        return Math.sqrt(Math.pow(node.lon - lon, 2) + Math.pow(node.lat - lat, 2));
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        double closestvalue = 4353464;
        long closestid = 0;
        for (Node node: nodemap.values()) {
            double distance = finddistance(node.id, lon, lat);
            if (distance < closestvalue) {
                closestvalue = distance;
                closestid = node.id;
            }
        }
        return closestid;
    }

    /** Longitude of vertex v. */
    double lon(long v) {
        return nodemap.get(v).lon;
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return nodemap.get(v).lat;
    }

    void addNode(Node n) {
        nodemap.put(n.id, n);
    }

    void removeNode(Node n) {
        nodemap.remove(n.id);
    }

    void addEdge(Edge e) {

        Node one = e.one;
        Node two = e.two;
        long oneid = one.id;
        long twoid = two.id;

        if (adj.containsKey(oneid)) {
            adj.get(oneid).add(two);
        } else {
            ArrayList<Node> temp = new ArrayList<>();
            temp.add(two);
            adj.put(oneid, temp);
        }
        if (adj.containsKey(twoid)) {
            adj.get(twoid).add(one);
        } else {
            ArrayList<Node> temp = new ArrayList<>();
            temp.add(one);
            adj.put(twoid, temp);
        }

    }
}
