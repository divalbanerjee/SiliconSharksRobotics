package com.SiliconSharks.Controller;

import net.java.games.input.*;
import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("unchecked")

public class GamepadCommunications {
    private Gamepad gamepad = new Gamepad(null);
    private boolean TimerTaskRunning = false;
    private int AttemptCommunicationsCounter = 0;
    private final int AttemptCommunicationsCycleLength = 10;
    public GamepadCommunications(){
        AttemptConnection();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(!TimerTaskRunning) {
                    TimerTaskRunning = true;
                    if (gamepad.isConnected()) {
                        gamepad.pollController();
                    } else {
                        AttemptCommunicationsCounter++;
                        if(AttemptCommunicationsCounter >= AttemptCommunicationsCycleLength) {
                            AttemptCommunicationsCounter = 0;
                            AttemptConnection();
                        }
                    }
                    TimerTaskRunning = false;
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask,1000,40);
    }
    private void AttemptConnection(){
        try{
            ControllerEnvironment controllerEnvironment = createDefaultEnvironment();
            Controller[] controllers = controllerEnvironment.getControllers();
            for(Controller controller : controllers){
                if(controller.getType() == Controller.Type.GAMEPAD){
                    gamepad = new Gamepad(controller);
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
