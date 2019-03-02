package frc.team3130.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.commands.Arm.TestArm;
import frc.team3130.robot.commands.Arm.WristToAngle;
import frc.team3130.robot.commands.Chassis.ShiftToggle;
import frc.team3130.robot.commands.Elevator.ElevatorShift;
import frc.team3130.robot.commands.Elevator.ElevatorTestPreference;
import frc.team3130.robot.commands.Elevator.ElevatorToHeight;
import frc.team3130.robot.commands.Groups.DepositHatch;
import frc.team3130.robot.commands.Groups.RunPreset;
import frc.team3130.robot.commands.Groups.TongueHatch;
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

    public static JoystickButton depositHatch;

    public static JoystickButton deployClimber;

    public static JoystickButton testArm;

    public static JoystickButton toggleTongue;

    public static POVTrigger elevCargo;
    public static POVTrigger elevGround;
    public static JoystickButton elevatorShift;
    public static JoystickButton testElevator;

    public static POVTrigger intakeStowed;
    public static POVTrigger intakePickup;

    public static POVTrigger highTongue;
    public static POVTrigger middleTongue;
    public static POVTrigger lowTongue;

    private static Command ballOutCommand = new BallOut();
    private static Command ballInCommand = new BallIn();


    public void checkTriggers() {
        if (Math.abs(OI.driverGamepad.getRawAxis(RobotMap.LST_AXS_LTRIGGER)) >= RobotMap.kIntakeTriggerDeadband) {
            ballOutCommand.start();
        }else{
            ballOutCommand.cancel();
        }
        if (Math.abs(OI.driverGamepad.getRawAxis(RobotMap.LST_AXS_RTRIGGER)) >= RobotMap.kIntakeTriggerDeadband) {
            ballInCommand.start();
        }else{
            ballInCommand.cancel();
        }
    }


    //Settings for gamepad
    private OI(){
        driverGamepad = new Joystick(0);
        weaponsGamepad = new Joystick(1);

        /*
         * Driver
         */
        shift = new JoystickButton(driverGamepad, RobotMap.LST_BTN_RJOYSTICKPRESS);

        depositHatch = new JoystickButton(driverGamepad, RobotMap.LST_BTN_B);

        intakeStowed = new POVTrigger(driverGamepad, RobotMap.LST_POV_W);
        intakePickup = new POVTrigger(driverGamepad, RobotMap.LST_POV_S);

        elevCargo = new POVTrigger(driverGamepad, RobotMap.LST_POV_N);
        elevGround = new POVTrigger(driverGamepad, RobotMap.LST_POV_E);

        toggleTongue = new JoystickButton(driverGamepad, RobotMap.LST_BTN_Y);

        /*
         * Weapons
         */
        testElevator = new JoystickButton(weaponsGamepad, RobotMap.LST_BTN_B);

        elevatorShift = new JoystickButton(weaponsGamepad, RobotMap.LST_BTN_MENU);

        testArm = new JoystickButton(weaponsGamepad, RobotMap.LST_BTN_A);

        deployClimber = new JoystickButton(weaponsGamepad, RobotMap.LST_BTN_WINDOW);

        highTongue = new POVTrigger(weaponsGamepad, RobotMap.LST_POV_N);
        middleTongue = new POVTrigger(weaponsGamepad, RobotMap.LST_POV_W);
        lowTongue = new POVTrigger(weaponsGamepad, RobotMap.LST_POV_S);

        //Map the button to command
        shift.whenPressed(new ShiftToggle());

        depositHatch.whileHeld(new DepositHatch());

        //deployClimber.whenPressed(new DeployClimber());

        intakePickup.whenActive(new WristToAngle(177.5));
        intakeStowed.whenActive(new WristToAngle(90.0));


        elevCargo.whenActive(new ElevatorToHeight(15.0));
        elevGround.whenActive(new ElevatorToHeight(8.6));

        elevatorShift.whenPressed(new ElevatorShift());

        testElevator.whenPressed(new ElevatorTestPreference());

        testArm.whenPressed(new TestArm());

        toggleTongue.whenPressed(new TongueHatch());

        highTongue.whenActive(new RunPreset(RobotMap.Presets.HighestTongue));
        middleTongue.whenActive(new RunPreset(RobotMap.Presets.MiddleTongue));
        lowTongue.whenActive(new RunPreset(RobotMap.Presets.LowestTongue));

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

        deployClimber.whenPressed(new DeployClimber());

        testElevator.whileHeld(new TestElevator());


    }
**/


}

