package com.SiliconSharks.Controller;

import net.java.games.input.*;

public class Gamepad {
    //This is a Logitech F310 implementation, edit the # of specific components and their identifiers when you plug them in for other controllers
    private final int numButtons = 4;
    private final int numDPad = 1;
    private final int numJoystick = 2;
    private Button[] Buttons = {new Button(Component.Identifier.Button.A),
                                new Button(Component.Identifier.Button.B),
                                new Button(Component.Identifier.Button.X),
                                new Button(Component.Identifier.Button.Y)};
    private DPad[] DPads;
    private Joystick[] Joysticks;
    private GamepadComponent[] GamepadComponents = new GamepadComponent[numButtons+numDPad+numJoystick];
    private Controller controller;
    public Gamepad(Controller controller){
        this.controller = controller;
        int counter = 0;
        for(Button button: Buttons){GamepadComponents[counter++] = button;}
        for(DPad dPad: DPads){GamepadComponents[counter++] = dPad;}
        for(Joystick joystick: Joysticks){GamepadComponents[counter++] = joystick;}
    }
    public void pollController(){
        if(controller != null){
            
        }
    }
}
