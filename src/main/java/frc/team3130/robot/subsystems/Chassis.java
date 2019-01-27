package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.commands.DefaultDrive;
import edu.wpi.first.wpilibj.SPI;


public class Chassis extends Subsystem{

    //Instance Handling
    private static Chassis m_pInstance;
    public static Chassis GetInstance()
    {
        if(m_pInstance == null) m_pInstance = new Chassis();
        return m_pInstance;
    }

    private static DifferentialDrive m_drive;

    private static WPI_TalonSRX m_leftMotorFront;
    private static WPI_TalonSRX m_leftMotorRear;
    private static WPI_TalonSRX m_rightMotorFront;
    private static WPI_TalonSRX m_rightMotorRear;
    private static Solenoid m_shifter;
    private static AHRS m_navX;

    //Create and define all standard data types needed

    private static boolean m_bNavXPresent;


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
        m_leftMotorRear.set(ControlMode.Follower, RobotMap.CAN_LEFTMOTORFRONT);
        m_rightMotorRear.set(ControlMode.Follower, RobotMap.CAN_RIGHTMOTORFRONT);
        
        m_drive = new DifferentialDrive(m_leftMotorFront, m_rightMotorFront);
        m_drive.setSafetyEnabled(false);



        m_shifter = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_SHIFT);
        //robot init should start robot in high gear (disabled also should be high gear)


        try{
            //Connect to navX Gyro on MXP port.
            m_navX = new AHRS(SPI.Port.kMXP);
            m_bNavXPresent = true;
        } catch(Exception ex){
            //If connection fails log the error and fall back to encoder based angles.
            String str_error = "Error instantiating navX from MXP: " + ex.getLocalizedMessage();
            DriverStation.reportError(str_error, true);
            m_bNavXPresent = false;
        }

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
     * Shifts the drivetrain gear box into absolute gear
     * @param shiftVal false is high gear, true is low gear
     */
    public static void shift(boolean shiftVal)
    {

        m_shifter.set(shiftVal);

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
    public static double GetDistanceL()
    {
        return (m_leftMotorFront.getSelectedSensorPosition(0)/RobotMap.kDriveCodesPerRev) * InchesPerRev ;
    }


    /**
     * Gets absolute distance traveled by the right side of the robot
     * @return The absolute distance of the right side in inches
     */
    public static double GetDistanceR()
    {
        return (m_rightMotorFront.getSensorCollection().getQuadraturePosition()/RobotMap.kDriveCodesPerRev) * InchesPerRev;
    }

    /**
     * Gets the absolute distance traveled by the robot
     * @return The absolute distance traveled of robot in inches
     */
    public static double GetDistance()
    {
        return (GetDistanceL() + GetDistanceR()) / 2.0; //the average of the left and right distances
    }



    public static void TalonsToCoast(boolean coast)
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


    public static double getAngle()
    {

        if(m_bNavXPresent)
        {
            //Angle use wants a faster, more accurate, but drifting angle, for quick use.
            //System.out.println(m_navX.getAngle());
            return m_navX.getAngle();
        }else {
            //Means that angle use wants a driftless angle measure that lasts.
            return ( GetDistanceR() - GetDistanceL() ) * 180 / (RobotMap.kChassisWidth * Math.PI);
            /* Angle is 180 degrees times encoder difference over Pi * the distance between the wheels
             * Made from geometry and relation between angle fraction and arc fraction with semicircles.
             */
        }
    }

    /**
     * Returns the current rate of change of the robots heading
     *
     * <p> GetRate() returns the rate of change of the angle the robot is facing,
     * with a return of negative one if the gyro isn't present on the robot,
     * as calculating the rate of change of the angle using encoders is not currently being done.
     * @return the rate of change of the heading of the robot.
     */
    public static double GetRate()
    {
        if(m_bNavXPresent) return m_navX.getRate();
        return -1;
    }

    /**
     * Returns the current speed of the front left motor
     * @return Current speed of the front left motor (RPS)
     */
    public static double GetSpeedL()
    {
        // The speed units will be in the sensor's native ticks per 100ms.
        return 10.0 * m_leftMotorFront.getSelectedSensorVelocity(0) * InchesPerRev / RobotMap.kDriveCodesPerRev;
    }

    /**
     * Returns the current speed of the front right motor
     * @return Current speed of the front right motor (RPS)
     */
    public static double GetSpeedR()
    {
        // The speed units will be in the sensor's native ticks per 100ms.
        return 10.0 * m_rightMotorFront.getSelectedSensorVelocity(0) * InchesPerRev / RobotMap.kDriveCodesPerRev;
    }

    /**
     * Returns the current speed of the robot by averaging the front left and right motors
     * @return Current speed of the robot
     */
    public static double GetSpeed()
    {
        return 0.5 * (GetSpeedL() + GetSpeedR());
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



}
