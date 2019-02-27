package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3130.robot.Robot;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.commands.Chassis.DefaultDrive;
import frc.team3130.robot.sensors.Navx;
import frc.team3130.robot.util.PIDCustom;


public class Chassis extends Subsystem {

    //Instance Handling
    private static Chassis m_pInstance;
    public static Chassis GetInstance()
    {
        if(m_pInstance == null) m_pInstance = new Chassis();
        return m_pInstance;
    }

    //Create necessary objects
    private static DifferentialDrive m_drive;

    private static WPI_TalonSRX m_leftMotorFront;
    private static WPI_TalonSRX m_leftMotorRear;
    private static WPI_TalonSRX m_rightMotorFront;
    private static WPI_TalonSRX m_rightMotorRear;

    private static Solenoid m_shifter;

    private static PIDCustom ChassisPID;

    //Create and define all standard data types needed
    public static final double InchesPerRev = ((RobotMap.kLWheelDiameter + RobotMap.kRWheelDiameter)/ 2.0) * Math.PI;

    private Chassis() {

        m_leftMotorFront = new WPI_TalonSRX(RobotMap.CAN_LEFTMOTORFRONT);
        m_leftMotorRear = new WPI_TalonSRX(RobotMap.CAN_LEFTMOTORREAR);
    	m_rightMotorFront = new WPI_TalonSRX(RobotMap.CAN_RIGHTMOTORFRONT);
        m_rightMotorRear = new WPI_TalonSRX(RobotMap.CAN_RIGHTMOTORREAR);

        m_leftMotorFront.configFactoryDefault();
        m_leftMotorRear.configFactoryDefault();
        m_rightMotorFront.configFactoryDefault();
        m_rightMotorRear.configFactoryDefault();
        
        m_leftMotorFront.setNeutralMode(NeutralMode.Brake);
        m_rightMotorFront.setNeutralMode(NeutralMode.Brake);
        m_leftMotorRear.setNeutralMode(NeutralMode.Brake);
        m_rightMotorRear.setNeutralMode(NeutralMode.Brake);


        m_leftMotorRear.set(ControlMode.Follower, RobotMap.CAN_LEFTMOTORFRONT);
        m_rightMotorRear.set(ControlMode.Follower, RobotMap.CAN_RIGHTMOTORFRONT);
        
        m_drive = new DifferentialDrive(m_leftMotorFront, m_rightMotorFront);
        m_drive.setSafetyEnabled(false);

        m_shifter = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_SHIFT);

        m_shifter.set(false); //robot init should start robot in high gear (disabled also should be high gear)

        ChassisPID = new PIDCustom(RobotMap.kChassisHighP, RobotMap.kChassisHighI, RobotMap.kChassisHighD);

        m_rightMotorFront.setInverted(false);
        m_leftMotorFront.setInverted(true);
        m_rightMotorRear.setInverted(false);
        m_leftMotorRear.setInverted(true);

