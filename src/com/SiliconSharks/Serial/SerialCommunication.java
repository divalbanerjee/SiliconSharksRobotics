package com.SiliconSharks.Serial;

import com.SiliconSharks.Queue;
import com.SiliconSharks.ROVComponents.ROVStatus;
import jssc.SerialPort;
import jssc.SerialPortList;

public class SerialCommunication {
    private boolean newReceived, Connected;
    private Queue<SentPackage> SentPackages;
    private Queue<ReceivedPackage> ReceivedPackages;
    private SerialPort serialPort = null;
    public SerialCommunication(){
        newReceived = false;
        Connected = false;
    }
    public void Refresh(){
        if(!Connected){
            String[] portNames = SerialPortList.getPortNames();
            if(portNames.length == 1){
                serialPort = new SerialPort(portNames[0]);
            }else if(portNames.length > 1){

            }
        }
    }
    public boolean getNewReceived(){return newReceived;}
    public ROVStatus getNewROVStatus(){

    }
}