package com.SiliconSharks.ROVComponents;

import com.SiliconSharks.Settings;

public class ROVInfo{
    private static ROVStatus[] rovStatuses;
    private static int numItems;
    public ROVInfo(){}
    public static void start(){
        numItems = Settings.getSetting("NumROVStatusSaved");
        rovStatuses = new ROVStatus[numItems];
        for(int i = 0; i < numItems; i++){
            rovStatuses[i] = new ROVStatus(-1);
        }
    }
    public static void insert(ROVStatus rovStatus){
        System.arraycopy(rovStatuses, 1, rovStatuses, 0, numItems);
        rovStatuses[numItems-1] = rovStatus;
    }
    public static ROVStatus[] getRovStatuses() {
        return rovStatuses;
    }
    public static ROVStatus update(int TimeStamp){
        for(ROVStatus rovStatus:rovStatuses){
            if(rovStatus.getTimeStamp()==TimeStamp) return rovStatus;
        }
        return null;
    }
}