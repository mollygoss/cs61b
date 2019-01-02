import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

/**
 * Created by molly on 4/26/17.
 */
public class SeamCarver {

    private Picture pic;
    private Double[][] energyarray;

    public SeamCarver(Picture picture) {
        pic = new Picture(picture);
        energyarray = new Double[height()][width()];
        for (int i = 0; i < pic.height(); i++) {
            for (int j = 0; j < pic.width(); j++) {
                energyarray[i][j] = energy(j, i);
            }
        }
    }
    public Picture picture() {
        Picture copy = new Picture(pic);
        return copy;
    }
    public int width() {
        return pic.width();
    }
    public int height() {
        return pic.height();
    }
    public double energy(int x, int y) {
        Color rightcolor = null;
        Color leftcolor = null;

        if (x - 1 < 0) {
            leftcolor = pic.get(pic.width() - 1, y);
            if (x + 1 < pic.width()) {
                rightcolor = pic.get(x + 1, y);
            }
        }
        if (x + 1 > pic.width() - 1) {
            rightcolor = pic.get(0, y);
            if (x - 1 > -1) {
                leftcolor = pic.get(x - 1, y);
            }
        }
        if (x + 1 < pic.width() && x - 1 > -1) {
            leftcolor = pic.get(x - 1, y);
            rightcolor = pic.get(x + 1, y);
        }

        double deltax = Math.pow((leftcolor.getRed() - rightcolor.getRed()), 2)
                + Math.pow((leftcolor.getGreen() - rightcolor.getGreen()), 2)
                + Math.pow((leftcolor.getBlue() - rightcolor.getBlue()), 2);

        Color abovecolor = null;
        Color belowcolor = null;
        if (y - 1 < 0) {
            abovecolor = pic.get(x, pic.height() - 1);
            if (y + 1 < pic.height()) {
                belowcolor = pic.get(x, y + 1);
            }
        }
        if (y + 1 > pic.height() - 1) {
            belowcolor = pic.get(x, 0);
            if (y - 1 > -1) {
                abovecolor = pic.get(x, y - 1);
            }
        }
        if (y + 1 < pic.height() && y - 1 > -1) {
            abovecolor = pic.get(x, y - 1);
            belowcolor = pic.get(x, y + 1);
        }

        double deltay = Math.pow((abovecolor.getRed() - belowcolor.getRed()), 2)
                + Math.pow((abovecolor.getGreen() - belowcolor.getGreen()), 2)
                + Math.pow((abovecolor.getBlue() - belowcolor.getBlue()), 2);

        return deltax + deltay;
    }

    public int[] findHorizontalSeam() {
        energyarray = transpose(energyarray);
        int[] result = findVerticalSeam();
        transpose(energyarray);
        return result;

    }
    public int[] findVerticalSeam() {
        Double[][] matrix = new Double[energyarray.length][energyarray[0].length];
        Integer[][] parentx = new Integer[energyarray.length][energyarray[0].length];
        for (int k = 0; k < energyarray[0].length; k++) {
            matrix[0][k] = energyarray[0][k];
        }
        for (int i = 1; i < energyarray.length; i++) {
            for (int j = 0; j < energyarray[0].length; j++) {
                if (j - 1 < 0) {
                    if (j + 1 > energyarray[0].length - 1) {
                        matrix[i][j] = energyarray[i][j] + matrix[i - 1][j];
                        parentx[i][j] = j;
                    } else {
                        matrix[i][j] = energyarray[i][j]
                                + Math.min(matrix[i - 1][j], matrix[i - 1][j + 1]);
                        if (matrix[i - 1][j] == matrix[i][j] - energyarray[i][j]) {
                            parentx[i][j] = j;
                        }
                        if (matrix[i - 1][j + 1] == matrix[i][j] - energyarray[i][j]) {
                            parentx[i][j] = j + 1;
                        }
                    }
                }
                if (j + 1 > energyarray[0].length - 1 && j - 1 > -1) {
                    matrix[i][j] = energyarray[i][j]
                            + Math.min(matrix[i - 1][j - 1], matrix[i - 1][j]);
                    if (matrix[i - 1][j] == matrix[i][j] - energyarray[i][j]) {
                        parentx[i][j] = j;
                    }
                    if (matrix[i - 1][j - 1] == matrix[i][j] - energyarray[i][j]) {
                        parentx[i][j] = j - 1;
                    }
                }
                if (j + 1 < energyarray[0].length && j - 1 > -1) {
                    matrix[i][j] = energyarray[i][j]
                            + min(matrix[i - 1][j - 1], matrix[i - 1][j], matrix[i - 1][j + 1]);
                    if (matrix[i - 1][j] == matrix[i][j] - energyarray[i][j]) {
                        parentx[i][j] = j;
                    }
                    if (matrix[i - 1][j - 1] == matrix[i][j] - energyarray[i][j]) {
                        parentx[i][j] = j - 1;
                    }
                    if (matrix[i - 1][j + 1] == matrix[i][j] - energyarray[i][j]) {
                        parentx[i][j] = j + 1;
                    }
                }
            }
        }
        int goalx = 0;
        int goaly = energyarray.length - 1;
        double min = Double.POSITIVE_INFINITY;
        for (int m = 0; m < energyarray[0].length; m++) {
            if (matrix[energyarray.length - 1][m] < min) {
                min = matrix[energyarray.length - 1][m];
                goalx = m;
            }
        }
        int newx = 0;
        int[] ret = new int[energyarray.length];
        int i = energyarray.length - 1;
        while (parentx[goaly][goalx] != null) {
            ret[i] = goalx;
            newx = parentx[goaly][goalx];
            goaly = goaly - 1;
            goalx = newx;
            i--;
        }
        ret[0] = goalx;
        return ret;
    }
    public void removeHorizontalSeam(int[] seam) {
    }
    public void removeVerticalSeam(int[] seam) {
    }
    private Double[][] transpose(Double[][] given) {

        int gc = given.length;
        int gr = given[0].length;
        Double[][] ret = new Double[gr][gc];

        for (int i = 0; i < gr; i++) {
            for (int j = 0; j < gc; j++) {
                ret[i][j] = given[j][i];
            }
        }
        return ret;
    }
    private static double min(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }
}
