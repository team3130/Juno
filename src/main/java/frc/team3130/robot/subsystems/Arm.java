package frc.team3130.robot.subsystems;

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
    private static WPI_TalonSRX m_Elbow;
    private static WPI_TalonSRX m_Wrist;



    private Arm() {

        m_Elbow = new WPI_TalonSRX(RobotMap.CAN_ARMELBOW);
        m_Wrist = new WPI_TalonSRX(RobotMap.CAN_ARMWRIST);


        //Talon reset for Intake motors
        m_Elbow.configFactoryDefault();
        m_Wrist.configFactoryDefault();

        //setNeutralMode for Talons
        m_Elbow.setNeutralMode(NeutralMode.Brake);
        m_Wrist.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    protected void initDefaultCommand() {

    }

    public static void runPivotMotor1(double speed){
        m_Elbow.set(speed);
    }

    public static void runPivotMotor2(double speed){
        m_Wrist.set(speed);
    }
}
