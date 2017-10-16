package com.SiliconSharks.Graphics;

import javax.swing.*;
import java.awt.*;

public class StatusIndicator extends JPanel{
    private String label;
    private int status = 0;
    private Color[] colors = new Color[]{new Color(230,45,50),
    new Color(210,110,30),
    new Color(200,200,50),
    new Color(70,200,100)};
    private Font font = new Font("Helvetica",Font.PLAIN,15);
    private FontMetrics fontMetrics = getFontMetrics(font);
    public StatusIndicator(String label){
        this.label = label;
        this.setPreferredSize(new Dimension(fontMetrics.stringWidth(label)+50,fontMetrics.getHeight()));
    }

    public void setStatus(int status) {
        if(status != this.status) {
            this.status = status;
            this.repaint();
        }
    }

    public void paintComponent(Graphics g){
        g.setColor(new Color(207,216,220));
        g.setFont(font);
        g.drawString(label,0,fontMetrics.getHeight()*3/4);
        g.setColor(colors[status]);
        g.fillRoundRect(fontMetrics.stringWidth(label)+5,0,45,fontMetrics.getHeight(),18,12);
    }
}
