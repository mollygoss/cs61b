import java.util.LinkedList;

/**
 * Created by molly on 4/16/17.
 */
public class QuadTree {

    Node root;
    static LinkedList<Node>[] nodes;

    public static class Node implements Comparable<Node> {

        Node NW, NE, SW, SE;
        String filename, name;
        int depth;
        double width;
        double lonDPP;
        double ullon, ullat, lrlon, lrlat, originlon, originlat;

        public Node(String n, int sink, double lrightlong,
                    double lrightlat, double uleftlong, double uleftlat) {

            name = n;
            filename = "img/" + name + ".png";
            depth = sink;
            width = 256.0;
            lrlon = lrightlong;
            lrlat = lrightlat;
            ullon = uleftlong;
            ullat = uleftlat;
            originlon = (ullon + lrlon) / 2;
            originlat = (ullat + lrlat) / 2;
            lonDPP = calclonDPP(this.lrlon, this.ullon, width);

        }

        public int compareTo(Node n) {
            if (this.ullat == n.ullat) {
                if (this.ullon < n.ullon) {
                    return -1;
                } if (this.ullon > n.ullon) {
                    return 1;
                }
            } if (this.ullat < n.ullat) {
                return 1;
            } if (this.ullat > n.ullat) {
                return -1;
            }
            return 0;
        }

    }

    public static double calclonDPP(double right, double left, double width) {

        return ((right - left) / width);

    }


    public QuadTree() {

        nodes = new LinkedList[8];

        LinkedList<Node> layerzero = new LinkedList<>();
        root = new Node("", 0, MapServer.ROOT_LRLON,
                MapServer.ROOT_LRLAT, MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT);
        layerzero.add(root);
        nodes[0] = layerzero;

        for (int i = 0; i < 7; i++) {
            expand(i);

        }
    }

    public void expand(int i) {

        LinkedList<Node> layer = new LinkedList<>();
        for (Node node: nodes[i]) {
            node.NW = new Node(node.name + "1", node.depth + 1, node.originlon,
                    node.originlat, node.ullon, node.ullat);
            layer.add(node.NW);
            node.NE = new Node(node.name + "2", node.depth + 1, node.lrlon,
                    node.originlat, node.originlon, node.ullat);
            layer.add(node.NE);
            node.SW = new Node(node.name + "3", node.depth + 1, node.originlon,
                    node.lrlat, node.ullon, node.originlat);
            layer.add(node.SW);
            node.SE = new Node(node.name + "4", node.depth + 1, node.lrlon,
                    node.lrlat, node.originlon, node.originlat);
            layer.add(node.SE);
        }
        nodes[i + 1] = layer;
    }

}

