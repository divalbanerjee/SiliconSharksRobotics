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
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(new Color(207,216,220));
        g2.setFont(font);
        g2.drawString(label,50,fontMetrics.getHeight()*4/5);
        g2.setColor(colors[status]);
        g2.fillRoundRect(2,0,45,fontMetrics.getHeight(),18,12);
    }
}
