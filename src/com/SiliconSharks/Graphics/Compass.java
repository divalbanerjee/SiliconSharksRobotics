package com.SiliconSharks.Graphics;

import javax.swing.*;
import java.awt.*;

/**i
 * Created by bandi on 6/1/2017.
 */
public class Compass extends JPanel{
    private String compassLabel;
    private double myAngle;
    private Font font20Pt = new Font("Helvetica" , Font.PLAIN, 16);
    private int type;

    public Compass(String label){
        this(label,0);
    }

    public Compass(String label, int type){
        compassLabel = label;
        myAngle  = Math.PI/2;
        this.setSize(new Dimension(140,160));
        setBackground(new Color(44, 62, 80));
        this.type = type;
    }

    public void setCompassLabel(String lbl){
        this.compassLabel = lbl;
    }

    public void setMyAngle(double angle){
        if(angle != myAngle) {
            this.myAngle = angle;
            this.repaint();
        }
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
        g2.drawOval(5,5,this.getWidth()-10,this.getWidth()-10);
        //g2.drawOval(0,1,150,150);
        int length = this.getWidth()/2;
        if(type ==1){ // accelerometers
            g2.drawLine(length,length,length+(int)(Math.cos(-Math.PI/6)*(length-5)),length-(int)(Math.sin(-Math.PI/6)*(length-5)));
            g2.drawLine(length,length,length+(int)(Math.cos(Math.PI*7/6)*(length-5)),length-(int)(Math.sin(Math.PI*7/6)*(length-5)));
            g2.drawLine(length,5,length,length);
        }else if (type == 0){ // compasses
            g2.drawLine(5,length, length*2-5, length);
            g2.drawLine(length,5, length, length*2-5);
        }else if (type == 2){ // gripper
            g2.drawLine(length,length,length+(int)(Math.cos(-Math.PI/6)*(length-5)),length-(int)(Math.sin(-Math.PI/6)*(length-5)));
            g2.drawLine(length,length,length+(int)(Math.cos(Math.PI*7/6)*(length-5)),length-(int)(Math.sin(Math.PI*7/6)*(length-5)));
            g2.drawLine(length,5,length,length);
            g2.setColor(Color.RED);
            g2.drawLine(length,length,length+(int)(Math.cos(-myAngle+Math.PI)*(length-5)),length-(int)(Math.sin(-myAngle+Math.PI)*(length-5)));
        }
        g2.setColor(Color.RED);
        g2.drawLine(length,length,length+(int)(Math.cos(myAngle)*(length-5)),length-(int)(Math.sin(myAngle)*(length-5)));
        g2.setColor(new Color(207, 216, 220));
        g2.setFont(font20Pt);
        g2.drawString(this.compassLabel,this.getWidth()/2-getFontMetrics(font20Pt).stringWidth(this.compassLabel)/2,this.getHeight()-5);
    }
}
