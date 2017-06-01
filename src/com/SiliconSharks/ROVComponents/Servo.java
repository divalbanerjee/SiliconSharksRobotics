package com.SiliconSharks.ROVComponents;

/**
 * Created by richard on 5/27/17.
 */
public class Servo {
    private double rotation;
    public Servo(){rotation = 0;}
    void fromSerial(double multiplier, int serial){
        rotation = ((serial-90)/90*multiplier);
    }
    double getRotation(){return rotation;}
}
