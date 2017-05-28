package com.SiliconSharks.ROVComponents;

import com.SiliconSharks.Queue;

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
