package com.SiliconSharks.ROVComponents;

public class Thruster {
    private double Throttle;
    public Thruster() {Throttle = 0;}
    void fromSerial(int serial){
        Throttle = (double) ((serial-90)/90);
    }
    public double getThrottle(){return Throttle;}
}
