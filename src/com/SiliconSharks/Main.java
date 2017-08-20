package com.SiliconSharks;

import com.SiliconSharks.Controller.ControlSystem;
import com.SiliconSharks.ROVComponents.ROVInfo;
import com.SiliconSharks.Serial.SerialCommunication;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private final static boolean[] DebugPrintEnabled = {true, true, true, true}; // 0 is Unnecessary and Unimportant, 1 is Non-Critical, 2 is Error and Redundancy handling, 3 is critical messages
    private static ROVInfo rovInfo = new ROVInfo(30);
    private static ControlSystem controlSystem = new ControlSystem();
    private static SerialCommunication serialCommunication;
    private static Timer timer = new Timer();
    //private static CustomPanel frame = new CustomPanel(rovInfo);
    private static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

            controlSystem.timerRefresh();
            serialCommunication.timerRefresh();
            if(serialCommunication.getNewReceived()){
                rovInfo.enqueueCurrentROVTelemetry(serialCommunication.getNewROVStatus());
                rovInfo.enqueueCurrentROVStatus(controlSystem.getCurrentROVStatus());
            }
        }
    };
    public static void main(String[]args){
        serialCommunication = new SerialCommunication(controlSystem);
        timer.scheduleAtFixedRate(timerTask, 1015, 30);
    }
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