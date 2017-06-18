package com.SiliconSharks.Controller;

import com.SiliconSharks.ROVComponents.ROVStatus;
import net.java.games.input.*;

public class Gamepad {
    //This is a Logitech F310 implementation, edit the # of specific components and their identifiers when you plug them in for other controllers
    private final int numCButtons = 4;
    private final int numDPad = 1;
    private final int numJoystick = 2;

    private GamepadComponent[] GamepadComponents = new GamepadComponent[numCButtons+numDPad+2*numJoystick];
    private Controller controller;
    Gamepad(){
        CButton[] CButtons = {new CButton(Component.Identifier.Button.A),
                              new CButton(Component.Identifier.Button.B),
                              new CButton(Component.Identifier.Button.X),
                              new CButton(Component.Identifier.Button.Y)};
        DPad[] DPads = {new DPad(Component.Identifier.Axis.POV)};
        Joystick[] Joysticks = {new Joystick(Component.Identifier.Axis.X, Component.Identifier.Axis.Y),
                                new Joystick(Component.Identifier.Axis.RX, Component.Identifier.Axis.RY)};
        int counter = 0;
        for(CButton cButton: CButtons){GamepadComponents[counter++] = cButton;}
        for(DPad dPad: DPads){GamepadComponents[counter++] = dPad;}
        for(Joystick joystick: Joysticks){
            GamepadComponents[counter++] = joystick.getXAxis();
            GamepadComponents[counter++] = joystick.getYAxis();
        }
        assert(counter == numCButtons+numDPad+2*numJoystick);
    }
    void setController(Controller controller){this.controller=controller;}
    public GamepadComponent getGamepadComponent(Component.Identifier identifier) {
        for(GamepadComponent gamepadComponent : GamepadComponents){
            if(gamepadComponent.getIdentifier() == identifier){
                return gamepadComponent;
            }
        }
        return null;
    }
    ROVStatus getROVStatus(){
        ROVStatus rovStatus = new ROVStatus();
        GamepadComponent gamepadComponent;
        JoystickAxis joystickAxis;
        gamepadComponent = getGamepadComponent(Component.Identifier.Axis.Y);
        if(gamepadComponent instanceof JoystickAxis) {
            joystickAxis = (JoystickAxis) gamepadComponent;
            rovStatus.setThrusters(0,joystickAxis.getValue());
            rovStatus.setThrusters(1,joystickAxis.getValue());
        }
        return rovStatus;
    }
    boolean pollController(){
        if(isConnected()){
            try {
                controller.poll();
            } catch (Exception ex) {
                System.out.println("Trouble Polling Controller: Possible Disconnection");
                return false;
            }
            EventQueue queue = controller.getEventQueue();
            Event event = new Event();
            while (queue.getNextEvent(event)) {
                StringBuilder buffer = new StringBuilder(controller.getName());
                buffer.append(" at ");
                buffer.append(event.getNanos()).append(", ");
                Component comp = event.getComponent();
                buffer.append(comp.getName()).append(" changed to ");
                float value = event.getValue();
                for (GamepadComponent gamepadComponent : GamepadComponents) {
                    if(gamepadComponent.getIdentifier() == event.getComponent().getIdentifier()){
                        gamepadComponent.setValue(value);
                        break;
                    }
                }
                System.out.println(buffer);
            }
            return true;
        }else{
            return true;
        }
    }
    public boolean isConnected(){return (controller != null);}
}
