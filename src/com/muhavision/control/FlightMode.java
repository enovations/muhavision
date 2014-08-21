package com.muhavision.control;

/**
 * Created by ziga on 21.8.2014.
 */
public class FlightMode {

    public enum eMode {

        NORMAL_MODE(0),
        MUHA_MODE(1),
        TAG_MODE(2);

        int mode = 0;

        eMode(int mode){
            this.mode = mode;
        }

    }

    private eMode mode = eMode.NORMAL_MODE;

    public eMode getMode(){
        return mode;
    }

    public void setMode(eMode mode){
        this.mode = mode;
        mode.toString();
    }




}
