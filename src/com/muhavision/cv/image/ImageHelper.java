package com.muhavision.cv.image;

import java.awt.*;

import javax.swing.JFrame;

/**
 * Created by ziga on 20.8.2014.
 */
public class ImageHelper {

    public static ImageData getScaledData(JFrame frame){
        Dimension dimension = frame.getSize();


        float h = 480;
        float w = 640;
        float sw = dimension.width;
        float sh = dimension.height;

        float ratio = h / w;
        float sratio = sh / sw;


        ImageData result = new ImageData();

        if(ratio == sratio){
            result.x = 0;
            result.y = 0;
            result.h = (int) sh;
            result.w = (int) sw;
        }else if (ratio > sratio){
            result.y = 0;
            result.h = (int) sh;
            result.w = Math.round((sh / h) * w);
            result.x = Math.round((sw - result.w) / 2);
        }else {
            result.x = 0;
            result.w = (int) sw;
            result.h = Math.round((sw / w) * h);
            result.y = Math.round((sh - result.h) / 2);
        }

        return new ImageData();

    }
}
