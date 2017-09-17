package com.SiliconSharks.Interface;

import com.SiliconSharks.Graphics.Compass;
import com.SiliconSharks.Graphics.ControllerInterface;
import com.SiliconSharks.Graphics.MButton;
import com.SiliconSharks.Graphics.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by bandi on 8/27/2017.
 */
public class ROVGUI extends JFrame {
    private Font font20Pt = new Font("Helvetica" , Font.PLAIN, 20);
    Container container;
    Color textColor = new Color(149, 165, 166);
    ControllerInterface controller1 = new ControllerInterface(1);
    ControllerInterface controller2 = new ControllerInterface(2);
    Compass xAxis = new Compass("X Axis Rotation");
    Compass zAxis  = new Compass("Z Axis Rotation");
    Compass yAxis = new Compass("Y Axis Rotation");
    MButton btnShutDown = new MButton("Shut Down");
    StatusBar rovStatusBar = new StatusBar();

    public ROVGUI(){

        JPanel layoutManager = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        btnShutDown.setBackColor(new Color(229, 57, 53));
        btnShutDown.setPaddingColor(new Color(183, 28, 28));
        btnShutDown.setPadding(5);
        btnShutDown.setBackGroundColor(new Color(84, 110, 122));
        btnShutDown.addMouseListener(new btnShutDownListener());
        setSize(1900,1200);
        setBackground(new Color(44, 62, 80));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        container = getContentPane();
        container.setBackground(new Color(44,62,80));
        layoutManager.setBackground(new Color(44,62,80));

        c.gridx = 0;
        c.gridy = 0;
        c.weightx  = 1;

        c.weighty = .2;

       // c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.ipady = 50;
        layoutManager.add(rovStatusBar,c);
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 1;
        //layoutManager.add(new JPanel(),c);
        c.ipady = 50;
        c.ipadx = 20;
        c.gridy = 1;
        c.weightx = .35;
        c.gridx = 0;
        c.weighty = .4;
        layoutManager.add(controller1,c);
        controller1.addMouseListener(new controller1MouseListener());
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = .4;
        layoutManager.add(controller2,c);
        controller2.addMouseListener(new controller2MouseListener());

        c.gridx = 4;
        c.gridy = 0;
        c.weightx = .3;
        c.weighty = .2;

        c.ipady = 10;

        layoutManager.add(xAxis,c);

        c.gridy = 1;

        layoutManager.add(yAxis,c);

        c.gridy = 2;

        layoutManager.add(zAxis,c);

        c.gridy = 3;
        layoutManager.add(btnShutDown,c);

        //layoutManager.add(dataPanel, c);
        container.add(layoutManager);
    }
    public class controller1MouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            controller1.repaint();
        }
    }

    public class controller2MouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            controller2.repaint();
        }
    }

    public class btnShutDownListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            btnShutDown.setPadding(0);
            try {
                Thread.sleep(100);
                btnShutDown.setPadding(5);
                Thread.sleep(100);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            System.exit(0);
        }
    }
    
}
