package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.commands.DefaultDrive;
import frc.team3130.robot.commands.RunIntake;

public class TestIntake extends Subsystem {
    //Instance Handling
    private static TestIntake m_pInstance;

    public static TestIntake GetInstance() {
        if (m_pInstance == null) m_pInstance = new TestIntake();
        return m_pInstance;
    }

    private static Solenoid pneumaticSolenoid1;

    private static WPI_TalonSRX m_testMotor;


    private TestIntake(){
        m_testMotor = new WPI_TalonSRX(RobotMap.CAN_TESTMOTOR);

        pneumaticSolenoid1 = new Solenoid(RobotMap.CAN_PNMMODULE,0);

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand();
        //setDefaultCommand(new MySpecialCommand());
    }

    public static void toggleSolenoid1(){
        pneumaticSolenoid1.set(!pneumaticSolenoid1.get());
    }

    public static void runBeaterBar(double speed){
        m_testMotor.set(speed);
    }
}
