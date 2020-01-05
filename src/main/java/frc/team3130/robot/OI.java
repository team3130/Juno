package frc.team3130.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.autoCommands.AimAssist;
import frc.team3130.robot.commands.BasicActuate;
import frc.team3130.robot.commands.BasicSpinMotor;
import frc.team3130.robot.commands.Chassis.AimHatch;
import frc.team3130.robot.commands.Chassis.ShiftToggle;
import frc.team3130.robot.commands.Climber.ArmsDown;
import frc.team3130.robot.commands.Climber.LegDown;
import frc.team3130.robot.commands.Climber.LegDrive;
import frc.team3130.robot.commands.Elevator.ElevatorToHeight;
import frc.team3130.robot.commands.Groups.RunPreset;
import frc.team3130.robot.commands.Shooter.SpinShooter;
import frc.team3130.robot.tantanDrive.Paths.SCurve;
import frc.team3130.robot.tantanDrive.RunMotionProfile;

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


    public static JoystickButton testArm;
    public static JoystickButton testButton;


    public static POVTrigger elevGround;


    public static JoystickButton lowBall;
    public static JoystickButton middleBall;
    public static JoystickButton highBall;
    public static JoystickButton toCargoship;

    private static JoystickButton armDeploy;
    private static JoystickTrigger legDown;
    private static JoystickTrigger legDrive;
    private static JoystickButton legUp;

    private static JoystickTrigger intakeBallIn;
    private static JoystickTrigger intakeHatchIn;
    private static JoystickTrigger intakeBallOut;
    private static JoystickTrigger intakeHatchOut;
    private static JoystickButton intakeActuate;
    private static JoystickButton fireShooter;

    private static JoystickButton aimHatch;

    //Settings for gamepad
    private OI(){
        driverGamepad = new Joystick(0);
        weaponsGamepad = new Joystick(1);

        /*
         * Driver
         */
        shift = new JoystickButton(driverGamepad, RobotMap.LST_BTN_RJOYSTICKPRESS);

        elevGround = new POVTrigger(driverGamepad, RobotMap.LST_POV_S);


        //testButton = new JoystickButton(driverGamepad, RobotMap.LST_BTN_B);

        /*
         * Weapons
         */

        //highTongue = new POVTrigger(weaponsGamepad, RobotMap.LST_POV_N);

        //lowBall = new JoystickButton(weaponsGamepad, RobotMap.LST_BTN_Y);
        //middleBall = new JoystickButton(weaponsGamepad, RobotMap.LST_BTN_X);
        //highBall = new JoystickButton(weaponsGamepad, RobotMap.LST_BTN_A);
        //toCargoship = new JoystickButton(weaponsGamepad , RobotMap.LST_BTN_B);

        armDeploy = new JoystickButton(weaponsGamepad, RobotMap.BTN_DROP_ARMS);
        legDown = new JoystickTrigger(weaponsGamepad, RobotMap.AXS_DROP_LEG, 0.1);
        legDrive = new JoystickTrigger(weaponsGamepad, RobotMap.AXS_DRIVE_LEG, 0.1);
        legUp = new JoystickButton(weaponsGamepad, RobotMap.BTN_UP_LEG);

        intakeBallIn = new JoystickTrigger(driverGamepad, RobotMap.AXS_INTAKE_IN);
        intakeHatchIn = new JoystickTrigger(driverGamepad, RobotMap.AXS_INTAKE_IN);
        intakeBallOut = new JoystickTrigger(driverGamepad, RobotMap.AXS_INTAKE_OUT);
        intakeHatchOut = new JoystickTrigger(driverGamepad, RobotMap.AXS_INTAKE_OUT);
        intakeActuate = new JoystickButton(driverGamepad, RobotMap.BTN_INTAKE_TOGGLE);

        fireShooter = new JoystickButton(driverGamepad, RobotMap.LST_BTN_X);

        aimHatch = new JoystickButton(driverGamepad, RobotMap.BTN_AIM_HATCH);

        //Map the button to command
        elevGround.whenActive(new ElevatorToHeight(8.6));


        shift.whenPressed(new ShiftToggle());



        //testArm.whenPressed(new TestArm());


        //lowBall.whenPressed(new RunPreset(RobotMap.Presets.LowestPort));
        //middleBall.whenPressed(new RunPreset(RobotMap.Presets.MiddlePort));
        //highBall.whenPressed(new RunPreset(RobotMap.Presets.HighestPort));
        //toCargoship.whenPressed(new RunPreset(RobotMap.Presets.Cargoship));

        //highTongue.whenActive(new RunPreset(RobotMap.Presets.HighestTongue));

        armDeploy.whileHeld(new ArmsDown());
        legDown.whileActive(new LegDown());
        legUp.whileHeld(new LegDown(true));
        legDrive.whileActive(new LegDrive());

        fireShooter.whileActive(new SpinShooter());

        BasicActuate intakeExtend = new BasicActuate(Robot.bcIntakeActuate);
        intakeBallIn.whileActive(new BasicSpinMotor(Robot.btBallMotor, 0.75));
        intakeHatchIn.whileActive(new BasicSpinMotor(Robot.btHatchMotor, 0.75));
        intakeBallOut.whileActive(new BasicSpinMotor(Robot.btBallMotor, -0.5));
        intakeHatchOut.whileActive(new BasicSpinMotor(Robot.btHatchMotor, -0.5));
        intakeActuate.toggleWhenPressed(intakeExtend);

        //aimHatch.whileActive(new AimHatch());
    }




    //Settings for Joysticks
    /** public static JoystickButton shift;

    public static JoystickButton runBallOut;
    public static JoystickButton runBallIn;

    public static JoystickButton runHatchIn;

    public static JoystickButton depositHatch;

    public static JoystickButton deployClimber;
    public static JoystickButton toggleClimber2;

    public static JoystickButton testButton;



    private OI(){
        stickL = new Joystick(0);
        stickR = new Joystick(1);
     runBallOut = new JoystickButton(stickL, 1);

     depositHatch = new JoystickButton(stickR, 6);
        gamepad = new Joystick(2);

        shift = new JoystickButton(stickR, 5);

        runHatchIn = new JoystickButton(stickR, 3);
        runBallIn = new JoystickButton(stickR, 1);

        deployClimber = new JoystickButton(stickL, 4);
        toggleClimber2 = new JoystickButton(stickL, 7);

        testButton = new JoystickButton(stickR, 2);




        shift.whenPressed(new ShiftToggle());

        runHatchIn.whileHeld(new HatchIn());

        runBallIn.whileHeld(new BallIn());
        runBallOut.whileHeld(new BallOut());

        depositHatch.whenPressed(new ClampToggle());

        deployClimber.whenPressed(new DeployClimber());

        testButton.whileHeld(new TestElevator());


    }
**/


}

