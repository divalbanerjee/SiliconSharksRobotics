package com.SiliconSharks.ROVComponents;

/**
 * Created by richard on 5/27/17.
 */
public class Thruster {
    double Throttle;
    public Thruster(){
        Throttle = 0;
    }
    public void fromSerial(byte SerialByte){

    }
    public double getThrottle(){return Throttle;}
}
