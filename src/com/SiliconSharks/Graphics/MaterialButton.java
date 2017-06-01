package com.SiliconSharks.Graphics;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bandi on 5/30/2017.
 * Button class produces much better buttons than the standard ones in Java Swing
 */
public class MaterialButton extends JComponent {
    //properties
    private int width;
    private int height;

    private String caption;
    private Boolean isPressed;
    private Color foreColor;
    private Color backColor;
    private int leftSpacing;
    private int rightSpacing;
    private int topSpacing;
    private int bottomSpacing;
    private int verticalMargin;
    private int horizontalMargin;
    private boolean isHovering;
    private int posX;
    private int posY;
    private Font font24Pt;

    private static final boolean IS_HIGH_QUALITY = true;

    public MaterialButton(String caption,int width, int height){
        font24Pt = new Font("Helvetica" , Font.PLAIN, 24);
        this.setCaption(caption);
        this.setBackColor(Color.GRAY);
        this.setForeColor(Color.white);
        this.setWidth(width);
        this.setHeight(height);
    }

    public void setCaption(String Caption){this.caption = Caption;}

    public void setWidth(int Width){this.width = Width;}

    public void setHeight(int Height){this.height = Height;}

    public void setLeftSpacing(int spacing){this.leftSpacing = spacing;}

    public void setRightSpacing(int spacing){this.rightSpacing = spacing;}

    public void setTopSpacing(int spacing){this.topSpacing = spacing;}

    public void setBottomSpacing(int spacing){this.bottomSpacing = spacing;}

    public void setForeColor(Color color){this.foreColor = color;}

    public void setBackColor(Color color){this.backColor = color;}

    public void press(){
        this.isPressed = true;
    }

    public void lift(){
        this.isPressed = false;
    }

    public void setIsHovering(boolean Hovering){this.isHovering = false;}

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.DARK_GRAY);
        if(IS_HIGH_QUALITY) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }
}
