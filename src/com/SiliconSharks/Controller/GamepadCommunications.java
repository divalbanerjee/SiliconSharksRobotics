package com.SiliconSharks.Controller;

import net.java.games.input.*;
import java.lang.reflect.Constructor;

@SuppressWarnings("unchecked")

public class GamepadCommunications {
    private Gamepad gamepad = new Gamepad();
    private boolean TimerTaskRunning = false;
    private int AttemptCommunicationsCounter = 0;
    public GamepadCommunications(){
        AttemptConnection();
    }
    public Gamepad getGamepad(){return gamepad;}
    public void timerRefresh(){
        if(!TimerTaskRunning) {
            TimerTaskRunning = true;
            if (gamepad.isConnected()) {
                if(!gamepad.pollController()){
                    gamepad.setController(null);
                }
            } else {
                AttemptCommunicationsCounter++;
                if(AttemptCommunicationsCounter >= 10) {
                    AttemptCommunicationsCounter = 0;
                    AttemptConnection();
                }
            }
            TimerTaskRunning = false;
        }
    }
    private void AttemptConnection(){
        try{
            ControllerEnvironment controllerEnvironment = createDefaultEnvironment();
            Controller[] controllers = controllerEnvironment.getControllers();
            for(Controller controller : controllers){
                if(controller.getType() == Controller.Type.GAMEPAD){
                    gamepad.setController(controller);
                    break;
                }
            }
        }catch(ReflectiveOperationException ex){
            ex.printStackTrace();
        }
    }
    private static ControllerEnvironment createDefaultEnvironment() throws ReflectiveOperationException {

        // Find constructor (class is package private, so we can't access it directly)
        Constructor<ControllerEnvironment> constructor = (Constructor<ControllerEnvironment>)
                Class.forName("net.java.games.input.DefaultControllerEnvironment").getDeclaredConstructors()[0];

        // Constructor is package private, so we have to deactivate access control checks
        constructor.setAccessible(true);

        // Create object with default constructor
        return constructor.newInstance();
    }

}
