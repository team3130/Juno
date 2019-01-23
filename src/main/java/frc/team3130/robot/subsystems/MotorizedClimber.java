package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team3130.robot.RobotMap;

public class MotorizedClimber extends Subsystem {
    //Instance Handling
    private static MotorizedClimber m_pInstance;

    public static MotorizedClimber GetInstance() {
        if (m_pInstance == null) m_pInstance = new MotorizedClimber();
        return m_pInstance;
    }


    private static WPI_TalonSRX m_motorOne;
    private static WPI_TalonSRX m_motorTwo;
    private static WPI_TalonSRX m_motorThree;
    //Create and define all standard data types needed


    private MotorizedClimber(){
        m_motorOne  = new WPI_TalonSRX(RobotMap.CAN_CLIMBER1);
        m_motorTwo = new WPI_TalonSRX(RobotMap.CAN_CLIMBER2);
        m_motorThree = new WPI_TalonSRX(RobotMap.CAN_CLIMBER3);

        m_motorOne.setNeutralMode(NeutralMode.Brake);
        m_motorTwo.setNeutralMode(NeutralMode.Brake);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public static void runClimber(double speed){
        m_motorOne.set(speed);
        m_motorTwo.set(speed);
    }
    public static void upClimber(double speed){
        m_motorOne.set(speed);
        m_motorTwo.set(speed);
    }
    public static void driveClimber(double speed){
        m_motorThree.set(speed);
    }
}
