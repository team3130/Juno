package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team3130.robot.RobotMap;


public class Arm extends Subsystem {
    //Instance Handling
    private static Arm m_pInstance;

    public static Arm GetInstance() {
        if (m_pInstance == null) m_pInstance = new Arm();
        return m_pInstance;
    }

    //Create necessary objects
    private static WPI_TalonSRX m_elbow;
    private static WPI_TalonSRX m_wrist;


    //Create and define all standard data types needed
    

    private Arm() {

        m_elbow = new WPI_TalonSRX(RobotMap.CAN_ARMELBOW);
        m_wrist = new WPI_TalonSRX(RobotMap.CAN_ARMWRIST);


        //Talon reset for Intake motors
        m_elbow.configFactoryDefault();
        m_wrist.configFactoryDefault();

        //setNeutralMode for Talons
        m_elbow.setNeutralMode(NeutralMode.Brake);
        m_wrist.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    protected void initDefaultCommand() {

    }

    /**
     * Run Wrist motor manually
     * @param speed percent value
     */
    public static void runWrist(double speed){
        m_wrist.set(ControlMode.PercentOutput,speed);
    }

    /**
     * Run Elbow motor manually
     * @param speed percent value
     */
    public static void runElbow(double speed){
        m_elbow.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Gets the angle of the Wrist motor
     * @return Angle of Wrist in degrees in relation to the arm (not relative the ground)
     */
    public static double getWristAngle(){
        return m_wrist.getSelectedSensorPosition(0) / RobotMap.kWristTicksPerDeg;
    }

    /**
     * Gets the angle of the Elbow motor
     * @return Angle of Elbow in degrees in relation to the elevator (this should be absolute to the ground)
     */
    public static double getElbowAngle(){
        return m_elbow.getSelectedSensorPosition(0) / RobotMap.kElbowTicksPerDeg;
    }

    /**
     *  Move the arm Wrist motor to an angle relative to the arm
     * @param angle The angle setpoint to go to in degrees
     */
    public synchronized static void setAngleWrist(double angle) {
        m_wrist.set(ControlMode.PercentOutput, 0.0); //Set talon to other mode to prevent weird glitches
        configMotionMagic(m_wrist, RobotMap.kWristMaxAcc, RobotMap.kWristMaxVel);
        m_wrist.set(ControlMode.MotionMagic, RobotMap.kWristTicksPerDeg * angle);

    }

    /**
     *  Move the arm Elbow motor to an absolute angle
     * @param angle The angle setpoint to go to in degrees
     */
    public synchronized static void setAngleElbow(double angle) {
        m_elbow.set(ControlMode.PercentOutput, 0.0); //Set talon to other mode to prevent weird glitches
        configMotionMagic(m_elbow, RobotMap.kElbowMaxAcc, RobotMap.kElbowMaxVel);
        m_elbow.set(ControlMode.MotionMagic, RobotMap.kElbowTicksPerDeg * angle);
    }

    /**
     *  Hold the Wrist's current angle by PID motion magic closed loop
     */
    public static void holdAngleWrist() {
        setAngleWrist(getWristAngle());
    }

    /**
     *  Hold the Elbow's current angle by PID motion magic closed loop
     */
    public static void holdAngleElbow() {
        setAngleElbow(getElbowAngle());
    }

    //C O N F I G M O T I O N M A G I C M E T H O D
    private static void configMotionMagic(WPI_TalonSRX yoskiTalon, int acceleration, int cruiseVelocity){
        yoskiTalon.configMotionCruiseVelocity(cruiseVelocity, 0);
        yoskiTalon.configMotionAcceleration(acceleration, 0);
    }

}
