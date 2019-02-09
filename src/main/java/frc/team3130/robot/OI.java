package frc.team3130.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.team3130.robot.commands.*;

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
    public static Joystick stickL;
    public static Joystick stickR;
    public static Joystick gamepad;

    public static JoystickButton shift;

    public static JoystickButton runBallOut;
    public static JoystickButton runBallIn;

    public static JoystickButton runHatchIn;

    public static JoystickButton toggleIntakeClamp;

    public static JoystickButton runPivotMotor1;
    public static JoystickButton runPivotMotor2;

    public static JoystickButton toggleClimber;



    private OI(){
        stickL = new Joystick(0);
        stickR = new Joystick(1);
        gamepad = new Joystick(2);

        shift = new JoystickButton(stickR, 5);

        runHatchIn = new JoystickButton(stickR, 3);
        runBallIn = new JoystickButton(stickR, 1);
        runBallOut = new JoystickButton(stickL, 1);

        toggleIntakeClamp = new JoystickButton(stickR, 6);

        runPivotMotor1 = new JoystickButton(stickL, 6);
        runPivotMotor2 = new JoystickButton(stickL, 7);



        //Button 8 deleted because motorized climber existn't

        toggleClimber = new JoystickButton(stickL, 4);

        shift.whenPressed(new ShiftToggle());

        runHatchIn.whileHeld(new HatchIn());

        runBallIn.whileHeld(new BallIn());
        runBallOut.whileHeld(new BallOut());

        toggleIntakeClamp.whenPressed(new ClampToggle());

        runPivotMotor1.whileHeld(new ArmDown());
        runPivotMotor2.whileHeld(new ArmUp());

        toggleClimber.whenPressed(new ClimberToggle());
        

    }



}
