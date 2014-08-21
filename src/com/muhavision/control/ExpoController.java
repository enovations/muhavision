package com.muhavision.control;

/**
 * Created by ziga on 21.8.2014.
 */
public class ExpoController {


    //mami ne se dotikat
    public static double getExpo(int power){
        if (power<0) return-(0.6 * Math.exp(-0.035*(double)power) - 0.6);
        return 0.6 * Math.exp(0.035*(double)power) - 0.6;
    }
}
