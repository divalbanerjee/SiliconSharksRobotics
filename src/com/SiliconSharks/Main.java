package com.SiliconSharks;

import com.SiliconSharks.Controller.GamepadCommunications;
import com.SiliconSharks.ROVComponents.ROVInfo;
import com.SiliconSharks.Serial.SerialCommunication;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static ROVInfo rovInfo = new ROVInfo();
    private static GamepadCommunications gamepadCommunications = new GamepadCommunications();
    private static SerialCommunication serialCommunication;
    private static Timer gTimer = new Timer();
    private static TimerTask gTimerTask = new TimerTask() {
        @Override
        public void run() {
            gamepadCommunications.timerRefresh();
        }
    };
    private static Timer sTimer = new Timer();
    private static TimerTask sTimerTask = new TimerTask() {
        @Override
        public void run() {
            serialCommunication.timerRefresh();
            if(serialCommunication.getNewReceived()){
                rovInfo.enqueueCurrentROVStatus(serialCommunication.getNewROVStatus());
            }
        }
    };
    public static void main(String[]args){
        serialCommunication = new SerialCommunication(gamepadCommunications.getGamepad());
        gTimer.scheduleAtFixedRate(gTimerTask,1000,30);
        sTimer.scheduleAtFixedRate(sTimerTask, 1015, 30);
    }
}