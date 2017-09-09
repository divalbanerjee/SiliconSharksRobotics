package com.SiliconSharks.Controller;

import com.SiliconSharks.ROVComponents.ROVStatus;
import com.SiliconSharks.Settings;
import net.java.games.input.*;

import java.lang.reflect.Constructor;

import static com.SiliconSharks.MainUpdateLoop.Message;
import static com.SiliconSharks.MainUpdateLoop.getStackTrace;
@SuppressWarnings("unchecked")

public class ControlSystem {
    private static int KeyboardRefreshCounter = 0;
    private static Gamepad[] gamepads;
    public ControlSystem(){}
    public static void start(){
        CustomKeyboard.start();
        switch (Settings.getSetting("NumGamepad")){
            case 0:{
                // GUI and Keyboard testing only, no gamepads expected;
                gamepads = new Gamepad[0];
                break;
            }case 1:{
                // One controller expected, testing purposes
                gamepads = new Gamepad[]{new Gamepad(0)};
                break;
            }case 2:{
                // Practice and Competition use, 2 controllers
                gamepads = new Gamepad[]{new Gamepad(0), new Gamepad(0)};
                break;
            }
        }
    }
    private static ROVStatus currentROVStatus = new ROVStatus();
    public static Gamepad getGamepad(int index) {return gamepads[index];}
    public static void timerRefresh(){
        KeyboardRefreshCounter++;
        if(KeyboardRefreshCounter >= Settings.getSetting("KeyboardUpdateRate")){
            CustomKeyboard.TimerRefresh();
            KeyboardRefreshCounter = 0;
        }
        boolean GamepadConnection = false;
        for(Gamepad gamepad: gamepads) {
            if (gamepad.isConnected()) {
                if (!gamepad.pollController()) {
                    gamepad.setController(null);
                    Message(1, "Error polling controller, disconnecting...");
                } else {
                    gamepad.update(currentROVStatus);
                    GamepadConnection = true;
                }
            } else {
                int ConnectionCounter = gamepad.getConnectionCounter();
                if (ConnectionCounter <= Settings.getSetting("GamepadConnectionAttemptRate")*Settings.getSetting("NumGamepadConnectionAttempts")) {
                    if (ConnectionCounter % Settings.getSetting("GamepadConnectionAttemptRate") == 0) {
                        Message(1,Integer.toString(ConnectionCounter) + Integer.toString(gamepads.length));
                        AttemptConnection(gamepad);
                    }
                }
            }
        }
        if(GamepadConnection){
            if(Settings.getSettingB("KeyboardEnabled") && Settings.getSettingB("KeyboardEnabledWhileGamepadConnected")) CustomKeyboard.update(currentROVStatus);
        }else{
            if(Settings.getSettingB("KeyboardEnabled")) CustomKeyboard.update(currentROVStatus);
        }
    }
    private static void AttemptConnection(Gamepad gamepad){
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
                    Message(1,"Found gamepad: "+ controller.getName());
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
    public static byte[] getSerialBytes(){
        return currentROVStatus.getStatus();
    }
    public static ROVStatus getCurrentROVStatus() {
        return currentROVStatus;
    }
}
