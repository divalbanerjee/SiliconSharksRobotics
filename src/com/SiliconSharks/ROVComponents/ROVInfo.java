package com.SiliconSharks.ROVComponents;

public class ROVInfo{
    private Queue<ROVStatus> ROVStatuses;
    public ROVInfo(){

    }
    public ROVStatus getCurrentROVStatus(){
        return ROVStatuses.peekLast();
    }
    public boolean enqueueCurrentROVStatus(ROVStatus status){
        if(){

        }
    }
}
