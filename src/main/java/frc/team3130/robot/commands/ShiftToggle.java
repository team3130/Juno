package frc.team3130.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Chassis;

/**
 *
 */
public class ShiftToggle extends Command {

	private Timer timer;
	private boolean hasShifted;
	private boolean currentShift;
	
    public ShiftToggle() {
    	requires(Chassis.GetInstance());

        timer = new Timer();
        hasShifted = false;
        currentShift = Chassis.isLowGear(); //true = low gear
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.reset();
    	Chassis.TalonsToCoast(true);
    	Chassis.DriveTank(0, 0); 		//Cut all power to the motors so they aren't running during the shift
        timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Execute the shift only once, and only at a certain time after the motors have been stopped
        System.out.println("UH");
    	if(!hasShifted && timer.get() > 0.1){
    	    System.out.println("shifting");
            currentShift = Chassis.isLowGear();

    		Chassis.shift(!currentShift); //toggle the gear to what it isn't currently
    		hasShifted = true;

    		//Reset the timer so that the ending dead time is from shifting rather than from the start.
    		timer.reset();
    		timer.start();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //End the command after the robot has shifted or after timeout period
        return (hasShifted || timer.get() > 0.2);
    }

    // Called once after isFinished returns true
    protected void end() {
    	//Set variables to default states for next execution of command
    	hasShifted = false;
    	timer.stop();
    	Chassis.TalonsToCoast(false); //set talons back to brake mode
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
