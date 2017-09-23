package com.SiliconSharks.Graphics;

import com.SiliconSharks.ROVComponents.ROVInfo;
import com.SiliconSharks.ROVComponents.ROVStatus;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bandi on 8/29/2017.
 */
public class dataGraph extends JPanel{
    private ROVStatus[] rovStatuses;
    public dataGraph(){
        rovStatuses = ROVInfo.getRovStatuses();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        int numItems = rovStatuses.length;
        g2.drawLine(1,1,400,200);
        for(int i = 0; i < numItems-1; i++){
            g2.drawLine(i*400/numItems,(int)(rovStatuses[i].getVoltage()*200/15),(i+1)*400/numItems,(int)(rovStatuses[i+1].getVoltage()*200/15));
        }
    }
}
