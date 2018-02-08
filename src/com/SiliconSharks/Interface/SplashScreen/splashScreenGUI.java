package com.SiliconSharks.Interface.SplashScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class splashScreenGUI extends JFrame {
    BufferedImage imgTeamLogo = null;
    Container container;

    public splashScreenGUI(){
        //Setup the splash screen window
        JPanel layoutManager = new JPanel(new GridLayout());
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        container = this.getContentPane();
        container.setBackground(new Color(40,50,60));
        layoutManager.setBackground(new Color(40,50,60));
        container.add(layoutManager);

    }

    public BufferedImage getLogoImage() {
        try {
            BufferedImage before = ImageIO.read(new File("src/com/SiliconSharks/" +
                    "Graphics/Images/controller.png"));

            int w = before.getWidth();
            int h = before.getHeight();
            //Scale the image
            AffineTransform at = new AffineTransform();
            at.scale(.3, .3);

            BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            AffineTransformOp scaleOp =
                    new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

            imgTeamLogo  = scaleOp.filter(before, after);

        } catch (IOException e) {
            System.out.println(e);
        }
        return imgTeamLogo;
    }
}
