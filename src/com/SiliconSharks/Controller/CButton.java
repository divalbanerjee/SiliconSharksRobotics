package com.SiliconSharks.Controller;

import net.java.games.input.*;

public class CButton extends GamepadComponent{
    private boolean ButtonState;
    CButton(Component.Identifier identifier){this.identifier = identifier;ButtonState = false;}
    void setValue(float value) {ButtonState = (value > 0.5f);}
    boolean getButtonState(){return ButtonState;}
}
