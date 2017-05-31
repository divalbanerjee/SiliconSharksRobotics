package com.SiliconSharks.Serial;

import com.SiliconSharks.Controller.Gamepad;

public class SentPackage {
    private double[] multipliers;
    private byte[] serialBytes;
    public SentPackage(double[] multipliers, Gamepad gamepad){
        this.multipliers = multipliers;
        serialBytes = new byte[10];

    }
    private byte fromDouble(double a, int index){
        int b = (int)(a*63*multipliers[index]+90); // WARNING : Do not set a*65 to a*90 or more or it will break serial
        if(b>=128){
            b = -1*(256-b);
            return ((byte) b);
        }else{
            return ((byte)b);
        }
    }
    public byte[] getSerialBytes(){return  serialBytes;}
}
