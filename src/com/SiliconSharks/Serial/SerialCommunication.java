package com.SiliconSharks.Serial;

import com.SiliconSharks.Controller.ControlSystem;
import com.SiliconSharks.Queue;
import com.SiliconSharks.ROVComponents.ROVStatus;
import jssc.*;

import java.util.ArrayList;

import static com.SiliconSharks.Main.Message;
import static com.SiliconSharks.Main.getStackTrace;

public class SerialCommunication implements SerialPortEventListener {
    private boolean newReceived, Connected;
    private Queue<byte[]> SentPackages = new Queue<>();
    private Queue<byte[]> ReceivedPackages = new Queue<>();
    private SerialPort serialPort = null;
    private ArrayList<String> prevPorts = new ArrayList<>();
    private String currentPort, successfulPort;
    private int SendPackageCounter = 0;
    private int NotConnectedCounter= 0;
    private int NotReceivedCounter = 0;
    private boolean timerRunning = false;
    private ROVStatus currentROVStatus = new ROVStatus();
    private ControlSystem controlSystem;
    private ArrayList<Object> serialBytes = new ArrayList<>(0);
    public SerialCommunication(ControlSystem controlSystem){
        newReceived = false;
        Connected = false;
        successfulPort =" ";
        this.controlSystem = controlSystem;
    }
    public void timerRefresh(){
        if(!timerRunning) {
            timerRunning = true;
            if (Connected) {
                SendPackageCounter++;
                if (SendPackageCounter >= 10) {
                    Message(0,"Serial is currently connected");
                    SendPackageCounter = 0;
                    Message(0,"Sending Package...");
                    if(sendPackage(controlSystem.getSerialBytes())){
                        Message(0,"Package Sent!");
                    }else{
                        Message(0,"Package Not Sent! Scroll up for Exception Stack Trace");
                    }
                }
                NotReceivedCounter++;
                if (newReceived) {
                    Message(0,"Package Received!");
                    newReceived = false;
                    NotReceivedCounter = 0;
                } else if (NotReceivedCounter > 70) {
                    Message(0,"Long Duration without Telemetry, Attempting Disconnect...");
                    if(Disconnect()) {
                        Message(0,"Disconnection Successful");
                    }else{
                        Message(0,"Disconnection Unsuccessful");
                    }
                }
            } else {
                if(NotConnectedCounter%10 ==0)Message(0,"Serial is not currently connected");
                NotConnectedCounter++;
                if (NotConnectedCounter >= 30) {
                    Message(0,"Attempting connection...");
                    NotConnectedCounter = 0;
                    if(AttemptConnection()){
                        Message(0,"Connection Successful");
                    }else{
                        Message(0,"Connection Unsuccessful");
                    }
                }
            }
            timerRunning = false;
        }
    }
    private boolean Disconnect(){
        try{
            serialPort.closePort();
            SendPackageCounter = 0;
            NotConnectedCounter = 0;
            NotReceivedCounter = 0;
            serialPort = null;
            currentPort = "";
            Connected = false;
            return true;
        }catch(SerialPortException ex){
            Message(0,getStackTrace(ex));
            return false;
        }
    }
    private boolean AttemptConnection(){
        if(!Connected){
            String[] portNames = SerialPortList.getPortNames();
            if (portNames.length >= 1) {
                if(portNames.length == 1){
                    serialPort = new SerialPort(portNames[0]);
                    currentPort = portNames[0];
                    Message(0,"One Port Detected, Connecting to Port: " + portNames[0] + "...");
                }else{
                    if(successfulPort.equals(" ")){
                        Message(0,"No previous connections successful, selecting new port...");
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
                                Message(0,"New port that has not been connected to previously: " + port);
                                break;
                            }
                        }
                        if(hasConnected){
                            int RandomNum = (int) (Math.random()*portNames.length);
                            serialPort = new SerialPort(portNames[RandomNum]);
                            currentPort = portNames[RandomNum];
                            Message(0,"All ports have been unsuccessful before, connecting to random port: "+ portNames[RandomNum]);
                        }
                    }else{
                        boolean successfulPortPresent = false;
                        for(String port : portNames){
                            if(port.equals(successfulPort)){
                                Message(0,"Previous Successful Port is available, connecting to: "+successfulPort);
                                serialPort = new SerialPort(successfulPort);
                                currentPort = successfulPort;
                                successfulPortPresent = true;
                                break;
                            }
                        }
                        if(!successfulPortPresent){
                            int RandomNum = (int) (Math.random()*portNames.length);
                            serialPort = new SerialPort(portNames[RandomNum]);
                            currentPort = portNames[RandomNum];
                            Message(0,"Successful Port not present, connecting to random port: "+ portNames[RandomNum]);
                        }
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
                    SendPackageCounter = 0;
                    NotConnectedCounter = 0;
                    NotReceivedCounter = 0;
                    Message(0,"Connection has been Successful");
                    return true;
                }catch(SerialPortException ex){
                    Message(0,getStackTrace(ex));
                    Message(0,"Connection failed! Read Stack Trace for details");
                    return false;
                }
            }else{
                Message(0,"Error: No current port detected!");
                return false;
            }
        }else{
            Message(0, "Error: Already Connected!");
            return false;
        }
    }
    public ROVStatus getNewROVStatus(){
        return currentROVStatus;
    }
    public boolean getNewReceived(){return newReceived;}
    private boolean sendPackage(byte[] serialBytes){
        if(Connected) {
            try{
                StringBuilder telemetry = new StringBuilder();
                for(byte b : serialBytes){
                    telemetry.append(b).append(' ');
                }
                Message(0,telemetry.toString());
                serialPort.writeBytes(serialBytes);
                SentPackages.enqueue(serialBytes);
            }catch(SerialPortException ex){
                Message(0,getStackTrace(ex));
                return false;
            }
        }else{
            return false;
        }
        return true;
    }
    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() >= 1) {
            try {
                byte a[] = serialPort.readBytes(event.getEventValue());
                for(byte b : a){
                    serialBytes.add(b);
                }
                Message(0,"Byte read: " + a[0]+ " Message length: " + event.getEventValue());
                for(int i = 0; i < serialBytes.size() - 8; i++){
                    if(serialBytes.get(i).equals((byte)-1) && i + 8 < serialBytes.size()){
                        byte[] telemetry = new byte[8];
                        StringBuilder arraylist = new StringBuilder("ArrayList contains: ");
                        for(int j = 0; j < 8; j++){
                            telemetry[j] = (byte) serialBytes.get(i+1+j);
                            arraylist.append(serialBytes.get(i+1+j)).append(' ');
                        }
                        Message(0,arraylist.toString());
                        currentROVStatus.setStatus(telemetry);
                        ReceivedPackages.enqueue(telemetry);
                        newReceived = true;
                        serialBytes = new ArrayList<>(serialBytes.subList(i+4, serialBytes.size()-1));
                    }
                }

            } catch (SerialPortException ex) {
                Message(0,"Error in receiving string from COM-port");
                Message(0,getStackTrace(ex));
            }
        }
    }
}