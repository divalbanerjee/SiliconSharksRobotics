package com.SiliconSharks.ROVComponents;

public class Thruster {
    private double Throttle;
    public Thruster() {Throttle = 0;}
    void fromSerial(double multiplier, int serial){
        Throttle = ((serial-90)/90*multiplier);
    }
    public double getThrottle(){return Throttle;}
}
