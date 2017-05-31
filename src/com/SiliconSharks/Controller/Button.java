package com.SiliconSharks.Controller;

import net.java.games.input.*;

class Button extends GamepadComponent{
    private boolean ButtonState;
    Button(Component.Identifier identifier){this.identifier = identifier;}
    void setButtonstate(boolean state){ButtonState = state;}
    boolean getButtonState(){return ButtonState;}                         //  USE THIS TO GET BOOLEAN STATE
    void setValue(float value){

    }
}
