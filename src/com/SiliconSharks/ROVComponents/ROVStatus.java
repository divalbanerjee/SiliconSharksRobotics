package com.SiliconSharks.ROVComponents;

public class ROVStatus {
    private Thruster[] Thrusters;
    private Servo[] Servos;
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
            switch(index){
                case 0: Thrusters[0].fromSerial(tempInt);
                case 1: Thrusters[1].fromSerial(tempInt);
                case 2: Thrusters[2].fromSerial(tempInt);
                case 3: Servos[0].fromSerial(tempInt);
                case 4: Servos[1].fromSerial(tempInt);
                case 5: Servos[2].fromSerial(tempInt);
            }
            index++;
        }
    }
}