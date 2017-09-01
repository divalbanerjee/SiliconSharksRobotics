package com.SiliconSharks;

import javafx.util.Pair;

import static com.SiliconSharks.Main.Message;
import static com.SiliconSharks.Main.getStackTrace;
import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("unchecked")

public class Settings {
    private Pair<String, Integer>[] settings = new Pair[]{
            new Pair("KeyboardEnabled", 1),
            new Pair("KeyboardEnabledWhileGamepadConnected", 1),
            new Pair("KeyboardUpdateRate", 50),
            new Pair("NumGamepad", 1),
            new Pair("NumGamepadConnectionAttempts", 4),
            new Pair("GamepadConnectionPauseDuration", 6),
            new Pair("GamepadUpdateRate", 50),
            new Pair("SerialConnectionPauseDuration", 1),
            new Pair("SerialBlackoutDuration", 5),
            new Pair("SerialUpdateRate", 5),
            new Pair("NumROVStatusSaved", 30),
    };
    Settings(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("file.txt"));
            String line;
            while ((line = br.readLine()) != null){
                for (int i = 0; i < settings.length; i++) {
                    if(line.startsWith(settings[i].getKey())){
                        settings[i]= new Pair<>(settings[i].getKey(),Integer.valueOf(line.split(" ")[1]));
                    }
                }
            }
            br.close();
        } catch (IOException ex){
            Message(0,getStackTrace(ex));
        }
    }
    private boolean getSettingB(String name){
        for(Pair pair : settings){
            if(pair.getKey() == name){
                return (pair.getValue() == Integer.valueOf(1));
            }
        }
        return false;
    }
    private int getSetting(String name){
        for(Pair pair : settings){
            if(pair.getKey() == name){
                return (int) pair.getValue();
            }
        }
        return 0;
    }
}
