package com.SiliconSharks.Controller;

import com.SiliconSharks.ROVComponents.ROVStatus;
import net.java.games.input.*;

import java.lang.reflect.Constructor;

import static com.SiliconSharks.Main.Message;
import static com.SiliconSharks.Main.getStackTrace;
@SuppressWarnings("unchecked")

public class ControlSystem {
    private Gamepad gamepad = new Gamepad();
    private boolean TimerTaskRunning = false;
    private int AttemptCommunicationsCounter = 0;
    public ControlSystem(){   }
    private CustomKeyboard customKeyboard = new CustomKeyboard();
    private ROVStatus currentROVStatus = new ROVStatus();
    public Gamepad getGamepad() {return gamepad;}
    public void timerRefresh(){
        customKeyboard.TimerRefresh();
        if(!TimerTaskRunning) {
            TimerTaskRunning = true;
            if (gamepad.isConnected()) {
                if(!gamepad.pollController()){
                    gamepad.setController(null);
                    Message(0,"Error polling controller, disconnecting...");
                }else{
                    gamepad.update(currentROVStatus);
                }
            } else {
                if(AttemptCommunicationsCounter < 45){
                    AttemptCommunicationsCounter++;
                    if(AttemptCommunicationsCounter % 10 == 0) {
                        AttemptConnection();
                    }
                }else{
                    customKeyboard.update(currentROVStatus);
                }
            }
            TimerTaskRunning = false;
        }
    }
    private void AttemptConnection(){
        try{
            Message(0,"Attempting connection...");
            ControllerEnvironment controllerEnvironment = createDefaultEnvironment();
            Controller[] controllers = controllerEnvironment.getControllers();
            Message(0,"Found " + controllers.length + " controllers, scanning...");
            for(Controller controller : controllers){
                if(controller.getType() == Controller.Type.GAMEPAD){
                    Message(0,"Found gamepad: "+ controller.getName());
                    gamepad.setController(controller);
                    break;
                }
            }
        }catch(ReflectiveOperationException ex){
            Message(0,getStackTrace(ex));
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
    public byte[] getSerialBytes(){
        return currentROVStatus.getStatus();
    }
    public ROVStatus getCurrentROVStatus() {
        return currentROVStatus;
    }
}
