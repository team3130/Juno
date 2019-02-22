package frc.team3130.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.team3130.robot.commands.*;
import frc.team3130.robot.commands.Arm.TestArm;
import frc.team3130.robot.commands.Arm.WristVertical;
import frc.team3130.robot.commands.Arm.WristPickup;
import frc.team3130.robot.commands.Chassis.ShiftToggle;
import frc.team3130.robot.commands.Climber.DeployClimber;
import frc.team3130.robot.commands.Elevator.ElevatorTestPreference;
import frc.team3130.robot.commands.Elevator.ElevatorToHeight;
import frc.team3130.robot.commands.Intake.BallIn;
import frc.team3130.robot.commands.Intake.BallOut;

public class OI {
    private class JoystickTrigger extends Trigger {

        private Joystick stick;
        private int axis;
        private double threshold;

        private JoystickTrigger(Joystick stick, int axis){
            this.stick = stick;
            this.axis = axis;
            threshold = 0.1;
        }

        private JoystickTrigger(Joystick stick, int axis, double threshold){
            this.stick = stick;
            this.axis = axis;
            this.threshold = threshold;
        }

        @Override
        public boolean get() {
            return stick.getRawAxis(axis) > threshold;
        }

    }

    private class POVTrigger extends Trigger{

        private Joystick stick;
        private int POV;

        public POVTrigger(Joystick stick, int POV) {
            this.stick = stick;
            this.POV = POV;
        }

        @Override
        public boolean get() {
            return stick.getPOV(0)==POV;
        }

    }

    //Instance Handling
    private static OI m_pInstance;
    public static OI GetInstance()
    {
        if(m_pInstance == null) m_pInstance = new OI();
        return m_pInstance;
    }

    /**
     * Definitions for joystick buttons start
     */
    //Joystick
    public static Joystick driverGamepad;
    public static Joystick weaponsGamepad;

    public static JoystickButton shift;

    public static JoystickButton runBallOut;
    public static JoystickButton runBallIn;

    public static JoystickButton depositHatch;

    public static JoystickButton deployClimber;
    public static JoystickButton testElevator;

    public static JoystickButton testArm;

    public static POVTrigger elevCargo;
    public static POVTrigger elevGround;

    public static POVTrigger intakeCargo;
    public static POVTrigger intakePickup;

    //Settings for gamepad
    private OI(){
        driverGamepad = new Joystick(0);
        weaponsGamepad = new Joystick(1);

        shift = new JoystickButton(driverGamepad, RobotMap.LST_BTN_LJOYSTICKPRESS);

        runBallIn = new JoystickButton(driverGamepad, RobotMap.LST_BTN_RBUMPER);
        runBallOut = new JoystickButton(driverGamepad, RobotMap.LST_BTN_LBUMPER);

        depositHatch = new JoystickButton(driverGamepad, RobotMap.LST_BTN_A);

        deployClimber = new JoystickButton(weaponsGamepad, RobotMap.LST_BTN_MENU);

        testElevator = new JoystickButton(weaponsGamepad, RobotMap.LST_BTN_B);

        testArm = new JoystickButton(weaponsGamepad, RobotMap.LST_BTN_A);

        elevCargo = new POVTrigger(driverGamepad, RobotMap.LST_POV_N);
        elevGround = new POVTrigger(driverGamepad, RobotMap.LST_POV_E);

        intakeCargo = new POVTrigger(driverGamepad, RobotMap.LST_POV_W);
        intakePickup = new POVTrigger(driverGamepad, RobotMap.LST_POV_S);


        //Map the button to command
        shift.whenPressed(new ShiftToggle());

        runBallIn.whileHeld(new BallIn());
        runBallOut.whileHeld(new BallOut());

        depositHatch.whileHeld(new DepositHatch());

        deployClimber.whenPressed(new DeployClimber());

        intakePickup.whenActive(new WristPickup());
        intakeCargo.whenActive(new WristVertical());


        elevCargo.whenActive(new ElevatorToHeight(15.0));
        elevGround.whenActive(new ElevatorToHeight(8.6));

        testElevator.whenPressed(new ElevatorTestPreference());

        testArm.whenPressed(new TestArm());


    }




    //Settings for Joysticks
    /** public static JoystickButton shift;

    public static JoystickButton runBallOut;
    public static JoystickButton runBallIn;

    public static JoystickButton runHatchIn;

    public static JoystickButton depositHatch;

    public static JoystickButton deployClimber;
    public static JoystickButton toggleClimber2;

    public static JoystickButton testElevator;



    private OI(){
        stickL = new Joystick(0);
        stickR = new Joystick(1);
        gamepad = new Joystick(2);

        shift = new JoystickButton(stickR, 5);

        runHatchIn = new JoystickButton(stickR, 3);
        runBallIn = new JoystickButton(stickR, 1);
        runBallOut = new JoystickButton(stickL, 1);

        depositHatch = new JoystickButton(stickR, 6);

        deployClimber = new JoystickButton(stickL, 4);
        toggleClimber2 = new JoystickButton(stickL, 7);

        testElevator = new JoystickButton(stickR, 2);


        shift.whenPressed(new ShiftToggle());

        runHatchIn.whileHeld(new HatchIn());

        runBallIn.whileHeld(new BallIn());
        runBallOut.whileHeld(new BallOut());

        depositHatch.whenPressed(new ClampToggle());

        deployClimber.whenPressed(new ClimberToggle1());
        toggleClimber2.whenPressed(new ClimberToggle2());

        testElevator.whileHeld(new TestElevator());


    }
**/


}

