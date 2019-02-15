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
     * Gets the angle of the Wrist motor
     * @return Angle of Wrist in degrees in relation to the arm (not relative the ground)
     */
    public static double getWristAngle(){
        return m_wrist.getSelectedSensorPosition(0) / RobotMap.kWristTicksPerDeg;
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
     *  Hold the Wrist's current angle by PID motion magic closed loop
     */
    public static void holdAngleWrist() {
        setWristSimpleRelativeAngle(getWristAngle());
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
        public boolean limit_switch;

        // OUTPUTS
    }

}
