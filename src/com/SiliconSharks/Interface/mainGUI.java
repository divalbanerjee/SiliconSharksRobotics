package com.SiliconSharks.Interface;

import com.SiliconSharks.MainUpdateLoop;

import javax.swing.*;

/**
 * Created by bandi on 8/27/2017.
 */
public class mainGUI {
    public static void main(String[]args){
        MainUpdateLoop.start();
        JFrame rovGUI = new ROVGUI();
    }
}
