package com.SiliconSharks.ROVComponents;

import com.SiliconSharks.Settings;

public class ROVStatus {
    public class Sensor{
        private int calibration;
        private double X, Y ,Z;
        private Sensor(){calibration=0;X=0;Y=0;Z=0;}
        public void setX(double x) {X = x; }
        public double getX() {return X;}
        public void setY(double y) {Y = y; }
        public double getY() {return Y;}
        public void setZ(double z) {Z = z; }
        public double getZ() {return Z;}
        public void setCalibration(int calibration) { this.calibration = calibration; }
        public int getCalibration() {return calibration;}
        void sum(Sensor sensor){
            this.X += sensor.X;
            this.Y += sensor.Y;
            this.Z += sensor.Z;
        }
        void scale(double factor){
            this.X *= factor;
            this.Y *= factor;
            this.Z *= factor;
        }
    }
    private int numThrusters = 3, numServos = 3;
    private double thrusters[] = new double[numThrusters];
    private double thrusterAmperage[] = new double[numThrusters];
    private double servos[] = new double[numServos];
    private double AmpScale = -1, AmpExp;
    private double Amperage = 0, Voltage = 0, Temperature =0;
    private Sensor System = new Sensor(), Magnet = new Sensor(), Accel = new Sensor(), Gyro = new Sensor(); // System sensor includes system calibration and orientation
    private int TimeStamp;
    private boolean TelemetryUpdated = false;
    public ROVStatus(int TimeStamp){
        this.TimeStamp = TimeStamp;
        numThrusters = Settings.getSetting("NumThrusters");
        numServos = Settings.getSetting("NumServos");
        for (int i = 0; i < numThrusters; i++) thrusters[i] = 0;
        for (int i = 0; i < numThrusters; i++) thrusterAmperage[i] = 0;
        for (int i = 0; i < numServos; i++) servos[i] = 0.5;
    }
    boolean isTelemetryUpdated() {return TelemetryUpdated; }
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
        AmpExp = ((Math.abs(thrusters[0])+Math.abs(thrusters[1])+Math.abs(thrusters[2]))*25.0);
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
    public void setAmperage(int amperage) { Amperage = ((double)amperage)/67.381855; }
    public void setThrusterAmperage(int amperage,int index) { thrusterAmperage[index] = ((double)amperage)/67.381855;}
    public void setTemperature(double temperature) { Temperature = temperature; }
    public void setVoltage(int voltage) { Voltage = ((double)voltage)*5.015*3/5/1024; }
    public double getAmperage(){return Amperage;}
    public double getTemperature() { return Temperature;}
    public double getThrusterAmperage(int index){return thrusterAmperage[index];}
    public double getVoltage() { return Voltage; }
    public double getAmpScale(){return AmpScale;}
    public String getString(String prefix){
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        String[] strings = getStringArray(prefix);
        for(String s : strings){
            sb.append(s).append("<br>");
        }
        sb.append("</html>");
        return sb.toString();
    }
    private String[] getStringArray(String prefix){
        return new String[] {
                prefix + " Timestamp: " + String.valueOf(TimeStamp),
                prefix + " Thruster 1: " + String.valueOf(thrusters[0]*100) + "%",
                prefix + " Thruster 2: " + String.valueOf(thrusters[1]*100) + "%",
                prefix + " Thruster 3: " + String.valueOf(thrusters[2]*100) + "%",
                prefix + " Expected Amperage: " + String.valueOf(AmpExp),
                prefix + " Measured Amperage: " + String.valueOf(Amperage),
                prefix + " Measured Voltage: " + String.valueOf(Voltage),
                prefix + " Temperature: " + String.valueOf(Temperature),
                prefix + " Left Gimbal: " + String.valueOf(servos[0]),
                prefix + " Right Gimbal: " + String.valueOf(servos[1]),
                prefix + " Gripper: " + String.valueOf(servos[2]),
        };
    }
    void Sum(ROVStatus rovStatus){
        this.TimeStamp = -3;
        this.getAccel().sum(rovStatus.getAccel());
        this.getSystem().sum(rovStatus.getSystem());
        this.getGyro().sum(rovStatus.getGyro());
        this.getMagnet().sum(rovStatus.getMagnet());
        this.Amperage += rovStatus.getAmperage();
        this.AmpExp += rovStatus.AmpExp;
        this.Voltage += rovStatus.getVoltage();
        this.Temperature += rovStatus.getTemperature();
        for (int i = 0; i < numThrusters; i++) {
            this.setThruster(i,rovStatus.getThruster(i)+this.getThruster(i));
            this.thrusterAmperage[i] += rovStatus.getThrusterAmperage(i);
        }
        for(int i = 0; i < numServos; i++){
            this.setServo(i,rovStatus.getServo(i)+this.getServo(i));
        }
    }
    void Scale(double factor){
        this.getAccel().scale(factor);
        this.getSystem().scale(factor);
        this.getGyro().scale(factor);
        this.getMagnet().scale(factor);
        this.Amperage *= factor;
        this.AmpExp *= factor;
        this.Voltage *= factor;
        this.Temperature *= factor;
        for (int i = 0; i < numThrusters; i++) {
            this.thrusters[i] *= factor;
            this.thrusterAmperage[i] *= factor;
        }
        for(int i = 0; i < numServos; i++){
            this.servos[i] *= factor;
        }
    }
}