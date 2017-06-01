package com.SiliconSharks.Controller;

import net.java.games.input.*;

public class JoystickAxis extends GamepadComponent{
    private float value;
    JoystickAxis(Component.Identifier identifier){this.identifier = identifier;}
    void setValue(float value){this.value = value;}
    public float getValue(){return value;}
}
