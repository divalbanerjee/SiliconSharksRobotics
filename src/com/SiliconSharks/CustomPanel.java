package com.SiliconSharks;

import com.SiliconSharks.ROVComponents.ROVInfo;
import com.SiliconSharks.ROVComponents.ROVStatus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.SiliconSharks.Main.Message;

class CustomPanel extends JFrame{
    private ROVInfo rovInfo;
    private JComponent newComponent(String type, String name,String text, Rectangle bounds, Color color){
        switch (type){
            case "Label":
                JLabel label = new JLabel();
                label.setName(name);
                label.setText(text);
                label.setBounds(bounds);
                label.setBackground(color);
                label.setVisible(true);
                label.setOpaque(true);
                return label;
        }
        Message(0,"Unhandled switch statement in CustomPanel class!");
        return new JButton();
    }
    CustomPanel(ROVInfo rovInfo){
        Container contentPane = new Container();
        contentPane.setBounds(0,0,640,480);
        contentPane.setVisible(true);
        contentPane.setLayout(null);
        contentPane.add(newComponent("Label","SerialDisplay","Serial Text",new Rectangle(0,100,200,50),Color.WHITE),BorderLayout.CENTER);
        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(20,180,180,255));
        setBounds(0,0,640,480);
        setVisible(true);
        this.rovInfo = rovInfo;
    }
    public void paintComponents(Graphics g){
        super.paintComponents(g);
        g.drawRect(10,110,300,310);
        ArrayList<ROVStatus> rovStatuses= rovInfo.getStatusArrayList();
        for (int i = 1; i < rovStatuses.size(); i++) {
            g.drawLine(10 + (i-1)*300/rovStatuses.size(),260-(int)(rovStatuses.get(i-1).getThruster(0)*150),10 + i*300/rovStatuses.size(),260-(int)(rovStatuses.get(i).getThruster(0)*150));
        }
        Message(0,Double.toString(rovInfo.getCurrentROVStatus().getAmperage()));
    }
}