        m_rightMotorFront.setSensorPhase(false);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new DefaultDrive());
    }

    public static void DriveTank(double moveL, double moveR)
    {
        m_drive.tankDrive(moveL, moveR, false);
    }

    public static void DriveArcade(double moveThrottle, double turnThrottle, boolean squaredinputs){
        m_drive.arcadeDrive(moveThrottle, turnThrottle, squaredinputs);
    }

    /**
     * Shifts the drivetrain gear box into an absolute gear
     * @param shiftVal false is high gear, true is low gear
     */
    public static void shift(boolean shiftVal)
    {
        m_shifter.set(shiftVal);
    }

    public static boolean getShift() {
        return m_shifter.get();
    }
    /**
     * Returns if robot is in low gear
     * @return true means robot is in low gear, false if it's in high gear
     */
    public static boolean isLowGear(){
        return m_shifter.get();
    }

    /**
     * Gets absolute distance traveled by the left side of the robot
     * @return The absolute distance of the left side in inches
     */
    public static double getDistanceL()
    {
        return (m_leftMotorFront.getSelectedSensorPosition(0)/RobotMap.kDriveCodesPerRev) * InchesPerRev ;
    }

    /**
     * Gets absolute distance traveled by the right side of the robot
     * @return The absolute distance of the right side in inches
     */
    public static double getDistanceR()
    {
        return (m_rightMotorFront.getSensorCollection().getQuadraturePosition()/RobotMap.kDriveCodesPerRev) * InchesPerRev;
    }

    /**
     * Gets the absolute distance traveled by the robot
     * @return The absolute distance traveled of robot in inches
     */
    public static double getDistance()
    {
        return (getDistanceL() + getDistanceR()) / 2.0; //the average of the left and right distances
    }

    public static void setPIDValues()
    {
        if(getShift()) { //Chassis is in high gear
            ChassisPID.setPID(RobotMap.kChassisHighP, RobotMap.kChassisHighI, RobotMap.kChassisHighD);
        }else{
            ChassisPID.setPID(RobotMap.kChassisLowP, RobotMap.kChassisLowI, RobotMap.kChassisLowD);
        }
    }


    /**
     * Returns the absolute angle of the drivetrain in relation to the robot's orientation upon last reset.
     * @return angle in degrees
     */
    public static double getAngle()
    {
        if(Navx.getNavxPresent())
        {
            //Gyro is present so use it's heading, has drift
            return Navx.getAngle();
        }else {
            //Means that angle use wants a driftless angle measure that lasts.
            return (getDistanceR() - getDistanceL()) * 180 / (RobotMap.kChassisWidth * Math.PI);
            /*
             *  Angle is 180 degrees times encoder difference over Pi * the distance between the wheels
             *	Made from geometry and relation between angle fraction and arc fraction with semicircles.
             */
        }
    }

    /**
     * Tell the Chassis to hold a relative angle
     * @param angle angle to hold in degrees
     */
    public static void holdAngle(double angle)
    {
        //TODO: Rework
    }

    public static void talonsToCoast(boolean coast)
    {
        if (coast){
            m_leftMotorFront.setNeutralMode(NeutralMode.Coast);
            m_leftMotorRear.setNeutralMode(NeutralMode.Coast);
            m_rightMotorFront.setNeutralMode(NeutralMode.Coast);
            m_rightMotorRear.setNeutralMode(NeutralMode.Coast);
        } else {
            m_leftMotorFront.setNeutralMode(NeutralMode.Brake);
            m_leftMotorRear.setNeutralMode(NeutralMode.Brake);
            m_rightMotorFront.setNeutralMode(NeutralMode.Brake);
            m_rightMotorRear.setNeutralMode(NeutralMode.Brake);
        }
    }


    /**
     * Returns the current speed of the front left motor
     * @return Current speed of the front left motor (inches per second)
     */
    public static double getSpeedL()
    {
        // The raw speed units will be in the sensor's native ticks per 100ms.
        return 10.0 * m_leftMotorFront.getSelectedSensorVelocity(0) * InchesPerRev / RobotMap.kDriveCodesPerRev;
    }

    /**
     * Returns the current speed of the front right motor
     * @return Current speed of the front right motor (inches per second)
     */
    public static double getSpeedR()
    {
        // The raw speed units will be in the sensor's native ticks per 100ms.
        return 10.0 * m_rightMotorFront.getSelectedSensorVelocity(0) * InchesPerRev / RobotMap.kDriveCodesPerRev;
    }

    /**
     * Returns the current speed of the robot by averaging the front left and right motors
     * @return Current speed of the robot
     */
    public static double getSpeed()
    {
        return 0.5 * (getSpeedL() + getSpeedR());
    }

    /**
     *
     * @return Raw absolute encoder ticks of the left side of the robot
     */
    public static double GetRawL(){
        return m_leftMotorFront.getSelectedSensorPosition(0);
    }

    /**
     *
     * @return Raw absolute encoder ticks of the right side of the robot
     */
    public static double GetRawR(){
        return m_rightMotorFront.getSelectedSensorPosition(0);
    }

    /**
     *
     * @return Returns the left main drive Talon
     */
    public static WPI_TalonSRX getFrontL(){
        return m_leftMotorFront;
    }

    /**
     *
     * @return Returns the right main drive Talon
     */
    public static WPI_TalonSRX getFrontR(){ return m_rightMotorFront; }

    public static void outputToSmartDashboard() {
        SmartDashboard.putNumber("Chassis Right Velocity", m_rightMotorFront.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Chassis Left Velocity", m_leftMotorFront.getSelectedSensorVelocity(0));


        SmartDashboard.putNumber("Chassis Right Sensor Value", m_rightMotorFront.getSelectedSensorPosition());
        SmartDashboard.putNumber("Chassis Left Sensor Value", m_leftMotorFront.getSelectedSensorPosition());

        SmartDashboard.putNumber("Chassis Right Output %", m_rightMotorFront.getMotorOutputPercent());
        SmartDashboard.putNumber("Chassis Left Output %", m_leftMotorFront.getMotorOutputPercent());

    }

}
