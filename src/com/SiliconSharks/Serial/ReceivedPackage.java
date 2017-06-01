package com.SiliconSharks.Serial;

import com.SiliconSharks.ROVComponents.ROVStatus;

class ReceivedPackage {
    private byte[] serialBytes;
    ReceivedPackage(){serialBytes = new byte[6];}
    void setSerialBytes(byte[] serialBytes){this.serialBytes = serialBytes;}
    ROVStatus getROVStatus(){
        ROVStatus status = new ROVStatus();
        status.setStatus(serialBytes);
        return status;
    }
}