package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.util.Epsilon;


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

    private PeriodicIO elbowPeriodicIO = new PeriodicIO();
    private PeriodicIO wristPeriodicIO = new PeriodicIO();
    //Create and define all standard data types needed
    

    private Arm() {

        m_elbow = new WPI_TalonSRX(RobotMap.CAN_ARMELBOW);
        m_wrist = new WPI_TalonSRX(RobotMap.CAN_ARMWRIST);


        //Talon reset for Intake motors
        m_elbow.configFactoryDefault();
        m_wrist.configFactoryDefault();

        m_elbow.configVoltageCompSaturation(12.0, 0);
        m_elbow.enableVoltageCompensation(true);

        m_wrist.configVoltageCompSaturation(12.0, 0);
        m_wrist.enableVoltageCompensation(true);

        //setNeutralMode for Talons
        m_elbow.setNeutralMode(NeutralMode.Brake);
        m_wrist.setNeutralMode(NeutralMode.Brake);

        /**
         * For both motors, rotation CCW away from the elevator is positive direction
         *
         * Absolute: 180 degrees is parallel to the ground and fully extended, 0 is full back into/toward the robot
         * Relative: Wrist angle is relative to the arm, elbow is technically relative
         * to the elevator and thus, the ground.
         */
        m_wrist.setInverted(false);
        m_elbow.setInverted(true);
    }

    @Override
    protected void initDefaultCommand() {

    }

    /*
        Elbow
     */
    /**
     * Run Elbow motor manually
     * @param speed percent value
     */
    public static void runElbow(double speed){
        m_elbow.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Gets the angle of the Elbow motor
     * @return Angle of Elbow in degrees in relation to the elevator (this should be absolute to the ground)
     */
    public static double getElbowAngle(){
        return m_elbow.getSelectedSensorPosition(0) / RobotMap.kElbowTicksPerDeg;
    }

    /**
     *  Move the arm Elbow motor to an absolute angle
     * @param angle The angle setpoint to go to in degrees
     */
    public synchronized static void setElbowSimpleAngle(double angle) {
        m_elbow.set(ControlMode.PercentOutput, 0.0); //Set talon to other mode to prevent weird glitches
        configMotionMagic(m_elbow, RobotMap.kElbowMaxAcc, RobotMap.kElbowMaxVel);
        m_elbow.set(ControlMode.MotionMagic, RobotMap.kElbowTicksPerDeg * angle);
    }

    /**
     *  Hold the Elbow's current angle by PID motion magic closed loop
     */
    public static void holdAngleElbow() {
        setElbowSimpleAngle(getElbowAngle());
    }


    /*
        Wrist
     */
    /**
     * Run Wrist motor manually
     * @param speed percent value
     */
    public static void runWrist(double speed){
        m_wrist.set(ControlMode.PercentOutput,speed);
    }


    /**
     * Gets the angle of the Wrist motor in relation to the arm
     * @return Angle of Wrist in degrees in relation to the arm (not relative the ground)
     */
    public static double getRelativeWristAngle(){
        return m_wrist.getSelectedSensorPosition(0) / RobotMap.kWristTicksPerDeg;
    }

    /**
     * Gets the absolute angle of the Wrist motor
     * @return Angle of Wrist in degrees relative the ground
     */
    public static double getAbsoluteWristAngle(){
        return getRelativeWristAngle() - (180 - getElbowAngle());
    }



    /**
     *  Move the arm Wrist motor to an angle relative to the arm
     * @param angle The angle setpoint to go to in degrees
     */
    public synchronized static void setWristSimpleRelativeAngle(double angle) {
        m_wrist.set(ControlMode.PercentOutput, 0.0); //Set talon to other mode to prevent weird glitches
        configMotionMagic(m_wrist, RobotMap.kWristMaxAcc, RobotMap.kWristMaxVel);
        m_wrist.set(ControlMode.MotionMagic, RobotMap.kWristTicksPerDeg * angle);
    }

    /**
     *  Move the arm Wrist motor to an angle relative to the ground
     * @param angle The angle setpoint to go to in degrees
     */
    public synchronized static void setWristSimpleAbsoluteAngle(double angle) {
        m_wrist.set(ControlMode.PercentOutput, 0.0); //Set talon to other mode to prevent weird glitches
        configMotionMagic(m_wrist, RobotMap.kWristMaxAcc, RobotMap.kWristMaxVel);
        m_wrist.set(ControlMode.MotionMagic, (RobotMap.kWristTicksPerDeg * angle) + (180.0 - getElbowAngle()));
    }



    /**
     *  Hold the Wrist's current angle by PID motion magic closed loop
     */
    public static void holdAngleWrist() {
        setWristSimpleRelativeAngle(getRelativeWristAngle());
    }



    public synchronized void readPeriodicInputs() {

        StickyFaults faults = new StickyFaults();
        m_elbow.getStickyFaults(faults);
        if (faults.hasAnyFault()) {
            m_elbow.clearStickyFaults(0);
        }
        m_wrist.getStickyFaults(faults);
        if (faults.hasAnyFault()) {
            m_wrist.clearStickyFaults(0);
        }


        if (m_wrist.getControlMode() == ControlMode.MotionMagic) {
            //read in current trajectory position
            wristPeriodicIO.active_trajectory_position = m_wrist.getActiveTrajectoryPosition();

            final int newVel = m_wrist.getActiveTrajectoryVelocity();
            if (Epsilon.epsilonEquals(newVel, RobotMap.kWristMaxVel, 5) ||
                    Epsilon.epsilonEquals(newVel, wristPeriodicIO.active_trajectory_velocity, 5)) {
                // Wrist is ~constant velocity.
                wristPeriodicIO.active_trajectory_acceleration_rad_per_s2 = 0.0;
            } else {
                // Wrist is accelerating so find which way it's accelerating
                wristPeriodicIO.active_trajectory_acceleration_rad_per_s2 = Math.signum(wristPeriodicIO
                        .active_trajectory_velocity - newVel) * RobotMap.kWristMaxAcc * 20.0 * Math.PI / 4096;
            }
            wristPeriodicIO.active_trajectory_velocity = newVel;
        } else {
            wristPeriodicIO.active_trajectory_position = Integer.MIN_VALUE;
            wristPeriodicIO.active_trajectory_velocity = 0;
            wristPeriodicIO.active_trajectory_acceleration_rad_per_s2 = 0.0;
        }
        //wristPeriodicIO.limit_switch = m_wrist.getSensorCollection().isFwdLimitSwitchClosed();
        wristPeriodicIO.output_voltage = m_wrist.getMotorOutputVoltage();
        wristPeriodicIO.output_percent = m_wrist.getMotorOutputPercent();
        wristPeriodicIO.position_ticks = m_wrist.getSelectedSensorPosition(0);
        wristPeriodicIO.velocity_ticks_per_100ms = m_wrist.getSelectedSensorVelocity(0);

        /*
        if (getAbsoluteWristAngle() > Robotmap.kWristEpsilon ||
                wristPeriodicIO.active_trajectory_position / RobotMap.kWristTicksPerDeg > RobotMap.kWristEpsilon) {*/
        if(m_wrist.isAlive()){
            double ka = RobotMap.kWristKaEmpty;
            double ff = RobotMap.kWristFFEmpty;
            if(Intake.GetInstance().getState() == Intake.IntakeState.HasBall){
                ff = RobotMap.kWristFFBall;
                ka = RobotMap.kWristKaWithBall;
            }
            if(Intake.GetInstance().getState() == Intake.IntakeState.HasHatch){
                ff = RobotMap.kWristFFHatch;
                ka = RobotMap.kWristKaWithHatch;
            }

            double wristGravityComponent = Math.cos(Math.toRadians(getAbsoluteWristAngle())) * ff;
            double elevatorAccelerationComponent = Elevator.getActiveTrajectoryAccelG();
            double wristAccelerationComponent = wristPeriodicIO.active_trajectory_acceleration_rad_per_s2 * ka;

            wristPeriodicIO.feedforward = wristGravityComponent + elevatorAccelerationComponent * wristGravityComponent + wristAccelerationComponent;
        } else {
            wristPeriodicIO.feedforward = 0.0;
        }

        if (m_elbow.getControlMode() == ControlMode.MotionMagic) {
            //read in current trajectory position
            elbowPeriodicIO.active_trajectory_position = m_elbow.getActiveTrajectoryPosition();

            final int newVel = m_elbow.getActiveTrajectoryVelocity();
            if (Epsilon.epsilonEquals(newVel, RobotMap.kElbowMaxVel, 5) ||
                    Epsilon.epsilonEquals(newVel, elbowPeriodicIO.active_trajectory_velocity, 5)) {
                // elbow is ~constant velocity.
                elbowPeriodicIO.active_trajectory_acceleration_rad_per_s2 = 0.0;
            } else {
                // elbow is accelerating so find which way it's accelerating
                elbowPeriodicIO.active_trajectory_acceleration_rad_per_s2 = Math.signum(elbowPeriodicIO
                        .active_trajectory_velocity - newVel) * RobotMap.kElbowMaxAcc * 20.0 * Math.PI / 4096;
            }
            elbowPeriodicIO.active_trajectory_velocity = newVel;
        } else {
            elbowPeriodicIO.active_trajectory_position = Integer.MIN_VALUE;
            elbowPeriodicIO.active_trajectory_velocity = 0;
            elbowPeriodicIO.active_trajectory_acceleration_rad_per_s2 = 0.0;
        }
        //elbowPeriodicIO.limit_switch = m_elbow.getSensorCollection().isFwdLimitSwitchClosed();
        elbowPeriodicIO.output_voltage = m_elbow.getMotorOutputVoltage();
        elbowPeriodicIO.output_percent = m_elbow.getMotorOutputPercent();
        elbowPeriodicIO.position_ticks = m_elbow.getSelectedSensorPosition(0);
        elbowPeriodicIO.velocity_ticks_per_100ms = m_elbow.getSelectedSensorVelocity(0);

        /*
        if (getElbowAngle() > Robotmap.kElbowEpsilon ||
                elbowPeriodicIO.active_trajectory_position / RobotMap.kElbowTicksPerDeg > RobotMap.kElbowEpsilon) {*/
        if(m_elbow.isAlive()){
            double ka = RobotMap.kElbowKaEmpty;
            double ff = RobotMap.kElbowFFEmpty;
            if(Intake.GetInstance().getState() == Intake.IntakeState.HasBall){
                ff = RobotMap.kElbowFFBall;
                ka = RobotMap.kElbowKaWithBall;
            }
            if(Intake.GetInstance().getState() == Intake.IntakeState.HasHatch){
                ff = RobotMap.kElbowFFHatch;
                ka = RobotMap.kElbowKaWithHatch;
            }

            double elbowGravityComponent = Math.cos(Math.toRadians(getElbowAngle())) * (ff - Math.cos(Math.toRadians(getAbsoluteWristAngle())) * wristPeriodicIO.feedforward);
            double elevatorAccelerationComponent = Elevator.getActiveTrajectoryAccelG();
            double elbowAccelerationComponent = elbowPeriodicIO.active_trajectory_acceleration_rad_per_s2 * ka;

            elbowPeriodicIO.feedforward = elbowGravityComponent + elevatorAccelerationComponent * elbowGravityComponent + elbowAccelerationComponent;
        } else {
            elbowPeriodicIO.feedforward = 0.0;
        }
    }

    //Configs
    /**
     * Configure motion magic parameters
     * @param yoskiTalon which talon to use
     * @param acceleration maximum/target acceleration
     * @param cruiseVelocity cruise velocity
     */
    private static void configMotionMagic(WPI_TalonSRX yoskiTalon, int acceleration, int cruiseVelocity){
        yoskiTalon.configMotionCruiseVelocity(cruiseVelocity, 0);
        yoskiTalon.configMotionAcceleration(acceleration, 0);
    }

    /**
     * Configure the PIDF values of an arm motor
     * @param _talon which talon to use
     * @param kP
     * @param kI
     * @param kD
     * @param kF
     */
    public static void configPIDF(WPI_TalonSRX _talon, double kP, double kI, double kD, double kF) {
        _talon.config_kP(0, kP, 0);
        _talon.config_kI(0, kI, 0);
        _talon.config_kD(0, kD, 0);
        _talon.config_kF(0, kF, 0);
    }

    public class PeriodicIO {
        // INPUTS
        public int position_ticks;
        public int velocity_ticks_per_100ms;
        public int active_trajectory_position;
        public int active_trajectory_velocity;
        public double active_trajectory_acceleration_rad_per_s2;
        public double output_percent;
        public double output_voltage;
        public double feedforward;
        //public boolean limit_switch;

        // OUTPUTS
    }

}
