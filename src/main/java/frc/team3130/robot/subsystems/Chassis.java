package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.commands.Chassis.DefaultDrive;
import frc.team3130.robot.sensors.Navx;


public class Chassis extends Subsystem {

    //Instance Handling
    private static Chassis m_pInstance;
    public static Chassis GetInstance()
    {
        if(m_pInstance == null) m_pInstance = new Chassis();
        return m_pInstance;
    }

    //Create necessary objects
    private static WPI_TalonSRX m_leftMotorFront;
    private static WPI_TalonSRX m_leftMotorRear;
    private static WPI_TalonSRX m_rightMotorFront;
    private static WPI_TalonSRX m_rightMotorRear;

    private static Solenoid m_shifter;



    private static ChassisControlState mChassisState = ChassisControlState.PERCENT_OUTPUT;

    //Create and define all standard data types needed


    private Chassis() {


        /**
         * For all motors, forward is the positive direction
         *
         * Shift false is low gear
         */

      }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new DefaultDrive());
    }

    public static void driveTank(double moveL, double moveR, boolean squaredInputs) {

    }

    /**
     * Drive the robot using arcade mode
     * @param moveThrottle Base forward and backward speed to move at. Positive is forward
     * @param turnThrottle Turning velocity
     * @param squaredInputs Whether or not to use squared inputs
     */
    public static void driveArcade(double moveThrottle, double turnThrottle, boolean squaredInputs) {


    }

    /**
     * Shifts the drivetrain gear box into an absolute gear
     * @param shiftVal true is high gear, false is low gear
     */
    public static void shift(boolean shiftVal)
    {
        m_shifter.set(shiftVal);
    }

    /**
     * Tell the Chassis to hold a relative angle
     * @param angle angle to hold in degrees
     */
    public static void holdAngle(double angle)
    {
        //TODO: Rework
    }

    /**
     * Reset the drivetrain encoder positions to 0
     */
    public static void reset(){
        m_leftMotorFront.setSelectedSensorPosition(0);
        m_rightMotorFront.setSelectedSensorPosition(0);
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
     * Set the Chassis PID values
     */
    public static void setPIDValues()
    {
        if(getShift()) { //Chassis is in high gear

        }else{

        }
    }

    public synchronized void writePeriodicOutputs() {
        if(mChassisState == ChassisControlState.MOTION_PROFILE){

        }


    }

    public synchronized void setControlState(int state){
        if(state == 0){
            mChassisState = ChassisControlState.MOTION_PROFILE;
        }else{
            mChassisState = ChassisControlState.PERCENT_OUTPUT;
        }

    }

    //Sensor Related
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
     * Returns the shift state of the Chassis
     * @return
     */
    public static boolean getShift() {
        return m_shifter.get();
    }

    /**
     * Returns if robot is in low gear
     * @return true means robot is in low gear, false if it's in high gear
     */
    public static boolean isLowGear(){
        return !m_shifter.get();
    }

    /**
     * Gets absolute distance traveled by the left side of the robot
     * @return The absolute distance of the left side in inches
     */
    public static double getDistanceL()
    {
        return m_leftMotorFront.getSelectedSensorPosition(0) / RobotMap.kLChassisTicksPerInch;
    }

    /**
     * Gets absolute distance traveled by the right side of the robot
     * @return The absolute distance of the right side in inches
     */
    public static double getDistanceR()
    {
        return m_rightMotorFront.getSelectedSensorPosition(0) / RobotMap.kRChassisTicksPerInch;
    }

    /**
     * Gets the absolute distance traveled by the robot
     * @return The absolute distance traveled of robot in inches
     */
    public static double getDistance()
    {
        return (getDistanceL() + getDistanceR()) / 2.0; //the average of the left and right distances
    }

    /**
     * Returns the current speed of the front left motor in native units
     * @return Current speed of the front left motor (ticks per 0.1 seconds)
     */
    public static double getRawSpeedL()
    {
        return m_leftMotorFront.getSelectedSensorVelocity(0);
    }

    /**
     * Returns the current speed of the front left motor in native units
     * @return Current speed of the front left motor (ticks per 0.1 seconds)
     */
    public static double getRawSpeedR()
    {
        return m_rightMotorFront.getSelectedSensorVelocity(0);
    }

    /**
     * Returns the current speed of the front left motor
     * @return Current speed of the front left motor (inches per second)
     */
    public static double getSpeedL()
    {
        // The raw speed units will be in the sensor's native ticks per 100ms.
        return 10.0 * getRawSpeedL() / RobotMap.kLChassisTicksPerInch;
    }

    /**
     * Returns the current speed of the front right motor
     * @return Current speed of the front right motor (inches per second)
     */
    public static double getSpeedR()
    {
        // The raw speed units will be in the sensor's native ticks per 100ms.
        return 10.0 * getRawSpeedR() / RobotMap.kRChassisTicksPerInch;
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
    public static double getRawL(){
        return m_leftMotorFront.getSelectedSensorPosition(0);
    }

    /**
     *
     * @return Raw absolute encoder ticks of the right side of the robot
     */
    public static double getRawR(){
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

    //Configs
    /**
     * Configure the drivetrain for motion profiling
     * @param duration fire rate of the motion profile in ms
     */
    public static void configMP(int duration) {

        //left
        m_leftMotorFront.config_kP(0, RobotMap.kMPChassisP, 0);
        m_leftMotorFront.config_kI(0, RobotMap.kMPChassisI, 0);
        m_leftMotorFront.config_kD(0, RobotMap.kMPChassisD, 0);
        m_leftMotorFront.config_kF(0, RobotMap.kMPChassisF, 0);
        m_leftMotorFront.configNeutralDeadband(RobotMap.kChassisMPOutputDeadband, 0);
        // Status 10 provides the trajectory target for motion profile AND motion magic
        m_leftMotorFront.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, duration, 0);
        //Profile already assumes base time is 0
        m_leftMotorFront.configMotionProfileTrajectoryPeriod(0, 0);

        //right
        m_rightMotorFront.config_kP(0, RobotMap.kMPChassisP, 0);
        m_rightMotorFront.config_kI(0, RobotMap.kMPChassisI, 0);
        m_rightMotorFront.config_kD(0, RobotMap.kMPChassisD, 0);
        m_rightMotorFront.config_kF(0, RobotMap.kMPChassisF, 0);
        m_rightMotorFront.configNeutralDeadband(RobotMap.kChassisMPOutputDeadband, 0);
        // Status 10 provides the trajectory target for motion profile AND motion magic
        m_rightMotorFront.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, duration, 0);
        //Profile already assumes base time is 0
        m_rightMotorFront.configMotionProfileTrajectoryPeriod(0, 0);
    }


    public static void outputToSmartDashboard() {
        SmartDashboard.putNumber("Chassis Right Velocity", m_rightMotorFront.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Chassis Left Velocity", m_leftMotorFront.getSelectedSensorVelocity(0));

        SmartDashboard.putNumber("Chassis Right Vel Traj", m_rightMotorFront.getActiveTrajectoryVelocity(0));
        SmartDashboard.putNumber("Chassis Left Vel Traj", m_leftMotorFront.getActiveTrajectoryVelocity(0));

        SmartDashboard.putNumber("Chassis Right Speed", getSpeedR());
        SmartDashboard.putNumber("Chassis Left Speed", getSpeedL());

        SmartDashboard.putNumber("Chassis Distance R", getDistanceR());
        SmartDashboard.putNumber("Chassis Distance L", getDistanceL());

        SmartDashboard.putNumber("Chassis Right Sensor Value", m_rightMotorFront.getSelectedSensorPosition());
        SmartDashboard.putNumber("Chassis Left Sensor Value", m_leftMotorFront.getSelectedSensorPosition());

        SmartDashboard.putNumber("Chassis Right Output %", m_rightMotorFront.getMotorOutputPercent());
        SmartDashboard.putNumber("Chassis Left Output %", m_leftMotorFront.getMotorOutputPercent());

    }

    private enum ChassisControlState{
        PERCENT_OUTPUT,
        MOTION_PROFILE
    }


}
