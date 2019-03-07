package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.commands.Climber.LandingGearDrive;

import frc.team3130.robot.util.Util;

public class PistonClimber extends Subsystem {
    //Instance Handling
    private static PistonClimber m_pInstance;

    public static PistonClimber GetInstance() {
        if (m_pInstance == null) m_pInstance = new PistonClimber();
        return m_pInstance;
    }

    //Create necessary objects

    private static DoubleSolenoid pistons;

    private static WPI_TalonSRX m_landingGear;

    //Create and define all standard data types needed


    private PistonClimber(){
        /**
         * Constructor:
         * Define and configure your defined objects (ie. talons, vars)
         */
        pistons = new DoubleSolenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_CLIMBPISTONFORWARD, RobotMap.PNM_CLIMBPISTONREVERSE);

        m_landingGear = new WPI_TalonSRX(RobotMap.CAN_PISTONMOTOR);

        m_landingGear.configFactoryDefault();

        m_landingGear.configVoltageCompSaturation(12.0, 0);
        m_landingGear.enableVoltageCompensation(true);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new LandingGearDrive());

    }

    public static void rawLandingGear(double throttle){
        throttle = Util.limit(throttle, 1.0);
        
        m_landingGear.set(ControlMode.PercentOutput, throttle);
    }

    public static void deployPistons() {
        pistons.set(DoubleSolenoid.Value.kForward);
    }

    public static void setPistons(DoubleSolenoid.Value val){
        pistons.set(val);
    }

    public static void retractPistons(){
        pistons.set(DoubleSolenoid.Value.kReverse);
    }

    public static DoubleSolenoid.Value getPiston() {
        return pistons.get();
    }

}


