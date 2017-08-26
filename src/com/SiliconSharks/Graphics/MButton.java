package com.SiliconSharks.Graphics;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bandi on 8/22/2017.
 */

public class MButton extends JPanel {
    Font myFont = new Font("Helvetica", Font.PLAIN, 20);
    String myButtonText = "New Button";
    private int myButtonPadding = 5;
    private int myButtonWidth = 150;
    private int myVerticalShift = 0;
    private int myButtonHeight = 60;
    private int myHorizontalShift = 20;
    private boolean centered = false;
    private Color myPaddingColor = Color.darkGray;
    private Color myBackColor = Color.gray;
    int myPush = 0;

    public MButton() {

    }

    public MButton(Color backColor){
        this.myBackColor = backColor;
    }

    public MButton(Color backColor, Color paddingColor, String buttonText){
        this.myBackColor = backColor;
        this.myPaddingColor = paddingColor;
        this.myButtonText = buttonText;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(new Color(100,100,100,0));
        g2.fillRect(0,0,getWidth(),getHeight());
        System.out.println(getWidth());
        System.out.println(getHeight());

        myVerticalShift = getHeight() / 2 - myButtonHeight / 2;
        g2.setFont(myFont);

        if(centered == true){
            myHorizontalShift = (getWidth()- myButtonWidth)/2;
        }
        //padded rectangle drawn
        g2.setColor(this.myPaddingColor);
        g2.fillRoundRect(myHorizontalShift + this.myButtonPadding, myVerticalShift + this.myButtonPadding, this.myButtonWidth, this.myButtonHeight, 25, 25); //xpos, y pos, width, height

        //padded
        g2.setColor(this.myBackColor);
        g2.fillRoundRect(myHorizontalShift + this.myPush, myVerticalShift + this.myPush, this.myButtonWidth, this.myButtonHeight, 25, 25); //xpos, y pos, width, height

        g2.setColor(Color.white);
        //g.drawString(myButtonText,(int)(getWidth()/2-myButtonText.length()*myFont.getSize()/2), (int)(getHeight()/2*myFont.getSize()/2));
        g2.drawString(this.myButtonText, (myHorizontalShift+(this.myButtonWidth / 2)) - (this.myButtonText.length()*5), myVerticalShift + 5 + this.myButtonHeight / 2);

    }

    public void setFont(Font font) {
        this.myFont = font;
    } //change font

    public void setText(String buttonText) {
        this.myButtonText = buttonText;
    } //Change text

    public void setPadding(int paddingAmount) {
        this.myButtonPadding = paddingAmount;
    } //Change text

    public void setPaddingColor(Color paddingColor) {
        this.myPaddingColor = paddingColor;
    } //change padding color of button

    public void setCenter(boolean center){
        this.centered = center;
    }

    public void setButtonWidth(int length){
        this.myButtonWidth = length;
    }

    public void setBackColor(Color backColor){
        this.myBackColor = backColor;
    }

    public void push(Boolean Pushed) {
        if (Pushed = true) {
            this.myPush = this.myButtonPadding;
        } else if (Pushed = false) {
            this.myPush = 0;
        }
    }
}
