package com.SiliconSharks;

import com.SiliconSharks.Controller.GamepadCommunications;
import com.SiliconSharks.ROVComponents.ROVInfo;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private ROVInfo rovInfo;
    private GamepadCommunications gamepadCommunications = new GamepadCommunications();
    public static void main(String[]args){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

            }
        };
    }
}
