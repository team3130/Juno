package frc.team3130.robot.subsystems;

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

    private static Solenoid pistons;


    //Create and define all standard data types needed


    private PistonClimber(){
        /**
         * Constructor:
         * Define and configure your defined objects (ie. talons, vars)
         */
        pistons = new Solenoid(RobotMap.CAN_PNMMODULE,2);

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }


    public static void toggleClimbPistons() {
        pistons.set(!pistons.get());
    }

}
