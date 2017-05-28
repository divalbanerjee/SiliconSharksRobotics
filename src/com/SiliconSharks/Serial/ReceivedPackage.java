package com.SiliconSharks.Serial;

import com.SiliconSharks.ROVComponents.ROVStatus;

public class ReceivedPackage {
    private byte[] serialBytes;
    public ReceivedPackage(){
        serialBytes = new byte[10];
    }
    ROVStatus getROVStatus(){
        ROVStatus status = new ROVStatus();
        status.setStatus(serialBytes);
        return status;
    }
}