package com.SiliconSharks.Controller;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class CustomKeyboard {
    private static volatile boolean[] keyPressed = {false,false,false,false,false,false};
    public static boolean getKeyPressed(char c) {
        synchronized (CustomKeyboard.class) {
            switch(c){
                case 'W': return keyPressed[0];
                case 'A': return keyPressed[1];
                case 'S': return keyPressed[2];
                case 'D': return keyPressed[3];
                case 'Q': return keyPressed[4];
                case 'E': return keyPressed[5];
            }
            return false;
        }
    }
    private int KeyCodeToIndex(int keycode){
        switch (keycode){
            case KeyEvent.VK_W: return 0;
            case KeyEvent.VK_A: return 1;
            case KeyEvent.VK_S: return 2;
            case KeyEvent.VK_D: return 3;
            case KeyEvent.VK_Q: return 4;
            case KeyEvent.VK_E: return 5;
            default: return -1;
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
}