package com.SiliconSharks.Graphics;

import com.SiliconSharks.Controller.ControlSystem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

/**
 * Created by bandi on 6/1/2017.
 */
public class ControllerInterface extends JPanel{

    private ControlSystem controlSystem = new ControlSystem();
    private int myPilotNumber;
    private Font font30Pt = new Font("Helvetica" , Font.PLAIN, 30);

    public ControllerInterface(int pilotNumber){
        setBackground(new Color(44, 62, 80));
        myPilotNumber = pilotNumber;
    }

    public BufferedImage getControllerImage() {
        //Open Image
        BufferedImage controllerIMG = new BufferedImage(400,250,BufferedImage.TYPE_INT_ARGB);

        try {
            BufferedImage before = ImageIO.read(new File("src/com/SiliconSharks/" +
                    "Graphics/Images/controller.png"));

            int w = before.getWidth();
            int h = before.getHeight();
            //Scale the image
            AffineTransform at = new AffineTransform();
            at.scale(.5, .5);

            BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            AffineTransformOp scaleOp =
                    new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

            controllerIMG  = scaleOp.filter(before, after);

        } catch (IOException e) {
            System.out.println(e);
        }
        return controllerIMG;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        BufferedImage imgController = getControllerImage(); //TODO:Super inefficient fix later
        g2.drawImage(imgController,30,30,null);
        this.setPreferredSize(new Dimension(imgController.getWidth(), imgController.getHeight()+120));
        g2.setFont(font30Pt);
        g2.setColor(new Color(236, 239, 241));
        g2.drawString("Pilot " + this.myPilotNumber +"'s Controller",
                (imgController.getWidth())/2-320 ,(imgController.getHeight()/2)+70);
    }
}
