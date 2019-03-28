package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3130.robot.RobotMap;

public class Climber extends Subsystem {
    //Instance Handling
    private static Climber m_pInstance;

    public static Climber GetInstance() {
        if (m_pInstance == null) m_pInstance = new Climber();
        return m_pInstance;
    }

    //Create necessary objects

    private static Solenoid pistons;

    private static WPI_TalonSRX m_legDown;
    private static WPI_TalonSRX m_legDrive;

    //Create and define all standard data types needed
    private static boolean climbEnabled;


    private Climber(){
        pistons = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_CLIMBARMS);

        m_legDown = new WPI_TalonSRX(RobotMap.CAN_CLIMBERLEG);
        m_legDrive = new WPI_TalonSRX(RobotMap.CAN_CLIMBERDRIVE);

        m_legDown.configFactoryDefault();

        m_legDown.overrideLimitSwitchesEnable(false);
        m_legDown.overrideSoftLimitsEnable(false);

        m_legDown.configVoltageCompSaturation(12.0, 0);
        m_legDown.enableVoltageCompensation(true);
        m_legDown.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        m_legDown.config_kP(0, .1, 0);
        m_legDown.config_kI(0, 0, 0);
        m_legDown.config_kD(0, 0, 0);

        m_legDrive.configFactoryDefault();
        m_legDrive.overrideLimitSwitchesEnable(false);
        m_legDrive.overrideSoftLimitsEnable(false);

        climbEnabled = false;
    }

    public void initDefaultCommand() {
    }

    public static void runLegDrive(double percent){
        m_legDrive.set(ControlMode.PercentOutput, percent);
    }

    public static void downLeg(double percent){
        m_legDown.set(ControlMode.PercentOutput, percent);
    }

    public static void deployArms(boolean deploy)
    {
        pistons.set(deploy);
    }

    public static void holdLeg(){
        m_legDown.set(ControlMode.Position, m_legDown.getSelectedSensorPosition());
    }

    public static void setClimbEnabled(boolean climbEnabled) {
        Climber.climbEnabled = climbEnabled;
    }

    public static boolean isClimbEnabled() {
        return climbEnabled;
    }

    public static void resetLeg(){
        m_legDrive.set(ControlMode.PercentOutput, 0.0);
        m_legDown.set(ControlMode.PercentOutput, 0.0);
    }

    public static void outputToSmartDashboard() {
        SmartDashboard.putBoolean("Climb Enabled", isClimbEnabled());
    }
}


