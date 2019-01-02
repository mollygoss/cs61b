import java.util.LinkedList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest, 
     * where the longs are node IDs.
     */

    private static class SearchNode {

        private SearchNode parent;
        private double previousmoves;
        private GraphDB.Node n;
        private double priority;
        private double vtow;

        private SearchNode(SearchNode current, GraphDB.Node node,
                           double moves, double closedistance, double distance) {
            parent = current;
            n = node;
            previousmoves = moves;
            vtow = closedistance;
            priority = previousmoves + vtow + distance;
        }
    }


    public static LinkedList<Long> shortestPath(GraphDB g,
                                                double stlon, double stlat,
                                                double destlon, double destlat) {

        HashSet<Long> marked = new HashSet<>();
        GraphDB.Node start = g.nodemap.get(g.closest(stlon, stlat));
        GraphDB.Node finish = g.nodemap.get(g.closest(destlon, destlat));
        SearchNode curr = new SearchNode(null, start, 0, 0, g.distance(start.id, finish.id));
        double currdistance = 0;


        PriorityQueue<SearchNode> priorityqueue = new PriorityQueue<>(new Comparator<SearchNode>() {
            @Override
            public int compare(SearchNode o1, SearchNode o2) {
                if (o1.priority > o2.priority) {
                    return 1;
                }
                if (o1.priority < o2.priority) {
                    return -1;
                }
                return 0;
            }
        });

        while (curr.n.lat != finish.lat && curr.n.lon != finish.lon) {

            for (Long l : g.adjacent(curr.n.id)) {
                if (!(marked.contains(l))) {
                    GraphDB.Node lnode = g.nodemap.get(l);
                    double closedistance = g.distance(curr.n.id, l);
                    double distance = g.distance(l, finish.id);
                    SearchNode searchnode = new SearchNode(curr,
                            lnode, currdistance, closedistance, distance);
                    priorityqueue.add(searchnode);
                }
            }

            SearchNode snode = priorityqueue.remove();
            curr = snode;
            currdistance = (snode.previousmoves + snode.vtow);
            marked.add(snode.n.id);
        }

        LinkedList<Long> results = new LinkedList<>();
        while (curr != null) {
            results.addFirst(curr.n.id);
            curr = curr.parent;
        }

        return results;

    }
}
