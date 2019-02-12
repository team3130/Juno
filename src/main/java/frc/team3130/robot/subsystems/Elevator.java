package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.commands.RunElevator;

/**
 * This subsystem controls the elevator of the robot
 */

public class Elevator extends Subsystem {
    //Instance Handling
    private static Elevator m_pInstance;

    public static Elevator GetInstance() {
        if (m_pInstance == null) m_pInstance = new Elevator();
        return m_pInstance;
    }

    //Create necessary objects
    private static WPI_TalonSRX m_elevatorMaster;
    private static WPI_TalonSRX m_elevatorSlave;
    private static boolean zeroed;

    //Create and define all standard data types needed


    private Elevator(){

        m_elevatorMaster = new WPI_TalonSRX(RobotMap.CAN_ELEVATOR1);
        m_elevatorSlave = new WPI_TalonSRX(RobotMap.CAN_ELEVATOR2);

        m_elevatorMaster.configFactoryDefault();
        m_elevatorSlave.configFactoryDefault();

        m_elevatorMaster.setNeutralMode(NeutralMode.Brake);
        m_elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);

        m_elevatorMaster.set(ControlMode.PercentOutput, 0);

        m_elevatorMaster.overrideLimitSwitchesEnable(true);
        m_elevatorMaster.overrideSoftLimitsEnable(false);

        m_elevatorMaster.configVoltageCompSaturation(12.0, 0);
        m_elevatorMaster.enableVoltageCompensation(true);

        m_elevatorMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        m_elevatorMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);

        configPIDF(RobotMap.kElevatorP, RobotMap.kElevatorI, RobotMap.kElevatorD, RobotMap.kElevatorF);

        m_elevatorSlave.set(ControlMode.Follower, RobotMap.CAN_ELEVATOR2);

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new RunElevator());
    }

    public static double getHeight(){
        return m_elevatorMaster.getSelectedSensorPosition(0) / RobotMap.kElevatorTicksPerInch; //Returns height in inches
    }

    public static void rawElevator(double percent){
        m_elevatorMaster.set(ControlMode.PercentOutput, percent);
    }
    /**
     * Run elevator manually using percent values. Performs gravity compensation and elevator down protection
     * @param percent
     */
    public static void runElevator(double percent){

        boolean isGoingDown = percent < 0;

        //Offset the output using constant bias
        percent += RobotMap.kElevatorBias;

        //When the elevator is going down
        if(isGoingDown){
            percent *= 0.75; //set to 75% of actual input when going down
            //Also, if we are in the extra slow zone, multiply by reduction ratio
            if(getHeight() < RobotMap.kElevatorSlowZone){
                percent *= Math.abs(getHeight()/RobotMap.kElevatorSlowZone);
            }
        }
        m_elevatorMaster.set(ControlMode.PercentOutput, percent);
    }

    /**
     *  Move the elevator to an absolute height
     * @param height The height setpoint to go to in inches
     */
    public synchronized static void setHeight(double height){
        m_elevatorMaster.set(ControlMode.PercentOutput, 0.0); //Set talon to other mode to prevent weird glitches
        configPIDF(RobotMap.kElevatorP, RobotMap.kElevatorI, RobotMap.kElevatorD, RobotMap.kElevatorF);
        configMotionMagic(RobotMap.kElevatorMaxAcc, RobotMap.kElevatorMaxVel);
        m_elevatorMaster.set(ControlMode.MotionMagic, RobotMap.kElevatorTicksPerInch * height);
    }

    private static void configMotionMagic(int acceleration, int cruiseVelocity){
        m_elevatorMaster.configMotionCruiseVelocity(cruiseVelocity, 0);
        m_elevatorMaster.configMotionAcceleration(acceleration, 0);
    }

    /**
     * Configure the PID values of elevator
     * @param kP
     * @param kI
     * @param kD
     * @param kF
     */
    public static void configPIDF(double kP, double kI, double kD, double kF) {
        m_elevatorMaster.config_kP(0, kP, 0);
        m_elevatorMaster.config_kI(0, kI, 0);
        m_elevatorMaster.config_kD(0, kD, 0);
        m_elevatorMaster.config_kF(0, kF, 0);
    }

    /**
     * Move the elevator to a relative amount
     * @param offset the offset in inches from the current height, positive is up
     */
    public static void addHeight(double offset) {
        double newHeight = getHeight() + offset;

        /*
        // If the elevator is (almost) at the bottom then just turn it off
        if(newHeight < RobotMap.ElevatorBottom) {
          elevator.set(ControlMode.PercentOutput, 0);
        }
        else {

        }
        */
        setHeight(newHeight);
    }


    /**
     * Hold the current height by PID closed loop
     */
    public static void holdHeight() {
        setHeight(getHeight());
    }

    public static void resetElevator(){
        m_elevatorMaster.set(ControlMode.PercentOutput, 0.0);
    }

    /**
     * Reset the elevator encoder
     */
    public static synchronized void zeroSensors(){
        m_elevatorMaster.setSelectedSensorPosition(0, 0, 0);
        zeroed = true;
    }

    public static synchronized boolean hasBeenZeroed(){
        return zeroed;
    }

    public static synchronized void setZeroedState(boolean isZeroed){
        zeroed = isZeroed;
    }

    public static boolean isRevLimitClosed(){
        return m_elevatorMaster.getSensorCollection().isRevLimitSwitchClosed();
    }

    public static void outputToSmartDashboard() {
        SmartDashboard.putNumber("elevator_velocity", m_elevatorMaster.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Elev_Height", getHeight());
        SmartDashboard.putNumber("elev_m1current", m_elevatorMaster.getOutputCurrent() );
        SmartDashboard.putNumber("elev_m2current", m_elevatorSlave.getOutputCurrent() );

        SmartDashboard.putBoolean("Elev_Rev_Switch",m_elevatorMaster.getSensorCollection().isRevLimitSwitchClosed());
        SmartDashboard.putBoolean("elev_Fwd_Switch", m_elevatorMaster.getSensorCollection().isFwdLimitSwitchClosed());
    }

}
