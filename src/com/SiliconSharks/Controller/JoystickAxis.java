package com.SiliconSharks.Controller;

import net.java.games.input.*;

class JoystickAxis extends GamepadComponent{
    private float value;
    JoystickAxis(Component.Identifier identifier){this.identifier = identifier;}
    void setValue(float value){this.value = value;}
    float getValue(){return value;}
}
