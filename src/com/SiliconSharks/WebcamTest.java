package com.SiliconSharks;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.TimerTask;

import javax.swing.*;

import com.SiliconSharks.Graphics.*;
import com.SiliconSharks.ROVComponents.ROVInfo;
import com.SiliconSharks.ROVComponents.ROVStatus;
import com.SiliconSharks.Serial.SerialCommunication;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;

@SuppressWarnings("unchecked")

/*
  Proof of concept of how to handle webcam video stream from Java

  @author Bartosz Firyn (SarXos)
 */
public class WebcamTest extends JFrame implements Runnable, WebcamListener, WindowListener, UncaughtExceptionHandler, ItemListener, WebcamDiscoveryListener {

    private static final long serialVersionUID = 1L;

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private WebcamPicker picker = null;

    private ROVStatus lastTelemetry = new ROVStatus(-3);

    @Override
    public void run() {
        MainUpdateLoop.start();

        Webcam.addDiscoveryListener(this);
        setTitle("Java Webcam Capture POC");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setBackground(new Color(44,62,80));
        getContentPane().setBackground(new Color(44,62,80));
        addWindowListener(this);

        picker = new WebcamPicker();
        picker.addItemListener(this);

        webcam = picker.getSelectedWebcam();

        if (webcam == null) {
            System.out.println("No webcams found...");
            System.exit(1);
        }

        webcam.setCustomViewSizes(new Dimension[]{new Dimension(1280,720)});
        webcam.setViewSize(new Dimension(1280,720));
        webcam.addWebcamListener(WebcamTest.this);
        System.out.println(WebcamResolution.VGA.getSize().toString());

        panel = new WebcamPanel(webcam, false);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setFPSLimit(10);
        panel.setFPSLimited(true);

        JLabel rawData = new JLabel("Initializing...");
        rawData.setVisible(true);
        rawData.setFont(new Font("Helvetica",Font.PLAIN,14));
        rawData.setForeground(Color.WHITE);

        ControllerInterface controllerInterface1 = new ControllerInterface(1);
        controllerInterface1.setOpaque(false);
        ControllerInterface controllerInterface2 = new ControllerInterface(2);
        controllerInterface2.setOpaque(false);

        Compass compass = new Compass("Compass");
        Compass pitch = new Compass("Pitch");
        Compass roll = new Compass("Roll");
        Compass[] thrusters = new Compass[]{new Compass("Thruster 1",1),
                new Compass("Thruster 2", 1), new Compass("Thruster 3",1)};
        Compass gimbal1 = new Compass("Left Gimbal");
        Compass gimbal2 = new Compass("Right Gimbal");
        Compass gripper = new Compass("Gripper",2);

        StatusIndicator serialStatusIndicator = new StatusIndicator("Serial Connection");
        StatusIndicator telemetryStatusIndicator = new StatusIndicator("Telemetry Status");
        StatusIndicator systemStatusIndicator = new StatusIndicator("System Calibration");
        StatusIndicator gyroStatusIndicator = new StatusIndicator("Gyroscope Calibration");
        StatusIndicator magnetStatusIndicator = new StatusIndicator("Magnetometer Calibration");
        StatusIndicator accelStatusIndicator = new StatusIndicator("Accelerometer Calibration");
        StatusIndicator amperageStatusIndicator = new StatusIndicator("Amperage Status      ");

        DataGraph voltageGraph = new DataGraph(0);
        DataGraph amperageGraph = new DataGraph(1);
        DataGraph[] thrustergraphs = new DataGraph[]{new DataGraph(2), new DataGraph(3), new DataGraph(4)};

        Switch KeyboardEnabled = new Switch("KeyboardEnabled", true);

        picker.setBounds(0,0,300,20);
        panel.setBounds(320,20,1280,720);
        controllerInterface1.setBounds(0,25,300,200);
        controllerInterface2.setBounds(0,225,300,200);
        compass.setBounds(0,425,100,115);
        pitch.setBounds(100,425,100,115);
        roll.setBounds(200,425,100,115);
        for(int i = 0; i < thrustergraphs.length; i++){
            thrusters[i].setBounds(100*i,540,100,115);
        }
        gimbal1.setBounds(0,655,100,115);
        gimbal2.setBounds(100,655,100,115);
        gripper.setBounds(200,655,100,115);
        serialStatusIndicator.setBounds(1620,10,300,30);
        telemetryStatusIndicator.setBounds(1620,40,300,30);
        systemStatusIndicator.setBounds(1620,70,300,30);
        gyroStatusIndicator.setBounds(1620,100,300,30);
        magnetStatusIndicator.setBounds(1620,130,300,30);
        accelStatusIndicator.setBounds(1620,160,300,30);
        amperageStatusIndicator.setBounds(1620,190,300,30);
        rawData.setBounds(1620,210,300,200);
        voltageGraph.setBounds(0,760,250,250);
        amperageGraph.setBounds(250,760,250,250);
        for(int i = 0; i < thrustergraphs.length; i++){
            thrustergraphs[i].setBounds(i*250+500,760,250,250);
        }
        KeyboardEnabled.setBounds(1620,410,300,50);

        add(picker);
        add(panel);
        add(controllerInterface1);
        add(controllerInterface2);
        add(compass);
        add(pitch);
        add(roll);
        for(Compass compass1 : thrusters){
            add(compass1);
        }
        add(gimbal1);
        add(gimbal2);
        add(gripper);
        add(serialStatusIndicator);
        add(telemetryStatusIndicator);
        add(systemStatusIndicator);
        add(gyroStatusIndicator);
        add(magnetStatusIndicator);
        add(accelStatusIndicator);
        add(amperageStatusIndicator);
        add(rawData);
        add(voltageGraph);
        add(amperageGraph);
        for (DataGraph thrustergraph : thrustergraphs) {
            add(thrustergraph);
        }
        add(KeyboardEnabled);

        setSize(1920,1040);
        setVisible(true);

        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                //updating componenets with most recent info
                KeyboardEnabled.updateposition();
                controllerInterface1.smartRepaint();
                controllerInterface2.smartRepaint();
                ROVStatus rovStatus = ROVInfo.getMostRecentTelemetry();
                if(SerialCommunication.isConnected()) {
                    serialStatusIndicator.setStatus(3);
                }else{
                    serialStatusIndicator.setStatus(0);
                }
                if(rovStatus.getTimeStamp() <= -1){
                    telemetryStatusIndicator.setStatus(0);
                }else{
                    int diff = MainUpdateLoop.getGlobalTimeStamp() - rovStatus.getTimeStamp();
                    if(diff > 200){
                        telemetryStatusIndicator.setStatus(0);
                    }else if(diff > 100){
                        telemetryStatusIndicator.setStatus(1);
                    }else if(diff > 40){
                        telemetryStatusIndicator.setStatus(2);
                    }else{
                        telemetryStatusIndicator.setStatus(3);
                    }
                }
                if(rovStatus.getTimeStamp() != lastTelemetry.getTimeStamp()){
                    rawData.setText(ROVInfo.getStatus());
                    lastTelemetry = rovStatus;
                    compass.setMyAngle((90-rovStatus.getSystem().getX())*Math.PI/180);
                    pitch.setMyAngle((90-rovStatus.getSystem().getY())*Math.PI/180);
                    roll.setMyAngle((90+rovStatus.getSystem().getZ())*Math.PI/180);
                    for(int i = 0; i < thrusters.length; i++){
                        thrusters[i].setMyAngle(rovStatus.getThruster(i)*-Math.PI*2/3+Math.PI/2);
                    }
                    gimbal1.setMyAngle((0.5-rovStatus.getServo(0))*Math.PI);
                    gimbal2.setMyAngle((rovStatus.getServo(1)-0.5)*Math.PI);
                    gripper.setMyAngle(rovStatus.getServo(2)*Math.PI*2/3-Math.PI/6);
                    systemStatusIndicator.setStatus(rovStatus.getSystem().getCalibration());
                    gyroStatusIndicator.setStatus(rovStatus.getGyro().getCalibration());
                    magnetStatusIndicator.setStatus(rovStatus.getMagnet().getCalibration());
                    accelStatusIndicator.setStatus(rovStatus.getAccel().getCalibration());
                    voltageGraph.repaint();
                    amperageGraph.repaint();
                    for(DataGraph thrusterGraph: thrustergraphs){
                        thrusterGraph.repaint();
                    }
                }
            }
        },1000,30);


        Thread t = new Thread(() -> panel.start());
        t.setName("example-starter");
        t.setDaemon(true);
        t.setUncaughtExceptionHandler(this);
        t.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new WebcamTest());
    }

    @Override
    public void webcamOpen(WebcamEvent we) {
        System.out.println("webcam open");
    }

    @Override
    public void webcamClosed(WebcamEvent we) {
        System.out.println("webcam closed");
    }

    @Override
    public void webcamDisposed(WebcamEvent we) {
        System.out.println("webcam disposed");
    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {
        // do nothing
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        webcam.close();
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        System.out.println("webcam viewer resumed");
        panel.resume();
    }

    @Override
    public void windowIconified(WindowEvent e) {
        System.out.println("webcam viewer paused");
        panel.pause();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println(String.format("Exception in thread %s", t.getName()));
        e.printStackTrace();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getItem() != webcam) {
            if (webcam != null) {

                panel.stop();

                remove(panel);

                webcam.removeWebcamListener(this);
                webcam.close();

                webcam = (Webcam) e.getItem();
                webcam.setCustomViewSizes(new Dimension[]{new Dimension(1280,720)});
                webcam.setViewSize(new Dimension(1280,720));
                webcam.addWebcamListener(WebcamTest.this);

                System.out.println("selected " + webcam.getName());

                panel = new WebcamPanel(webcam, false);
                panel.setFPSDisplayed(true);
                panel.setDisplayDebugInfo(true);
                panel.setImageSizeDisplayed(true);
                panel.setFPSLimit(10);
                panel.setFPSLimited(true);
                panel.setBounds(320,20,1280,720);

                add(panel);

                Thread t = new Thread(() -> panel.start());
                t.setName("example-stoper");
                t.setDaemon(true);
                t.setUncaughtExceptionHandler(this);
                t.start();
            }
        }
    }

    @Override
    public void webcamFound(WebcamDiscoveryEvent event) {
        if (picker != null) {
            picker.addItem(event.getWebcam());
        }
    }

    @Override
    public void webcamGone(WebcamDiscoveryEvent event) {
        if (picker != null) {
            picker.removeItem(event.getWebcam());
        }
    }
}