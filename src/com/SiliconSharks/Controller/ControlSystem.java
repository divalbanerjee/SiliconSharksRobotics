package com.SiliconSharks.Controller;

import com.SiliconSharks.ROVComponents.ROVStatus;
import net.java.games.input.*;

import java.lang.reflect.Constructor;

import static com.SiliconSharks.Main.Message;
import static com.SiliconSharks.Main.getStackTrace;
@SuppressWarnings("unchecked")

public class ControlSystem {
    private Gamepad[] gamepads;
    private boolean TimerTaskRunning = false;
    public ControlSystem(int SetupNum){
        switch (SetupNum){
            case 0:{
                // GUI and Keyboard testing only, no gamepads expected;
                gamepads = new Gamepad[0];
            }case 1:{
                // One controller expected, testing purposes
                gamepads = new Gamepad[]{new Gamepad(0)};
            }case 2:{
                // Practice and Competition use, 2 controllers
                gamepads = new Gamepad[]{new Gamepad(1), new Gamepad(2)};
            }
        }
    }
    public ControlSystem(){this(1);}
    private CustomKeyboard customKeyboard = new CustomKeyboard();
    private ROVStatus currentROVStatus = new ROVStatus();
    public Gamepad getGamepad(int index) {return gamepads[index];}
    public void timerRefresh(){
        customKeyboard.TimerRefresh();
        if(!TimerTaskRunning) {
            TimerTaskRunning = true;
            for(Gamepad gamepad: gamepads) {
                if (gamepad.isConnected()) {
                    if (!gamepad.pollController()) {
                        gamepad.setController(null);
                        Message(0, "Error polling controller, disconnecting...");
                    } else {
                        gamepad.update(currentROVStatus);
                    }
                } else {
                    int ConnectionCounter = gamepad.getConnectionCounter();
                    if (ConnectionCounter < 45) {
                        ConnectionCounter++;
                        if (ConnectionCounter % 10 == 0) {
                            AttemptConnection(gamepad);
                        }
                    }
                }
            }
            TimerTaskRunning = false;
        }
    }
    private void AttemptConnection(Gamepad gamepad){
        try{
            Message(0,"Attempting connection...");
            ControllerEnvironment controllerEnvironment = createDefaultEnvironment();
            Controller[] controllers = controllerEnvironment.getControllers();
            Message(0,"Found " + controllers.length + " controllers, scanning...");
            for(Controller controller : controllers){
                if(controller.getType() == Controller.Type.GAMEPAD){
                    for(Gamepad gamepad1 : gamepads){
                        if(gamepad.getController().getName().equals(gamepad1.getController().getName()) &&
                                gamepad.getController().getPortNumber()==gamepad1.getController().getPortNumber()){
                            return;
                        }
                    }
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
