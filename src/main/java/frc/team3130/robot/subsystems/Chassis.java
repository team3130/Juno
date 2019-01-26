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
    private static boolean m_bShiftedHigh;
    private static boolean m_bNavXPresent;

    private static double prevSpeedLimit;

    public static final double InchesPerRev = ((RobotMap.kLWheelDiameter + RobotMap.kRWheelDiameter)/ 2.0) * Math.PI;
    public static double moveSpeed;

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
        m_bShiftedHigh = true;

        moveSpeed = 0;

        try{
            //Connect to navX Gyro on MXP port.
            m_navX = new AHRS(SPI.Port.kMXP);
            m_bNavXPresent = true;
            //navX.setName("Chassis", "NavX");
        } catch(Exception ex){
            //If connection fails log the error and fall back to encoder based angles.
            String str_error = "Error instantiating navX from MXP: ";
            str_error += ex.getLocalizedMessage();
            DriverStation.reportError(str_error, true);
            m_bNavXPresent = false;
        }

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new DefaultDrive());
        //setDefaultCommand(new MySpecialCommand());
    }

    public static void DriveTank(double moveL, double moveR)
    {
        m_drive.tankDrive(moveL, moveR, false);
    }

    public static void DriveArcade(double moveThrottle, double turnThrottle, boolean squaredinputs){
        m_drive.arcadeDrive(moveThrottle, turnThrottle, squaredinputs);
    }

    //shifts the robot either into high or low gear
    public static void ShiftDown(boolean shiftDown)
    {
        m_shifter.set(shiftDown);
        m_bShiftedHigh = !shiftDown;
    }

    /**
     * Returns the current shift of the robot
     * @return Current shift of the robot
     */
    public static boolean GetShiftedUp(){return m_bShiftedHigh;}

    protected void usePIDOutput(double bias) {
        //Chassis ramp rate is the limit on the voltage change per cycle to prevent skidding.
        final double speedLimit = prevSpeedLimit + Preferences.getInstance().getDouble("ChassisRampRate", 0.25);
        if (bias >  speedLimit) bias = speedLimit;
        if (bias < -speedLimit) bias = -speedLimit;
        double speed_L = moveSpeed+bias;
        double speed_R = moveSpeed-bias;
        DriveTank(speed_L, speed_R);
        prevSpeedLimit = Math.abs(speedLimit);
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

    public static double GetAngle()
    {
        //System.out.println("navx "+m_bNavXPresent);
        if(m_bNavXPresent)
        {
            //Angle use wants a faster, more accurate, but drifting angle, for quick use.
            //System.out.println(m_navX.getAngle());
            return m_navX.getAngle();
        }else {
            //Means that angle use wants a driftless angle measure that lasts.
            return ( GetDistanceR() - GetDistanceL() ) * 180 / (RobotMap.kChassisWidth * Math.PI);
            /*
             *  Angle is 180 degrees times encoder difference over Pi * the distance between the wheels
             *	Made from geometry and relation between angle fraction and arc fraction with semicircles.
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

    public static double GetDistanceL()
    {
        return (m_leftMotorFront.getSelectedSensorPosition(0)/RobotMap.kDriveCodesPerRev) * InchesPerRev ;
    }

    /**
     *
     * @return Current distance of the front left motor in inches
     *
     *
     */
    public static double GetRawL(){
        return m_leftMotorFront.getSelectedSensorPosition(0);
    }public static double GetRawR(){
        return m_rightMotorFront.getSelectedSensorPosition(0);
    }

    /**
     *
     * @return Current distance of the front right motor in inches
     */
    public static double GetDistanceR()
    {
        return (m_rightMotorFront.getSensorCollection().getQuadraturePosition()/RobotMap.kDriveCodesPerRev) * InchesPerRev;
    }

    public static double GetDistance()
    {
        //Returns the average of the left and right distances
        return (GetDistanceL() + GetDistanceR()) / 2.0;
    }




}
