package com.SiliconSharks.Controller;

import net.java.games.input.*;
abstract class GamepadComponent {
    Component.Identifier identifier;
    Component.Identifier getIdentifier(){return identifier;}
    abstract void setValue(float value);
}
