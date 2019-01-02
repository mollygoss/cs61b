import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.LinkedList;

/**
 * Created by molly on 4/24/17.
 */
public class SeamCarverStart {

    private class Pixel {

        double energy;
        double mx;
        double my;
        int x;
        int y;
        Color color;
        Picture pic;
        Pixel yparent;
        Pixel xparent;
        LinkedList<Pixel> xneighbor;
        LinkedList<Pixel> yneighbor;
        LinkedList<Pixel> xparents;
        LinkedList<Pixel> yparents;

        protected Pixel(Picture p, int ex, int why) {
            pic = p;
            x = ex;
            y = why;
            color = p.get(x, y);
            xneighbor = new LinkedList<>();
            yneighbor = new LinkedList<>();
            xparents = new LinkedList<>();
            yparents = new LinkedList<>();
            energy = 0;
            mx = 0;
            my = 0;
        }
    }

    private Picture pic;
    private Pixel[][] ourpicture;
    private Pixel[][] ourpicturetranspose;
    private LinkedList<Pixel> rightmost;
    private LinkedList<Pixel> bottom;

    public SeamCarverStart(Picture picture) {
        pic = picture;
        ourpicture = new Pixel[width()][height()];
        ourpicturetranspose = new Pixel[height()][width()];
        rightmost = new LinkedList<>();
        bottom = new LinkedList<>();

        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                ourpicture[i][j] = new Pixel(pic, i, j);
                if (i == pic.width() - 1) {
                    rightmost.add(ourpicture[i][j]);
                }
                if (j == pic.height() - 1) {
                    bottom.add(ourpicture[i][j]);
                }
            }
        }

        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                navigate(ourpicture[i][j], ourpicture);
            }
        }

        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                pixelenergy(ourpicture[i][j]);
            }
        }

        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                findMx(ourpicture[i][j]);
            }
        }
        ourpicturetranspose = transpose(ourpicture);
        for (int i = 0; i < pic.height(); i++) {
            for (int j = 0; j < pic.width(); j++) {
                findMy(ourpicturetranspose[i][j]);
            }
        }

    }

    public Picture picture() {
        return new Picture(pic);
    }

    public int width() {
        return pic.width();
    }

    public int height() {
        return pic.height();
    }

    public double energy(int x, int y) {
        return ourpicture[x][y].energy;
    }

    public int[] findHorizontalSeam() {
        Pixel start = null;
        double min = 8754959549.847477480;
        int[] ret = new int[pic.width()];
        for (Pixel pixel : rightmost) {
            if (pixel.mx < min) {
                min = pixel.mx;
                start = pixel;
            }
        }
        int i = pic.width() - 1;
        while (start != null) {
            ret[i] = start.y;
            start = start.xparent;
            i--;
        }
        return ret;

    }

    public int[] findVerticalSeam() {
        Pixel start = null;
        double min = Double.POSITIVE_INFINITY;
        int[] ret = new int[pic.height()];
        for (Pixel pixel : bottom) {
            if (pixel.my < min) {
                min = pixel.my;
                start = pixel;
            }
        }
        int i = pic.height() - 1;
        while (start != null) {
            ret[i] = start.x;
            start = start.yparent;
            i--;
        }
        return ret;
    }

    public void removeHorizontalSeam(int[] seam) {
        //SeamRemover.removeHorizontalSeam(pic, seam);
    }

    public void removeVerticalSeam(int[] seam) {
        //SeamRemover.removeVerticalSeam(pic, seam);
    }

    private static void navigate(Pixel pix, Pixel[][] ourpicture) {
        if (pix.x == 0 && pix.x == pix.pic.width() - 1
                && pix.y == 0 && pix.y == pix.pic.height() - 1) {
            pix.xneighbor.add(pix);
            pix.xneighbor.add(pix);
            pix.yneighbor.add(pix);
            pix.yneighbor.add(pix);
            return;
        }
        if (pix.x == pix.pic.width() - 1 && pix.y == 0 && pix.y == pix.pic.height() - 1) {
            pix.yneighbor.add(pix);
            pix.yneighbor.add(pix);
            pix.xneighbor.add(ourpicture[pix.x - 1][pix.y]);
            pix.xneighbor.add(ourpicture[0][pix.y]); //
            return;
        }
        if (pix.x == 0 && pix.y == 0 && pix.y == pix.pic.height() - 1) {
            pix.yneighbor.add(pix);
            pix.yneighbor.add(pix);
            pix.xneighbor.add(ourpicture[pix.x + 1][pix.y]);
            pix.xneighbor.add(ourpicture[pix.pic.width() - 1][pix.y]);
            return;
        }
        if (pix.x == 0 && pix.x == pix.pic.width() - 1 && pix.y == 0) {
            pix.xneighbor.add(pix);
            pix.xneighbor.add(pix);
            pix.yneighbor.add(ourpicture[pix.x][pix.y + 1]);
            pix.yneighbor.add(ourpicture[pix.x][pix.pic.height() - 1]);
            return;
        }
        if (pix.x == 0 && pix.x == pix.pic.width() - 1 && pix.y == pix.pic.height() - 1) {
            pix.xneighbor.add(pix);
            pix.xneighbor.add(pix);
            pix.yneighbor.add(ourpicture[pix.x][pix.y - 1]);
            pix.yneighbor.add(ourpicture[pix.x][0]);
            return;
        }
        if (pix.x == 0 && pix.y == 0) {
            pix.xneighbor.add(ourpicture[1][0]);
            pix.xneighbor.add(ourpicture[pix.pic.width() - 1][0]);
            pix.yneighbor.add(ourpicture[0][1]);
            pix.yneighbor.add(ourpicture[0][pix.pic.height() - 1]); // done //
            return;
        }
        if (pix.x == pix.pic.width() - 1 && pix.y == 0) {
            pix.yneighbor.add(ourpicture[pix.x][pix.y + 1]);
            pix.yneighbor.add(ourpicture[pix.x][pix.pic.height() - 1]);
            pix.xneighbor.add(ourpicture[pix.x - 1][pix.y]);
            pix.xneighbor.add(ourpicture[0][pix.y]);
            pix.xparents.add(ourpicture[pix.x - 1][pix.y]);
            pix.xparents.add(ourpicture[pix.x - 1][pix.y + 1]); // yy
            return;
        }
        if (pix.x == 0 && pix.y == pix.pic.height() - 1) {
            pix.yneighbor.add(ourpicture[0][pix.y - 1]);
            pix.yneighbor.add(ourpicture[0][0]);
            pix.xneighbor.add(ourpicture[pix.x + 1][pix.y]);
            pix.xneighbor.add(ourpicture[pix.pic.width() - 1][pix.y]); //
            pix.yparents.add(ourpicture[pix.x][pix.y - 1]);
            pix.yparents.add(ourpicture[pix.x + 1][pix.y - 1]); //
            return;
        }
        if (pix.x == pix.pic.width() - 1 && pix.y == pix.pic.height() - 1) {
            pix.yneighbor.add(ourpicture[pix.x][pix.y - 1]);
            pix.yneighbor.add(ourpicture[pix.x][0]); //
            pix.xneighbor.add(ourpicture[pix.x - 1][pix.y]);
            pix.xneighbor.add(ourpicture[0][pix.y]); //
            pix.yparents.add(ourpicture[pix.x - 1][pix.y - 1]);
            pix.yparents.add(ourpicture[pix.x][pix.y - 1]); //
            pix.xparents.add(ourpicture[pix.x - 1][pix.y - 1]);
            pix.xparents.add(ourpicture[pix.x - 1][pix.y]); //woo
            return;
        }
        if (pix.x == pix.pic.width() - 1) {
            pix.yneighbor.add(ourpicture[pix.x][pix.y - 1]);
            pix.yneighbor.add(ourpicture[pix.x][pix.y + 1]); //
            pix.xneighbor.add(ourpicture[pix.x - 1][pix.y]);
            pix.xneighbor.add(ourpicture[0][pix.y]); //
            pix.xparents.add(ourpicture[pix.x - 1][pix.y - 1]);
            pix.xparents.add(ourpicture[pix.x - 1][pix.y]);
            pix.xparents.add(ourpicture[pix.x - 1][pix.y + 1]);
            pix.yparents.add(ourpicture[pix.x - 1][pix.y - 1]);
            pix.yparents.add(ourpicture[pix.x][pix.y - 1]);
            return;
        }
        if (pix.x == 0) {
            pix.yneighbor.add(ourpicture[pix.x][pix.y - 1]);
            pix.yneighbor.add(ourpicture[pix.x][pix.y + 1]);
            pix.xneighbor.add(ourpicture[pix.x + 1][pix.y]);
            pix.xneighbor.add(ourpicture[pix.pic.width() - 1][pix.y]); //
            pix.yparents.add(ourpicture[pix.x][pix.y - 1]);
            pix.yparents.add(ourpicture[pix.x + 1][pix.y - 1]); //
            return;
        }
        if (pix.y == 0) {
            pix.xneighbor.add(ourpicture[pix.x - 1][pix.y]); // next
            pix.xneighbor.add(ourpicture[pix.x + 1][pix.y]); //
            pix.yneighbor.add(ourpicture[pix.x][pix.y + 1]);
            pix.yneighbor.add(ourpicture[pix.x][pix.pic.height() - 1]); //
            pix.xparents.add(ourpicture[pix.x - 1][pix.y]);
            pix.xparents.add(ourpicture[pix.x - 1][pix.y + 1]); //
            return;
        }
        if (pix.y == pix.pic.height() - 1) {
            pix.xneighbor.add(ourpicture[pix.x - 1][pix.y]);
            pix.xneighbor.add(ourpicture[pix.x + 1][pix.y]); //
            pix.yneighbor.add(ourpicture[pix.x][pix.y - 1]);
            pix.yneighbor.add(ourpicture[pix.x][0]); //
            pix.xparents.add(ourpicture[pix.x - 1][pix.y - 1]);
            pix.xparents.add(ourpicture[pix.x - 1][pix.y]); //
            pix.yparents.add(ourpicture[pix.x - 1][pix.y - 1]);
            pix.yparents.add(ourpicture[pix.x][pix.y - 1]);
            pix.yparents.add(ourpicture[pix.x + 1][pix.y - 1]);
            return;
        } else {
            pix.xneighbor.add(ourpicture[pix.x - 1][pix.y]);
            pix.xneighbor.add(ourpicture[pix.x + 1][pix.y]); //
            pix.yneighbor.add(ourpicture[pix.x][pix.y - 1]);
            pix.yneighbor.add(ourpicture[pix.x][pix.y + 1]); //
            pix.yparents.add(ourpicture[pix.x - 1][pix.y - 1]);
            pix.yparents.add(ourpicture[pix.x][pix.y - 1]); //
            pix.yparents.add(ourpicture[pix.x + 1][pix.y - 1]); //
            pix.xparents.add(ourpicture[pix.x - 1][pix.y - 1]); //
            pix.xparents.add(ourpicture[pix.x - 1][pix.y]);
            pix.xparents.add(ourpicture[pix.x - 1][pix.y + 1]); //
        }
    }

    private static void pixelenergy(Pixel p) {

        Pixel left = p.xneighbor.getFirst();
        Pixel right = p.xneighbor.getLast();
        double deltax = Math.pow((left.color.getRed() - right.color.getRed()), 2)
                + Math.pow((left.color.getGreen() - right.color.getGreen()), 2)
                + Math.pow((left.color.getBlue() - right.color.getBlue()), 2);
        Pixel above = p.yneighbor.getFirst();
        Pixel below = p.yneighbor.getLast();
        double deltay = Math.pow((above.color.getRed() - below.color.getRed()), 2)
                + Math.pow((above.color.getGreen() - below.color.getGreen()), 2)
                + Math.pow((above.color.getBlue() - below.color.getBlue()), 2);
        p.energy = deltax + deltay;
    }

    private static void findMx(Pixel p) {
        double min = Double.MAX_VALUE;
        Pixel smallest = null;
        if (p.xparents.isEmpty()) {
            p.mx = p.energy;
            return;
        }
        for (Pixel parent : p.xparents) {
            if (parent.mx < min) {
                min = parent.mx;
                smallest = parent;
            }
        }
        p.xparent = smallest;
        p.mx = p.energy + min;

    }

    private static void findMy(Pixel p) {
        double min = Double.MAX_VALUE;
        Pixel smallest = null;
        if (p.yparents.isEmpty()) {
            p.my = p.energy;
            return;
        }
        for (Pixel parent : p.yparents) {
            if (parent.my < min) {
                min = parent.my;
                smallest = parent;
            }
        }
        p.yparent = smallest;
        p.my = p.energy + min;
    }

    private Pixel[][] transpose(Pixel[][] given) {

        int gc = given.length;
        int gr = given[0].length;
        Pixel[][] ret = new Pixel[gr][gc];

        for (int i = 0; i < gr; i++) {
            for (int j = 0; j < gc; j++) {
                ret[i][j] = given[j][i];
            }
        }
        return ret;
    }

}
