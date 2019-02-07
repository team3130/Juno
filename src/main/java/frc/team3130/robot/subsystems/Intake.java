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

    //Create necessary objects
    private static Solenoid pneumaticSolenoid1;
    private static Solenoid pneumaticSolenoid2;

    private static WPI_TalonSRX m_ballMotor;
    private static WPI_TalonSRX m_hatchMotor;



    private Intake(){
        m_ballMotor = new WPI_TalonSRX(RobotMap.CAN_TESTINTAKE1);
        m_hatchMotor = new WPI_TalonSRX(RobotMap.CAN_TESTINTAKE2);


        //talon reset for ball and hatch
        m_ballMotor.configFactoryDefault();
        m_hatchMotor.configFactoryDefault();

        pneumaticSolenoid1 = new Solenoid(RobotMap.CAN_PNMMODULE,RobotMap.PNM_INTAKEPISTON1);
        pneumaticSolenoid2 = new Solenoid(RobotMap.CAN_PNMMODULE,RobotMap.PNM_INTAKEPISTON2);

        m_ballMotor.setNeutralMode(NeutralMode.Brake);
        m_hatchMotor.setNeutralMode(NeutralMode.Brake);
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

    /*
    *TODO: check to see how many solenoids are needed for intake, might be one but code has 2
     */

    public static void toggleSolenoid1(){
        pneumaticSolenoid1.set(!pneumaticSolenoid1.get());
    }

	public static void toggleSolenoid2() {
        pneumaticSolenoid2.set(!pneumaticSolenoid2.get());
	}
}
