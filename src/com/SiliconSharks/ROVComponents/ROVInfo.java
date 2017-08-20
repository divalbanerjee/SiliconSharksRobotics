package com.SiliconSharks.ROVComponents;

import com.SiliconSharks.Queue;

import java.util.ArrayList;

public class ROVInfo{
    private int numItems = 30;
    private Queue<ROVStatus> ROVTelemetry = new Queue<>();
    private Queue<ROVStatus> ROVCurrent = new Queue<>();
    public ROVInfo(int numItems){
        this.numItems = numItems;
        for(int i = 0; i < numItems; i++){
            enqueueCurrentROVStatus(new ROVStatus());
            enqueueCurrentROVTelemetry(new ROVStatus());
        }
    }
    public void enqueueCurrentROVStatus(ROVStatus status){
        ROVCurrent.enqueue(status);
        if(ROVCurrent.size() > numItems)
            ROVCurrent.dequeue();
    }
    public void enqueueCurrentROVTelemetry(ROVStatus status){
        ROVTelemetry.enqueue(status);
        if(ROVTelemetry.size() > numItems)
            ROVTelemetry.dequeue();
    }
    public ROVStatus getCurrentROVStatus(){return ROVCurrent.peekLast();}
    public ROVStatus getCurrentROVTelemetry(){return ROVTelemetry.peekLast();}
    public ArrayList<ROVStatus> getStatusArrayList(){return ROVCurrent.getAllArrayList();}
    public ArrayList<ROVStatus> getTelemetryArrayList(){return ROVCurrent.getAllArrayList();}
}
