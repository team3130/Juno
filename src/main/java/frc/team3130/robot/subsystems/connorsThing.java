package frc.team3130.robot.subsystems;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import frc.team3130.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;

public class connorsThing extends Subsystem {
    //Instance Handling
    private static connorsThing m_pInstance;

    public static connorsThing GetInstance() {
        if (m_pInstance == null) m_pInstance = new connorsThing();
        return m_pInstance;
    }

    //Create necessary objects


    //Create and define all standard data types needed
    private static WPI_TalonSRX tantan1;
    private static WPI_TalonSRX tantan2;

    private static Solenoid matt2;
    private static Solenoid damon3;
    private static Solenoid nickolas4;
    private static Solenoid cage5;

    private connorsThing() {
        /**
         * Constructor:
         * Define and configure your defined objects (ie. talons, vars)
         */
        tantan1 = new WPI_TalonSRX(RobotMap.CAN_TESTMOTOR);
        tantan2 = new WPI_TalonSRX(RobotMap.CAN_TESTMOTOR);

        matt2 = new Solenoid(RobotMap.CAN_PNMMODULE, 2);
        damon3 = new Solenoid(RobotMap.CAN_PNMMODULE, 3);
        nickolas4 = new Solenoid(RobotMap.CAN_PNMMODULE, 4);
        cage5 = new Solenoid(RobotMap.CAN_PNMMODULE, 5);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public static void toggleMotors() {

    }

    public static void toggleSolenoids() {
        matt2.set(!matt2.get());
        nickolas4.set(!nickolas4.get());
    }

    protected void solenoids() {
        connorsThing.toggleSolenoids();
    }

    protected void motors() {
        connorsThing.toggleMotors();
    }

}