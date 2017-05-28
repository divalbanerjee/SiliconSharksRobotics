package com.SiliconSharks.ROVComponents;

/**
 * Created by richard on 5/27/17.
 */
public class Servo {
    private double rotation;
    public Servo(){
        rotation = 0;
    }
    void fromSerial(int serial){
        rotation = (double) ((serial-90)/90);
    }
    double getRotation(){return rotation;}
}
