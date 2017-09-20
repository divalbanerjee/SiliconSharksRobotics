package com.SiliconSharks.ROVComponents;

import com.SiliconSharks.Settings;

public class ROVStatus {
    public class Sensor{
        private int calibration;
        private double X, Y ,Z;
        private Sensor(){}
        public void setX(double x) {X = x; }
        public double getX() {return X;}
        public void setY(double y) {Y = y; }
        public double getY() {return Y;}
        public void setZ(double z) {Z = z; }
        public double getZ() {return Z;}
        public void setCalibration(int calibration) { this.calibration = calibration; }
        public int getCalibration() {return calibration;}
    }
    private int numThrusters = 3, numServos = 3;
    private double thrusters[] = new double[numThrusters];
    private double servos[] = new double[numServos];
    private double AmpScale = -1;
    private double Amperage = 0, Voltage = 0, Temperature =0;
    private Sensor System = new Sensor(), Magnet = new Sensor(), Accel = new Sensor(), Gyro = new Sensor(); // System sensor includes system calibration and orientation
    private int TimeStamp;
    private boolean TelemetryUpdated = false;
    public ROVStatus(int TimeStamp){
        this.TimeStamp = TimeStamp;
        numThrusters = Settings.getSetting("NumThrusters");
        numServos = Settings.getSetting("NumServos");
        for (int i = 0; i < numThrusters; i++) thrusters[i] = 0;
        for (int i = 0; i < numServos; i++) servos[i] = 0;
    }

    public boolean isTelemetryUpdated() {return TelemetryUpdated; }
    public void setTelemetryUpdated(boolean telemetryUpdated) { TelemetryUpdated = telemetryUpdated; }
    public int getTimeStamp(){return TimeStamp;}
    public Sensor getAccel() {return Accel;}
    public Sensor getGyro() { return Gyro; }
    public Sensor getMagnet() {return Magnet;}
    public Sensor getSystem() {return System;}
    private double trim(double a){
        a = (a > 1) ? 1: a;
        a = (a < -1) ? -1: a;
        return a;
    }
    public void calibrate(int nServos){
        //this function is designed for a 25 Amp cap, 23 for safety leeway
        double AmpCap = 23-nServos;
        double AmpExp = ((Math.abs(thrusters[0])+Math.abs(thrusters[1])+Math.abs(thrusters[2]))*25.0);
        if(AmpExp > AmpCap) {
            AmpScale = (AmpCap / AmpExp);
            thrusters[0] *= AmpScale;
            thrusters[1] *= AmpScale;
            thrusters[2] *= AmpScale;
        }else{
            AmpScale = -1;
        }
    }
    public void setThruster(int index, double value){thrusters[index] = trim(value);}
    public void setServo(int index, double value){servos[index] = trim(value);}
    public double getThruster(int index){return thrusters[index];}
    public double getServo(int index){return servos[index];}
    public void setAmperage(double amperage) { Amperage = amperage; }
    public void setTemperature(double temperature) { Temperature = temperature; }
    public void setVoltage(double voltage) { Voltage = voltage; }
    public double getAmperage(){return Amperage;}
    public double getTemperature() { return Temperature;}
    public double getVoltage() { return Voltage; }
    public double getAmpScale(){return AmpScale;}
}