package frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team3130.robot.RobotMap;

public class PistonClimber extends Subsystem {
    //Instance Handling
    private static PistonClimber m_pInstance;

    public static PistonClimber GetInstance() {
        if (m_pInstance == null) m_pInstance = new PistonClimber();
        return m_pInstance;
    }

    //Create necessary objects

    private static Solenoid pistons1;

    private static Solenoid pistons2;

    private static WPI_TalonSRX m_Pistons;

    //Create and define all standard data types needed


    private PistonClimber(){
        /**
         * Constructor:
         * Define and configure your defined objects (ie. talons, vars)
         */
        pistons1 = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_CLIMBPISTON1);

        pistons2 = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_CLIMBPISTON2);

        m_Pistons = new WPI_TalonSRX(RobotMap.CAN_PISTON);


    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public static void rawPiston(double percent){ m_Pistons.set(ControlMode.PercentOutput, percent); }

    public static void toggleClimbPistons1() {
        pistons1.set(!pistons1.get());
    }

    public static void toggleClimbPistons2() {
        pistons2.set(!pistons2.get());
    }

}


