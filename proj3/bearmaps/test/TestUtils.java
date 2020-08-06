package bearmaps.test;
import bearmaps.utils.Constants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestUtils {
    @Test
    public void test() {
        getRenderGrid(0, 0, 3, 3, 0);
    }

    private String[][] getRenderGrid(int ullon, int ullat, int lrlon, int lrlat, int depth) {
        int lonSize = lrlon - ullon;
        int latSize = lrlat - ullat;
        String[][] grid = new String[lonSize][latSize];
        for (int lon = ullon; lon < lrlon; ++lon) {
            for (int lat = ullat; lat < lrlat; ++lat) {
                grid[lon][lat] = "d" + depth + "_x" + lat + "_y" + lon + ".png";
                System.out.println(grid[lon][lat]);
            }
        }
        return grid;
    }

    private double getTileSize(int depth) {
        double tileSize = (Constants.ROOT_LRLON - Constants.ROOT_ULLON)
                / Math.pow(2, depth);
        return tileSize;
    }

    private int getDepth(double lonDPP) {
        double num = (Constants.ROOT_LRLON - Constants.ROOT_ULLON)
                / (double) Constants.TILE_SIZE / lonDPP;
        return Math.min((int) Math.ceil(Math.log(num) / Math.log(2)), 7);
    }

    private double lonDPP(double lrlon, double ullon, double widthOfImage) {
        return (lrlon - ullon)  / widthOfImage;
    }
}
