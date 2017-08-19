package com.SiliconSharks.ROVComponents;

public class ROVStatus {
    private final int numThrusters = 3, numServos = 3;
    private final double[] multipliers = {1,1,1,1,1,1};
    private double thrusters[] = new double[numThrusters];
    private double servos[] = new double[numServos];
    private double AmpScale = -1;
    private double Amperage = 0;
    public ROVStatus(){
        for (int i = 0; i < numThrusters; i++) thrusters[i] = 0;
        for (int i = 0; i < numServos; i++) servos[i] = 0;
    }
    public void setStatus(byte[] serialBytes){
        int index, tempInt, tempInt2 = 0;
        index = 0;
        for(byte b : serialBytes){
            tempInt = (int) b;
            if(tempInt < 0) tempInt += 256;
            if(index == 6){
                tempInt2 = tempInt;
            }else if(index == 7){
                Amperage = ((double)tempInt2 * 255 + tempInt)*50.15/1024; // The Arduino measures voltage from a 0.1 Ohm
            }else if(index >= 3){                                        // Shunt and finds the amperage
                servos[index-numThrusters] = ((tempInt-90)*multipliers[index]/90);
            }else{
                thrusters[index] = ((tempInt-125)*multipliers[index]/125);
            }
            index++;
        }
    }
    private double trim(double a){
        a = (a > 1) ? 1: a;
        a = (a < -1) ? -1: a;
        return a;
    }
    public void calibrate(int nServos){
        //this function is designed for a 25 Amp cap, 23 for safety leeway
        int AmpCap = 23-nServos;
        int AmpExp = (int)((thrusters[0]+thrusters[1]+thrusters[2])*25.0);
        if(AmpExp > AmpCap) {
            AmpScale = ((double) AmpCap / AmpExp);
            thrusters[0] *= AmpScale;
            thrusters[1] *= AmpScale;
            thrusters[2] *= AmpScale;
        }else{
            AmpScale = -1;
        }
    }
    public byte[] getStatus(){
        byte[] serialBytes= new byte[7];
        serialBytes[0] = -1;
        for(int i = 0; i < numThrusters + numServos; i++){
            if(i < numThrusters) {
                serialBytes[i + 1] = (byte)(thrusters[i]*125*multipliers[i]+125);
            }else{
                serialBytes[i + 1] = (byte)(servos[i-numThrusters]*90*multipliers[i]+90);
            }
        }
        return serialBytes;
    }
    public void setThruster(int index, double value){thrusters[index] = trim(value);}
    public void setServo(int index, double value){servos[index] = trim(value);}
    public double getThruster(int index){return thrusters[index];}
    public double getServo(int index){return servos[index];}
    public double getAmperage(){return Amperage;}
    public double getAmpScale(){return AmpScale;}
}