package com.SiliconSharks.Graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by bandi on 6/1/2017.
 */
public class StatusBar extends JPanel {

    private double rovVoltage;
    private double rovCurrent;
    private Long initialSystemTime;
    private Long timeElapsed;
    private boolean isNominal;
    private BufferedImage rovIcon;
    private Font font20Pt = new Font("Impact" , Font.PLAIN, 60);

    public StatusBar(){
        this.rovVoltage = 0;
        this.rovCurrent = 0;
        this.initialSystemTime = System.currentTimeMillis();
        this.isNominal  = false;
    }

    public void updateDisplayTime(){
        timeElapsed = System.currentTimeMillis() - this.initialSystemTime;
        repaint();
    }

    public void updateROVCurrent(double current){
        this.rovCurrent = current;
    }

    public void updateROVVoltage(double voltage){
        this.rovVoltage = voltage;
    }

    public BufferedImage getTeamIcon(){
        BufferedImage teamIcon = new BufferedImage(400,250,BufferedImage.TYPE_INT_ARGB);

        try {
            BufferedImage before = ImageIO.read(new File("src/com/SiliconSharks/" +
                    "Graphics/Images/logo.png"));

            int w = before.getWidth();
            int h = before.getHeight();
            //Scale the image
            AffineTransform at = new AffineTransform();
            at.scale(.22, .22);

            BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            AffineTransformOp scaleOp =
                    new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

            teamIcon  = scaleOp.filter(before, after);

        } catch (IOException e) {
            System.out.println(e);
        }
        return teamIcon;
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(new Color(44, 62, 80));
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (isNominal) {
            g2.setColor(new Color(40, 145, 80));
        }
        else{
            g2.setColor(new Color(200,70,50));
        }

        g2.fillRect(0, 0, this.getWidth(), this.getHeight() / 2);
        //Creates a cool looking bar!
        int[] xPoints = new int[]{0, this.getWidth(), this.getWidth(), this.getWidth() / 2, this.getWidth() / 2 - 50, 0};
        int[] yPoints = new int[]{0, 0, this.getHeight() / 2, this.getHeight() / 2, this.getHeight(), this.getHeight()};

        g2.fillPolygon(xPoints, yPoints, 6);

        g2.setColor(new Color(236, 239, 241));
        g2.fillOval(this.getWidth() / 6 - 125, this.getHeight() / 2 - 125, 250, 250);

        g2.setColor(new Color(176, 190, 197));
        g2.fillOval(this.getWidth() / 6 - 115, this.getHeight() / 2 - 115, 230, 230);
        BufferedImage teamIcon = getTeamIcon();
        //g2.drawImage(teamIcon,(this.getWidth()/6)-teamIcon.getWidth()/2,
        //        (this.getHeight()/2)-teamIcon.getHeight()/2,null);
        g2.drawImage(teamIcon, this.getWidth() / 6 - 110, -10, null);

        g2.setFont(font20Pt);

        g2.setColor(new Color(236,239,241));
        if (isNominal) {
            g2.drawString("ROV is nominal", (int)(this.getWidth()-this.getWidth()/3.5), this.getHeight() / 2 - 30);
        }
        else{

            g2.drawString("Error", this.getWidth()-this.getWidth()/6,this.getHeight()/2-30);
        }
    }
}
