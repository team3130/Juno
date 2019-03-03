package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.commands.Elevator.RunElevator;
import frc.team3130.robot.util.Epsilon;
import frc.team3130.robot.util.Util;

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

    private enum ElevatorState{
        Manual,
        MotionMagic,
    }

    public ElevatorState getState(){
        return state;
    }

    public void setState(ElevatorState newState){
        this.state = newState;
    }

    //Create necessary objects
    private static Solenoid m_shifter;

    private static WPI_TalonSRX m_elevatorMaster;
    private static WPI_TalonSRX m_elevatorSlave;

    private volatile ElevatorState state;
    private static PeriodicIO mPeriodicIO = new PeriodicIO();

    //Create and define all standard data types needed
    private static boolean zeroed = false;

    private Elevator(){

        m_elevatorMaster = new WPI_TalonSRX(RobotMap.CAN_ELEVATOR1);
        m_elevatorSlave = new WPI_TalonSRX(RobotMap.CAN_ELEVATOR2);


        m_elevatorMaster.configFactoryDefault();
        m_elevatorSlave.configFactoryDefault();

        m_elevatorSlave.set(ControlMode.Follower, RobotMap.CAN_ELEVATOR1);

        m_elevatorMaster.setNeutralMode(NeutralMode.Brake);
        m_elevatorSlave.setNeutralMode(NeutralMode.Brake);

        m_elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);

        m_elevatorMaster.overrideLimitSwitchesEnable(true);
        m_elevatorMaster.overrideSoftLimitsEnable(false);

        m_elevatorMaster.configVoltageCompSaturation(12.0, 0);
        m_elevatorMaster.enableVoltageCompensation(true);

        m_elevatorMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        m_elevatorMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);

        configPIDF(RobotMap.kElevatorP, RobotMap.kElevatorI, RobotMap.kElevatorD, RobotMap.kElevatorF);

        m_elevatorMaster.set(ControlMode.PercentOutput, 0);

        m_shifter = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_ELEVATORSHIFT);
        m_shifter.set(false); //false should be high gear or normal running mode

        /**
         * Upward is positive motor direction
         *
         * Upward is positive encoder direction
         */

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new RunElevator());
    }

    //For testing
    public static void rawElevator(double percent){
        m_elevatorMaster.set(ControlMode.PercentOutput, percent);
    }

    /**
     * Run elevator manually using percent values. Performs gravity compensation and elevator down protection
     * @param percent
     */
    public static void runElevator(double percent){
        if(!getShift()) { //Only apply power modification if in high gear
            boolean isGoingDown = percent < 0;
            //When the elevator is going down
            if (isGoingDown) {
                percent *= 0.75; //set to 75% of actual input when going down
                //Also, if we are in the extra slow zone, multiply by reduction ratio
                if (getHeightOffGround() < RobotMap.kElevatorSlowZone) {
                    percent *= Math.abs(getHeightOffGround() / RobotMap.kElevatorSlowZone);
                    percent = Math.min(-0.2, percent);
                }
            }
            //Offset the output using normal operation feed forward
            percent += mPeriodicIO.feedforward;
        }
        m_elevatorMaster.set(ControlMode.PercentOutput, percent);
    }

    /**
     *  Move the elevator to an absolute height off the ground using simple motion magic
     * @param height The height setpoint to go to in inches
     */
    public synchronized static void setSimpleMotionMagic(double height){
        m_elevatorMaster.set(ControlMode.PercentOutput, 0.0); //Set talon to other mode to prevent weird glitches
        configPIDF(
                RobotMap.kElevatorP,
                RobotMap.kElevatorI,
                RobotMap.kElevatorD,
                mPeriodicIO.feedforward);
        configMotionMagic(RobotMap.kElevatorMaxAcc, RobotMap.kElevatorMaxVel);
        m_elevatorMaster.set(ControlMode.MotionMagic, RobotMap.kElevatorTicksPerInch * (height - RobotMap.kElevatorHomingHeight) );
    }


    /**
     * Hold the current height by PID closed loop
     */
    public static synchronized void holdHeight() {
        setSimpleMotionMagic(getHeightOffGround());
    }

    /**
     * Shifts the elevator gear box into an absolute gear
     * @param shiftVal false is high gear, true is low gear
     */
    public static void shift(boolean shiftVal)
    {
        if(shiftVal){
            m_elevatorMaster.overrideLimitSwitchesEnable(false);
        }else{
            m_elevatorMaster.overrideLimitSwitchesEnable(true);
        }
        m_shifter.set(shiftVal);
    }

    /**
     * Reset the elevator by clearing the motion profile buffer and putting it in percent output mode at 0%
     */
    public static synchronized void resetElevator(){
        m_elevatorMaster.clearMotionProfileTrajectories();
        m_elevatorMaster.set(ControlMode.PercentOutput, 0.0);
    }

    /**
     * Periodically read in the physical inputs to calculate variables
     */
    public synchronized void readPeriodicInputs() {
        final double t = Timer.getFPGATimestamp();
        //Read in raw position and velocity
        mPeriodicIO.position_ticks = m_elevatorMaster.getSelectedSensorPosition(0);
        mPeriodicIO.velocity_ticks_per_100ms = m_elevatorMaster.getSelectedSensorVelocity(0);
        
        //Handle acceleration of elevator
        if (m_elevatorMaster.getControlMode() == ControlMode.MotionMagic) {
            int newPos = m_elevatorMaster.getActiveTrajectoryPosition(); //read in the new motion profile Position trajectory
            int newVel = m_elevatorMaster.getActiveTrajectoryVelocity(); //read in the new motion profile Velocity trajectory

            //Check if elevator is accelerating, decelerating, or at constant velocity
            if (Epsilon.epsilonEquals(newVel, RobotMap.kElevatorMaxVel, 5) ||
                    Epsilon.epsilonEquals(newVel, mPeriodicIO.active_trajectory_velocity, 5)) {
                // Elevator is at almost constant velocity.
                mPeriodicIO.active_trajectory_accel_g = 0.0;
            }else{
                if(newVel > mPeriodicIO.active_trajectory_velocity) { //TODO: check if velocities are negative in MP
                    //elevator is accelerating upward
                    mPeriodicIO.active_trajectory_accel_g = RobotMap.kElevatorMaxAcc * 10.0 /
                            (RobotMap.kElevatorTicksPerInch * 386.09);
                }else{
                    //elevator is accelerating downward
                    mPeriodicIO.active_trajectory_accel_g = -RobotMap.kElevatorMaxAcc * 10.0 /
                            (RobotMap.kElevatorTicksPerInch * 386.09);
                }
            }
            /*} else if (newPos >= mPeriodicIO.active_trajectory_position){ //elevator is moving up
                if(newVel > mPeriodicIO.active_trajectory_velocity) {
                    //elevator is accelerating upward
                    mPeriodicIO.active_trajectory_accel_g = RobotMap.kElevatorMaxAcc * 10.0 /
                            (RobotMap.kElevatorTicksPerInch * 386.09);
                }else{
                    //elevator is accelerating downward
                    mPeriodicIO.active_trajectory_accel_g = -RobotMap.kElevatorMaxAcc * 10.0 /
                            (RobotMap.kElevatorTicksPerInch * 386.09);
                }
            }else { //elevator is moving downward
                if (newVel > mPeriodicIO.active_trajectory_velocity) {
                    //elevator is accelerating downward
                    mPeriodicIO.active_trajectory_accel_g = -RobotMap.kElevatorMaxAcc * 10.0 /
                            (RobotMap.kElevatorTicksPerInch * 386.09);
                } else {
                    //elevator is accelerating upward
                    mPeriodicIO.active_trajectory_accel_g = RobotMap.kElevatorMaxAcc * 10.0 /
                            (RobotMap.kElevatorTicksPerInch * 386.09);
                }
            }*/
            //set values for next run
            mPeriodicIO.active_trajectory_velocity = newVel;
            mPeriodicIO.active_trajectory_position = newPos;
        } else { //not in motion profiling mode
            mPeriodicIO.active_trajectory_position = Integer.MIN_VALUE;
            mPeriodicIO.active_trajectory_velocity = 0;
            mPeriodicIO.active_trajectory_accel_g = 0.0;
        }
        mPeriodicIO.output_percent = m_elevatorMaster.getMotorOutputPercent();
        mPeriodicIO.t = t;

        //Calculate the feed forward necessary this loop
        double currHeight = getHeightOffGround();
        if (currHeight > RobotMap.kElevatorHeightEpsilon && !getShift()) { //above epsilon and in high gear
            if(Intake.GetInstance().getState() == Intake.IntakeState.Empty){
                mPeriodicIO.feedforward = RobotMap.kElevatorFFEmpty;
            }else if(Intake.GetInstance().getState() == Intake.IntakeState.HasBall){
                mPeriodicIO.feedforward = RobotMap.kElevatorFFWithBall;
            }else if(Intake.GetInstance().getState() == Intake.IntakeState.HasHatch){
                mPeriodicIO.feedforward = RobotMap.kElevatorFFWithHatch;
            }else{
                mPeriodicIO.feedforward = RobotMap.kElevatorFFEmpty;
            }
            if(currHeight > RobotMap.kElevatorStagePickup1){
                mPeriodicIO.feedforward += RobotMap.kElevatorFFAddition1;
                if(currHeight > RobotMap.kElevatorStagePickup2){
                    mPeriodicIO.feedforward += RobotMap.kElevatorFFAddition2;
                }
            }
        } else {
            mPeriodicIO.feedforward = 0.0;
        }
    }

    //Sensor Related
    /**
     * Gets the height of the elevator from the ground
     * @return height in inches
     */
    public static double getHeightOffGround(){
        return mPeriodicIO.position_ticks / RobotMap.kElevatorTicksPerInch + RobotMap.kElevatorHomingHeight; //Returns height from ground in inches
    }

    /**
     * Returns the gear elevator is in
     * @return false is high, true is low
     */
    public static boolean getShift(){
        return m_shifter.get();
    }

    /**
     * Gets the acceleration of the elevator
     * @return Acceleration of the elevator in inches per second^2
     */
    public static synchronized double getActiveTrajectoryAccelG() {
        return mPeriodicIO.active_trajectory_accel_g;
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

    //Configs
    /**
     * Configure motion magic parameters
     * @param acceleration maximum/target acceleration
     * @param cruiseVelocity cruise velocity
     */
    private static synchronized void configMotionMagic(int acceleration, int cruiseVelocity){
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

    public static void outputToSmartDashboard() {
        SmartDashboard.putNumber("Elevator Velocity", m_elevatorMaster.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Elevator Height", getHeightOffGround());

        SmartDashboard.putNumber("Elevator m1current", m_elevatorMaster.getOutputCurrent() );
        SmartDashboard.putNumber("Elevator m2current", m_elevatorSlave.getOutputCurrent() );

        SmartDashboard.putNumber("Elevator Sensor Value", mPeriodicIO.position_ticks);
        SmartDashboard.putNumber("Elevator Output %", mPeriodicIO.output_percent);

        SmartDashboard.putNumber("Elevator Slave Output %", m_elevatorMaster.getMotorOutputPercent());

        SmartDashboard.putNumber("Elevator Current Trajectory Point", mPeriodicIO.active_trajectory_position);
        SmartDashboard.putNumber("Elevator Traj Vel", mPeriodicIO.active_trajectory_velocity);
        SmartDashboard.putNumber("Elevator Traj Accel", mPeriodicIO.active_trajectory_accel_g);

        SmartDashboard.putNumber("Elevator Feed Forward", mPeriodicIO.feedforward);

        SmartDashboard.putBoolean("Elevator Rev_Switch",m_elevatorMaster.getSensorCollection().isRevLimitSwitchClosed());
        SmartDashboard.putBoolean("Elevator Fwd_Switch", m_elevatorMaster.getSensorCollection().isFwdLimitSwitchClosed());
    }

    public static class PeriodicIO {
        // INPUTS
        public int position_ticks;
        public int velocity_ticks_per_100ms;
        public double active_trajectory_accel_g;
        public int active_trajectory_velocity;
        public int active_trajectory_position;
        public double output_percent;
        public double feedforward;
        public double t;

        // OUTPUTS
    }

}
