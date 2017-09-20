package com.SiliconSharks.ROVComponents;

import com.SiliconSharks.Settings;

import static com.SiliconSharks.MainUpdateLoop.Message;

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
        for (int i = 0; i < numItems-1; i++) {
            rovStatuses[i] = rovStatuses[i+1];
        }
        rovStatuses[numItems-1] = rovStatus;
    }
    public static ROVStatus[] getRovStatuses() {
        return rovStatuses;
    }
    public static ROVStatus update(int TimeStamp){
        StringBuilder s = new StringBuilder();
        for (int i = rovStatuses.length-1; i >= 0; i--) {
            ROVStatus rovStatus = rovStatuses[i];
            s.append(rovStatus.getTimeStamp()).append(' ');
            if (!rovStatus.isTelemetryUpdated() && rovStatus.getTimeStamp() == TimeStamp) {
                return rovStatus;
            }
        }
        Message(2,s.toString());
        return null;
    }
    public static ROVStatus getMostRecentSentStatus(){
        return rovStatuses[numItems-1];
    }
    public static ROVStatus getMostRecentTelemetry(){
        for (int i = rovStatuses.length-1; i >= 0; i--) {
            ROVStatus rovStatus = rovStatuses[i];
            if (rovStatus.isTelemetryUpdated()) {
                return rovStatus;
            }
        }
        return null;
    }
}