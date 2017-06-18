package com.SiliconSharks.Controller;

import net.java.games.input.*;

class DPad extends GamepadComponent{
    private int DPadValue;
    DPad(Component.Identifier identifier){this.identifier = identifier;DPadValue=0;}
    void setValue(float value){
        DPadValue = (int) (8*value+0.5);
    }
    int getDPadValue(){return DPadValue;}
}
