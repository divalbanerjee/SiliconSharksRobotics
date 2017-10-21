package com.SiliconSharks.Graphics;

import com.SiliconSharks.ROVComponents.ROVInfo;
import com.SiliconSharks.ROVComponents.ROVStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by bandi on 8/29/2017.
 */
public class DataGraph extends JPanel{
    private ROVStatus[] rovStatuses;
    private int type;
    public DataGraph(int type){
        rovStatuses = ROVInfo.getRovStatuses();
        this.type = type;
    }
    private int getY1(ROVStatus rovStatus){
        if(rovStatus.getTimeStamp() <= -1) return 0;
        switch(type){
            case 0: return (int)(rovStatus.getVoltage()*230/15);
            case 1: return (int)(rovStatus.getAmperage()*230/30);
            case 2: return (int)(rovStatus.getThrusterAmperage(0)*230/30);
            case 3: return (int)(rovStatus.getThrusterAmperage(1)*230/30);
            case 4: return (int)(rovStatus.getThrusterAmperage(2)*230/30);
        }
        return 0;
    }
    private int getY2(ROVStatus rovStatus){
        if(rovStatus.getTimeStamp() <= -1) return 0;
        switch(type){
            case 1: return (int)((Math.abs(rovStatus.getThruster(0))+Math.abs(rovStatus.getThruster(1))+Math.abs(rovStatus.getThruster(2)))*25*230/30);
            case 2: return (int)(Math.abs(rovStatus.getThruster(0))*25*230/30);
            case 3: return (int)(Math.abs(rovStatus.getThruster(1))*25*230/30);
            case 4: return (int)(Math.abs(rovStatus.getThruster(2))*25*230/30);
        }
        return 0;
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.GREEN);
        g2.drawLine(20,20,20,230);
        g2.drawLine(20,230,230,230);
        g2.setColor(Color.YELLOW);
        AffineTransform orig = g2.getTransform();
        g2.translate(125,125);
        g2.rotate(-Math.PI/2);
        switch(type){
            case 0:{
                g2.drawString("Voltage",-30,-110);
                g2.setTransform(orig);
                g2.drawString("12",0,70);
                g2.drawLine(20,66,230,66);
                break;
            }
            case 1:{
                g2.drawString("Amperage",-35,-110);
                g2.setTransform(orig);
                g2.drawString("25",0,63);
                g2.drawLine(20,58,230,58);
                break;
            }
            default:{
                g2.drawString("T"+ String.valueOf(type-1)+" Amperage",-42,-110);
                g2.setTransform(orig);
                g2.drawString("25",0,63);
                g2.drawLine(20,58,230,58);
                break;
            }
        }
        int prevY = getY1(rovStatuses[0]);
        int newY;
        g2.setColor(Color.RED);
        for(int i = 0; i < 105; i++){
            newY = getY1(rovStatuses[i+1]);
            g2.drawLine(i*2+20,230-prevY,(i+1)*2+20,230-newY);
            prevY = newY;
        }
        prevY = getY2(rovStatuses[0]);
        g2.setColor(Color.cyan);
        if(type >= 1){
            for(int i = 0; i < 105; i++){
                newY = getY2(rovStatuses[i+1]);
                g2.drawLine(i*2+20,230-prevY,(i+1)*2+20,230-newY);
                prevY = newY;
            }
        }
    }
}
