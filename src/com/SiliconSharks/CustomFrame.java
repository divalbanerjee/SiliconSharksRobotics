package com.SiliconSharks;

import com.SiliconSharks.ROVComponents.ROVInfo;
import com.SiliconSharks.ROVComponents.ROVStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

import static com.SiliconSharks.MainUpdateLoop.Message;

class CustomFrame extends JFrame{
    private class CustomPanel extends JPanel{
        private ROVInfo rovInfo;
        CustomPanel(ROVInfo rovInfo){
            JPanel contentPanel = new JPanel();
            contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            contentPanel.setLayout(new BorderLayout(0, 0));
            this.rovInfo = rovInfo;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawRect(10,110,300,310);
            ArrayList<ROVStatus> rovStatuses= rovInfo.getStatusArrayList();
            for (int i = 1; i < rovStatuses.size(); i++) {
                g.drawLine(10 + (i-1)*300/rovStatuses.size(),260-(int)(rovStatuses.get(i-1).getThruster(0)*150),10 + i*300/rovStatuses.size(),260-(int)(rovStatuses.get(i).getThruster(0)*150));
            }
        }
        void Refresh()    {
            repaint(10,110,300,310);
        }
    }
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
        Message(0,"Unhandled switch statement in CustomFrame class!");
        return new JButton();
    }
    private CustomPanel customPanel;
    CustomFrame(ROVInfo rovInfo){
        customPanel = new CustomPanel(rovInfo);
        customPanel.setBounds(0,0,640,480);
        customPanel.setVisible(true);
        customPanel.setLayout(null);
        customPanel.add(newComponent("Label","SerialDisplay","Serial Text",new Rectangle(0,100,200,50),Color.WHITE),BorderLayout.CENTER);
        setContentPane(customPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(20,180,180,255));
        setBounds(0,0,640,480);
        setVisible(true);
    }
    private int RefreshCounter = 0;
    void Refresh(){
        RefreshCounter++;
        if(RefreshCounter >= 3){
            customPanel.Refresh();
            RefreshCounter = 0;
        }
    }
}
