package com.SiliconSharks.ROVComponents;

public class ROVStatus {
    private Thruster[] Thrusters;
    private Servo[] Servos;
    private double[] multipliers = {1,1,1,1,1,1};
    public ROVStatus(){
        Thrusters = new Thruster[3];
        Servos = new Servo[3];
    }
    public void setStatus(byte[] serialBytes){
        int index, tempInt;
        index = 0;
        for(byte b : serialBytes){
            tempInt = (int) b;
            if(tempInt < 0){
                tempInt += 256;
            }
            double value = ((tempInt-90)*multipliers[index]/90);
            if(index >= 3){
                Servos[index-3].setRotation(value);
            }else{
                Thrusters[index].setThrottle(value);
            }
            index++;
        }
    }
    public void setThrusters(int index, double throttle){Thrusters[index].setThrottle(throttle);}
    public void setServos(int index, double rotation){Servos[index].setRotation(rotation);}
}