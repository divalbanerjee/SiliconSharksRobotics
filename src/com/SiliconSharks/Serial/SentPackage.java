package com.SiliconSharks.Serial;

import com.SiliconSharks.Controller.*;
import net.java.games.input.*;

class SentPackage {
    private double[] multipliers = {1, -1, 1,1,1,1};
    private byte[] serialBytes;
    SentPackage(Gamepad gamepad){
        serialBytes = new byte[10];
        GamepadComponent someGamepadComponent = gamepad.getGamepadComponent(Component.Identifier.Axis.Y);
        if(someGamepadComponent instanceof JoystickAxis){
            JoystickAxis YAxis = (JoystickAxis) someGamepadComponent;
            serialBytes[0] = fromDouble(YAxis.getValue(),0);
            serialBytes[1] = fromDouble(YAxis.getValue(),1);
        }else{
            System.out.print("Error retrieving component: Y Axis of First Joystick");
        }
        someGamepadComponent = gamepad.getGamepadComponent(Component.Identifier.Axis.RY);
        if(someGamepadComponent instanceof JoystickAxis){
            JoystickAxis YAxis = (JoystickAxis) someGamepadComponent;
            serialBytes[2] = fromDouble(YAxis.getValue(),2);
        }else{
            System.out.print("Error retrieving component: Y Axis of First Joystick");
        }
    }
    private byte fromDouble(double a, int index){
        int b = (int)(a*90*multipliers[index]+90); // WARNING : Do not set a*90 to a*91 or more or it will break serial
        if(b>=128){
            b = -1*(256-b);
            return ((byte) b);
        }else{
            return ((byte)b);
        }
    }
    byte[] getSerialBytes(){return serialBytes;}
}
