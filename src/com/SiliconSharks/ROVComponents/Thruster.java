package com.SiliconSharks.ROVComponents;

public class Thruster {
    double Throttle;
    public Thruster(){
        Throttle = 0;
    }
    public void fromSerial(byte SerialByte){

    }
    public double getThrottle(){return Throttle;}
}
