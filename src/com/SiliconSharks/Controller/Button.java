package com.SiliconSharks.Controller;

import net.java.games.input.*;
public class Button extends GamepadComponent{
    private boolean ButtonState;
    public Button(Component.Identifier identifier){this.identifier = identifier;}
    void setButtonstate(boolean state){ButtonState = state;}
    boolean getButtonState(){return ButtonState;}
}
