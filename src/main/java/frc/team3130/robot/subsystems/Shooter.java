package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team3130.robot.RobotMap;


public class Shooter extends Subsystem {
    //Instance Handling
    private static Shooter m_pInstance;

    public static Shooter GetInstance() {
        if (m_pInstance == null) m_pInstance = new Shooter();
        return m_pInstance;
    }

    //Create necessary objects
    private static WPI_TalonSRX m_shooterLeft;
    private static WPI_TalonSRX m_shooterRight;
    private static WPI_TalonSRX m_shooterTop;


    //Create and define all standard data types needed


    private Shooter(){
        m_shooterLeft = new WPI_TalonSRX(RobotMap.CAN_SHOOTERLEFT);
        m_shooterRight = new WPI_TalonSRX(RobotMap.CAN_SHOOTERRIGHT);
        m_shooterTop = new WPI_TalonSRX(RobotMap.CAN_SHOOTERTOP);

        m_shooterLeft.configFactoryDefault();
        m_shooterRight.configFactoryDefault();
        m_shooterTop.configFactoryDefault();
        /**
         * Constructor:
         * Define and configure your defined objects (ie. talons, vars)
         *
         * _talon.configFactoryDefault();
         * resets hardware defaults that could have been configured on talon before
         *
         */

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }



}
