package com.SiliconSharks.ROVComponents;

public class ROVInfo{
    private Queue<ROVStatus> ROVStatuses;
    public ROVInfo(){

    }
    public ROVStatus getCurrentROVStatus(){
        return ROVStatuses.peekLast();
    }
    public void enqueueCurrentROVStatus(ROVStatus status){
        ROVStatuses.enqueue(status);
    }
}
