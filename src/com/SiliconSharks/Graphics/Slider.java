package com.SiliconSharks.Graphics;

import javax.swing.*;
import java.awt.*;

public class Slider extends JPanel{
    private Font font = new Font("Helvetica", Font.PLAIN,15);
    private FontMetrics fontMetrics;
    private String label;
    public Slider(String label){
        this.label = label;
    }
}
