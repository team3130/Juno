package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
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


    private Climber(){
        pistons = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_CLIMBARMS);

        m_legDown = new WPI_TalonSRX(RobotMap.CAN_CLIMBERLEG);
        m_legDrive = new WPI_TalonSRX(RobotMap.CAN_CLIMBERDRIVE);

        m_legDown.configFactoryDefault();

        m_legDown.configVoltageCompSaturation(12.0, 0);
        m_legDown.enableVoltageCompensation(true);

        m_legDrive.configFactoryDefault();
    }

    public void initDefaultCommand() {
    }

    public static void driveLeg(double PVbus){
        m_legDrive.set(ControlMode.PercentOutput, PVbus);
    }

    public static void downLeg(double pVbus){
        m_legDown.set(ControlMode.PercentOutput, pVbus);
    }

    public static void deployArms(boolean deploy)
    {
        pistons.set(deploy);
    }
}


