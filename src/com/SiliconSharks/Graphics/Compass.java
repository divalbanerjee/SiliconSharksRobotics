package com.SiliconSharks.Graphics;

import javax.swing.*;
import java.awt.*;

/**i
 * Created by bandi on 6/1/2017.
 */
public class Compass extends JPanel{
    private String compassLabel;
    private double myAngle;
    private Font font20Pt = new Font("Helvetica" , Font.PLAIN, 20);

    public Compass(String label){
        compassLabel = label;
        myAngle  = 0;
        this.setSize(new Dimension(150,170));
        setBackground(new Color(84, 110, 122));
    }

    public void setCompassLabel(String lbl){
        this.compassLabel = lbl;
    }

    public void setMyAngle(double angle){
        this.myAngle = angle;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(new Color(207, 216, 220));

        //Fixed scaling issues
        g2.drawOval(this.getWidth()/2-75,this.getHeight()/2-85,150,150);
        //g2.drawOval(0,1,150,150);
        g2.drawLine(this.getWidth()/2,this.getHeight()/2 -85,this.getWidth()/2,
                this.getHeight()/2 +65);
        g2.drawLine(this.getWidth()/2 -75,this.getHeight()/2-10,
                this.getWidth()/2 + 75,this.getHeight()/2-10);

        g2.setFont(font20Pt);
        g2.drawString(this.compassLabel,this.getWidth()/2 - 70,this.getHeight()/2+90);
    }
}
