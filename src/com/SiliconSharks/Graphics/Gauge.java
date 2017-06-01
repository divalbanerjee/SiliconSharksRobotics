package com.SiliconSharks.Graphics;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bandi on 6/1/2017.
 */
public class Gauge extends JComponent {
    private double minVal;
    private double maxVal;
    private double currentVal;
    private double scale;
    private Color backColor;
    private Color foreColor;
    private String gaugeLabel;


    public Gauge(double min, double max, String label){
        this.minVal = min;
        this.maxVal = max;
        this.currentVal = min;
    //    this.backColor = new Color(Color.DARK_GRAY);
     //   this.foreColor = new Color(Color.RED);
        this.gaugeLabel = label;
    }

    public void setMin(int min){this.minVal = min;}

    public void setMax(int max){this.maxVal = max;}

    public void setCurrentVal(int current){this.currentVal = current;}

    public void setScale(double size){this.scale = size;}

    public void setBackColor(Color back){this.backColor = back;}

    public void setForeColor(Color foreGround){this.foreColor = foreGround;}

    public void paintComponent(Graphics g){
        super.paintComponent(g);

    }
}
