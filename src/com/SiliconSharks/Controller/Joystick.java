package com.SiliconSharks.Controller;

import net.java.games.input.*;

class Joystick{
    private JoystickAxis XAxis,YAxis;
    Joystick(Component.Identifier XAxisIdentifier, Component.Identifier YAxisIdentifier){
        XAxis = new JoystickAxis(XAxisIdentifier);
        YAxis = new JoystickAxis(YAxisIdentifier);
    }
    JoystickAxis getXAxis() {return XAxis;}
    JoystickAxis getYAxis() {return YAxis;}
}