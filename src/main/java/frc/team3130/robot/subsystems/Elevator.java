package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3130.robot.RobotMap;

/**
 * This subsystem controls the power cube elevator of the robot
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

    private static final int MAX_VELOCITY = 6300; // 1024
    private static final int MAX_ACCELERATION = 6100; // 1024
    private static final int MAX_VELOCITY_DOWN = (int) (MAX_VELOCITY * 0.45); // 1024
    private static final int MAX_ACCELERATION_DOWN = (int) (MAX_ACCELERATION * 0.4); // 1024


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

        m_elevatorMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        m_elevatorMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);

        m_elevatorMaster.config_kP(0, RobotMap.kElevatorP, 0);
        m_elevatorMaster.config_kI(0, RobotMap.kElevatorI, 0);
        m_elevatorMaster.config_kD(0, RobotMap.kElevatorD, 0);
        m_elevatorMaster.config_kF(0, RobotMap.kElevatorF, 0);

        m_elevatorSlave.set(ControlMode.Follower, RobotMap.CAN_ELEVATOR2);

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public synchronized static double getHeight(){
        return m_elevatorMaster.getSelectedSensorPosition(0) / RobotMap.kElevatorTicksPerInch; //Returns height in inches
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
        if(m_elevatorMaster.getSelectedSensorPosition(0) >= RobotMap.kElevatorTicksPerInch * height){
            configMotionMagic(MAX_VELOCITY_DOWN, MAX_ACCELERATION_DOWN);
        }else{
            configMotionMagic(MAX_VELOCITY, MAX_ACCELERATION);
        }
        m_elevatorMaster.set(ControlMode.MotionMagic, RobotMap.kElevatorTicksPerInch * height);

    }
    public static void configMotionMagic(int cruiseVelocity, int acceleration){
        m_elevatorMaster.configMotionCruiseVelocity(cruiseVelocity, 0);
        m_elevatorMaster.configMotionAcceleration(acceleration, 0);
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

    public static void outputToSmartDashboard() {
        //SmartDashboard.putNumber("elevator_velocity", elevator.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Elev_Height", getHeight());
        SmartDashboard.putNumber("elev_m1current", m_elevatorMaster.getOutputCurrent() );
        SmartDashboard.putNumber("elev_m2current", m_elevatorSlave.getOutputCurrent() );

        SmartDashboard.putBoolean("Elev_Rev_Switch",m_elevatorMaster.getSensorCollection().isRevLimitSwitchClosed());
        SmartDashboard.putBoolean("elev_Fwd_Switch", m_elevatorMaster.getSensorCollection().isFwdLimitSwitchClosed());

        //Zero Handling
        if(m_elevatorMaster.getSensorCollection().isRevLimitSwitchClosed()){
            if(!zeroed){
                m_elevatorMaster.setSelectedSensorPosition(0, 0, 0);
                setHeight(0.0);
                DriverStation.reportWarning("Elevator is Zero!", false);
                zeroed = true;
            }
        }
        else{
            if(zeroed)
                zeroed = false;
        }
    }


}
