package com.SiliconSharks.Graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Switch extends JPanel {
    private boolean switchstate, positionchanged = true;
    private Font font30Pt = new Font("Helvetica" , Font.PLAIN, 15);
    private FontMetrics fontMetrics;
    private double position;
    private String label;
    public Switch(String label){
        this(label,false);
    }
    public Switch(String label, boolean switchstate){
        this.switchstate = switchstate;
        if(switchstate) position = 0;
        else position = 1;
        this.label = label;
        fontMetrics = getFontMetrics(font30Pt);
        addMouseListener(new MouseEventListener());
    }
    public void updateposition(){
        double idealposition = 1;
        if(switchstate) idealposition--;
        positionchanged = !(position == idealposition);
        double change = (idealposition - position)/5;
        if(Math.abs(change) > 0.05){
            change = 0.05 * Math.abs(change)/change;
            position += change;
        }else if(Math.abs(change) < 0.005){
            position = idealposition;
        }else{
            position += change;
        }
    }
    public boolean getpositionchanged(){
        return  positionchanged;
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        int start = fontMetrics.stringWidth(label)+3;
        int height = fontMetrics.getHeight();
        g2.setFont(font30Pt);
        g2.setColor(Color.WHITE);
        g2.drawString(label,0,height);
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(start,0,84,height+8,10,10);
        g2.setColor(Color.RED);
        g2.fillRoundRect(start+2,2,45,height+4,8,8);
        g2.setColor(Color.GREEN);
        g2.fillRoundRect(start+37,2,45,height+4,8,8);
        g2.setColor(Color.RED);
        g2.fillRect(start+32,2,10,height+4);
        g2.setColor(Color.BLACK);
        g2.drawString("OFF",start+21-fontMetrics.stringWidth("OFF")/2,height+1);
        g2.drawString("ON",start+61-fontMetrics.stringWidth("ON")/2,height+1);
        g2.fillRoundRect(start+(int)(position*42),0,42,height+8,10,10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(start+2+(int)(position*42),2,38,height+4,8,8);
    }
    public boolean getState(){
        return switchstate;
    }
    private class MouseEventListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            switchstate = !switchstate;
        }
    }
}
