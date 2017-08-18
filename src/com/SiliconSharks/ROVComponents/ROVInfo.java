package com.SiliconSharks.ROVComponents;

import com.SiliconSharks.Queue;

public class ROVInfo{
    private Queue<ROVStatus> ROVTelemetry = new Queue<>();
    private Queue<ROVStatus> ROVCurrent = new Queue<>();
    public ROVInfo(){}
    public void enqueueCurrentROVStatus(ROVStatus status){ROVCurrent.enqueue(status);}
    public void enqueueCurrentROVTelemetry(ROVStatus status){ROVTelemetry.enqueue(status);}
}
