import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.

    QuadTree q;

    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    public Rasterer(String imgRoot) {
        q = new QuadTree();
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     * </p>
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     *                    Can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     *                    forget to set this to true! <br>
     //* @see #REQUIRED_RASTER_REQUEST_PARAMS
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {

        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double width = params.get("w");
        double height = params.get("h");
        double londpp = QuadTree.calclonDPP(lrlon, ullon, width);

        LinkedList<QuadTree.Node> allnodes = new LinkedList<>();
        intersectnodes(allnodes, q.root, lrlon, lrlat, ullon, ullat, londpp);

        int depth = allnodes.getFirst().depth;
        double rasterullon = allnodes.getFirst().ullon;
        double rasterullat = allnodes.getFirst().ullat;
        double rasterlrlon = allnodes.getLast().lrlon;
        double rasterlrlat = allnodes.getLast().lrlat;

        String[][] rendergrid = rendergrid(allnodes);

        double rasterwidth = rendergrid[0].length * 256;
        double rasterheight = rendergrid.length * 256;

        boolean querysuccess = intersects(q.root, lrlon, lrlat, ullon, ullat);

        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", rendergrid);
        results.put("depth", depth);
        results.put("raster_ul_lon", rasterullon);
        results.put("raster_ul_lat", rasterullat);
        results.put("raster_lr_lon", rasterlrlon);
        results.put("raster_lr_lat", rasterlrlat);
        results.put("query_success", querysuccess);
        results.put("raster_width", rasterwidth);
        results.put("raster_height", rasterheight);

        return results;
    }

    public static boolean intersects(QuadTree.Node n, double lrl,
                                     double lrlat, double ull, double ullat) {

        return ((n.ullon < lrl && n.lrlon > ull && n.ullat > lrlat && n.lrlat < ullat));

    }

    public static void intersectnodes(LinkedList<QuadTree.Node> a,
                                      QuadTree.Node q, double lrl, double lrlat,
                                      double ull, double ullat, double londpp) {

        double inputLonDPP = londpp;
        if (intersects(q, lrl, lrlat, ull, ullat)) {
            if (q.lonDPP <= inputLonDPP || q.depth == 7) {
                a.add(q);
            } else {
                intersectnodes(a, q.NW, lrl, lrlat, ull, ullat, londpp);
                intersectnodes(a, q.NE, lrl, lrlat, ull, ullat, londpp);
                intersectnodes(a, q.SW, lrl, lrlat, ull, ullat, londpp);
                intersectnodes(a, q.SE, lrl, lrlat, ull, ullat, londpp);
            }
        }
    }

    public static String[][] rendergrid(LinkedList<QuadTree.Node> allnodes) {

        Collections.sort(allnodes);
        double rowlatitude = allnodes.peek().ullat;

        int i = 0;
        for (QuadTree.Node curr: allnodes) {
            if (curr.ullat == rowlatitude) {
                i++;
            }
            if (curr.ullat != rowlatitude) {
                break;
            }
        }

        String[][] result = new String[(allnodes.size() / i)][i];

        int column = 0;
        int row = 0;
        for (QuadTree.Node node: allnodes) {
            if (node.ullat == rowlatitude) {
                result[row][column] = node.filename;
                column++;
            }
            if (node.ullat < rowlatitude) {
                row++;
                column = 0;
                result[row][column] = node.filename;
                rowlatitude = node.ullat;
                column++;
            }
        }
        return result;

    }
}

