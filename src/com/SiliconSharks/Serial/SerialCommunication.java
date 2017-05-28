package com.SiliconSharks.Serial;

import com.SiliconSharks.Controller.Gamepad;
import com.SiliconSharks.Queue;
import com.SiliconSharks.ROVComponents.ROVStatus;
import jssc.*;

import java.util.ArrayList;

public class SerialCommunication implements SerialPortEventListener {
    private boolean newReceived, Connected;
    private Queue<SentPackage> SentPackages;
    private Queue<ReceivedPackage> ReceivedPackages;
    private SerialPort serialPort = null;
    private ArrayList<String> prevPorts = new ArrayList<>();
    private String currentPort, successfulPort;
    public SerialCommunication(){
        newReceived = false;
        Connected = false;
        successfulPort =" ";
        AttemptConnection();
    }
    public void Disconnect(){
        try{
            serialPort.closePort();
        }catch(SerialPortException ex){
            ex.printStackTrace();
        }
        serialPort = null;
        currentPort = "";
    }
    public void AttemptConnection(){
        if(!Connected){
            String[] portNames = SerialPortList.getPortNames();
            if (portNames.length >= 1) {
                if(portNames.length == 1){
                    serialPort = new SerialPort(portNames[0]);
                    currentPort = portNames[0];
                }else{
                    if(successfulPort.equals(" ")){
                        boolean hasConnected = true;
                        for(String port : portNames){
                            hasConnected = false;
                            for(String prevPort : prevPorts){
                                if(port.equals(prevPort)){
                                    hasConnected = true;
                                }
                            }
                            if(!hasConnected){
                                serialPort = new SerialPort(port);
                                currentPort = port;
                                break;
                            }
                        }
                        if(hasConnected){
                            serialPort = new SerialPort(portNames[0]);
                            currentPort = portNames[0];
                        }
                    }else{
                        serialPort = new SerialPort(successfulPort);
                        currentPort = successfulPort;
                    }
                }
                try {
                    prevPorts.add(currentPort);
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_19200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                            SerialPort.FLOWCONTROL_RTSCTS_OUT);
                    serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
                    Connected = true;
                }catch(SerialPortException ex){
                    ex.printStackTrace();
                }
            }else{
                System.out.print("Error: No current port detected!");
            }
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
    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() >= 11) {
            try {
                byte a[] = serialPort.readBytes(1);
                if(a[0] != 0){
                    return;
                }
                a = serialPort.readBytes(10);

            } catch (SerialPortException ex) {
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
    }
}