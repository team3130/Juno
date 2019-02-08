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
    private static WPI_TalonSRX m_elbow;
    private static WPI_TalonSRX m_wrist;




    private Arm() {

        m_elbow = new WPI_TalonSRX(RobotMap.CAN_ARMELBOW);
        m_wrist = new WPI_TalonSRX(RobotMap.CAN_ARMWRIST);


        //Talon reset for Intake motors
        m_elbow.configFactoryDefault();
        m_wrist.configFactoryDefault();

        //setNeutralMode for Talons
        m_elbow.setNeutralMode(NeutralMode.Brake);
        m_wrist.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    protected void initDefaultCommand() {

    }

    public static void runPivotMotor1(double speed){
        m_elbow.set(speed);
    }

    public static void runPivotMotor2(double speed){
        m_wrist.set(speed);
    }
}
