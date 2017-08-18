package com.SiliconSharks.Controller;

import com.SiliconSharks.ROVComponents.ROVStatus;
import net.java.games.input.*;

public class Gamepad {
    //This is a Logitech F310 implementation, edit the # of specific components and their identifiers when you plug them in for other controllers
    private final int NumComponents = 9;
    private Component.Identifier[] identifiers = {Component.Identifier.Button.A,
                                                  Component.Identifier.Button.B,
                                                  Component.Identifier.Button.X,
                                                  Component.Identifier.Button.Y,
                                                  Component.Identifier.Axis.POV,
                                                  Component.Identifier.Axis.X,
                                                  Component.Identifier.Axis.Y,
                                                  Component.Identifier.Axis.RX,
                                                  Component.Identifier.Axis.RY};
    private double[] values = new double[8];
    private Controller controller;
    Gamepad(){
        for(int i = 0; i < NumComponents; i++) values[i] = 0;
    }
    void setController(Controller controller){this.controller=controller;}
    boolean pollController(){
        if(isConnected()){
            try {
                controller.poll();
            } catch (Exception ex) {
                System.out.println("Trouble Polling Controller: Possible Disconnection");
                return false;
            }
            Component.Identifier eventIdentifier;
            EventQueue queue = controller.getEventQueue();
            Event event = new Event();
            while (queue.getNextEvent(event)) {
                StringBuilder buffer = new StringBuilder(controller.getName());
                buffer.append(" at ");
                buffer.append(event.getNanos()).append(", ");
                Component comp = event.getComponent();
                buffer.append(comp.getName()).append(" changed to ");
                float value = event.getValue();
                eventIdentifier = event.getComponent().getIdentifier();
                for(int i = 0; i < NumComponents; i++){
                    if(identifiers[i] == eventIdentifier){
                        values[i] = value;
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
    private boolean getButton(char c){
        switch (c){
            case 'A': return values[0] > 0.5;
            case 'B': return values[1] > 0.5;
            case 'X': return values[2] > 0.5;
            case 'Y': return values[3] > 0.5;
        }
        System.out.println("Error: Unhandled getButton() call in Gamepad.java");
        return false;
    }
    private double getAxis(String s){
        switch(s){
            case "LX": return values[5];
            case "LY": return values[6];
            case "RX": return values[7];
            case "RY": return values[8];
        }
        System.out.println("Error: Unhandled getAxis() call in Gamepad.java");
        return 0;
    }
    private String getDPad(){
        String s;
        if(values[4] == 0.125 || values[4] == 1 || values[4] == 0.875) s = "L";
        else if(values[4] == 0.25 || values[4] == 0 || values[4] == 0.75) s = "M";
        else s = "R";
        if(values[4] == 0.125 || values[4] == 0.25 || values[4] == 0.375) s += "U";
        else if(values[4] == 1 || values[4] == 0 || values[4] == 0.5) s += "M";
        else s = "D";
        return s;
    }
    void update(ROVStatus rovStatus){
        final int mode = 0;
        switch (mode){
            case 0: rovStatus.setThruster(0,getAxis("LY"));
                    rovStatus.setThruster(1,getAxis("LY"));
                    rovStatus.setThruster(1,getAxis("RY"));
                    rovStatus.setThruster(0,rovStatus.getThruster(0)+getAxis("LX"));
                    rovStatus.setThruster(1,rovStatus.getThruster(1)-getAxis("LX"));
                    int nServos = 0;
                    String DPad = getDPad();
                    if(DPad.charAt(0) == 'L'){
                        nServos++;
                        rovStatus.setServo(0,rovStatus.getServo(0)-0.01);
                    }else if(DPad.charAt(0) == 'R') {
                        nServos++;
                        rovStatus.setServo(0, rovStatus.getServo(0) + 0.01);
                    }
                    if(getButton('A') && !getButton('Y')){
                        nServos++;
                        rovStatus.setServo(1,1);
                    }
                    if(getButton('Y') && !getButton('A')){
                        nServos++;
                        rovStatus.setServo(1,-1);
                    }
                    rovStatus.calibrate(nServos);
                    break;
        }
    }
    boolean isConnected(){return (controller != null);}
}
