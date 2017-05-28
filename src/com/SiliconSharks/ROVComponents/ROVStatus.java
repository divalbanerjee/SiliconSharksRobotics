package com.SiliconSharks.ROVComponents;

/**
 * Created by richard on 5/27/17.
 */
public class ROVStatus {
    Thruster[] Thrusters;
    Servo[] Servos;
    public ROVStatus(){
        Thrusters = new Thruster[3];
        Servos = new Servo[3];
    }
    public void setStatus(byte[] serialBytes){

    }
}