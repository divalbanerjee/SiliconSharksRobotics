package com.SiliconSharks;

import javafx.util.Pair;

import static com.SiliconSharks.MainUpdateLoop.Message;
import static com.SiliconSharks.MainUpdateLoop.getStackTrace;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("unchecked")

public class Settings {
    private static Pair<String, Integer>[] settings = new Pair[]{
            new Pair("KeyboardEnabled", 1),
            new Pair("KeyboardEnabledWhileGamepadConnected", 1),
            new Pair("KeyboardUpdateRate", 1),
            new Pair("KeyboardMaxTaps", 10),
            new Pair("KeyboardCounterResetRate",30),
            new Pair("NumGamepad", 1),
            new Pair("NumGamepadConnectionAttempts", 1),
            new Pair("GamepadConnectionAttemptRate", 100),
            new Pair("SerialConnectionPauseDuration", 100),
            new Pair("SerialConnectionAttemptRate",30),
            new Pair("SerialUpdateRate", 2),
            new Pair("SerialDurationBeforeDisconnect", 300),
            new Pair("SerialBaudRate", 19200),
            new Pair("NumROVStatusSaved", 30),
    };
    static void start(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("Settings.txt"));
            String line;
            while ((line = br.readLine()) != null){
                for (int i = 0; i < settings.length; i++) {
                    if(line.startsWith(settings[i].getKey()+" ")){
                        settings[i]= new Pair<>(settings[i].getKey(),Integer.valueOf(line.split(" ")[1]));
                        Message(1,settings[i].getKey()+" Output: "+(line.split(" ")[1]));
                        break;
                    }
                }
            }
            br.close();
        } catch (IOException ex){
            Message(1, "Error retrieving/reading file: ");
            Message(1,getStackTrace(ex));
        }
    }
    public static boolean getSettingB(String name){
        for(Pair pair : settings){
            if(pair.getKey() == name){
                return (pair.getValue() == Integer.valueOf(1));
            }
        }
        Message(1,"Error: Setting " + name + " not found!");
        return false;
    }
    public static int getSetting(String name){
        for(Pair pair : settings){
            if(pair.getKey() == name){
                return (int) pair.getValue();
            }
        }
        Message(1,"Error: Setting " + name + " not found!");
        return 0;
    }
}
