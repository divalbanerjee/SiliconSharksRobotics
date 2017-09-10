package com.SiliconSharks;

import com.SiliconSharks.Controller.ControlSystem;
import com.SiliconSharks.ROVComponents.ROVInfo;
import com.SiliconSharks.Serial.SerialCommunication;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Timer;
import java.util.TimerTask;

public class MainUpdateLoop {

    private final static boolean[] DebugPrintEnabled = {false, true, true, true}; // 0 is Unnecessary and Unimportant, 1 is Non-Critical, 2 is Error and Redundancy handling, 3 is critical messages
    private static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            ControlSystem.timerRefresh();
            SerialCommunication.timerRefresh();
        }
    };
    public static void start(){
        Settings.start();
        ControlSystem.start();
        SerialCommunication.start();
        ROVInfo.start();
        //CustomFrame frame = new CustomFrame();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 1015, 10);
    }
    public static void main(String[]args){start();}
    public static void Message(int classification, String object){
        if(DebugPrintEnabled[classification]){
            System.out.println(object);
        }
    }
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}