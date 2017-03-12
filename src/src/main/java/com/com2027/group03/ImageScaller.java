package com.com2027.group03;

/**
 * Created by Matus on 12-Mar-17.
 */
public class ImageScaller {
    public static int[] contain(int imgwidth, int imgheight, int maxwidth, int maxheight) {
        float frameAspect = imgheight / (float)(imgwidth);
        int[] ret = {0, 0, 0, 0};

        if (maxwidth*frameAspect <= maxheight) {
            ret[2] = maxwidth;
            ret[3] = (int)(maxwidth*frameAspect);
            ret[0] = 0;
            ret[1] = (maxheight - ret[3]) / 2;

        }
        else {
            ret[2] = (int)(maxheight / frameAspect);
            ret[3] = maxheight;
            ret[0] = (maxwidth - ret[2]) / 2;
            ret[1] = 0;
        }

        return ret;
    }

    public static int[] cover(int imgwidth, int imgheight, int maxwidth, int maxheight) {
        float imgAspect = imgheight / (float)(imgwidth);
        int[] ret = {0, 0, 0, 0};

        // Target area is vertical
        if (maxheight / (float)(maxwidth) >= 1.0f) {
            // Image is vertical
            if (imgAspect >= 1.0f) {
                ret[2] = (int)(maxheight / imgAspect);
                ret[3] = maxheight;
                if (ret[2] < maxwidth) {
                    ret[2] = maxwidth;
                    ret[3] = (int)(maxwidth*imgAspect);
                }
                // Image is horizontal
            }
            else {
                ret[2] = (int)(maxheight / imgAspect);
                ret[3] = maxheight;
            }
            // Target area is horizontal
        }
        else {
            // Image is vertical
            if (imgAspect >= 1.0f) {
                ret[2] = maxwidth;
                ret[3] = (int)(maxwidth*imgAspect);
                // Image is horizontal
            }
            else {
                ret[2] = maxwidth;
                ret[3] = (int)(maxwidth*imgAspect);
                if (ret[3] < maxheight) {
                    ret[2] = (int)(maxheight / imgAspect);
                    ret[3] = maxheight;
                }
            }
        }
        ret[0] = (ret[2] - maxwidth) / -2;
        ret[1] = (ret[3] - maxheight) / -2;
        return ret;
    }
}
