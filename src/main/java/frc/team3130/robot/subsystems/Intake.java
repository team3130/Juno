package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team3130.robot.RobotMap;

public class Intake extends Subsystem {
    //Instance Handling
    private static Intake m_pInstance;

    public static Intake GetInstance() {
        if (m_pInstance == null) m_pInstance = new Intake();
        return m_pInstance;
    }

    //Intake state handler
    public enum IntakeState{
        Empty,
        HasBall,
        HasHatch
    }

    public IntakeState getState(){
        return state;
    }

    public void setState(IntakeState newState){
        this.state = newState;
    }

    //Create necessary objects
    private static Solenoid clamp;
    private static Solenoid tongue;

    private static WPI_TalonSRX m_ballMotor;
    private static WPI_TalonSRX m_hatchMotor;

    private volatile IntakeState state;

    private Intake(){
        m_ballMotor = new WPI_TalonSRX(RobotMap.CAN_BALLMOTOR);
        m_hatchMotor = new WPI_TalonSRX(RobotMap.CAN_HATCHMOTOR);

        //set the intake state to empty on construction
        state = IntakeState.Empty;

        //talon reset for ball and hatch motors
        m_ballMotor.configFactoryDefault();
        m_hatchMotor.configFactoryDefault();

        m_ballMotor.setNeutralMode(NeutralMode.Brake);
        m_hatchMotor.setNeutralMode(NeutralMode.Brake);

        clamp = new Solenoid(RobotMap.CAN_PNMMODULE,RobotMap.PNM_INTAKEPISTON);

        m_ballMotor.setInverted(true);
    }

    public void initDefaultCommand() {
        //Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public static void runBallIntake(double speed){
        m_ballMotor.set(speed);
    }

    public static void runHatchIntake(double speed){
        m_hatchMotor.set(speed);
    }

    public static synchronized void toggleClamp(){
        clamp.set(!clamp.get());
    }

    public static synchronized void closeClamp(){ clamp.set(false);}

    public static synchronized void openClamp(){ clamp.set(true);}

    public static synchronized void toggleTongue() { tongue.set(tongue.get());}

    public static synchronized void extendTongue() { tongue.set(true);}

    public static synchronized void retractTongue() {tongue.set(false);}
}
