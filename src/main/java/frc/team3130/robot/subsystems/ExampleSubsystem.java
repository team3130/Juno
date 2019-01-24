package frc.team3130.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ExampleSubsystem extends Subsystem {
    //Instance Handling
    private static ExampleSubsystem m_pInstance;

    public static ExampleSubsystem GetInstance() {
        if (m_pInstance == null) m_pInstance = new ExampleSubsystem();
        return m_pInstance;
    }
    /**_talon.configFactoryDefault();
     * resets hardware defaults that could have been configured on talon before
    */

    //Create necessary objects



    //Create and define all standard data types needed


    private ExampleSubsystem(){
        /**
         * Constructor:
         * Define and configure your defined objects (ie. talons, vars)
         */

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }


}
