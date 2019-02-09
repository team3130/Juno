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

    public static void runWrist(double speed){
        m_wrist.set(ControlMode.PercentOutput,speed);
    }

    public static void runElbow(double speed){
        m_elbow.set(ControlMode.PercentOutput, speed);
    }

    /**
     *  Move the arm wrist motor to an absolute angle
     * @param angle The angle setpoint to go to in degrees
     */
    public synchronized static void setAngleWrist(double angle) {
        m_wrist.set(ControlMode.PercentOutput, 0.0); //Set talon to other mode to prevent weird glitches
        configMotionMagic(m_wrist, RobotMap.kWristMaxAcc, RobotMap.kWristMaxVel);
        m_wrist.set(ControlMode.MotionMagic, RobotMap.kWristTicksPerDeg * angle);

    }

    /**
     *  Move the arm elbow motor to an absolute angle
     * @param angle The angle setpoint to go to in degrees
     */
    public synchronized static void setAngleElbow(double angle) {
        m_elbow.set(ControlMode.PercentOutput, 0.0); //Set talon to other mode to prevent weird glitches
        configMotionMagic(m_elbow, RobotMap.kElbowMaxAcc, RobotMap.kElbowMaxVel);
        m_elbow.set(ControlMode.MotionMagic, RobotMap.kElbowTicksPerDeg * angle);
    }

    //C O N F I G M O T I O N M A G I C M E T H O D
    private static void configMotionMagic(WPI_TalonSRX yoskiTalon, int acceleration, int cruiseVelocity){
        yoskiTalon.configMotionCruiseVelocity(cruiseVelocity, 0);
        yoskiTalon.configMotionAcceleration(acceleration, 0);
    }

    /**
     * * Hold the current angle by PID closed loop
     */
    public static void holdHeight() {
        setAngleElbow(getHeight());
    }

    public static void holdHeight() {
        setAngleWrist(getHeight());
    }
}
