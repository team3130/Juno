package frc.team3130.robot.commands.Chassis;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.RobotMap;
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
    	Chassis.talonsToCoast(true);
    	Chassis.driveTank(0, 0, false); 		//Cut all power to the motors so they aren't running during the shift
        timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Execute the shift only once, and only at a certain time after the motors have been stopped

    	if(!hasShifted && timer.get() > RobotMap.kChassisShiftWait){
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
        //End the command after the robot has shifted and after shift wait period
        return (hasShifted && timer.get() > RobotMap.kChassisShiftWait);
    }

    // Called once after isFinished returns true
    protected void end() {
    	//Set variables to default states for next execution of command
    	hasShifted = false;
    	timer.stop();
    	Chassis.talonsToCoast(false); //set talons back to brake mode
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
