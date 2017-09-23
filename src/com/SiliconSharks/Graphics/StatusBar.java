package com.SiliconSharks.Graphics;

import com.SiliconSharks.ROVComponents.ROVInfo;
import com.SiliconSharks.ROVComponents.ROVStatus;
import com.SiliconSharks.Serial.SerialCommunication;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by bandi on 6/1/2017.
 */
public class StatusBar extends JPanel {

    private double rovVoltage;
    private double rovCurrent;
    private Long initialSystemTime;
    private Long timeElapsed;
    private boolean isNominal;
    private BufferedImage rovIcon = null;
    private Font font20Pt = new Font("Impact" , Font.PLAIN, 60);
    private Font font10Pt = new Font("Impact", Font.PLAIN,15);
    DecimalFormat df = new DecimalFormat("#,###,##0.0000");
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
        ROVStatus rovStatus = ROVInfo.getMostRecentTelemetry();
        isNominal = rovStatus != null;
        g2.setColor(new Color(44, 62, 80));
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        if(this.rovIcon == null){
            rovIcon = getTeamIcon();
        }
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
        //g2.drawImage(teamIcon,(this.getWidth()/6)-teamIcon.getWidth()/2,
        //        (this.getHeight()/2)-teamIcon.getHeight()/2,null);
        g2.drawImage(rovIcon, this.getWidth() / 6 - 110, -10, null);

        g2.setFont(font20Pt);
        g2.setColor(new Color(236,239,241));
        if (isNominal) {
            g2.drawString("ROV is nominal", (int)(this.getWidth()-this.getWidth()/3), this.getHeight() / 2 - 30);
            g2.setFont(font10Pt);
            g2.drawString("Voltage: "+df.format(rovStatus.getVoltage()) + "V, Amperage: " + df.format(rovStatus.getAmperage()) + "A",(int)(this.getWidth()/3.8),this.getHeight()/10);
            g2.drawString("Calibrations-- System: "+rovStatus.getSystem().getCalibration() +", Accel: "+ rovStatus.getAccel().getCalibration() + ", Magnet: "+ rovStatus.getMagnet().getCalibration() + ", Gyro: "+ rovStatus.getGyro().getCalibration(),(int)(this.getWidth()/3.8),this.getHeight()*2/10);
            g2.drawString("Thruster 1: " + df.format(rovStatus.getThruster(0)) + ", Thruster 2: " + df.format(rovStatus.getThruster(1)) + ", Thruster 3: " + df.format(rovStatus.getThruster(2)),(int)(this.getWidth()/3.8),this.getHeight()*3/10);
            g2.drawString("Orientation- X: "+ df.format(rovStatus.getSystem().getX())+", Y: " + df.format(rovStatus.getSystem().getY()) + ", Z: " + df.format(rovStatus.getSystem().getZ()),(int)(this.getWidth()/3.8),this.getHeight()*4/10);
            g2.drawString("Accelerometer- X: "+ df.format(rovStatus.getAccel().getX())+", Y: " + df.format(rovStatus.getAccel().getY()) + ", Z: " + df.format(rovStatus.getAccel().getZ()),(int)(this.getWidth()/3.8),this.getHeight()*5/10);
            g2.drawString("Gyroscope- X: "+ df.format(rovStatus.getGyro().getX())+", Y: " + df.format(rovStatus.getGyro().getY()) + ", Z: " + df.format(rovStatus.getGyro().getZ()),(int)(this.getWidth()/3.8),this.getHeight()*6/10);
            g2.drawString("Magnetic- X: "+ df.format(rovStatus.getMagnet().getX())+", Y: " + df.format(rovStatus.getMagnet().getY()) + ", Z: " + df.format(rovStatus.getMagnet().getZ()),(int)(this.getWidth()/3.8),this.getHeight()*7/10);
        }
        else{
            g2.drawString("Error", this.getWidth()-this.getWidth()/6,this.getHeight()/2-30);
        }
    }
}
