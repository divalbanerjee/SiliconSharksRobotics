package com.SiliconSharks.Controller;

import com.SiliconSharks.ROVComponents.ROVStatus;
import com.SiliconSharks.Settings;
import javafx.scene.input.KeyCode;

import static com.SiliconSharks.MainUpdateLoop.Message;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

class CustomKeyboard {
    private static final int numkeys = 13;
    private static int[] time = new int[numkeys];
    private static int[] taps = new int[numkeys];
    private static volatile boolean[] keyPressed = {false,false,false,false,false,false,false,false,false,false,false,false,false};
    private static int getKeyTaps(char c) {
        synchronized (CustomKeyboard.class) {
            switch(c){
                case 'W': return taps[0];
                case 'A': return taps[1];
                case 'S': return taps[2];
                case 'D': return taps[3];
                case 'Q': return taps[4];
                case 'E': return taps[5];
                case ' ': return taps[6];
                case 'u': return taps[7];
                case 'd': return taps[8];
                case 'l': return taps[9];
                case 'r': return taps[10];
            }
            return 0;
        }
    }
    private static int KeyCodeToIndex(int KeyCode){
        switch (KeyCode){
            case KeyEvent.VK_W: return 0;
            case KeyEvent.VK_A: return 1;
            case KeyEvent.VK_S: return 2;
            case KeyEvent.VK_D: return 3;
            case KeyEvent.VK_Q: return 4;
            case KeyEvent.VK_E: return 5;
            case KeyEvent.VK_SPACE: return 6;
            case KeyEvent.VK_UP: return 7;
            case KeyEvent.VK_DOWN: return 8;
            case KeyEvent.VK_LEFT: return 9;
            case KeyEvent.VK_RIGHT: return 10;
            default: return -1;
        }
    }
    static void TimerRefresh(){
        for(int i = 0; i < 11; i++) {
            if (time[i] == -1) {
                if (keyPressed[i]) {
                    if(i==1 && taps[0]>0) taps[0] = 0;
                    else if(i==0 && taps[1]>0) taps[1] = 0;
                    else if(i==2 && taps[3]>0) taps[3] = 0;
                    else if(i==3 && taps[2]>0) taps[2] = 0;
                    else if(i==4 && taps[5]>0) taps[5] = 0;
                    else if(i==5 && taps[4]>0) taps[4] = 0;
                    else {
                        time[i] = 0;
                        taps[i] = 1;
                    }
                }
            } else {
                if (time[i] > Settings.getSetting("KeyboardCounterResetRate")) {
                    taps[i] = 0;
                    time[i] = -1;
                } else if (!keyPressed[i]) {
                    time[i]++;
                } else if (time[i] > 0) {
                    if(Settings.getSetting("KeyboardMaxTaps") > taps[i]) taps[i]++;
                    time[i] = 0;
                }
            }
        }
    }
    CustomKeyboard() {}
    static void start(){
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
            (KeyEvent ke) -> {
                synchronized (CustomKeyboard.class) {
                    int index = KeyCodeToIndex(ke.getKeyCode());
                    if(index < 0){return false;}
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            keyPressed[index] = true;
                            break;

                        case KeyEvent.KEY_RELEASED:
                            keyPressed[index] = false;
                            break;
                    }
                    return false;
                }
            });
        Message(1, "Successful CustomKeyboard Startup!");
    }
    static void update(ROVStatus rovStatus, ROVStatus prevROVStatus){
        int maxtaps = Settings.getSetting("KeyboardMaxTaps");
        //rovStatus.setThruster(0,rovStatus.getThruster(0)+((double)(getKeyTaps('W')-getKeyTaps('S')))/maxtaps);
        //rovStatus.setThruster(1,rovStatus.getThruster(1)+((double)(getKeyTaps('W')-getKeyTaps('S')))/maxtaps);
        rovStatus.setThruster(0,rovStatus.getThruster(0)+((double)(getKeyTaps('A')-getKeyTaps('D')))/maxtaps);
        rovStatus.setThruster(1,rovStatus.getThruster(1)-((double)(getKeyTaps('A')-getKeyTaps('D')))/maxtaps);
        rovStatus.setThruster(2,rovStatus.getThruster(2)+((double)(getKeyTaps('Q')-getKeyTaps('E')))/maxtaps);
        if(keyPressed[KeyCodeToIndex(KeyEvent.VK_SPACE)]){
            for(int i = 0; i < 3; i++){
                rovStatus.setThruster(i,0);
            }
        }
        if(keyPressed[KeyCodeToIndex(KeyEvent.VK_W)] && !keyPressed[KeyCodeToIndex(KeyEvent.VK_S)]){
            rovStatus.setThruster(0,rovStatus.getThruster(0)+0.01);
            rovStatus.setThruster(1,rovStatus.getThruster(1)+0.01);
        }else if(!keyPressed[KeyCodeToIndex(KeyEvent.VK_W)] && keyPressed[KeyCodeToIndex(KeyEvent.VK_S)]){
            rovStatus.setThruster(0,rovStatus.getThruster(0)-0.01);
            rovStatus.setThruster(1,rovStatus.getThruster(1)-0.01);
        }
        if(time[KeyCodeToIndex(KeyEvent.VK_UP)] == 1){
            prevROVStatus.getServo(0);
            rovStatus.setServo(0,prevROVStatus.getServo(0)+0.25);
            rovStatus.setServo(1,prevROVStatus.getServo(1)+0.25);
        }
        if(time[KeyCodeToIndex(KeyEvent.VK_DOWN)] == 1){
            rovStatus.setServo(0,prevROVStatus.getServo(0)-0.25);
            rovStatus.setServo(1,prevROVStatus.getServo(1)-0.25);
        }
        rovStatus.calibrate(0);
    }
}