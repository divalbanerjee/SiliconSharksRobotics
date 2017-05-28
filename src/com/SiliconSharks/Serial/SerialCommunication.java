package com.SiliconSharks.Serial;

import com.SiliconSharks.Controller.Gamepad;
import com.SiliconSharks.Queue;
import com.SiliconSharks.ROVComponents.ROVStatus;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SerialCommunication {
    private boolean newReceived, Connected;
    private Queue<SentPackage> SentPackages;
    private Queue<ReceivedPackage> ReceivedPackages;
    private SerialPort serialPort = null;
    public SerialCommunication(){
        newReceived = false;
        Connected = false;
        String[] portNames = SerialPortList.getPortNames();
        if(portNames.length == 1){
            serialPort = new SerialPort(portNames[0]);
        }else if(portNames.length > 1){
            serialPort = new SerialPort(portNames[0]);
            System.out.print("Warning: Potentially incorrect port");
        }
    }
    public void Refresh(){
        if(!Connected){
            String[] portNames = SerialPortList.getPortNames();
            if(portNames.length == 1){
                serialPort = new SerialPort(portNames[0]);
            }else if(portNames.length > 1){
                serialPort = new SerialPort(portNames[0]);
                System.out.print("Warning: Potentially incorrect port");
            }
        }else{

        }
    }
    public boolean getNewReceived(){return newReceived;}
    public boolean getConnnected(){return Connected;}
    public ROVStatus getNewROVStatus(){
        newReceived = false;
        return ReceivedPackages.peekLast().getROVStatus();
    }
    public void sendPackage(Gamepad gamepad){
        if(Connected) {
            SentPackage newPackage = new SentPackage();
            newPackage.setSerialBytes(gamepad);
            try{
                serialPort.writeBytes(newPackage.getSerialBytes());
            }catch(SerialPortException ex){
                ex.printStackTrace();
            }
        }
    }
}