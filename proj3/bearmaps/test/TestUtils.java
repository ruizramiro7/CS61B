package bearmaps.test;
import bearmaps.utils.Constants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestUtils {
    @Test
    public void testGetDepth() {
        System.out.println((Constants.ROOT_LRLON - Constants.ROOT_ULLON) / Constants.TILE_SIZE);
        assertEquals(2, getDepth(0.000161661376953125));
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
