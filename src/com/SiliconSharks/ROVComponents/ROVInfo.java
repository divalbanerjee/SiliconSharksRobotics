package com.SiliconSharks.ROVComponents;

import com.SiliconSharks.Queue;
import com.SiliconSharks.Settings;

import java.util.ArrayList;

public class ROVInfo{
    private static Queue<ROVStatus> ROVTelemetry = new Queue<>();
    private static Queue<ROVStatus> ROVCurrent = new Queue<>();
    public ROVInfo(){}
    public static void start(){
        int numItems = Settings.getSetting("NumROVStatusSaved");
        for(int i = 0; i < numItems; i++){
            enqueueCurrentROVStatus(new ROVStatus());
            enqueueCurrentROVTelemetry(new ROVStatus());
        }
    }
    public static void enqueueCurrentROVStatus(ROVStatus status){
        ROVCurrent.enqueue(status);
        ROVCurrent.dequeue();
    }
    public static void enqueueCurrentROVTelemetry(ROVStatus status){
        ROVTelemetry.enqueue(status);
        ROVTelemetry.dequeue();
    }
    public static ArrayList<ROVStatus> getStatusArrayList(){return ROVCurrent.getAllArrayList();}
    public static ArrayList<ROVStatus> getTelemetryArrayList(){return ROVCurrent.getAllArrayList();}
}
