package com.SiliconSharks.Serial;

import com.SiliconSharks.Controller.ControlSystem;
import com.SiliconSharks.ROVComponents.ROVInfo;
import com.SiliconSharks.ROVComponents.ROVStatus;
import com.SiliconSharks.Settings;
import jssc.*;

import java.util.ArrayList;

import static com.SiliconSharks.MainUpdateLoop.Message;
import static com.SiliconSharks.MainUpdateLoop.getStackTrace;

public class SerialCommunication {
    private static boolean newReceived, Connected;
    private static SerialPort serialPort = null;
    private static ArrayList<String> prevPorts = new ArrayList<>();
    private static String currentPort, successfulPort;
    private static int SendPackageCounter = 0, NotConnectedCounter= 0, NotReceivedCounter = 0, WaitSend = 0;
    private static ROVStatus currentROVStatus = new ROVStatus();
    private static ArrayList<Object> serialBytes = new ArrayList<>(0);
    public SerialCommunication(){}
    public static void start(){
        newReceived = false;
        Connected = false;
        successfulPort =" ";
    }
    public static void timerRefresh(){
        if (Connected) {
            if(WaitSend < Settings.getSetting("SerialConnectionPauseDuration") && WaitSend > -1){
                WaitSend++;
            }else if(WaitSend >= Settings.getSetting("SerialConnectionPauseDuration")){
                WaitSend = -1;
            }else {
                SendPackageCounter++;
                if (SendPackageCounter >= Settings.getSetting("SerialUpdateRate")) {
                    Message(0, "Serial is currently connected");
                    SendPackageCounter = 0;
                    Message(0, "Sending Package...");
                    if (sendPackage(ControlSystem.getSerialBytes())) {
                        Message(0, "Package Sent!");
                    } else {
                        Message(1, "Package Not Sent! Scroll up for Exception Stack Trace");
                    }
                }
                NotReceivedCounter++;
                if (newReceived) {
                    Message(0,"Package Received!");
                    newReceived = false;
                    NotReceivedCounter = 0;
                } else if (NotReceivedCounter > Settings.getSetting("SerialDurationBeforeDisconnect")) {
                    Message(1,"Long Duration without Telemetry, Attempting Disconnect...");
                    if(Disconnect()) {
                        Message(0,"Disconnection Successful");
                    }else{
                        Message(0,"Disconnection Unsuccessful");
                    }
                }
            }
        } else {
            NotConnectedCounter++;
            if (NotConnectedCounter >= Settings.getSetting("SerialConnectionAttemptRate")) {
                Message(0,"Attempting connection...");
                NotConnectedCounter = 0;
                if(AttemptConnection()){
                    Message(0,"Connection Successful");
                }else{
                    Message(0,"Connection Unsuccessful");
                }
            }
        }
        ROVInfo.enqueueCurrentROVTelemetry(currentROVStatus);
    }
    private static boolean Disconnect(){
        try{
            serialPort.closePort();
            SendPackageCounter = 0;
            NotConnectedCounter = 0;
            NotReceivedCounter = 0;
            WaitSend = 0;
            serialPort = null;
            currentPort = "";
            Connected = false;
            return true;
        }catch(SerialPortException ex){
            Message(1,getStackTrace(ex));
            return false;
        }
    }
    private static boolean AttemptConnection(){
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
                    serialPort.setParams(
                            Settings.getSetting("SerialBaudRate"),
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    serialPort.setFlowControlMode(
                            SerialPort.FLOWCONTROL_RTSCTS_IN |
                            SerialPort.FLOWCONTROL_RTSCTS_OUT);
                    serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                        if (serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() >= 1) {
                            try {
                                byte a[] = serialPort.readBytes(serialPortEvent.getEventValue());
                                for(byte b : a){
                                    serialBytes.add(b);
                                }
                                //Message(0,"Byte read: " + a[0]+ " Message length: " + event.getEventValue());
                                for(int i = 0; i < serialBytes.size() - 8; i++){
                                    if(serialBytes.get(i).equals((byte)-1) && i + 8 < serialBytes.size()){
                                        byte[] telemetry = new byte[8];
                                        StringBuilder arraylist = new StringBuilder("bytes read: ");
                                        for(int j = 0; j < 8; j++){
                                            telemetry[j] = (byte) serialBytes.get(i+1+j);
                                            arraylist.append(serialBytes.get(i+1+j)).append(' ');
                                        }
                                        Message(0,arraylist.toString());
                                        currentROVStatus.setStatus(telemetry);
                                        newReceived = true;
                                        serialBytes = new ArrayList<>(serialBytes.subList(i+4, serialBytes.size()-1));
                                    }
                                }

                            } catch (SerialPortException ex) {
                                Message(1,"Error in receiving string from COM-port");
                                Message(1,getStackTrace(ex));
                            }
                        }
                    }, SerialPort.MASK_RXCHAR);
                    Connected = true;
                    SendPackageCounter = 0;
                    NotConnectedCounter = 0;
                    NotReceivedCounter = 0;
                    WaitSend = 0;
                    Message(0,"Connection has been Successful");
                    return true;
                }catch(SerialPortException ex){
                    Message(1,getStackTrace(ex));
                    Message(1,"Connection failed! Read Stack Trace for details");
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
    public static ROVStatus getNewROVStatus(){
        return currentROVStatus;
    }
    public static boolean getNewReceived(){return newReceived;}
    private static boolean sendPackage(byte[] serialBytes){
        if(Connected) {
            try{
                StringBuilder telemetry = new StringBuilder();
                for(byte b : serialBytes){
                    telemetry.append(b).append(' ');
                }
                Message(0,telemetry.toString());
                serialPort.writeBytes(serialBytes);
            }catch(SerialPortException ex){
                Message(0,"Error: Package Send Failed!");
                Message(1,getStackTrace(ex));
                return false;
            }
        }else{
            return false;
        }
        return true;
    }
}