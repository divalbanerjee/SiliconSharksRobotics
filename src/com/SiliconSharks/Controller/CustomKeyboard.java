package com.SiliconSharks.Controller;

import com.SiliconSharks.ROVComponents.ROVStatus;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

class CustomKeyboard {
    private static int[] time = {0,0,0,0,0,0};
    private static int[] taps = {0,0,0,0,0,0};
    private static volatile boolean[] keyPressed = {false,false,false,false,false,false};
    private static int getKeyTaps(char c) {
        synchronized (CustomKeyboard.class) {
            switch(c){
                case 'W': return taps[0];
                case 'A': return taps[1];
                case 'S': return taps[2];
                case 'D': return taps[3];
                case 'Q': return taps[4];
                case 'E': return taps[5];
            }
            return 0;
        }
    }
    private int KeyCodeToIndex(int KeyCode){
        switch (KeyCode){
            case KeyEvent.VK_W: return 0;
            case KeyEvent.VK_A: return 1;
            case KeyEvent.VK_S: return 2;
            case KeyEvent.VK_D: return 3;
            case KeyEvent.VK_Q: return 4;
            case KeyEvent.VK_E: return 5;
            default: return -1;
        }
    }
    void TimerRefresh(){
        for(int i = 0; i < 6; i++) {
            if (time[i] == -1) {
                if (keyPressed[i]) {
                    time[i] = 0;
                    taps[i] = 1;
                }
            } else {
                if (time[i] > 10) {
                    taps[i] = 0;
                    time[i] = -1;
                } else if (!keyPressed[i]) {
                    time[i]++;
                } else if (time[i] > 0) {
                    taps[i]++;
                    time[i] = 0;
                }
            }
        }
    }
    CustomKeyboard() {
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
    }
    void update(ROVStatus rovStatus){
        rovStatus.setThruster(0,((double)(getKeyTaps('W')-getKeyTaps('S')))/5);
        rovStatus.setThruster(1,((double)(getKeyTaps('W')-getKeyTaps('S')))/5);
        rovStatus.setThruster(0,rovStatus.getThruster(0)+((double)(getKeyTaps('A')-getKeyTaps('D')))/5);
        rovStatus.setThruster(1,rovStatus.getThruster(1)+((double)(getKeyTaps('A')-getKeyTaps('D')))/5);
        rovStatus.setThruster(2,((double)(getKeyTaps('Q')-getKeyTaps('E')))/5);
        rovStatus.calibrate(0);
    }
}